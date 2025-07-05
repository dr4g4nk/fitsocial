package org.unibl.etf.fitsocial.feed.post;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
import core.util.CurrentUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitsocial.auth.user.User;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.entity.FileType;
import org.unibl.etf.fitsocial.feed.comment.Comment;
import org.unibl.etf.fitsocial.feed.comment.CommentDto;
import org.unibl.etf.fitsocial.feed.comment.CommentMapper;
import org.unibl.etf.fitsocial.feed.media.MediaDto;
import org.unibl.etf.fitsocial.feed.media.MediaMapper;
import org.unibl.etf.fitsocial.feed.media.MediaRepository;
import org.unibl.etf.fitsocial.feed.media.MediaService;
import org.unibl.etf.fitsocial.service.FileStorageService;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService extends BaseSoftDeletableServiceImpl<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create, Long> {
    private final MediaMapper mediaMapper;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    protected PostRepository postRepository;
    protected CommentMapper commentMapper;
    protected UserMapper userMapper;

    public PostService(PostRepository postRepository, IMapper<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create> mapper, CommentMapper commentMapper, UserMapper userMapper, MediaMapper mediaMapper, MediaService mediaService, MediaRepository mediaRepository) {
        super(postRepository, mapper);
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.mediaMapper = mediaMapper;
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public ResponseDto<PageResponseDto<PostDto.List>, Post> findAll(Pageable pageable) {
        return finaAllByPublic(pageable, false);
    }

    public ResponseDto<PageResponseDto<PostDto.List>, Post> finaAllByPublic(Pageable pageable, boolean onlyPublic) {
        var userDetails = getUserDetails();

        var userId = userDetails.isPresent() ? userDetails.get().getId() : -1;

        return new ResponseDto<>(new PageResponseDto<>(postRepository.findAllByDeletedAtIsNullAndPublic(onlyPublic, userId, pageable).map(pl -> new PostDto.List(pl.post.getId(), pl.post.getContent(), userMapper.toDto(pl.post.getUser()), pl.post.getCreatedAt(), pl.post.getIsPublic(), pl.post.getLikeCount(), 0L, pl.like != null ? pl.like.getActive() : false, pl.post.getMedia().stream().map(mediaMapper::toDto).toList()))));
    }

    public ResponseDto<PageResponseDto<PostDto.List>, Post> findAllByUserId(Long userId, Pageable pageable) {
        return new ResponseDto<>(new PageResponseDto<>(postRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable).map(mapper::toListDto)));
    }

    public ResponseDto<PageResponseDto<PostDto.List>, Post> findPublicPostsWithLikesAndComments(Pageable pageable, boolean onlyPublic) {
        // 1. Povuci postove sa brojem lajkova
        var rawPosts = postRepository.findPublicPostsWithLikes(onlyPublic, pageable);
        java.util.List<Long> postIds = rawPosts.stream().map(Post::getId).collect(Collectors.toList());

        // 2. Povuci komentare za te postove
        java.util.List<Comment> rawComments = postRepository.findCommentsForPosts(postIds);

        // 3. Grupisi komentare po postId-u, uzmi samo 3 najnovija
        Map<Long, java.util.List<CommentDto.List>> commentsByPostId = rawComments.stream().collect(Collectors.groupingBy(c -> c.getPost().getId(), Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(Comment::getCreatedAt).reversed()).limit(3).map(c -> commentMapper.toListDto(c)).toList())));

        // 4. Mapiraj rezultate u PostDto
        var results = rawPosts.map(p -> new PostDto.List(p.getId(), p.getContent(), userMapper.toDto(p.getUser()), p.getCreatedAt(), p.getIsPublic(), p.getLikeCount(), 0L, false, null));


        // 6. Vrati kao Page
        return new ResponseDto<PageResponseDto<PostDto.List>, Post>(new PageResponseDto<PostDto.List>(results));
    }

    @Override
    public ResponseDto<PostDto, Post> update(Long id, PostDto.Update dto) {
        ResponseDto<PostDto, Post> response;
        try {

            var mediaIds = dto.media().stream().map(MediaDto.Update::id).filter(i -> i > 0).collect(Collectors.toList());
            mediaRepository.deleteByPostIdAndIdNotIn(id, mediaIds);

            dto.media().stream().filter(m -> Objects.equals(m.postId(), id)).forEach(m -> {
                if (m.id() > 0)
                    mediaService.update(m.id(), new MediaDto.Update(m.id(), id, m.order(), m.mimeType(), null));
                else
                    mediaService.save(new MediaDto.Create(id, m.order(), m.mimeType(), m.file(), null));
            });

            var res = super.update(id, new PostDto.Update(dto.content(), dto.isPublic(), null));
            var updatedEntity = res.getEntity();

            response = new ResponseDto<>(mapper.toDto(updatedEntity), updatedEntity);
        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException noTransactionException) {
                response.setMessage(response.getMessage() + " No transaction available");
            }
        }
        return response;
    }

    public ResponseDto<PostDto, Post> save(PostDto.Create dto, List<MultipartFile> newMedia) {
        ResponseDto<PostDto, Post> response;
        try {
            var userDetails = getUserDetails();
            var user = entityManager.getReference(User.class, userDetails.orElse(new CurrentUserDetails()).getId());
            var entity = mapper.fromCreateDto(dto);
            entity.setUser(user);
            var savedEntity = postRepository.save(entity);

            dto.media().forEach(mediaService::save);

            entityManager.refresh(savedEntity);
            response = new ResponseDto<PostDto, Post>(mapper.toDto(savedEntity), savedEntity);
        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException noTransactionException) {
                response.setMessage(response.getMessage() + " No transaction available");
            }
        }
        return response;
    }

    @Override
    public ResponseDto<PostDto, Post> save(PostDto.Create dto) {
        ResponseDto<PostDto, Post> response;
        try {
            var userDetails = getUserDetails();
            var user = entityManager.getReference(User.class, userDetails.orElse(new CurrentUserDetails()).getId());
            var entity = mapper.fromCreateDto(dto);
            entity.setUser(user);
            var savedEntity = postRepository.save(entity);

            dto.media().forEach(mediaService::save);

            entityManager.refresh(savedEntity);
            response = new ResponseDto<PostDto, Post>(mapper.toDto(savedEntity), savedEntity);
        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException noTransactionException) {
                response.setMessage(response.getMessage() + " No transaction available");
            }
        }
        return response;
    }

    @Override
    public ResponseDto<PostDto, Post> deleteById(Long id) {
        var userId = getUserDetails().orElse(new CurrentUserDetails()).getId();
        if (!postRepository.existsByIdAndUserIdAndDeletedAtIsNull(id, userId))
            return new ResponseDto<>("Entite je obirisan ili nemate pravo da brisite");
        return super.deleteById(id);
    }
}
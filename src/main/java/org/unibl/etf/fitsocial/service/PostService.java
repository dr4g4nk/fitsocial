package org.unibl.etf.fitsocial.service;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.util.CurrentUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.dto.CommentDto;
import org.unibl.etf.fitsocial.dto.PostDto;
import org.unibl.etf.fitsocial.dto.FeedItemDto;
import org.unibl.etf.fitsocial.entity.Post;
import org.unibl.etf.fitsocial.entity.Comment;
import org.unibl.etf.fitsocial.entity.User;
import org.unibl.etf.fitsocial.mapper.CommentMapper;
import core.mapper.IMapper;
import org.unibl.etf.fitsocial.mapper.UserMapper;
import org.unibl.etf.fitsocial.repository.PostRepository;
import core.service.BaseSoftDeletableServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService extends BaseSoftDeletableServiceImpl<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create, Long> {

    protected PostRepository postRepository;
    protected CommentMapper CommentMapper;
    protected UserMapper userMapper;

    public PostService(PostRepository postRepository, IMapper<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create> mapper, CommentMapper CommentMapper, UserMapper userMapper) {
        super(postRepository, mapper);
        this.postRepository = postRepository;
        this.CommentMapper = CommentMapper;
        this.userMapper = userMapper;
    }

    public ResponseDto<PageResponseDto<PostDto.List>, Post> findPublicPostsWithLikesAndComments(Pageable pageable, boolean onlyPublic) {
        // 1. Povuci postove sa brojem lajkova
        var rawPosts = postRepository.findPublicPostsWithLikes(onlyPublic, pageable);
        List<Long> postIds = rawPosts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // 2. Povuci komentare za te postove
        List<Comment> rawComments = postRepository.findCommentsForPosts(postIds);

        // 3. Grupisi komentare po postId-u, uzmi samo 3 najnovija
        Map<Long, List<CommentDto.List>> commentsByPostId = rawComments.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getPost().getId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                                        .limit(3)
                                        .map(c -> CommentMapper.toListDto(c))
                                        .toList()
                        )
                ));

        // 4. Mapiraj rezultate u PostDto
        var results = rawPosts
                .map(p -> new PostDto.List(p.getId(), p.getContent(), userMapper.toDto(p.getUser()), p.getCreatedAt(), p.getIsPublic(), p.getLikeCount(), commentsByPostId.getOrDefault(p.getId(), Collections.emptyList())));


        // 6. Vrati kao Page
        return new ResponseDto<PageResponseDto<PostDto.List>, Post>(new PageResponseDto<PostDto.List>(results));
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
}

package org.unibl.etf.fitsocial.feed.like;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.auth.user.User;
import org.unibl.etf.fitsocial.feed.post.Post;
import org.unibl.etf.fitsocial.feed.post.PostRepository;

@Service
@Transactional
public class LikeService extends BaseSoftDeletableServiceImpl<
        Like,
        LikeDto,
        LikeDto.List,
        LikeDto.Update,
        LikeDto.Create,
        Long
        > {
    protected LikeRepository repository;
    protected PostRepository postRepository;

    public LikeService(LikeRepository repository, PostRepository postRepository, IMapper<Like, LikeDto, LikeDto.List, LikeDto.Update, LikeDto.Create> mapper) {
        super(repository, mapper);

        this.repository = repository;
        this.postRepository = postRepository;
    }

    public ResponseDto<PageResponseDto<LikeDto.List>, Like> findAllByPostId(Long postId, Pageable pageable) {
        boolean onlyPublic = true;
        var userDetails = getUserDetails();
        if (userDetails.isPresent()) onlyPublic = false;

        if (postRepository.existsByIdAndDeletedAtIsNullAndIsPublicTrueOrIsPublicEquals(postId, onlyPublic))
            return new ResponseDto<>(new PageResponseDto<>(repository.findAllByPostIdAndDeletedAtIsNull(postId, pageable).map(mapper::toListDto)));

        return new ResponseDto<>("Not found");
    }

    public ResponseDto<LikeDto, Like> likePost(Long postId) {
        var userDetails = getUserDetails();

        if (userDetails.isEmpty()) return new ResponseDto<>("Not Authorized");

        var entity = new Like();

        var like = repository.findByPostIdAndUserId(postId, userDetails.get().getId());

        if (like.isPresent()) {
            entity = like.get();
            entity.setActive(!entity.getActive());
        } else {
            entity.setActive(true);
            entity.setPost(entityManager.getReference(Post.class, postId));
            entity.setUser(entityManager.getReference(User.class, userDetails.get().getId()));

        }
        var res = repository.save(entity);

        return new ResponseDto<>(mapper.toDto(res), res);
    }
}

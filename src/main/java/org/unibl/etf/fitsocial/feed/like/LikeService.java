package org.unibl.etf.fitsocial.feed.like;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
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
}

package org.unibl.etf.fitsocial.service;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.LikeDto;
import org.unibl.etf.fitsocial.entity.Like;
import core.mapper.IMapper;
import org.unibl.etf.fitsocial.repository.LikeRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.repository.PostRepository;

@Service
@Transactional
public class LikeService extends BaseSoftDeletableServiceImpl<Like, LikeDto, LikeDto.List, LikeDto.Update, LikeDto.Create, Long> {
    protected LikeRepository repository;
    protected PostRepository postRepository;

    public LikeService(LikeRepository repository, PostRepository postRepository, IMapper<Like, LikeDto, LikeDto.List, LikeDto.Update, LikeDto.Create> mapper) {
        super(repository, mapper);

        this.repository = repository;
        this.postRepository = postRepository;
    }

    @Override
    public ResponseDto<PageResponseDto<LikeDto.List>, Like> findAll(Pageable pageable) {
        return super.findAll(pageable);
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

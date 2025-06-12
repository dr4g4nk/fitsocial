package org.unibl.etf.fitsocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.PostLikeDto;
import org.unibl.etf.fitsocial.entity.PostLike;
import org.unibl.etf.fitsocial.mapper.base.IMapper;
import org.unibl.etf.fitsocial.repository.PostLikeRepository;
import org.unibl.etf.fitsocial.service.base.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class PostLikeService extends BaseSoftDeletableServiceImpl<PostLike, PostLikeDto.GetAll, PostLikeDto, PostLikeDto, PostLikeDto.Create, Long> {
    protected PostLikeRepository repository;
    public PostLikeService(PostLikeRepository repository, IMapper<PostLike, PostLikeDto.GetAll, PostLikeDto, PostLikeDto, PostLikeDto.Create> mapper) {
        super(repository, mapper);

        this.repository = repository;
    }

    public Page<PostLikeDto.GetAll> findAllByPostId(Long postId, Pageable pageable) {
        return repository.findAllByPostIdAndDeletedAtIsNull(postId, pageable).map(mapper::toGetAllDto);
    }
}

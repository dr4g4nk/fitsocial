package org.unibl.etf.fitsocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.PostCommentDto;
import org.unibl.etf.fitsocial.dto.PostLikeDto;
import org.unibl.etf.fitsocial.entity.PostComment;
import org.unibl.etf.fitsocial.mapper.base.IMapper;
import org.unibl.etf.fitsocial.repository.PostCommentRepository;
import org.unibl.etf.fitsocial.repository.base.BaseSoftDeletableRepository;
import org.unibl.etf.fitsocial.service.base.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class PostCommentService extends BaseSoftDeletableServiceImpl<PostComment, PostCommentDto, PostCommentDto, PostCommentDto.Update, PostCommentDto.Create, Long> {
    protected PostCommentRepository repository;
    public PostCommentService(PostCommentRepository repository, IMapper<PostComment, PostCommentDto, PostCommentDto, PostCommentDto.Update, PostCommentDto.Create> mapper) {
        super(repository, mapper);
        this.repository = repository;
    }

    public Page<PostCommentDto> findAllByPostId(Long postId, Pageable pageable) {
        return repository.findAllByPostIdAndDeletedAtIsNull(postId, pageable).map(mapper::toGetAllDto);
    }
}

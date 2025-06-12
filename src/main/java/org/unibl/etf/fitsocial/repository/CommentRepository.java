package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.fitsocial.entity.Comment;
import core.repository.BaseSoftDeletableRepository;

public interface CommentRepository extends BaseSoftDeletableRepository<Comment, Long> {
    Page<Comment> findAllByPostIdAndDeletedAtIsNull(Long id, Pageable pageable);
}
package org.unibl.etf.fitsocial.feed.comment;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends BaseSoftDeletableRepository<Comment,Long> {
    Page<Comment> findAllByPostIdAndDeletedAtIsNull(Long id, Pageable pageable);
}
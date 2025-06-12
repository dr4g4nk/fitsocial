package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.fitsocial.entity.PostComment;
import org.unibl.etf.fitsocial.repository.base.BaseSoftDeletableRepository;

public interface PostCommentRepository extends BaseSoftDeletableRepository<PostComment, Long> {
    Page<PostComment> findAllByPostIdAndDeletedAtIsNull(Long post_id, Pageable pageable);
}
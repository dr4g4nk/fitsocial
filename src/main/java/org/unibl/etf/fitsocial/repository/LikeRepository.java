package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.fitsocial.entity.PostLike;
import org.unibl.etf.fitsocial.repository.base.BaseSoftDeletableRepository;

public interface PostLikeRepository extends BaseSoftDeletableRepository<PostLike, Long> {
    Page<PostLike> findAllByPostIdAndDeletedAtIsNull(Long post_id, Pageable pageable);
}
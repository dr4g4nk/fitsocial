package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibl.etf.fitsocial.entity.Like;
import core.repository.BaseSoftDeletableRepository;

public interface LikeRepository extends BaseSoftDeletableRepository<Like, Long> {
    @Query("select l from Like l where l.post.id = :_id and l.deletedAt is null")
    Page<Like> findAllByPostIdAndDeletedAtIsNull(@Param("_id") Long _id, Pageable pageable);
}
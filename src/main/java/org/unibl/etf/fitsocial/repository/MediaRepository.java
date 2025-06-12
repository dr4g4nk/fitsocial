package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Pageable;
import org.unibl.etf.fitsocial.entity.PostMedia;
import org.unibl.etf.fitsocial.repository.base.BaseSoftDeletableRepository;

import java.util.List;

public interface PostMediaRepository extends BaseSoftDeletableRepository<PostMedia, Long> {
    List<PostMedia> findAllByPostId(Long post_id, Pageable pageable);
}
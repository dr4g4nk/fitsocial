package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Pageable;
import org.unibl.etf.fitsocial.entity.Media;
import core.repository.BaseSoftDeletableRepository;

import java.util.List;

public interface MediaRepository extends BaseSoftDeletableRepository<Media, Long> {
    List<Media> findAllByPostIdAndDeletedAtIsNull(Long _id, Pageable pageable);
}
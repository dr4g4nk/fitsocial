package org.unibl.etf.fitsocial.feed.media;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MediaRepository extends BaseSoftDeletableRepository<Media,Long> {
    List<Media> findAllByPostIdAndDeletedAtIsNull(Long _id, Pageable pageable);
}
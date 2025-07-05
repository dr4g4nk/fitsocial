package org.unibl.etf.fitsocial.feed.media;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.feed.post.Post;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends BaseSoftDeletableRepository<Media,Long> {
    List<Media> findAllByPostIdAndDeletedAtIsNull(Long _id, Pageable pageable);

    @Query("select m from Media m join Post p on p.id = m.post.id and p.isPublic = true and p.deletedAt is null where m.id = :id and m.deletedAt is null")
    Optional<Media> findByIdAndPostPublic(@Param("id")Long Id);


    @Transactional
    @Modifying
    @Query("update Media m set m.deletedAt = instant where m.post.id = :postId and m.id not in :ids")
    void deleteByPostIdAndIdNotIn(@Param("postId") Long postId, @Param("ids") Collection<Long> ids);
}
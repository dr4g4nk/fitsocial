package org.unibl.etf.fitsocial.feed.post;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibl.etf.fitsocial.feed.comment.Comment;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends BaseSoftDeletableRepository<Post,Long> {
    @Query(
            value = "SELECT COUNT(*) FROM Post p WHERE p.deletedAt IS NULL AND (p.isPublic = true or :onlyPublic = p.isPublic)"
    )
    long countPost(@Param("onlyPublic") boolean onlyPublic);

    @Query(
            value = "SELECT p " +
                    "FROM Post p " +
                    "WHERE p.deletedAt IS NULL AND (p.isPublic = true or :onlyPublic = p.isPublic) " +
                    "ORDER BY p.createdAt DESC ")
    Page<Post> findPublicPostsWithLikes(@Param("onlyPublic") boolean onlyPublic, Pageable pageable);

    @Query(
            value = "SELECT c.id AS id, c.post_id, c.user_id, c.content, c.created_at, c.updated_at, c.deleted_at " +
                    "FROM feed.comment c " +
                    "WHERE c.deleted_at IS NULL AND c.post_id IN :postIds " +
                    "ORDER BY c.post_id, c.created_at DESC",
            nativeQuery = true)
    List<Comment> findCommentsForPosts(@Param("postIds") List<Long> postIds);

    @Query("select p from Post p where p.deletedAt is null AND (p.isPublic = true or :onlyPublic = p.isPublic)")
    Page<Post> findAllByDeletedAtIsNullAndPublic(@Param("onlyPublic") Boolean onlyPublic, Pageable pageable);

    @Query("""
            select (count(p) > 0) from Post p
            where p.id = :aLong and p.deletedAt is null and (p.isPublic = true or p.isPublic = :onlyPublic)""")
    boolean existsByIdAndDeletedAtIsNullAndIsPublicTrueOrIsPublicEquals(@Param("aLong") Long aLong, boolean onlyPublic);
}
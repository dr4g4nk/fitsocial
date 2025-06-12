package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.PostComment}
 */
@Relation(collectionRelation = "items")
public record PostCommentDto(Long id, @NotNull Long postId, @NotNull Long userId,
                             @NotNull String content, Instant createdAt,
                             UserDto user) implements Serializable {

    @Relation(collectionRelation = "items")
    public record Create(Long id, @NotNull Long postId, @NotNull Long userId,
                         @NotNull String content, UserDto user) implements Serializable {}

    @Relation(collectionRelation = "items")
    public record Update(Long id, @NotNull Long postId, @NotNull Long userId,
                         @NotNull String content, UserDto user) implements Serializable {}
}
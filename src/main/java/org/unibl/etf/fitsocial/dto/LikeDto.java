package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.PostLike}
 */
@Relation(collectionRelation = "items")
public record PostLikeDto(Long id, @NotNull PostDto post, @NotNull UserDto user) implements Serializable {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull Long postId, @NotNull Long userId) implements Serializable {
    }

    @Relation(collectionRelation = "items")
    public record GetAll(@NotNull Long postId, @NotNull Long userId, @NotNull UserDto user) implements Serializable {
    }
}
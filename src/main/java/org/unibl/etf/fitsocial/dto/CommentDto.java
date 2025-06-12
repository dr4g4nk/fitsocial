package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

import java.time.Instant;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.Comment}
 */
@Relation(collectionRelation = "items")
public record CommentDto(Long id, @NotNull Long postId, @NotNull Long userId,
                             @NotNull String content, Instant createdAt,
                             UserDto user) implements IBasicDto {

    @Relation(collectionRelation = "items")
    public record Create(Long id, @NotNull Long postId, @NotNull Long userId,
                         @NotNull String content, UserDto user) implements ICreateDto {}

    @Relation(collectionRelation = "items")
    public record Update(Long id, @NotNull Long postId, @NotNull Long userId,
                         @NotNull String content, UserDto user) implements IUpdateDto {}

    public record List(Long id, @NotNull Long postId, @NotNull Long userId,
                             @NotNull String content, Instant createdAt,
                             UserDto user) implements IListDto {}
}
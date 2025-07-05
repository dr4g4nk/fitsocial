package org.unibl.etf.fitsocial.feed.comment;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

import org.unibl.etf.fitsocial.auth.user.UserDto;

@Relation(collectionRelation = "items")
public record CommentDto(Long id, @NotNull Long postId, @NotNull Long userId,
						 @NotNull String content, Instant createdAt,
						 UserDto user) implements IBasicDto {

	@Relation(collectionRelation = "items")
	public record Create(@NotNull Long postId, Long userId,
						 @NotNull String content) implements ICreateDto {}

	@Relation(collectionRelation = "items")
	public record Update(@NotNull String content) implements IUpdateDto {}

	public record List(Long id, @NotNull Long postId,
					   @NotNull String content, Instant createdAt,
					   UserDto user) implements IListDto {}
}
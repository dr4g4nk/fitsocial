package org.unibl.etf.fitsocial.feed.like;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import org.unibl.etf.fitsocial.feed.post.PostDto;
import org.unibl.etf.fitsocial.auth.user.UserDto;

@Relation(collectionRelation = "items")
public record LikeDto(
		Long id,
		PostDto post,
		UserDto user,
		Boolean active
) implements IBasicDto {
	@Relation(collectionRelation = "items")
	public record Create(
			Long id,
			PostDto post,
			UserDto user,
			Boolean active
	) implements ICreateDto {}

	@Relation(collectionRelation = "items")
	public record Update(
			Long id,
			PostDto post,
			UserDto user,
			Boolean active
	) implements IUpdateDto {}

	@Relation(collectionRelation = "items")
	public record List(
			Long id,
			PostDto post,
			UserDto user,
			Boolean active
	) implements IListDto {}
}
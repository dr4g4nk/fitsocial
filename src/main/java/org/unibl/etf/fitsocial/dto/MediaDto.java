package org.unibl.etf.fitsocial.dto;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Relation(collectionRelation = "items")
public record MediaDto(
	Long id,
	PostDto post,
	@NotNull 
	@Size(max = 500) 
	String mediaUrl,
	@NotNull 
	String mediaType,
	Integer ordering
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
			Long id,
			PostDto post,
			@NotNull
			@Size(max = 500)
			String mediaUrl,
			@NotNull
			String mediaType,
			Integer ordering
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
		Long id,
		PostDto post,
		@NotNull 
		@Size(max = 500) 
		String mediaUrl,
		@NotNull 
		String mediaType,
		Integer ordering
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		PostDto post,
		@NotNull
		@Size(max = 500)
		String mediaUrl,
		@NotNull
		String mediaType,
		Integer ordering
	) implements IListDto {}
}
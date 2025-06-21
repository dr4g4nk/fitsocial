package org.unibl.etf.fitsocial.auth.role;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Relation(collectionRelation = "items")
public record RoleDto(
	Long id,
	@NotNull 
	String name
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
			Long id,
			@NotNull
			String name
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
		Long id,
		@NotNull 
		String name
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		@NotNull
		String name
	) implements IListDto {}
}
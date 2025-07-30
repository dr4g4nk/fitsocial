package org.unibl.etf.fitsocial.feed.activity;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Relation(collectionRelation = "items")
public record ActivityDto(
	Long id,
	@NotNull 
	@Size(max = 50) 
	String type,
	@NotNull 
	Instant startTime,
	Instant endTime,
	Double distance,
	Integer steps,
	Integer calories
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
			@NotNull
			@Size(max = 50)
			String type,
			@NotNull
			Instant startTime,
			Instant endTime,
			Double distance,
			Integer steps,
			Integer calories
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
				Long id,
				@NotNull
				@Size(max = 50)
				String type,
				@NotNull
				Instant startTime,
				Instant endTime,
				Double distance,
				Integer steps,
				Integer calories
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
				Long id,
				@NotNull
				@Size(max = 50)
				String type,
				@NotNull
				Instant startTime,
				Instant endTime,
				Double distance,
				Integer steps,
				Integer calories
	) implements IListDto {}
}
package org.unibl.etf.fitsocial.feed.activity;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.unibl.etf.fitsocial.auth.user.UserDto;

@Relation(collectionRelation = "items")
public record ActivityDto(
	Long id,
	UserDto user,
	@NotNull 
	@Size(max = 50) 
	String activityType,
	@NotNull 
	Instant activityDate,
	Integer durationMinutes,
	Double distanceKm,
	Integer calories,
	String description
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
			Long id,
			UserDto user,
			@NotNull
			@Size(max = 50)
			String activityType,
			@NotNull
			Instant activityDate,
			Integer durationMinutes,
			Double distanceKm,
			Integer calories,
			String description
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
		Long id,
		UserDto user,
		@NotNull 
		@Size(max = 50) 
		String activityType,
		@NotNull 
		Instant activityDate,
		Integer durationMinutes,
		Double distanceKm,
		Integer calories,
		String description
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		UserDto user,
		@NotNull
		@Size(max = 50)
		String activityType,
		@NotNull
		Instant activityDate,
		Integer durationMinutes,
		Double distanceKm,
		Integer calories,
		String description
	) implements IListDto {}
}
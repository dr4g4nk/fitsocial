package org.unibl.etf.fitsocial.auth.userrole;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.unibl.etf.fitsocial.auth.role.RoleDto;
import org.unibl.etf.fitsocial.auth.user.UserDto;

@Relation(collectionRelation = "items")
public record UserRoleDto(
	Long id,
	UserDto user,
	RoleDto role
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
			Long id,
			UserDto user,
			RoleDto role
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
		Long id,
		UserDto user,
		RoleDto role
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		UserDto user,
		RoleDto role
	) implements IListDto {}
}
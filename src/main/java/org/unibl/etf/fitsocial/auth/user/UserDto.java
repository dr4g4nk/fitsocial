package org.unibl.etf.fitsocial.auth.user;

import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;


@Relation(collectionRelation = "items")
public record UserDto(Long id, @Size(max = 200) String firstName,
                      @Size(max = 200) String lastName) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull @Size(max = 200) String firstName, @NotNull @Size(max = 200) String lastName,
                         @NotNull @Size(max = 200) String username, @NotNull @Size(max = 100) String email,
                         @NotNull String password) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(@NotNull @Size(max = 200) String firstName,
                         @NotNull @Size(max = 200) String lastName) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(@NotNull Long id, @NotNull @Size(max = 200) String firstName,
                       @NotNull @Size(max = 200) String lastName) implements IListDto {
    }
}
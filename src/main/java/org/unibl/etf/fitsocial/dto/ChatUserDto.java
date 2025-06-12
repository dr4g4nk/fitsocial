package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.ChatUser}
 */
@Relation(collectionRelation = "items")
public record ChatUserDto(Long id, @NotNull ChatDto chat, @NotNull UserDto user, Boolean isAdmin)
                           implements IBasicDto, IListDto {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull Long chatId, @NotNull Long userId, Boolean isAdmin) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, @NotNull Long chatId, @NotNull Long userId, Boolean isAdmin) implements IUpdateDto {
    }
}
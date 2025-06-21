package org.unibl.etf.fitsocial.conversation.chatuser;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.conversation.chat.ChatDto;

@Relation(collectionRelation = "items")
public record ChatUserDto(
        Long id,
        ChatDto chat,
        UserDto user,
        Boolean isAdmin
) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull Long chatId, @NotNull Long userId, Boolean isAdmin) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, @NotNull Long chatId, @NotNull Long userId, Boolean isAdmin) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(
            Long id,
            ChatDto chat,
            UserDto user,
            Boolean isAdmin
    ) implements IListDto {
    }
}
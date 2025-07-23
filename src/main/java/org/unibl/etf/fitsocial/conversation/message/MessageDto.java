package org.unibl.etf.fitsocial.conversation.message;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;
import org.unibl.etf.fitsocial.conversation.chat.ChatDto;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserDto;

@Relation(collectionRelation = "items")
public record MessageDto(
        Long id,
        Long chatId,
        UserDto user,
        @NotNull
        String content,
        String label,
        Boolean my, AttachmentDto attachment,
        String subject,
        Boolean isGroup
) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull Long chatId, @NotNull String content,
                         AttachmentDto.Create attachment) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(@NotNull String content,
                         AttachmentDto.Create attachment) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(
            Long id,
            Long chatId,
            UserDto user,
            @NotNull
            String content,
            String label,
            Instant createdAt,
            Instant updatedAt,
            Boolean my, AttachmentDto attachment
    ) implements IListDto {
        public List(Long id,
                    Long chatId,
                    UserDto user,
                    @NotNull
                    String content,
                    String label,
                    Instant createdAt,
                    Instant updatedAt) {
            this(id, chatId, user, content, label, createdAt, updatedAt, false, null);
        }
    }
}
package org.unibl.etf.fitsocial.conversation.chat;

import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;
import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;

import java.time.Instant;
import java.util.Collections;

@Relation(collectionRelation = "items")
public record ChatDto(Long id, String subject, String text, Instant lastMessageTime,
                      java.util.List<UserDto> users) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(String subject, @NotNull @Size(min = 1) java.util.List<Long> userIds, String content,
                         AttachmentDto.Create attachment) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, String subject) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, String subject, String text, Instant lastMessageTime,
                       java.util.List<UserDto> users) implements IListDto {
        public List(Long id, String subject) {
            this(id, subject, null, null, Collections.emptyList());
        }

        public List(List dto, java.util.List<UserDto> users) {
            this(dto.id, dto.subject, dto.text, dto.lastMessageTime, users);
        }
    }
}
package org.unibl.etf.fitsocial.conversation.chat;

import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

import org.unibl.etf.fitsocial.auth.user.UserDto;

@Relation(collectionRelation = "items")
public record ChatDto(Long id, String subject, UserDto createdBy) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(String subject, @NotNull @Size(min = 1) java.util.List<Long> userIds) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, String subject) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, String subject, UserDto createdBy) implements IListDto {
    }
}
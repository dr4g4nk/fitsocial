package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.Message}
 */
@Relation(collectionRelation = "items")
public record MessageDto(Long id, @NotNull UserDto user, @NotNull Long chatId, @NotNull String content, @NotNull Instant createdAt, List<AttachmentDto> attachments) implements IBasicDto, IListDto {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull Long chatId, @NotNull String content, List<AttachmentDto.Create> attachments) implements ICreateDto {
    }
    @Relation(collectionRelation = "items")
    public record Update(@NotNull Long chatId, @NotNull String content) implements IUpdateDto {
    }
}
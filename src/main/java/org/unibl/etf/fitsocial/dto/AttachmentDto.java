package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

import java.time.Instant;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.Attachment}
 */
@Relation(collectionRelation = "items")
public record AttachmentDto(Long id, @NotNull Long messageId, @NotNull String contentType, String fileUrl,
                            @NotNull Instant createdAt) implements IBasicDto, IListDto {
    @Relation(collectionRelation = "items")
    public record Create(@NotNull Long messageId, @NotNull String contentType,  String fileUrl
    ) implements ICreateDto {}

    @Relation(collectionRelation = "items")
    public record Update(Long id, @NotNull Long messageId, @NotNull String contentType,  String fileUrl
    ) implements IUpdateDto {}
}

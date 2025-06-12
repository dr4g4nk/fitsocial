package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.Chat}
 */
@Relation(collectionRelation = "items")
public record ChatDto(Long id, String subject) implements IBasicDto, IListDto {
    @Relation(collectionRelation = "items")
    public record Create(String subject, @NotNull @Size(min = 1) java.util.List<Long> userIds) implements ICreateDto{
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, String subject) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, String subject, @NotNull UserDto createdBy) implements IListDto {}
}
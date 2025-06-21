package org.unibl.etf.fitsocial.feed.media;

import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
public record MediaDto(Long id, Long postId, Integer order, String contentType) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(Long id, Long postId, Integer order, String contentType) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, Long postId, Integer order, String contentType) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, Long postId, Integer order, String contentType) implements IListDto {
    }
}
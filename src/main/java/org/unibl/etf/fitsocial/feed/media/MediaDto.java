package org.unibl.etf.fitsocial.feed.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.web.multipart.MultipartFile;

@Relation(collectionRelation = "items")
public record MediaDto(Long id, Long postId, Integer order, String mimeType) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(Long postId, Integer order, String mimeType, MultipartFile file, @JsonIgnore String mediaUrl) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(Long id, Long postId, Integer order, String mimeType, MultipartFile file) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, Long postId, Integer order, String mimeType) implements IListDto {
    }
}
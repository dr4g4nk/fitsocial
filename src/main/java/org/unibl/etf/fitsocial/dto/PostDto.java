

package org.unibl.etf.fitsocial.dto;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import java.time.Instant;

@Relation(collectionRelation = "items")
public record PostDto(Long id, String content, UserDto user, boolean isPublic) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(String content, boolean isPublic, java.util.List<MediaDto> media) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(String content, boolean isPublic) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, String content, UserDto user, Instant createdAt, boolean isPublic,
                       Long likeCount, java.util.List<CommentDto.List> lastComments) implements IListDto {
    }
}
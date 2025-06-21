package org.unibl.etf.fitsocial.feed.post;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.feed.activity.ActivityDto;
import org.unibl.etf.fitsocial.feed.comment.CommentDto;
import org.unibl.etf.fitsocial.feed.media.MediaDto;

@Relation(collectionRelation = "items")
public record PostDto(
        Long id,
        UserDto author,
        ActivityDto activity,
        String content,
        Boolean isPublic,
        Long likeCount,
        Instant createdAt,
        java.util.List<MediaDto> media
) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(
            Long id,
            ActivityDto activity,
            String content,
            Boolean isPublic,
            java.util.List<MediaDto.Create> media
    ) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(
            Long id,
            ActivityDto activity,
            String content,
            Boolean isPublic,
            Long likeCount
    ) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(
            Long id,
            String content,
            UserDto author,
            Instant createdAt,
            Boolean isPublic,
            Long likeCount,
            java.util.List<MediaDto> media
    ) implements IListDto {
    }
}
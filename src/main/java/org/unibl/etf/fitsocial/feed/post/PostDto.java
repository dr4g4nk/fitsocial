package org.unibl.etf.fitsocial.feed.post;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

import java.time.Instant;

import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.feed.activity.ActivityDto;
import org.unibl.etf.fitsocial.feed.media.MediaDto;

@Relation(collectionRelation = "items")
public record PostDto(Long id, UserDto author, ActivityDto activity, String content, Boolean isPublic, Long likeCount,
                      Instant createdAt, java.util.List<MediaDto> media) implements IBasicDto {
    @Relation(collectionRelation = "items")
    public record Create(String content, Boolean isPublic,
                         java.util.List<MediaDto.Create> media, ActivityDto.Create activity) implements ICreateDto {
    }

    @Relation(collectionRelation = "items")
    public record Update(String content, Boolean isPublic,
                         java.util.List<MediaDto.Update> media, ActivityDto.Update activity) implements IUpdateDto {
    }

    @Relation(collectionRelation = "items")
    public record List(Long id, String content, UserDto author, Instant createdAt, Boolean isPublic, Long likeCount, Long commentCount, Boolean isLiked,
                       java.util.List<MediaDto> media, ActivityDto activity) implements IListDto {
    }
}
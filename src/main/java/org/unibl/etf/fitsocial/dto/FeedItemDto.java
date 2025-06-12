package org.unibl.etf.fitsocial.dto;

import org.unibl.etf.fitsocial.dto.base.IDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public record FeedItemDto(
        Long postId,
        String content,
        Long userId,
        UserPostDto user,
        Timestamp createdAt,
        Long likesCount,
        List<PostCommentDto> lastComments
) implements IDto, Serializable {}
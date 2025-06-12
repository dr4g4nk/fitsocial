package org.unibl.etf.fitsocial.dto;

import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Relation(collectionRelation = "items")
public record FeedItemDto(
        Long postId,
        String content,
        Long userId,
        Long likesCount,
        List<CommentDto> lastComments
) implements Serializable {}
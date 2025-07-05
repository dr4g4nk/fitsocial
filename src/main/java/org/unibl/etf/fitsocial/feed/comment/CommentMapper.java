package org.unibl.etf.fitsocial.feed.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.feed.post.PostMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PostMapper.class, UserMapper.class})
public interface CommentMapper extends IMapper<
    Comment,
    CommentDto,
    CommentDto.List,
    CommentDto.Update,
    CommentDto.Create
> {
    @Override
    @Mapping(target = "postId", source = "post.id")
    CommentDto.List toListDto(Comment entity);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    CommentDto toDto(Comment entity);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "post.id", source = "postId")
    Comment fromCreateDto(CommentDto.Create create);
}
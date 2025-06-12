package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.fitsocial.dto.PostCommentDto;
import org.unibl.etf.fitsocial.entity.PostComment;
import org.unibl.etf.fitsocial.mapper.base.IMapper;

@Mapper(componentModel = "spring")
public interface PostCommentMapper extends IMapper<PostComment, PostCommentDto,PostCommentDto,PostCommentDto.Update, PostCommentDto.Create> {
    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    PostCommentDto toGetAllDto(PostComment entity);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    PostCommentDto toGetByIdDto(PostComment entity);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "post.id", source = "postId")
    PostComment updateDto2Entity(PostCommentDto.Update update);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "post.id", source = "postId")
    PostComment createDto2Entity(PostCommentDto.Create create);

}

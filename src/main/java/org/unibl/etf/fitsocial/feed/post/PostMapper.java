package org.unibl.etf.fitsocial.feed.post;

import org.mapstruct.*;
import core.mapper.IMapper;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.feed.activity.ActivityMapper;
import org.unibl.etf.fitsocial.feed.media.MediaMapper;
import org.unibl.etf.fitsocial.feed.post.PostDto.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, MediaMapper.class, ActivityMapper.class})
public interface PostMapper extends IMapper<
    Post,
    PostDto,
        PostDto.List,
    PostDto.Update,
    PostDto.Create
> {
    @Override
    @Mapping(target = "author", source = "user")
    PostDto toDto(Post entity);

    @Override
    @Mapping(target = "author", source = "user")
    PostDto.List toListDto(Post entity);

    @Override
    @Mapping(target = "media", ignore = true)
    Post fromCreateDto(PostDto.Create dto);

    @Override
    @Mapping(target = "media", ignore = true)
    Post partialUpdate(PostDto.Update dto, @MappingTarget Post entity);
}
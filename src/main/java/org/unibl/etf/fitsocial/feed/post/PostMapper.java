package org.unibl.etf.fitsocial.feed.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.feed.media.MediaMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, MediaMapper.class})
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
}
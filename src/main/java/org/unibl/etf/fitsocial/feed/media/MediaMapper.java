package org.unibl.etf.fitsocial.feed.media;

import org.mapstruct.*;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper extends IMapper<
    Media,
    MediaDto,
    MediaDto.List,
    MediaDto.Update,
    MediaDto.Create
> {
    @Override
    @Mapping(target = "postId", source = "post.id")
    MediaDto toDto(Media entity);

    @Override
    @Mapping(target = "postId", source = "post.id")
    MediaDto.List toListDto(Media entity);

    @Override
    @Mapping(target = "post.id", source = "postId")
    Media fromCreateDto(MediaDto.Create dto);

    @Override
    @Mapping(target = "post.id", source = "postId")
    Media partialUpdate(MediaDto.Update dto, @MappingTarget Media entity);
}
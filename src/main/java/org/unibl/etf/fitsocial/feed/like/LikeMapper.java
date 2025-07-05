package org.unibl.etf.fitsocial.feed.like;

import core.mapper.IMapper;
import org.mapstruct.*;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.feed.post.PostMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, PostMapper.class})
public interface LikeMapper extends IMapper<
        Like,
        LikeDto,
        LikeDto.List,
        LikeDto.Update,
        LikeDto.Create
        > {
    @Override
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "userId", source = "user.id")
    LikeDto toDto(Like entity);

    @Override
    @Mapping(target = "postId", source = "post.id")
    LikeDto.List toListDto(Like entity);

    @Override
    @Mapping(target = "post.id", source = "postId")
    @Mapping(target = "user.id", source = "userId")
    Like fromCreateDto(LikeDto.Create dto);

    @Override
    @Mapping(target = "post.id", source = "postId")
    @Mapping(target = "user.id", source = "userId")
    Like toEntity(LikeDto dto);

    @Override
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "userId", source = "user.id")
    LikeDto.Update toUpdateDto(Like entity);

    @Override
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "userId", source = "user.id")
    LikeDto.Create toCreateDto(Like entity);

    @Override
    @Mapping(target = "post.id", source = "postId")
    @Mapping(target = "user.id", source = "userId")
    Like partialUpdate(LikeDto.Update dto, @MappingTarget Like entity);
}
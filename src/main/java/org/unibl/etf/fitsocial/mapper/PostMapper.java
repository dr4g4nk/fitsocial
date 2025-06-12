/*package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.PostDto;
import org.unibl.etf.fitsocial.entity.Post;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface PostMapper extends IMapper<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create> {
    @Override
    @Mapping(target = "isPublic", source = "public")
    PostDto toDto(Post entity);

    @Override
    @Mapping(target = "public", source = "isPublic")
    @Mapping(target = "user.id", source = "userId")
    Post fromCreateDto(PostDto.Create dto);

    @Override
    @Mapping(target = "public", source = "isPublic")
    Post partialUpdate(PostDto.Update dto, @MappingTarget Post entity);

}*/
package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.PostDto;
import org.unibl.etf.fitsocial.entity.Post;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper extends IMapper<
        Post,
        PostDto,
        PostDto.List,
        PostDto.Update,
        PostDto.Create
        > {

    @Override
    PostDto toDto(Post entity);

    @Override
    Post fromCreateDto(PostDto.Create dto);

    @Override
    Post partialUpdate(PostDto.Update dto, @MappingTarget Post entity);
}
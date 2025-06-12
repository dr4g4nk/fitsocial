package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.fitsocial.dto.PostLikeDto;
import org.unibl.etf.fitsocial.entity.PostLike;
import org.unibl.etf.fitsocial.mapper.base.IMapper;

@Mapper(componentModel = "spring")
public interface PostLikeMapper extends IMapper<PostLike, PostLikeDto.GetAll, PostLikeDto, PostLikeDto, PostLikeDto.Create> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    PostLikeDto.GetAll toGetAllDto(PostLike entity);
}

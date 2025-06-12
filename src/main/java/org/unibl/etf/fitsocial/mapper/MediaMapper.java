package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.Mapper;
import org.unibl.etf.fitsocial.dto.PostMediaDto;
import org.unibl.etf.fitsocial.entity.PostMedia;
import org.unibl.etf.fitsocial.mapper.base.IMapper;

@Mapper(componentModel = "spring")
public interface PostMediaMapper extends IMapper<PostMedia, PostMediaDto, PostMediaDto, PostMediaDto, PostMediaDto> {
}

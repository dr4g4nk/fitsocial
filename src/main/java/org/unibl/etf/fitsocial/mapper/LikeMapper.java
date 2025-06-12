package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.unibl.etf.fitsocial.dto.LikeDto;
import org.unibl.etf.fitsocial.entity.Like;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LikeMapper extends IMapper<
    Like,
    LikeDto,
    LikeDto.List,
    LikeDto.Update,
    LikeDto.Create
> {
}
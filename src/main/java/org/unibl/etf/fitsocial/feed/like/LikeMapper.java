package org.unibl.etf.fitsocial.feed.like;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
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
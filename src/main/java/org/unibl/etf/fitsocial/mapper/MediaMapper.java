package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.unibl.etf.fitsocial.dto.MediaDto;
import org.unibl.etf.fitsocial.entity.Media;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper extends IMapper<
    Media,
    MediaDto,
    MediaDto.List,
    MediaDto.Update,
    MediaDto.Create
> {
}
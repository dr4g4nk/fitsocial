package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.unibl.etf.fitsocial.dto.ActivityDto;
import org.unibl.etf.fitsocial.entity.Activity;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapper extends IMapper<
    Activity,
    ActivityDto,
    ActivityDto.List,
    ActivityDto.Update,
    ActivityDto.Create
> {
}
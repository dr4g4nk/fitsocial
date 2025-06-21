package org.unibl.etf.fitsocial.auth.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends IMapper<
    User,
    UserDto,
    UserDto.List,
    UserDto.Update,
    UserDto.Create
> {
}
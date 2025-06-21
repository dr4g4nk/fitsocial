package org.unibl.etf.fitsocial.auth.role;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper extends IMapper<
    Role,
    RoleDto,
    RoleDto.List,
    RoleDto.Update,
    RoleDto.Create
> {
}
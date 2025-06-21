package org.unibl.etf.fitsocial.auth.userrole;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleMapper extends IMapper<
    UserRole,
    UserRoleDto,
    UserRoleDto.List,
    UserRoleDto.Update,
    UserRoleDto.Create
> {
}
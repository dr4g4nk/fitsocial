package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.UserDto;
import org.unibl.etf.fitsocial.entity.User;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends IMapper<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create> {
}

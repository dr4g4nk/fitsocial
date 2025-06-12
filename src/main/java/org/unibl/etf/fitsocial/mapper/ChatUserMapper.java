package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.ChatUserDto;
import org.unibl.etf.fitsocial.entity.ChatUser;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ChatMapper.class, UserMapper.class})
public interface ChatUserMapper extends IMapper<ChatUser, ChatUserDto, ChatUserDto, ChatUserDto.Update, ChatUserDto.Create> {

}
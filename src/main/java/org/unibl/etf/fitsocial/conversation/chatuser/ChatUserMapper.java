package org.unibl.etf.fitsocial.conversation.chatuser;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatUserMapper extends IMapper<
    ChatUser,
    ChatUserDto,
    ChatUserDto.List,
    ChatUserDto.Update,
    ChatUserDto.Create
> {
}
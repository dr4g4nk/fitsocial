package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.MessageDto;
import org.unibl.etf.fitsocial.entity.Message;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ChatUserMapper.class})
public interface MessageMapper  extends IMapper<Message, MessageDto, MessageDto, MessageDto.Update, MessageDto.Create> {
    @Override
    @Mapping(target = "user", source = "chatUser.user")
    @Mapping(target = "chatId", source = "chatUser.chat.id")
    MessageDto toDto(Message entity);

    @Override
    @Mapping(target = "user", source = "chatUser.user")
    @Mapping(target = "chatId", source = "chatUser.chat.id")
    MessageDto toListDto(Message entity);
}
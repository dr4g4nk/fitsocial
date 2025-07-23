package org.unibl.etf.fitsocial.conversation.message;

import org.mapstruct.*;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper extends IMapper<
    Message,
    MessageDto,
    MessageDto.List,
    MessageDto.Update,
    MessageDto.Create
> {
    @Override
    @Mapping(target = "user", source = "chatUser.user")
    @Mapping(target = "chatId", source = "chatUser.chat.id")
    @Mapping(target = "subject", source = "chatUser.chat.subject")
    MessageDto toDto(Message entity);

    @Override
    @Mapping(target = "user", source = "chatUser.user")
    @Mapping(target = "chatId", source = "chatUser.chat.id")
    MessageDto.List toListDto(Message entity);
}
package org.unibl.etf.fitsocial.conversation.message;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
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
    MessageDto toDto(Message entity);

    @Override
    @Mapping(target = "user", source = "chatUser.user")
    @Mapping(target = "chatId", source = "chatUser.chat.id")
    MessageDto.List toListDto(Message entity);
}
package org.unibl.etf.fitsocial.conversation.chat;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper extends IMapper<
    Chat,
    ChatDto,
    ChatDto.List,
    ChatDto.Update,
    ChatDto.Create
> {
}
package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.ChatDto;
import org.unibl.etf.fitsocial.entity.Chat;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface ChatMapper extends IMapper<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create> {
}
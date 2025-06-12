package org.unibl.etf.fitsocial.mapper;

import org.mapstruct.*;
import org.unibl.etf.fitsocial.dto.AttachmentDto;
import org.unibl.etf.fitsocial.entity.Attachment;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {MessageMapper.class})
public interface AttachmentMapper extends IMapper<Attachment, AttachmentDto, AttachmentDto, AttachmentDto.Update, AttachmentDto.Create> {
    @Override
    @Mapping(target="message.id", source = "messageId")
    Attachment fromCreateDto(AttachmentDto.Create dto);

    @Override
    @Mapping(target="message.id", source = "messageId")
    Attachment partialUpdate(AttachmentDto.Update dto, @MappingTarget Attachment entity);
}
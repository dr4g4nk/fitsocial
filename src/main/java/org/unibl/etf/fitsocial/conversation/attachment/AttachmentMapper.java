package org.unibl.etf.fitsocial.conversation.attachment;

import org.mapstruct.*;
import core.mapper.IMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper extends IMapper<
        Attachment,
        AttachmentDto,
        AttachmentDto.List,
        AttachmentDto.Update,
        AttachmentDto.Create
        > {
}
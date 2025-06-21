package org.unibl.etf.fitsocial.conversation.attachment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class AttachmentService extends BaseSoftDeletableServiceImpl<
    Attachment,
    AttachmentDto,
    AttachmentDto.List,
    AttachmentDto.Update,
    AttachmentDto.Create,
    Long
> {
    public AttachmentService(
        BaseSoftDeletableRepository<Attachment, Long> repository,
        IMapper<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create> mapper
    ) {
        super(repository, mapper);
    }
}
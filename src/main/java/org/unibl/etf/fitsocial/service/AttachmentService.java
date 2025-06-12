package org.unibl.etf.fitsocial.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.AttachmentDto;
import org.unibl.etf.fitsocial.entity.Attachment;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class AttachmentService extends BaseSoftDeletableServiceImpl<Attachment, AttachmentDto, AttachmentDto, AttachmentDto.Update, AttachmentDto.Create, Long> {
    public AttachmentService(BaseSoftDeletableRepository<Attachment, Long> repository, IMapper<Attachment, AttachmentDto, AttachmentDto, AttachmentDto.Update, AttachmentDto.Create> mapper) {
        super(repository, mapper);
    }
}

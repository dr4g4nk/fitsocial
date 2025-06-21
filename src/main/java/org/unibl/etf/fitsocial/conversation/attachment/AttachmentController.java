package org.unibl.etf.fitsocial.conversation.attachment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController extends BaseController<
    Attachment,
    AttachmentDto,
    AttachmentDto.List,
    AttachmentDto.Update,
    AttachmentDto.Create,
    Long
> {
    public AttachmentController(BaseSoftDeletableServiceImpl<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create, Long> service) {
        super(service);
    }
}
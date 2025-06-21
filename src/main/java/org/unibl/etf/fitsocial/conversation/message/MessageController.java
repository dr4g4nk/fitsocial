package org.unibl.etf.fitsocial.conversation.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;

@RestController
@RequestMapping("/api/message")
public class MessageController extends BaseController<
    Message,
    MessageDto,
    MessageDto.List,
    MessageDto.Update,
    MessageDto.Create,
    Long
> {
    public MessageController(BaseSoftDeletableServiceImpl<Message, MessageDto, MessageDto.List, MessageDto.Update, MessageDto.Create, Long> service) {
        super(service);
    }
}
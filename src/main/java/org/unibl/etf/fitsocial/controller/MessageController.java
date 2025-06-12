package org.unibl.etf.fitsocial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import org.unibl.etf.fitsocial.dto.MessageDto;
import org.unibl.etf.fitsocial.entity.Message;
import org.unibl.etf.fitsocial.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController extends BaseController<Message, MessageDto, MessageDto, MessageDto.Update, MessageDto.Create, Long> {
    public MessageController(MessageService service) {
        super(service);
    }
}

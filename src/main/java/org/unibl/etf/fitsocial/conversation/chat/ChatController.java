package org.unibl.etf.fitsocial.conversation.chat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;

@RestController
@RequestMapping("/api/chat")
public class ChatController extends BaseController<
    Chat,
    ChatDto,
    ChatDto.List,
    ChatDto.Update,
    ChatDto.Create,
    Long
> {
    public ChatController(BaseSoftDeletableServiceImpl<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create, Long> service) {
        super(service);
    }
}
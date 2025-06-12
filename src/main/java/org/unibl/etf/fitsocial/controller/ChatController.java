package org.unibl.etf.fitsocial.controller;

import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import org.unibl.etf.fitsocial.dto.ChatDto;
import org.unibl.etf.fitsocial.entity.Chat;

@RestController
@RequestMapping("/api/chats")
public class ChatController extends BaseController<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create, Long> {
    public ChatController(BaseSoftDeletableServiceImpl<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create, Long> service) {
        super(service);
    }
}

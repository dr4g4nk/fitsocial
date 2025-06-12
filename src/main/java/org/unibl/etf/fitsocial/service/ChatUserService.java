package org.unibl.etf.fitsocial.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.ChatUserDto;
import org.unibl.etf.fitsocial.entity.ChatUser;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class ChatUserService extends BaseSoftDeletableServiceImpl<ChatUser, ChatUserDto, ChatUserDto, ChatUserDto.Update, ChatUserDto.Create, Long> {
    public ChatUserService(BaseSoftDeletableRepository<ChatUser, Long> repository, IMapper<ChatUser, ChatUserDto, ChatUserDto, ChatUserDto.Update, ChatUserDto.Create> mapper) {
        super(repository, mapper);
    }
}

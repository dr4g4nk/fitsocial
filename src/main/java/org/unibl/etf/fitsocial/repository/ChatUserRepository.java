package org.unibl.etf.fitsocial.repository;

import org.unibl.etf.fitsocial.entity.ChatUser;
import core.repository.BaseSoftDeletableRepository;

import java.util.Optional;

public interface ChatUserRepository extends BaseSoftDeletableRepository<ChatUser, Long> {
    Optional<ChatUser> findFirstByChatIdAndUserIdAndDeletedAtIsNull(Long chatId, Long userId);
}
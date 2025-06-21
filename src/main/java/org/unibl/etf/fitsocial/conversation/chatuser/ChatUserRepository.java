package org.unibl.etf.fitsocial.conversation.chatuser;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends BaseSoftDeletableRepository<ChatUser,Long> {
    Optional<ChatUser> findFirstByChatIdAndUserIdAndDeletedAtIsNull(Long chatId, Long userId);
    @Query("select c from ChatUser c where c.user.id = :userId and c.deletedAt is null")
    List<ChatUser> findAllByUserIdAndDeletedAtIsNull(@Param("userId") Long userId);
}
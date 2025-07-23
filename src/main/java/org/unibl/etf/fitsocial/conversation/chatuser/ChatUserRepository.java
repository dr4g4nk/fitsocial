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

    @Query("""
            select cu from ChatUser cu where cu.deletedAt is null and cu.user.id != :userId and cu.chat.id in :chatId
            """)
    List<ChatUser> findAllByChatIdInAndNotUserId(@Param("chatId") List<Long> chatId, @Param("userId") Long userId);

    @Query("""
            select count(*) > 2 from ChatUser cu where cu.chat.id = :chatId and cu.deletedAt is null
            """)
    Boolean isGroupChat(@Param("chatId") Long chatId);
}
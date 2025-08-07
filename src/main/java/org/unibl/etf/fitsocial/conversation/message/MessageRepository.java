package org.unibl.etf.fitsocial.conversation.message;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibl.etf.fitsocial.conversation.message.projection.MessageWithChatId;

import java.util.List;

public interface MessageRepository extends BaseSoftDeletableRepository<Message, Long> {

    @Query("""
            select m.id as id, m.content as content, m.chatUser.chat.id as chatId, m.label as label, m.chatUser.user.firstName as sender
            from Message m where m.id in (
            select MAX(m2.id) from Message m2
            where m2.chatUser.chat.id in :chatId and m2.deletedAt is null
            group by m2.chatUser.chat.id
            )
            """)
    List<MessageWithChatId> findLastMessagesForChats(@Param("chatId") List<Long> chatId);

    @Query("""
            select m from Message m where m.chatUser.chat.id = :chatId and m.deletedAt is null
            """)
    Page<Message> findAllByChatId(@Param("chatId") Long chatId, Pageable pageable);
}
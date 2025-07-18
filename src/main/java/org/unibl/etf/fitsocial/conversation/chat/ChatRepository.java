package org.unibl.etf.fitsocial.conversation.chat;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends BaseSoftDeletableRepository<Chat,Long> {
    @Query("""
             SELECT c FROM Chat c JOIN ChatUser cu ON c.id = cu.chat.id AND cu.deletedAt is null WHERE c.deletedAt is null and cu.user.id = :userId
             """)
    Page<Chat> findAllChatsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
             SELECT c FROM Chat c JOIN ChatUser cu ON c.id = cu.chat.id AND cu.deletedAt is null WHERE c.deletedAt is null and cu.user.id = :userId
             and (lower(c.subject) like lower(concat('%',:name,'%')) or
                c.id in (select cu2.chat.id from ChatUser cu2 where cu2.user.id != :userId
                and lower(concat(cu2.user.firstName, ' ', cu2.user.lastName)) like lower(concat('%', :name, '%')))
             )
             """)
    Page<Chat> findAllChatsByUserIdAndName(@Param("userId") Long userId, @Param("name") String name, Pageable pageable);
}
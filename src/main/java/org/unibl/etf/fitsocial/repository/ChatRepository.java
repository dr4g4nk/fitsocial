package org.unibl.etf.fitsocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibl.etf.fitsocial.entity.Chat;
import core.repository.BaseSoftDeletableRepository;

public interface ChatRepository extends BaseSoftDeletableRepository<Chat, Long>{
    @Query("SELECT c FROM Chat c JOIN ChatUser cu ON c.id = cu.chat.id AND cu.deletedAt is null WHERE c.deletedAt is null and cu.user.id = :userId")
    Page<Chat> findAllChatsByUserId(@Param("userId") Long userId, Pageable pageable);
}
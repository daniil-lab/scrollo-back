package com.inst.base.repository.chat;

import com.inst.base.entity.chat.Chat;
import com.inst.base.entity.follow.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Page<Chat> findByStarterIdOrAcceptorId(UUID userId, UUID receiverId, Pageable pageable);
}

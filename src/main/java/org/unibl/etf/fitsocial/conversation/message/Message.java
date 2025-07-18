package org.unibl.etf.fitsocial.conversation.message;

import jakarta.persistence.*;
import core.entity.SoftDeletableEntity;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.unibl.etf.fitsocial.conversation.attachment.Attachment;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUser;
import org.unibl.etf.fitsocial.feed.media.Media;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "message", schema = "conversation")
public class Message extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_user_id", nullable = false)
    private ChatUser chatUser;
    @NotNull
    @Column(name = "content")
    private String content;
    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    // Getteri i setteri

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatUser getChatUser() {
        return this.chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
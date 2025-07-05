package org.unibl.etf.fitsocial.conversation.attachment;

import jakarta.persistence.*;
import core.entity.SoftDeletableEntity;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.unibl.etf.fitsocial.conversation.message.Message;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "attachment", schema = "conversation")
public class Attachment extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
    @NotNull
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_url")
    private String fileUrl;
    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    // Getteri i setteri

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
}
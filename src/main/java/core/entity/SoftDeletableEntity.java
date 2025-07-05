package core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.FilterDef;

import java.time.Instant;

@MappedSuperclass
@FilterDef(
        name = "sotfDeleteFilter",
        defaultCondition = "deleted_at IS NULL"
)
public abstract class SoftDeletableEntity{
    @Column(name = "deleted_at")
    protected Instant deletedAt;
    public abstract Instant getDeletedAt();

    public abstract void setDeletedAt(Instant deletedAt);
}

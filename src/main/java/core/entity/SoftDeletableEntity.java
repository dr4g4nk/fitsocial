package org.unibl.etf.fitsocial.entity.base;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.Instant;

public abstract class SoftDeletableEntity<ID extends Serializable> extends BaseEntity<ID> {
    public abstract Instant getDeletedAt();

    public abstract void setDeletedAt(Instant deletedAt);
}

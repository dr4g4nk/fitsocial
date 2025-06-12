package org.unibl.etf.fitsocial.entity.base;

import java.io.Serializable;

public abstract class BaseEntity<ID extends Serializable> {
    public abstract ID getId();
    public abstract void setId(ID value);
}

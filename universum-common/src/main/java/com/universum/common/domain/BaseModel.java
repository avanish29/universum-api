package com.universum.common.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel implements Serializable {
	private static final long serialVersionUID = -267213456825280495L;

	public abstract Long getId();

    @Column(nullable = false)
    protected Boolean deleted = Boolean.FALSE;

    @Column(unique=true, nullable=false, updatable=false, length = 36)
    protected String guid = UUID.randomUUID().toString();

    @Column(nullable = false)
    @Version
    protected int version;
}

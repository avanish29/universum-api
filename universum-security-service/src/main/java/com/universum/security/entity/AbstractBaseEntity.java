package com.universum.security.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public class AbstractBaseEntity implements Serializable {
	private static final long serialVersionUID = -5171115289717864524L;
	
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;

	protected LocalDateTime created;

	protected Boolean deleted;
	
	@Column(unique=true, nullable=false, updatable=false, length = 36)
	protected String guid;
	
	@Column(name="last_update")
	protected LocalDateTime lastUpdate;

	@Column(nullable = false)
	@Version
	protected int version;
	
	public AbstractBaseEntity() {
		super();
		this.created = LocalDateTime.now();
		this.lastUpdate = LocalDateTime.now();
		this.deleted = false;
		this.guid = UUID.randomUUID().toString();
	}
}

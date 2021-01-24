package com.universum.common.jpa.domin;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


import lombok.Data;

@MappedSuperclass
@Data
public class AbstractBaseEntity implements Serializable {
	private static final long serialVersionUID = -5171115289717864524L;
	
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private LocalDateTime created;
	
	private Boolean deleted;
	
	@Column(unique=true, nullable=false, updatable=false, length = 36)
	private String guid;
	
	@Column(name="last_update")
	private LocalDateTime lastUpdate;
	
	public AbstractBaseEntity() {
		super();
		this.created = LocalDateTime.now();
		this.lastUpdate = LocalDateTime.now();
		this.deleted = false;
		this.guid = UUID.randomUUID().toString();
	}
}

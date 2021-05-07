package com.universum.common.jpa.domin;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractBaseEntity implements Serializable {
	private static final long serialVersionUID = -5171115289717864524L;
	
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy=GenerationType.AUTO)
	@NotAudited
	protected Long id;
	
	@Column(nullable = false, updatable = false)
	@CreatedDate
	protected LocalDateTime createdOn = LocalDateTime.now();
	
	@Column(nullable = false, updatable = false)
	@CreatedBy
	protected String createdBy;
	
	@NotAudited
	protected Boolean deleted = Boolean.FALSE;
	
	@Column(unique=true, nullable=false, updatable=false, length = 36)
	@NotAudited
	@EqualsAndHashCode.Include
	protected String guid = UUID.randomUUID().toString();
	
	@LastModifiedDate
	protected LocalDateTime updatedOn = LocalDateTime.now();
	
	@Column(nullable = false)
	@LastModifiedBy
	protected String updatedBy;
	
	@Column(nullable = false)
	@Version
	@NotAudited
	protected int version;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		AbstractBaseEntity that = (AbstractBaseEntity) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}

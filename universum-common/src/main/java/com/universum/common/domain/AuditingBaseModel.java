package com.universum.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingBaseModel extends BaseModel {
	private static final long serialVersionUID = -5959621408942149175L;

	@Column(nullable = false, updatable = false)
    @CreatedDate
    protected LocalDateTime createdOn = LocalDateTime.now();

    @Column(nullable = false, updatable = false)
    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    protected LocalDateTime updatedOn = LocalDateTime.now();

    @Column(nullable = false)
    @LastModifiedBy
    protected String updatedBy;
}

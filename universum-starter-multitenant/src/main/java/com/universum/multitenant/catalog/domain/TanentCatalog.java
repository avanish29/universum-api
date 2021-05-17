package com.universum.multitenant.catalog.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tanent_catalog",uniqueConstraints = { @UniqueConstraint(columnNames = { "tenant_name", "guid" }) })
public class TanentCatalog implements Serializable {
	private static final long serialVersionUID = -1518252051790355980L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long tenantId;
	
	@Column(unique = true, nullable = false, updatable = false, length = 36)
    protected String guid = UUID.randomUUID().toString();
	
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
    
    @Size(max = 50)
    @Column(name = "tenant_name", nullable = false, unique = true)
    private String tenantName;

    @Size(max = 100)
    @Column(name = "url",nullable = false)
    private String url;

    @Size(max = 50)
    @Column(name = "user_name",nullable = false)
    private String userName;
    
    @Size(max = 100)
    @Column(name = "password",nullable = false)
    private String password;
    
    @Size(max = 100)
    @Column(name = "driver_class",nullable = false)
    private String driverClass;
    
    @Size(max = 10)
    @Column(name = "status",nullable = false)
    private Boolean status;
}

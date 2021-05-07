package com.universum.service.security.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;

import com.universum.common.jpa.domin.enums.RoleType;
import com.universum.common.jpa.domin.model.AuditingBaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AppRole")
@Table(name = "roles", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "deleted is NULL or deleted != true")
@SequenceGenerator(name = Role.ROLE_SEQUENCE_GENERATOR_NAME, sequenceName = Role.ROLE_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class Role extends AuditingBaseModel {
	private static final long serialVersionUID = 6596348151274000268L;
	
	public static final String ROLE_SEQUENCE_GENERATOR_NAME = "role_sequence";
	public static final int ROLE_NAME_MAX_LENGTH = 100;
	
	@Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ROLE_SEQUENCE_GENERATOR_NAME)
    protected Long id;
	
	@NaturalId(mutable = true)
	@Column(unique = true, nullable = false, length = ROLE_NAME_MAX_LENGTH)
    private String name;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;
    
	private String description;

    @Column(name = "system", nullable = false)
    private Boolean isSystem = Boolean.FALSE;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_permissions",
        joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)},
        inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false, updatable = false)})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Permission> permissions = new HashSet<>();

    @Override
    public String toString() {
        return "ApplicationRole(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "isSystem = " + isSystem + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role that = (Role) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}

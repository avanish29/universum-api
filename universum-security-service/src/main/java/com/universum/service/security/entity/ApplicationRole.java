package com.universum.service.security.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.envers.Audited;

import com.universum.common.jpa.domin.AbstractBaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "AppRole")
@Getter
@Setter
@Table(name = "app_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@Audited
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationRole extends AbstractBaseEntity {
	private static final long serialVersionUID = 6596348151274000268L;
	@NaturalId
    private String name;
    private String description;

    @Column(name = "is_system")
    private Boolean isSystem = Boolean.FALSE;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "app_role_permissions",
        joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<ApplicationPermission> permissions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ApplicationRole that = (ApplicationRole) o;
        return Objects.equals(name, that.name) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}

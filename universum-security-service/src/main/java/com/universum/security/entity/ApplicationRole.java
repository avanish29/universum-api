package com.universum.security.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "AppRole")
@Getter
@Setter
@Table(name = "app_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@Audited
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationRole extends AbstractBaseEntity {
	private static final long serialVersionUID = 6596348151274000268L;
	@NaturalId
    private String name;
    private String description;

    @Column(name = "is_system")
    private Boolean isSystem;

    @OneToMany(mappedBy = "appRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationPagePermission> pagePermissions = new ArrayList<>();

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

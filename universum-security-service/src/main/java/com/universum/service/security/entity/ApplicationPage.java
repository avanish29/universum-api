package com.universum.service.security.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
@Table(name = "app_page", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "category" }) })
public class ApplicationPage extends AbstractBaseEntity {
	private static final long serialVersionUID = 1487227621155711682L;
	@Column(nullable = false)
    private String name;
    private String description;
    private String category;
    private String route;
    private Boolean isClientSpecific;

    @OneToMany(mappedBy = "appPage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationPagePermission> pagePermissions = new ArrayList<>();
}

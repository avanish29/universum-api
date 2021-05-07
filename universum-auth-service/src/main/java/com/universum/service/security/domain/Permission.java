package com.universum.service.security.domain;

import com.universum.common.jpa.domin.model.AuditingBaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@SequenceGenerator(sequenceName = Permission.PERMISSION_SEQUENCE_GENERATOR_NAME, name = Permission.PERMISSION_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class Permission extends AuditingBaseModel {
	private static final long serialVersionUID = 1487227621155711682L;
	
	public static final String PERMISSION_SEQUENCE_GENERATOR_NAME = "permission_sequence";
	
	public static final int NAME_MIN_LENGTH = 6;
    public static final int NAME_MAX_LENGTH = 40;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERMISSION_SEQUENCE_GENERATOR_NAME)
    protected Long id;
	
	@Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;
    
    @Override
    public String toString() {
        return "ApplicationPermission(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        var that = (Permission) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

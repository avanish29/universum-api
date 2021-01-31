package com.universum.service.security.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.universum.service.security.entity.converter.PagePermissionTypeConverter;
import com.universum.service.security.util.PagePermissionType;

import lombok.Data;

@Entity
@Data
@Audited
@Table(name = "page_permission")
public class ApplicationPagePermission implements Serializable {
	private static final long serialVersionUID = 7203029852519156545L;

	@EmbeddedId
    private PagePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private ApplicationRole appRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pageId")
    private ApplicationPage appPage;

    @Column(name = "browse_permission", length = 1)
    @Convert(converter = PagePermissionTypeConverter.class)
    private PagePermissionType browsePermissionType;

    @Column(name = "view_permission", length = 1)
    @Convert(converter = PagePermissionTypeConverter.class)
    private PagePermissionType viewPermissionType;

    @Column(name = "edit_permission", length = 1)
    @Convert(converter = PagePermissionTypeConverter.class)
    private PagePermissionType editPermissionType;

    @Column(name = "delete_permission", length = 1)
    @Convert(converter = PagePermissionTypeConverter.class)
    private PagePermissionType deletePermissionType;

    @Column(name = "import_permission", length = 1)
    @Convert(converter = PagePermissionTypeConverter.class)
    private PagePermissionType importPermissionType;

    @Column(name = "export_permission", length = 1)
    @Convert(converter = PagePermissionTypeConverter.class)
    private PagePermissionType exportPermissionType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ApplicationPagePermission that = (ApplicationPagePermission) o;
        return Objects.equals(appRole, that.appRole) && Objects.equals(appPage, that.appPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appRole, appPage);
    }

    @Data
    @Embeddable
    public static class PagePermissionId implements Serializable {
		private static final long serialVersionUID = 3938589889247128049L;

		@Column(name = "role_id")
        private Long roleId;

        @Column(name = "page_id")
        private Long pageId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass())
                return false;

            PagePermissionId that = (PagePermissionId) o;
            return Objects.equals(roleId, that.roleId) && Objects.equals(pageId, that.pageId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roleId, pageId);
        }
    }
}

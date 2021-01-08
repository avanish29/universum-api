package com.universum.security.util;

import java.util.Arrays;

public enum PagePermissionType {
    PAGE_PERMISSIONS_DISABLED("D"),
    PAGE_PERMISSION_ENABLED("E"),
    PAGE_PERMISSION_NOT_SET("N");
    private String permissionCode;

    private PagePermissionType(String permissionType) {
        this.permissionCode = permissionType;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public static PagePermissionType defaultType() {
        return PAGE_PERMISSIONS_DISABLED;
    }

    public static PagePermissionType findByPermissionCode(final String permissionCode) {
        return Arrays.stream(PagePermissionType.values()).filter(type -> type.getPermissionCode().equalsIgnoreCase(permissionCode)).findFirst().orElse(defaultType());
    }
}

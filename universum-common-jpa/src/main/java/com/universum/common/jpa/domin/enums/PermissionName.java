package com.universum.common.jpa.domin.enums;

public enum PermissionName {
	USERS_VIEW(Values.USERS_VIEW),
    USERS_ADD(Values.USERS_ADD),
    USERS_EDIT(Values.USERS_EDIT),
    USERS_DELETE(Values.USERS_DELETE),

    ROLES_VIEW(Values.ROLES_VIEW),
    ROLES_ADD(Values.ROLES_ADD),
    ROLES_EDIT(Values.ROLES_EDIT),
    ROLES_DELETE(Values.ROLES_DELETE);
	
	private final String name;

    public String getName() {
        return name;
    }

    PermissionName(String name) {
        this.name = name;
    }

    public static class Values {
    	public static final String USERS_VIEW = "USERS_VIEW";
        public static final String USERS_ADD = "USERS_ADD";
        public static final String USERS_EDIT = "USERS_EDIT";
        public static final String USERS_DELETE = "USERS_DELETE";

        public static final String ROLES_VIEW = "ROLES_VIEW";
        public static final String ROLES_ADD = "ROLES_ADD";
        public static final String ROLES_EDIT = "ROLES_EDIT";
        public static final String ROLES_DELETE = "ROLES_DELETE";
    }
}

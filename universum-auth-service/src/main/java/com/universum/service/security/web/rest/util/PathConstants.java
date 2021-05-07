package com.universum.service.security.web.rest.util;

public final class PathConstants {
    private PathConstants() {
        throw new AssertionError("Can't create instance !");
    }

    private static final String API_ROOT = "/api/admin/security/";
    private static final String ID = "/{id}";

    public static final String AUTHENTICATE = "/authenticate";

    public static final String USERS = API_ROOT + "users";
    public static final String USERS_BY_ID = USERS + ID;

    public static final String ROLES = API_ROOT + "roles";
    public static final String ROLES_BY_ID = ROLES + ID;
}

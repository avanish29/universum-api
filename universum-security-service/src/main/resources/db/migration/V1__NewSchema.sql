ALTER TABLE IF EXISTS app_permission_aud DROP CONSTRAINT IF EXISTS fk_app_permission_aud_revinfo_pkey;
ALTER TABLE IF EXISTS app_role_aud DROP CONSTRAINT IF EXISTS fk_app_role_aud_revinfo_pkey;
ALTER TABLE IF EXISTS app_role_permissions DROP CONSTRAINT IF EXISTS fk_app_role_permissions_app_permission_pkey;
ALTER TABLE IF EXISTS app_role_permissions DROP CONSTRAINT IF EXISTS fk_app_role_permissions_app_role_pkey;
ALTER TABLE IF EXISTS app_role_permissions_aud DROP CONSTRAINT IF EXISTS fk_app_role_permissions_aud_revinfo_pkey;
ALTER TABLE IF EXISTS app_user_aud DROP CONSTRAINT IF EXISTS fk_app_user_aud_revinfo_pkey;
ALTER TABLE IF EXISTS app_user_roles DROP CONSTRAINT IF EXISTS fk_app_user_roles_app_role_pkey;
ALTER TABLE IF EXISTS app_user_roles DROP CONSTRAINT IF EXISTS fk_app_user_roles_app_user_pkey;
ALTER TABLE IF EXISTS app_user_roles_aud DROP CONSTRAINT IF EXISTS fk_app_user_roles_aud_revinfo_pkey;


DROP TABLE IF EXISTS app_permission CASCADE;
DROP TABLE IF EXISTS app_permission_aud CASCADE;
DROP TABLE IF EXISTS app_role CASCADE;
DROP TABLE IF EXISTS app_role_aud CASCADE;
DROP TABLE IF EXISTS app_role_permissions CASCADE;
DROP TABLE IF EXISTS app_role_permissions_aud CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS app_user_aud CASCADE;
DROP TABLE IF EXISTS app_user_roles CASCADE;
DROP TABLE IF EXISTS app_user_roles_aud CASCADE;
DROP TABLE IF EXISTS revinfo CASCADE;
DROP SEQUENCE IF EXISTS hibernate_sequence;

-- public.hibernate_sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 50 INCREMENT 1;

-- Extension: pgcrypto
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Extension: uuid-ossp
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE app_permission (
     id                 INT8 NOT NULL,
     created            TIMESTAMP,
     deleted            BOOLEAN,
     guid               VARCHAR(36) NOT NULL,
     last_update        TIMESTAMP,
     version 			int4 NOT NULL,
     action             INT4,
     description        VARCHAR(255),
     is_client_specific BOOLEAN,
     name               VARCHAR(255) NOT NULL,
     route              VARCHAR(255),
     PRIMARY KEY (id)
);

CREATE TABLE app_permission_aud (
     id                 INT8 NOT NULL,
     rev                INT4 NOT NULL,
     revtype            INT2,
     action             INT4,
     description        VARCHAR(255),
     is_client_specific BOOLEAN,
     name               VARCHAR(255),
     route              VARCHAR(255),
     PRIMARY KEY (id, rev)
);

CREATE TABLE app_role (
     id          INT8 NOT NULL,
     created     TIMESTAMP,
     deleted     BOOLEAN,
     guid        VARCHAR(36) NOT NULL,
     last_update TIMESTAMP,
     version 	 int4 NOT NULL,
     description VARCHAR(255),
     is_system   BOOLEAN,
     name        VARCHAR(255),
     PRIMARY KEY (id)
);

CREATE TABLE app_role_aud (
     id          INT8 NOT NULL,
     rev         INT4 NOT NULL,
     revtype     INT2,
     description VARCHAR(255),
     is_system   BOOLEAN,
     name        VARCHAR(255),
     PRIMARY KEY (id, rev)
);

CREATE TABLE app_role_permissions (
     role_id       INT8 NOT NULL,
     permission_id INT8 NOT NULL
);

CREATE TABLE app_role_permissions_aud (
     rev           INT4 NOT NULL,
     role_id       INT8 NOT NULL,
     permission_id INT8 NOT NULL,
     revtype       INT2,
     PRIMARY KEY (rev, role_id, permission_id)
);

CREATE TABLE app_user (
     id                                  INT8 NOT NULL,
     created                             TIMESTAMP,
     deleted                             BOOLEAN,
     guid                                VARCHAR(36) NOT NULL,
     last_update                         TIMESTAMP,
     version 							 int4 NOT NULL,
     active                              BOOLEAN,
     email_address                       VARCHAR(255),
     email_token_generated_time          TIMESTAMP,
     email_token_hash                    VARCHAR(255),
     email_verification_attempts         INT4 NOT NULL,
     email_verified                      BOOLEAN,
     email_verified_time                 TIMESTAMP,
     failed_login_attempts               INT4,
     first_name                          VARCHAR(255),
     last_login_failure_time             TIMESTAMP,
     last_name                           VARCHAR(255),
     last_password_changed_time          TIMESTAMP,
     last_successful_login_time          TIMESTAMP,
     password_hash                       VARCHAR(255),
     password_reset_token                VARCHAR(255),
     password_reset_token_generated_time TIMESTAMP,
     username                            VARCHAR(255),
     PRIMARY KEY (id)
);

CREATE TABLE app_user_aud (
     id                                  INT8 NOT NULL,
     rev                                 INT4 NOT NULL,
     revtype                             INT2,
     active                              BOOLEAN,
     email_address                       VARCHAR(255),
     email_token_generated_time          TIMESTAMP,
     email_token_hash                    VARCHAR(255),
     email_verification_attempts         INT4,
     email_verified                      BOOLEAN,
     email_verified_time                 TIMESTAMP,
     failed_login_attempts               INT4,
     first_name                          VARCHAR(255),
     last_login_failure_time             TIMESTAMP,
     last_name                           VARCHAR(255),
     last_password_changed_time          TIMESTAMP,
     last_successful_login_time          TIMESTAMP,
     password_hash                       VARCHAR(255),
     password_reset_token                VARCHAR(255),
     password_reset_token_generated_time TIMESTAMP,
     username                            VARCHAR(255),
     PRIMARY KEY (id, rev)
);

CREATE TABLE app_user_roles (
     user_id INT8 NOT NULL,
     role_id INT8 NOT NULL
);

CREATE TABLE app_user_roles_aud (
     rev     INT4 NOT NULL,
     user_id INT8 NOT NULL,
     role_id INT8 NOT NULL,
     revtype INT2,
     PRIMARY KEY (rev, user_id, role_id)
);

CREATE TABLE revinfo (
     rev      INT4 NOT NULL,
     revtstmp INT8,
     PRIMARY KEY (rev)
);

ALTER TABLE app_permission ADD CONSTRAINT uk_app_permission_name_action UNIQUE (name, action);
ALTER TABLE app_permission ADD CONSTRAINT uk_app_permission_guid UNIQUE (guid);
ALTER TABLE app_role ADD CONSTRAINT uk_app_role_name UNIQUE (name);
ALTER TABLE app_role ADD CONSTRAINT uk_app_role_guid UNIQUE (guid);
ALTER TABLE app_user ADD CONSTRAINT uk_app_user_username UNIQUE (username);
ALTER TABLE app_user ADD CONSTRAINT uk_app_user_guid UNIQUE (guid);

ALTER TABLE app_permission_aud ADD CONSTRAINT fk_app_permission_aud_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
ALTER TABLE app_role_aud ADD CONSTRAINT fk_app_role_aud_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
ALTER TABLE app_role_permissions ADD CONSTRAINT fk_app_role_permissions_app_permission_pkey FOREIGN KEY (permission_id) REFERENCES app_permission;
ALTER TABLE app_role_permissions ADD CONSTRAINT fk_app_role_permissions_app_role_pkey FOREIGN KEY (role_id) REFERENCES app_role;
ALTER TABLE app_role_permissions_aud ADD CONSTRAINT fk_app_role_permissions_aud_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
ALTER TABLE app_user_aud ADD CONSTRAINT fk_app_user_aud_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
ALTER TABLE app_user_roles ADD CONSTRAINT fk_app_user_roles_app_role_pkey FOREIGN KEY (role_id) REFERENCES app_role;
ALTER TABLE app_user_roles ADD CONSTRAINT fk_app_user_roles_app_user_pkey FOREIGN KEY (user_id) REFERENCES app_user;
ALTER TABLE app_user_roles_aud ADD CONSTRAINT fk_app_user_roles_aud_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
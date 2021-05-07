ALTER TABLE IF EXISTS role_permissions DROP CONSTRAINT IF EXISTS role_permissions_fk_permission_pkey;
ALTER TABLE IF EXISTS role_permissions DROP CONSTRAINT IF EXISTS role_permissions_fk_role_pkey;
ALTER TABLE IF EXISTS user_roles DROP CONSTRAINT IF EXISTS user_roles_fk_role_pkey;
ALTER TABLE IF EXISTS user_roles DROP CONSTRAINT IF EXISTS user_roles_fk_user_pkey;
ALTER TABLE IF EXISTS users_aud DROP CONSTRAINT IF EXISTS users_aud_fk_revinfo_pkey;


DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS revinfo CASCADE;
DROP TABLE IF EXISTS role_permissions CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS users_aud CASCADE;

-- Drop public.hibernate_sequence
DROP SEQUENCE IF EXISTS hibernate_sequence;
-- Drop public.permission_sequence
DROP SEQUENCE IF EXISTS permission_sequence;
-- Drop public.role_sequence
DROP SEQUENCE IF EXISTS role_sequence;
-- Drop public.user_sequence
DROP SEQUENCE IF EXISTS user_sequence;

-- Create public.hibernate_sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;
-- Create public.permission_sequence
CREATE SEQUENCE IF NOT EXISTS permission_sequence START 1 INCREMENT 1;
-- Create public.role_sequence
CREATE SEQUENCE IF NOT EXISTS role_sequence START 1 INCREMENT 1;
-- Create public.user_sequence
CREATE SEQUENCE IF NOT EXISTS user_sequence START 1 INCREMENT 1;

-- Create Extension: pgcrypto
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create Extension: uuid-ossp
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
     id                                  INT8 NOT NULL,
     deleted                             BOOLEAN NOT NULL,
     guid                                VARCHAR(36) NOT NULL,
     version 							 INT4 NOT NULL,
     created_by                          VARCHAR(255) NOT NULL,
     created_on                          TIMESTAMP NOT NULL,
     updated_by                          VARCHAR(255) NOT NULL,
     updated_on                          TIMESTAMP NOT NULL,
     active                              BOOLEAN NOT NULL,
     email			                     VARCHAR(255) NOT NULL,
     email_token_time          			 TIMESTAMP,
     email_token                    	 VARCHAR(255),
     email_verification_attempts         INT4 NOT NULL,
     email_verified                      BOOLEAN,
     email_verified_time                 TIMESTAMP,
     failed_login_attempts               INT4,
     first_name                          VARCHAR(255) NOT NULL,
     last_login_failure_time             TIMESTAMP,
     last_name                           VARCHAR(255) NOT NULL,
     last_password_changed_time          TIMESTAMP,
     last_successful_login_time          TIMESTAMP,
     password                       	 VARCHAR(255) NOT NULL,
     password_reset_token                VARCHAR(255),
     password_reset_token_generated_time TIMESTAMP,
     username                            VARCHAR(50) NOT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE users_aud (
	id 										INT8 NOT NULL,
	rev 									INT4 NOT NULL,
	revtype 								INT2,
	active 									BOOLEAN,
	email 									VARCHAR(255),
	email_token_time 						TIMESTAMP,
	email_token 							VARCHAR(255),
	email_verification_attempts 			INT4,
	email_verified 							BOOLEAN,
	email_verified_time 					TIMESTAMP,
	failed_login_attempts 					INT4,
	first_name 								VARCHAR(64),
	last_login_failure_time 				TIMESTAMP,
	last_name 								VARCHAR(64),
	last_password_changed_time 				TIMESTAMP,
	last_successful_login_time 				TIMESTAMP,
	password 								VARCHAR(255),
	password_reset_token 					VARCHAR(255),
	password_reset_token_generated_time 	TIMESTAMP,
	username 								VARCHAR(50),
	PRIMARY KEY (id, rev)
);

CREATE TABLE roles (
     id          		INT8 NOT NULL,
     deleted     		BOOLEAN NOT NULL,
     guid        		VARCHAR(36) NOT NULL,
     version 			INT4 NOT NULL,
     created_by  		VARCHAR(255) NOT NULL,
     created_on  		TIMESTAMP NOT NULL,
     updated_by  		VARCHAR(255) NOT NULL,
     updated_on  		TIMESTAMP NOT NULL,
     name        		VARCHAR(100) NOT NULL,
     description 		VARCHAR(255),
     system   			BOOLEAN NOT NULL,
     role_type 			VARCHAR(50) NOT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE user_roles (
     user_id INT8 NOT NULL,
     role_id INT8 NOT NULL,
     PRIMARY KEY (user_id, role_id)
);


CREATE TABLE permissions (
     id                 INT8 NOT NULL,
     deleted            BOOLEAN NOT NULL,
     guid               VARCHAR(36) NOT NULL,
     version 			int4 NOT NULL,
     created_by         VARCHAR(255) NOT NULL,
     created_on         TIMESTAMP NOT NULL,
     updated_by         VARCHAR(255) NOT NULL,
     updated_on         TIMESTAMP NOT NULL,
     name               VARCHAR(255) NOT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE role_permissions (
     role_id       		INT8 NOT NULL,
     permission_id 		INT8 NOT NULL,
     PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE revinfo (
     rev      INT4 NOT NULL,
     revtstmp INT8,
     PRIMARY KEY (rev)
);

ALTER TABLE users ADD CONSTRAINT uk_users_guid UNIQUE (guid);
ALTER TABLE users ADD CONSTRAINT uk_users_username UNIQUE (username);
ALTER TABLE roles ADD CONSTRAINT uk_roles_guid UNIQUE (guid);
ALTER TABLE roles ADD CONSTRAINT uk_roles_name UNIQUE (name);
ALTER TABLE permissions ADD CONSTRAINT uk_permissions_name UNIQUE (name);
ALTER TABLE permissions ADD CONSTRAINT uk_permissions_guid UNIQUE (guid);
ALTER TABLE users_aud ADD CONSTRAINT fk_user_aud_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
ALTER TABLE role_permissions ADD CONSTRAINT fk_role_permissions_permission_pkey FOREIGN KEY (permission_id) REFERENCES permissions;
ALTER TABLE role_permissions ADD CONSTRAINT fk_role_permissions_role_pkey FOREIGN KEY (role_id) REFERENCES roles;
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_role_pkey FOREIGN KEY (role_id) REFERENCES roles;
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user_pkey FOREIGN KEY (user_id) REFERENCES users;


ALTER TABLE role_permissions ADD CONSTRAINT role_permissions_fk_permission_pkey FOREIGN KEY (permission_id) REFERENCES permissions;
ALTER TABLE role_permissions ADD CONSTRAINT role_permissions_fk_role_pkey FOREIGN KEY (role_id) REFERENCES roles;
ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk_role_pkey FOREIGN KEY (role_id) REFERENCES roles;
ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk_user_pkey FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE users_aud ADD CONSTRAINT users_aud_fk_revinfo_pkey FOREIGN KEY (rev) REFERENCES revinfo;
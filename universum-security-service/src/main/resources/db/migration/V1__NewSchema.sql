-- public.hibernate_sequence
DROP SEQUENCE IF EXISTS hibernate_sequence;

CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

-- Extension: pgcrypto
CREATE EXTENSION "pgcrypto";

-- Extension: uuid-ossp
CREATE EXTENSION "uuid-ossp";

-- public.available_language definition
ALTER TABLE IF EXISTS app_page_aud DROP CONSTRAINT IF EXISTS FKglkoc2a1f5etsg994ip6xetf6;

ALTER TABLE IF EXISTS app_role_aud DROP CONSTRAINT IF EXISTS FKhd8msl9b8usp6k1q9mk6drg4v;

ALTER TABLE IF EXISTS app_user_aud DROP CONSTRAINT IF EXISTS FKlrwde4gab1o0jmxy358bobg55;

ALTER TABLE IF EXISTS app_user_roles DROP CONSTRAINT IF EXISTS FKoakt07mc9x8g42934jlh183n7;

ALTER TABLE IF EXISTS app_user_roles DROP CONSTRAINT IF EXISTS FK3lwiahkol5aetc57pto5olacf;

ALTER TABLE IF EXISTS app_user_roles_aud DROP CONSTRAINT IF EXISTS FKlom97d0pt38f0u65pvt4nqnun;

ALTER TABLE IF EXISTS page_permission DROP CONSTRAINT IF EXISTS FKrl97hc8lbnts0we3x3gkh1p0q;

ALTER TABLE IF EXISTS page_permission DROP CONSTRAINT IF EXISTS FKrkdpxud1iymsdnmfnsx594be9;

ALTER TABLE IF EXISTS page_permission_aud DROP CONSTRAINT IF EXISTS FKl1tobgrqmkjmkcjtid80b3dkl;

DROP TABLE IF EXISTS app_page CASCADE;

DROP TABLE IF EXISTS app_page_aud CASCADE;

DROP TABLE IF EXISTS app_role CASCADE;

DROP TABLE IF EXISTS app_role_aud CASCADE;

DROP TABLE IF EXISTS app_user CASCADE;

DROP TABLE IF EXISTS app_user_aud CASCADE;

DROP TABLE IF EXISTS app_user_roles CASCADE;

DROP TABLE IF EXISTS app_user_roles_aud CASCADE;

DROP TABLE IF EXISTS page_permission CASCADE;

DROP TABLE IF EXISTS page_permission_aud CASCADE;

DROP TABLE IF EXISTS revinfo CASCADE;

CREATE TABLE app_page (
	id int8 NOT NULL,
	created timestamp,
	deleted boolean,
	guid varchar(36) NOT NULL,
	last_update timestamp,
	version int4 NOT NULL,
	category varchar(255),
	description varchar(255),
	is_client_specific boolean,
	name varchar(255) NOT NULL,
	route varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE app_page_aud (
	id int8 NOT NULL,
	rev int4 NOT NULL,
	revtype int2,
	category varchar(255),
	description varchar(255),
	is_client_specific boolean,
	name varchar(255),
	route varchar(255),
	PRIMARY KEY (id, rev)
);

CREATE TABLE app_role (
	id int8 NOT NULL,
	created timestamp,
	deleted boolean,
	guid varchar(36) NOT NULL,
	last_update timestamp,
	version int4 NOT NULL,
	description varchar(255),
	is_system boolean,
	name varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE app_role_aud (
	id int8 NOT NULL,
	rev int4 NOT NULL,
	revtype int2,
	description varchar(255),
	is_system boolean,
	name varchar(255),
	PRIMARY KEY (id, rev)
);

CREATE TABLE app_user (
	id int8 NOT NULL,
	created timestamp,
	deleted boolean,
	guid varchar(36) NOT NULL,
	last_update timestamp,
	version int4 NOT NULL,
	active boolean,
	email_address varchar(255),
	email_token_generated_time timestamp,
	email_token_hash varchar(255),
	email_verification_attempts int4 NOT NULL,
	email_verified boolean,
	email_verified_time timestamp,
	failed_login_attempts int4,
	first_name varchar(255),
	last_login_failure_time timestamp,
	last_name varchar(255),
	last_password_changed_time timestamp,
	last_successful_login_time timestamp,
	password_hash varchar(255),
	password_reset_token varchar(255),
	password_reset_token_generated_time timestamp,
	username varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE app_user_aud (
	id int8 NOT NULL,
	rev int4 NOT NULL,
	revtype int2,
	active boolean,
	email_address varchar(255),
	email_token_generated_time timestamp,
	email_token_hash varchar(255),
	email_verification_attempts int4,
	email_verified boolean,
	email_verified_time timestamp,
	failed_login_attempts int4,
	first_name varchar(255),
	last_login_failure_time timestamp,
	last_name varchar(255),
	last_password_changed_time timestamp,
	last_successful_login_time timestamp,
	password_hash varchar(255),
	password_reset_token varchar(255),
	password_reset_token_generated_time timestamp,
	username varchar(255),
	PRIMARY KEY (id, rev)
);

CREATE TABLE app_user_roles (user_id int8 NOT NULL, role_id int8 NOT NULL);

CREATE TABLE app_user_roles_aud (
	rev int4 NOT NULL,
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	revtype int2,
	PRIMARY KEY (rev, user_id, role_id)
);

CREATE TABLE page_permission (
	browse_permission varchar(1),
	delete_permission varchar(1),
	edit_permission varchar(1),
	export_permission varchar(1),
	import_permission varchar(1),
	view_permission varchar(1),
	app_page_id int8 NOT NULL,
	app_role_id int8 NOT NULL,
	PRIMARY KEY (app_page_id, app_role_id)
);

CREATE TABLE page_permission_aud (
	app_page_id int8 NOT NULL,
	app_role_id int8 NOT NULL,
	rev int4 NOT NULL,
	revtype int2,
	browse_permission varchar(1),
	delete_permission varchar(1),
	edit_permission varchar(1),
	export_permission varchar(1),
	import_permission varchar(1),
	view_permission varchar(1),
	PRIMARY KEY (app_page_id, app_role_id, rev)
);

CREATE TABLE revinfo (rev int4 NOT NULL, revtstmp int8, PRIMARY KEY (rev));

ALTER TABLE app_page ADD CONSTRAINT UKh09tpompex08o0en9ncvqjhkt UNIQUE (name, category);

ALTER TABLE app_page ADD CONSTRAINT UK_fg7fsm1xufp52ks8c00gkmdv8 UNIQUE (guid);

ALTER TABLE app_role ADD CONSTRAINT UKdyf5hu50ddvjoy0pfpleevwfl UNIQUE (name);

ALTER TABLE app_role ADD CONSTRAINT UK_dwqlkhagjxbiidqro5jsmpgq2 UNIQUE (guid);

ALTER TABLE app_user ADD CONSTRAINT UK3k4cplvh82srueuttfkwnylq0 UNIQUE (username);

ALTER TABLE app_user ADD CONSTRAINT UK_n3fkey6ry4s5gwxobbwbnudrc UNIQUE (guid);

ALTER TABLE app_page_aud ADD CONSTRAINT FKglkoc2a1f5etsg994ip6xetf6 FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE app_role_aud ADD CONSTRAINT FKhd8msl9b8usp6k1q9mk6drg4v FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE app_user_aud ADD CONSTRAINT FKlrwde4gab1o0jmxy358bobg55 FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE app_user_roles ADD CONSTRAINT FKoakt07mc9x8g42934jlh183n7 FOREIGN KEY (role_id) REFERENCES app_role;

ALTER TABLE app_user_roles ADD CONSTRAINT FK3lwiahkol5aetc57pto5olacf FOREIGN KEY (user_id) REFERENCES app_user;

ALTER TABLE app_user_roles_aud ADD CONSTRAINT FKlom97d0pt38f0u65pvt4nqnun FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE page_permission ADD CONSTRAINT FKrl97hc8lbnts0we3x3gkh1p0q FOREIGN KEY (app_page_id) REFERENCES app_page;

ALTER TABLE page_permission ADD CONSTRAINT FKrkdpxud1iymsdnmfnsx594be9 FOREIGN KEY (app_role_id) REFERENCES app_role;

ALTER TABLE page_permission_aud ADD CONSTRAINT FKl1tobgrqmkjmkcjtid80b3dkl FOREIGN KEY (rev) REFERENCES revinfo;
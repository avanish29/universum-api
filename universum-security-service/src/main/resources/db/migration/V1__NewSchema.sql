CREATE DATABASE resource_message;

-- public.hibernate_sequence
DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

-- Extension: pgcrypto
CREATE EXTENSION pgcrypto;

-- public.available_language definition
alter table app_page_aud drop constraint FKglkoc2a1f5etsg994ip6xetf6;
alter table app_role_aud drop constraint FKhd8msl9b8usp6k1q9mk6drg4v;
alter table app_user_aud drop constraint FKlrwde4gab1o0jmxy358bobg55;
alter table app_user_roles drop constraint FKoakt07mc9x8g42934jlh183n7;
alter table app_user_roles drop constraint FK3lwiahkol5aetc57pto5olacf;
alter table app_user_roles_aud drop constraint FKlom97d0pt38f0u65pvt4nqnun;
alter table page_permission drop constraint FKrl97hc8lbnts0we3x3gkh1p0q;
alter table page_permission drop constraint FKrkdpxud1iymsdnmfnsx594be9;
alter table page_permission_aud drop constraint FKl1tobgrqmkjmkcjtid80b3dkl;


drop table if exists app_page cascade;
drop table if exists app_page_aud cascade;
drop table if exists app_role cascade;
drop table if exists app_role_aud cascade;
drop table if exists app_user cascade;
drop table if exists app_user_aud cascade;
drop table if exists app_user_roles cascade;
drop table if exists app_user_roles_aud cascade;
drop table if exists page_permission cascade;
drop table if exists page_permission_aud cascade;
drop table if exists revinfo cascade;

create table app_page (id int8 not null, created timestamp, deleted boolean, guid varchar(36) not null, last_update timestamp, version int4 not null, category varchar(255), description varchar(255), is_client_specific boolean, name varchar(255) not null, route varchar(255), primary key (id));
create table app_page_aud (id int8 not null, rev int4 not null, revtype int2, category varchar(255), description varchar(255), is_client_specific boolean, name varchar(255), route varchar(255), primary key (id, rev));
create table app_role (id int8 not null, created timestamp, deleted boolean, guid varchar(36) not null, last_update timestamp, version int4 not null, description varchar(255), is_system boolean, name varchar(255), primary key (id));
create table app_role_aud (id int8 not null, rev int4 not null, revtype int2, description varchar(255), is_system boolean, name varchar(255), primary key (id, rev));
create table app_user (id int8 not null, created timestamp, deleted boolean, guid varchar(36) not null, last_update timestamp, version int4 not null, active boolean, email_address varchar(255), email_token_generated_time timestamp, email_token_hash varchar(255), email_verification_attempts int4 not null, email_verified boolean, email_verified_time timestamp, failed_login_attempts int4, first_name varchar(255), last_login_failure_time timestamp, last_name varchar(255), last_password_changed_time timestamp, last_successful_login_time timestamp, password_hash varchar(255), password_reset_token varchar(255), password_reset_token_generated_time timestamp, username varchar(255), primary key (id));
create table app_user_aud (id int8 not null, rev int4 not null, revtype int2, active boolean, email_address varchar(255), email_token_generated_time timestamp, email_token_hash varchar(255), email_verification_attempts int4, email_verified boolean, email_verified_time timestamp, failed_login_attempts int4, first_name varchar(255), last_login_failure_time timestamp, last_name varchar(255), last_password_changed_time timestamp, last_successful_login_time timestamp, password_hash varchar(255), password_reset_token varchar(255), password_reset_token_generated_time timestamp, username varchar(255), primary key (id, rev));
create table app_user_roles (user_id int8 not null, role_id int8 not null);
create table app_user_roles_aud (rev int4 not null, user_id int8 not null, role_id int8 not null, revtype int2, primary key (rev, user_id, role_id));
create table page_permission (browse_permission varchar(1), delete_permission varchar(1), edit_permission varchar(1), export_permission varchar(1), import_permission varchar(1), view_permission varchar(1), app_page_id int8 not null, app_role_id int8 not null, primary key (app_page_id, app_role_id));
create table page_permission_aud (app_page_id int8 not null, app_role_id int8 not null, rev int4 not null, revtype int2, browse_permission varchar(1), delete_permission varchar(1), edit_permission varchar(1), export_permission varchar(1), import_permission varchar(1), view_permission varchar(1), primary key (app_page_id, app_role_id, rev));
create table revinfo (rev int4 not null, revtstmp int8, primary key (rev));
alter table app_page add constraint UKh09tpompex08o0en9ncvqjhkt unique (name, category);
alter table app_page add constraint UK_fg7fsm1xufp52ks8c00gkmdv8 unique (guid);
alter table app_role add constraint UKdyf5hu50ddvjoy0pfpleevwfl unique (name);
alter table app_role add constraint UK_dwqlkhagjxbiidqro5jsmpgq2 unique (guid);
alter table app_user add constraint UK3k4cplvh82srueuttfkwnylq0 unique (username);
alter table app_user add constraint UK_n3fkey6ry4s5gwxobbwbnudrc unique (guid);
alter table app_page_aud add constraint FKglkoc2a1f5etsg994ip6xetf6 foreign key (rev) references revinfo;
alter table app_role_aud add constraint FKhd8msl9b8usp6k1q9mk6drg4v foreign key (rev) references revinfo;
alter table app_user_aud add constraint FKlrwde4gab1o0jmxy358bobg55 foreign key (rev) references revinfo;
alter table app_user_roles add constraint FKoakt07mc9x8g42934jlh183n7 foreign key (role_id) references app_role;
alter table app_user_roles add constraint FK3lwiahkol5aetc57pto5olacf foreign key (user_id) references app_user;
alter table app_user_roles_aud add constraint FKlom97d0pt38f0u65pvt4nqnun foreign key (rev) references revinfo;
alter table page_permission add constraint FKrl97hc8lbnts0we3x3gkh1p0q foreign key (app_page_id) references app_page;
alter table page_permission add constraint FKrkdpxud1iymsdnmfnsx594be9 foreign key (app_role_id) references app_role;
alter table page_permission_aud add constraint FKl1tobgrqmkjmkcjtid80b3dkl foreign key (rev) references revinfo;
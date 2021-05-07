-- Permission for user add/edit/view/delete
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(1, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'USERS_VIEW');
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(2, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'USERS_ADD');
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(3, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'USERS_EDIT');
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(4, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'USERS_DELETE');
-- Permission for role add/edit/view/delete
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(5, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'ROLES_VIEW');
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(6, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'ROLES_ADD');
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(7, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'ROLES_EDIT');
INSERT INTO public.permissions (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, "name")
VALUES(8, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'ROLES_DELETE');

-- Create super admin role
INSERT INTO public.roles (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, description, "system", "name", role_type)
VALUES(1, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'Super Admin users has access to all tasks.', true, 'SUPER_ADMIN', 'ROLE_ADMIN');

-- Create config admin role
INSERT INTO public.roles (id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, description, "system", "name", role_type)
VALUES(2, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), 'Config Admin users has access to all configuration tasks.', true, 'CONFIG_ADMIN', 'ROLE_ADMIN');

-- Create super admin user
INSERT INTO public.users
(id, deleted, guid, "version", created_by, created_on, updated_by, updated_on, active, email, email_token_time, email_token, email_verification_attempts, email_verified, email_verified_time, failed_login_attempts, first_name, last_login_failure_time, last_name, last_password_changed_time, last_successful_login_time, "password", password_reset_token, password_reset_token_generated_time, username)
VALUES(1, false, uuid_generate_v4()::text, 0, 'SYSTEM', now(), 'SYSTEM', now(), true, 'cris.universum@gmail.com', NULL, NULL, 0, true, now(), 0, 'Avanish', NULL, 'Pandey', NULL, NULL, '$2a$10$VPCXtovwk8g2vkBBv5Na1u/.UaXSmSGskG92WKdZKYFk/bn5nK7W2', NULL, NULL, 'admin');

-- Create user role
INSERT INTO public.user_roles (user_id, role_id) VALUES (1, 1);
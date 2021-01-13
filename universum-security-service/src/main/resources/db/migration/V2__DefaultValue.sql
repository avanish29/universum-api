INSERT INTO public.app_role (id, created, deleted, guid, last_update, "version", description, is_system, name)
VALUES(0, now(), false, uuid_generate_v4()::text, now(), 0, 'Super admin user has all permission', true, 'SUPER_ADMIN') on conflict (id) do nothing;

INSERT INTO public.app_role (id, created, deleted, guid, last_update, "version", description, is_system, name)
VALUES(1, now(), false, uuid_generate_v4()::text, now(), 0, 'Configuration admin user only access to configuration module', true, 'CONFIG_ADMIN') on conflict (id) do nothing;
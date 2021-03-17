INSERT INTO public.app_role (id, created, deleted, guid, last_update, "version", description, is_system, name)
VALUES(0, now(), false, uuid_generate_v4()::text, now(), 0, 'Super Admin users has access to all tasks.', true, 'SUPER_ADMIN') on conflict (id) do nothing;

INSERT INTO public.app_role (id, created, deleted, guid, last_update, "version", description, is_system, name)
VALUES(1, now(), false, uuid_generate_v4()::text, now(), 0, 'Config Admin users has access to all configuration tasks.', true, 'CONFIG_ADMIN') on conflict (id) do nothing;
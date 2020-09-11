CREATE DATABASE resource_message;

-- public.hibernate_sequence
CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence;

-- Extension: pgcrypto
CREATE EXTENSION pgcrypto;

-- public.available_language definition
CREATE TABLE IF NOT EXISTS public.available_language (
	id int8 NOT NULL DEFAULT nextval('hibernate_sequence'),
	created timestamp NOT NULL DEFAULT now(),
	guid varchar(36) NOT NULL DEFAULT gen_random_uuid(),
	last_update timestamp NOT NULL DEFAULT now(),
	code varchar(36) NOT NULL,
	"label" varchar(255) NOT NULL,
	dir varchar(20) NOT NULL,
	deleted bool NOT NULL DEFAULT false,
	"default" bool NOT NULL DEFAULT false,
	CONSTRAINT available_language_pkey PRIMARY KEY (id),
	CONSTRAINT available_language_unique_guid UNIQUE (guid),
	CONSTRAINT available_language_unique_code UNIQUE (code)
);

-- public.resource_message definition
CREATE TABLE IF NOT EXISTS public.resource_message (
	id int8 NOT NULL DEFAULT nextval('hibernate_sequence'),
	created timestamp NOT NULL DEFAULT now(),
	guid varchar(36) NOT NULL DEFAULT gen_random_uuid(),
	last_update timestamp NOT NULL DEFAULT now(),
	resource_name varchar(255) NOT NULL,
	resource_value text NOT NULL,
	available_language_fk int8 NOT NULL,
	deleted bool NOT NULL DEFAULT false,
	CONSTRAINT resource_message_pkey PRIMARY KEY (id),
	CONSTRAINT resource_message_unique_guid UNIQUE (guid),
	CONSTRAINT resource_message_unique_nameandlanguage UNIQUE (resource_name,available_language_fk)
);
ALTER TABLE public.resource_message ADD CONSTRAINT available_language_pkey FOREIGN KEY (available_language_fk) REFERENCES available_language(id);
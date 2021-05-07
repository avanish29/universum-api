ALTER TABLE IF EXISTS available_language_messages DROP CONSTRAINT IF EXISTS fk_available_language_messages_available_language_pkey;

DROP TABLE IF EXISTS available_language cascade;
DROP TABLE IF EXISTS available_language_messages cascade;
DROP SEQUENCE IF EXISTS hibernate_sequence;

-- public.hibernate_sequence
CREATE SEQUENCE hibernate_sequence start 9999 increment 1;

-- Extension: pgcrypto
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Extension: uuid-ossp
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE available_language (
     id          	INT8 NOT NULL,
     created_on     TIMESTAMP,
     created_by     VARCHAR(255),
     deleted     	BOOLEAN,
     guid        	VARCHAR(36) NOT NULL,
     updated_on     TIMESTAMP,
     updated_by     VARCHAR(255),
     version     	INT4 NOT NULL,
     code        	VARCHAR(36) NOT NULL,
     dir         	VARCHAR(255),
     is_default     BOOLEAN,
     label       	VARCHAR(255) NOT NULL,
     PRIMARY KEY (id)
);
 
CREATE TABLE available_language_messages (
     available_language_id INT8 NOT NULL,
     key                   VARCHAR(255) NOT NULL,
     value                 VARCHAR(255) NOT NULL
); 
  
ALTER TABLE available_language ADD CONSTRAINT uk_available_language_guid UNIQUE (guid);
ALTER TABLE available_language ADD CONSTRAINT uk_available_language_code UNIQUE (code);
ALTER TABLE available_language_messages ADD CONSTRAINT uk_available_language_messages_available_language_id_key UNIQUE (available_language_id, key);

ALTER TABLE available_language_messages ADD CONSTRAINT fk_available_language_messages_available_language_pkey FOREIGN KEY (available_language_id) REFERENCES available_language;
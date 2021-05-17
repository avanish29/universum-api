-- Create Extension: uuid-ossp
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS tanent_catalog CASCADE;

CREATE TABLE tanent_catalog (
	tenant_id 		INT8 GENERATED BY DEFAULT AS IDENTITY,
    created_by 		VARCHAR(255) NOT NULL,
    created_on 		TIMESTAMP NOT NULL,
    driver_class 	VARCHAR(100) NOT NULL,
    guid 			VARCHAR(36) NOT NULL,
    password 		VARCHAR(100) NOT NULL,
    status 			BOOLEAN NOT NULL,
    tenant_name 	VARCHAR(50) NOT NULL,
    updated_by 		VARCHAR(255) NOT NULL,
    updated_on 		TIMESTAMP,
    url 			VARCHAR(100) NOT NULL,
    user_name 		VARCHAR(50) NOT NULL,
    PRIMARY KEY (tenant_id)
);
	
ALTER TABLE IF EXISTS tanent_catalog ADD CONSTRAINT uk_tanent_catalog_tenant_name_guid UNIQUE (tenant_name, guid);

ALTER TABLE IF EXISTS tanent_catalog ADD CONSTRAINT uk_tanent_catalog_guid UNIQUE (guid);
	   
ALTER TABLE IF EXISTS tanent_catalog ADD CONSTRAINT uk_tanent_catalog_tenant_name UNIQUE (tenant_name);
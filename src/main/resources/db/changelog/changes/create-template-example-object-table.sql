--liquibase formatted sql
--changeset argenta:create-template-example-object-table
CREATE TABLE IF NOT EXISTS example_object (
	eo_id uuid NOT null PRIMARY KEY,
	message varchar NULL
);

--rollback DROP TABLE example_object
ALTER TABLE child ADD COLUMN duplicate_key varchar(200);

--rollback alter table child drop column duplicate_key;
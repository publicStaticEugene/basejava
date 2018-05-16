CREATE TABLE resume
(
  uuid      CHAR(36) NOT NULL
    CONSTRAINT resume_pkey
    PRIMARY KEY,
  full_name TEXT     NOT NULL
);

CREATE TABLE contact
(
  id           SERIAL   NOT NULL,
  resume_uuid  CHAR(36) NOT NULL
    CONSTRAINT contact_resume_uuid_fk
    REFERENCES resume
    ON DELETE CASCADE,
  contact_type TEXT     NOT NULL,
  value        TEXT     NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, contact_type);

CREATE TABLE text_section
(
  id          SERIAL   NOT NULL,
  resume_uuid CHAR(36) NOT NULL
    CONSTRAINT text_section_resume_uuid_fk
    REFERENCES resume
    ON DELETE CASCADE,
  text_type   TEXT     NOT NULL,
  content     TEXT     NOT NULL
);

CREATE TABLE list_section
(
  id          SERIAL   NOT NULL,
  resume_uuid CHAR(36) NOT NULL
    CONSTRAINT list_section_resume_uuid_fk
    REFERENCES resume
    ON DELETE CASCADE,
  list_type   TEXT     NOT NULL,
  items       TEXT     NOT NULL
);

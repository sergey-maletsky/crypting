CREATE SEQUENCE IF NOT EXISTS user_certificate_id_seq MINVALUE 1;

CREATE TABLE user_certificate (
  id BIGINT PRIMARY KEY NOT NULL,
  serial_number VARCHAR(255) NOT NULL DEFAULT '',
  data bytea NOT NULL,
  validity_date_from TIMESTAMP NOT NULL,
  validity_date_to TIMESTAMP NOT NULL,
  organisation_name VARCHAR(255),
  full_name VARCHAR(255),
  revoked BOOL NOT NULL DEFAULT true
);

CREATE UNIQUE INDEX IF NOT EXISTS serial_number_indx
  ON user_certificate (serial_number);
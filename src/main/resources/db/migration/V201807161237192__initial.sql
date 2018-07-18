CREATE SEQUENCE IF NOT EXISTS user_certificate_id_seq MINVALUE 1;
CREATE SEQUENCE IF NOT EXISTS certifying_center_id_seq MINVALUE 1;
CREATE SEQUENCE IF NOT EXISTS certificate_authority_id_seq MINVALUE 1;
CREATE SEQUENCE IF NOT EXISTS revocation_list_id_seq MINVALUE 1;
CREATE SEQUENCE IF NOT EXISTS revocation_list_meta_id_seq MINVALUE 1;

CREATE TABLE certifying_center (
  id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(255) NOT NULL DEFAULT '',
  url VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE UNIQUE INDEX IF NOT EXISTS ca_name_indx
  ON certifying_center (name);

CREATE TABLE certificate_authority (
  id BIGINT PRIMARY KEY NOT NULL,
  serial_number VARCHAR(255) NOT NULL DEFAULT '',
  data bytea NOT NULL,
  validity_date_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  validity_date_to TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  certifying_id BIGINT NOT NULL,
  CONSTRAINT ca_certificate_ca FOREIGN KEY (certifying_id) REFERENCES certifying_center (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ca_serial_number_indx
  ON certificate_authority (serial_number);

CREATE TABLE user_certificate (
  id BIGINT PRIMARY KEY NOT NULL,
  serial_number VARCHAR(255) NOT NULL DEFAULT '',
  data bytea NOT NULL,
  validity_date_from TIMESTAMP NOT NULL,
  validity_date_to TIMESTAMP NOT NULL,
  organisation_name VARCHAR(255),
  full_name VARCHAR(255),
  revoked BOOL NOT NULL DEFAULT true,
  certifying_id BIGINT NOT NULL,
  CONSTRAINT certificate_ca FOREIGN KEY (certifying_id) REFERENCES certifying_center (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS serial_number_indx
  ON user_certificate (serial_number);

CREATE TABLE revocation_list_meta (
  id BIGINT PRIMARY KEY NOT NULL,
  url VARCHAR(255) NOT NULL DEFAULT '',
  algorithm VARCHAR(255) NOT NULL DEFAULT '',
  update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  certifying_id BIGINT NOT NULL,
  CONSTRAINT crl_meta_ca FOREIGN KEY (certifying_id) REFERENCES certifying_center (id)
);

CREATE TABLE revocation_list (
  id BIGINT PRIMARY KEY NOT NULL,
  serial_number VARCHAR(255) NOT NULL DEFAULT '',
  revocation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  crl_meta_id BIGINT NOT NULL,
  CONSTRAINT crl_crl_meta FOREIGN KEY (crl_meta_id) REFERENCES revocation_list_meta (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS crl_serial_number_indx
  ON revocation_list (serial_number);
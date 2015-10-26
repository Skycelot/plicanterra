CREATE TABLE principal (
  id BIGINT NOT NULL,
  login VARCHAR (50),
  full_name VARCHAR (250),
  password VARCHAR (50),
  CONSTRAINT principal_pk PRIMARY KEY (id)
);

CREATE TABLE project (
  id BIGINT NOT NULL,
  code VARCHAR (50),
  name VARCHAR (150),
  description VARCHAR (2000),
  CONSTRAINT project_pk PRIMARY KEY (id),
  CONSTRAINT project_code_unique UNIQUE (code)
);

CREATE TABLE profile (
  id BIGINT NOT NULL,
  principal_id BIGINT,
  project_id BIGINT,
  CONSTRAINT profile_pk PRIMARY KEY (id),
  CONSTRAINT profile_principal_fk FOREIGN KEY (principal_id) REFERENCES principal (id),
  CONSTRAINT profile_project_fk FOREIGN KEY (project_id) REFERENCES project (id)
);

CREATE TABLE role (
  id BIGINT NOT NULL,
  code VARCHAR (50),
  name VARCHAR (150),
  description VARCHAR (2000),
  project_id BIGINT,
  CONSTRAINT role_pk PRIMARY KEY (id),
  CONSTRAINT role_project_fk FOREIGN KEY (project_id) REFERENCES project (id),
  CONSTRAINT role_code_unique UNIQUE (project_id, code)
);

CREATE TABLE principal_roles (
  profile_id BIGINT,
  role_id BIGINT,
  CONSTRAINT principal_roles_pk PRIMARY KEY (profile_id, role_id),
  CONSTRAINT principal_roles_profile_fk FOREIGN KEY (profile_id) REFERENCES profile (id),
  CONSTRAINT principal_roles_role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE template (
  id BIGINT NOT NULL,
  code VARCHAR (50),
  name VARCHAR (150),
  description VARCHAR (2000),
  project_id BIGINT,
  CONSTRAINT template_pk PRIMARY KEY (id),
  CONSTRAINT template_project_fk FOREIGN KEY (project_id) REFERENCES project (id),
  CONSTRAINT template_code_unique UNIQUE (project_id, code)
);

CREATE TABLE attribute (
  id BIGINT NOT NULL,
  code VARCHAR (50),
  name VARCHAR (150),
  description VARCHAR (2000),
  template_id BIGINT,
  type VARCHAR (20),
  CONSTRAINT attribute_pk PRIMARY KEY (id),
  CONSTRAINT attribute_template_fk FOREIGN KEY (template_id) REFERENCES template (id),
  CONSTRAINT attribute_code_unique UNIQUE (template_id, code)
);

CREATE TABLE link (
  id BIGINT NOT NULL,
  code VARCHAR (50),
  name VARCHAR (150),
  description VARCHAR (2000),
  template_left_id BIGINT,
  template_right_id BIGINT,
  type VARCHAR (20),
  CONSTRAINT link_pk PRIMARY KEY (id),
  CONSTRAINT link_template_left_fk FOREIGN KEY (template_left_id) REFERENCES template (id),
  CONSTRAINT link_template_right_fk FOREIGN KEY (template_right_id) REFERENCES template (id)
);

CREATE TABLE status (
  id BIGINT NOT NULL,
  code VARCHAR (50),
  name VARCHAR (150),
  description VARCHAR (2000),
  template_id BIGINT,
  initial BOOLEAN,
  CONSTRAINT status_pk PRIMARY KEY (id),
  CONSTRAINT status_template_fk FOREIGN KEY (template_id) REFERENCES template (id),
  CONSTRAINT status_code_unique UNIQUE (template_id, code)
);

CREATE TABLE transition (
  id BIGINT NOT NULL,
  status_source_id BIGINT,
  status_destination_id BIGINT,
  CONSTRAINT transition_pk PRIMARY KEY (id),
  CONSTRAINT transition_status_src_fk FOREIGN KEY (status_source_id) REFERENCES status (id),
  CONSTRAINT transition_status_dest_fk FOREIGN KEY (status_destination_id) REFERENCES status (id),
  CONSTRAINT transition_unique UNIQUE (status_source_id, status_destination_id)
);

CREATE TABLE attribute_grants (
  attribute_id BIGINT NOT NULL,
  status_id BIGINT NOT NULL,
  permission VARCHAR (20),
  CONSTRAINT attribute_grants_pk PRIMARY KEY (attribute_id, status_id),
  CONSTRAINT attribute_grants_attribute_fk FOREIGN KEY (attribute_id) REFERENCES attribute (id),
  CONSTRAINT attribute_grants_status_fk FOREIGN KEY (status_id) REFERENCES status (id)
);

CREATE TABLE link_grants (
  link_id BIGINT NOT NULL,
  status_id BIGINT NOT NULL,
  permission VARCHAR (20),
  CONSTRAINT link_grants_pk PRIMARY KEY (link_id, status_id),
  CONSTRAINT link_grants_link_fk FOREIGN KEY (link_id) REFERENCES link (id),
  CONSTRAINT link_grants_status_fk FOREIGN KEY (status_id) REFERENCES status (id)
);

CREATE TABLE template_permissions (
  template_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  permission VARCHAR (20),
  CONSTRAINT template_permissions_pk PRIMARY KEY (template_id, role_id),
  CONSTRAINT template_permissions_PROFILE_fk FOREIGN KEY (template_id) REFERENCES template (id),
  CONSTRAINT template_permissions_role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE attribute_permissions (
  attribute_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  permission VARCHAR (20),
  CONSTRAINT attribute_permissions_pk PRIMARY KEY (attribute_id, role_id),
  CONSTRAINT attribute_permissions_attribute_fk FOREIGN KEY (attribute_id) REFERENCES attribute (id),
  CONSTRAINT attribute_permissions_role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE link_permissions (
  link_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  permission VARCHAR (20),
  CONSTRAINT link_permissions_pk PRIMARY KEY (link_id, role_id),
  CONSTRAINT link_permissions_fk FOREIGN KEY (link_id) REFERENCES link (id),
  CONSTRAINT link_permissions_role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE transition_permissions (
  transition_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  CONSTRAINT transition_permissions_pk PRIMARY KEY (transition_id, role_id),
  CONSTRAINT transition_permissions_transition_fk FOREIGN KEY (transition_id) REFERENCES transition (id),
  CONSTRAINT transition_permissions_role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);

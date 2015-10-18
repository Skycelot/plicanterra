create table PROJECT (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  constraint PROJECT_PK primary key (ID)
);

create table PRINCIPAL (
  ID bigint not null,
  LOGIN varchar (50),
  FULL_NAME varchar (250),
  PASSWORD varchar (50),
  constraint PRINCIPAL_PK primary key (ID)
);

create table TEMPLATE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  PROJECT_ID bigint,
  constraint TEMPLATE_PK primary key (ID),
  constraint TEMPLATE_PROJECT_FK foreign key (PROJECT_ID) references PROJECT (ID)
);

create table ROLE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  PROJECT_ID bigint,
  constraint ROLE_PK primary key (ID),
  constraint ROLE_PROJECT_FK foreign key (PROJECT_ID) references PROJECT (ID)
);

create table ATTRIBUTE_TYPE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  constraint ATTRIBUTE_TYPE_PK primary key (ID)
);

create table ATTRIBUTE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  TEMPLATE_ID bigint,
  ATTRIBUTE_TYPE_ID bigint,
  constraint ATTRIBUTE_PK primary key (ID),
  constraint ATTRIBUTE_TEMPLATE_FK foreign key (TEMPLATE_ID) references TEMPLATE (ID),
  constraint ATTRIBUTE_ATTRIBUTE_TYPE_FK foreign key (ATTRIBUTE_TYPE_ID) references ATTRIBUTE_TYPE (ID)
);

create table LINK_TYPE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  constraint LINK_TYPE_PK primary key (ID)
);

create table LINK (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  TEMPLATE_1_ID bigint,
  TEMPLATE_2_ID bigint,
  LINK_TYPE_ID bigint,
  constraint LINK_PK primary key (ID),
  constraint LINK_TEMPLATE_1_FK foreign key (TEMPLATE_1_ID) references TEMPLATE (ID),
  constraint LINK_TEMPLATE_2_FK foreign key (TEMPLATE_2_ID) references TEMPLATE (ID),
  constraint LINK_LINK_TYPE_FK foreign key (LINK_TYPE_ID) references LINK_TYPE (ID)
);

create table STATUS_STAGE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  constraint STATUS_STAGE_PK primary key (ID)
);

create table STATUS (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  TEMPLATE_ID bigint,
  STATUS_STAGE_ID bigint,
  constraint STATUS_PK primary key (ID),
  constraint STATUS_TEMPLATE_FK foreign key (TEMPLATE_ID) references TEMPLATE (ID),
  constraint STATUS_STATUS_STAGE_FK foreign key (STATUS_STAGE_ID) references STATUS_STAGE (ID)
);

create table TRANSITION (
  ID bigint not null,
  SOURCE_STATUS_ID bigint,
  DESTINATION_STATUS_ID bigint,
  constraint TRANSITION_PK primary key (ID),
  constraint TRANSITION_STATUS_SRC_FK foreign key (SOURCE_STATUS_ID) references STATUS (ID),
  constraint TRANSITION_STATUS_DEST_FK foreign key (DESTINATION_STATUS_ID) references STATUS (ID)
);

create table TRANSITION_PERMISSIONS (
  TRANSITION_ID bigint not null,
  ROLE_ID bigint not null,
  constraint TRANSITION_PERMISSIONS_PK primary key (TRANSITION_ID, ROLE_ID),
  constraint TRANSITION_PERMISSIONS_TRANSITION_FK foreign key (TRANSITION_ID) references TRANSITION (ID),
  constraint TRANSITION_PERMISSIONS_ROLE_FK foreign key (ROLE_ID) references ROLE (ID),
);

create table PROFILE (
  ID bigint not null,
  PRINCIPAL_ID bigint,
  PROJECT_ID bigint,
  constraint PROFILE_PK primary key (ID),
  constraint PROFILE_PRINCIPAL_FK foreign key (PRINCIPAL_ID) references PRINCIPAL (ID),
  constraint PROFILE_PROJECT_FK foreign key (PROJECT_ID) references PROJECT (ID)
);

create table PRINCIPAL_ROLES (
  PROFILE_ID bigint,
  ROLE_ID bigint,
  constraint PRINCIPAL_ROLES_PK primary key (PROFILE_ID, ROLE_ID),
  constraint PRINCIPAL_ROLES_PROFILE_FK foreign key (PROFILE_ID) references PROFILE (ID),
  constraint PRINCIPAL_ROLES_ROLE_FK foreign key (ROLE_ID) references ROLE (ID)
);

create table PERMISSION_TYPE (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  constraint PERMISSION_TYPE_PK primary key (ID)
);

create table PERMISSION (
  ID bigint not null,
  CODE varchar (50),
  NAME varchar (150),
  DESCRIPTION varchar (2000),
  PERMISSION_TYPE_ID bigint,
  constraint PERMISSION_PK primary key (ID),
  constraint PERMISSION_PERMISSION_TYPE_FK foreign key (PERMISSION_TYPE_ID) references PERMISSION_TYPE (ID)
);

create table TEMPLATE_PERMISSIONS (
  TEMPLATE_ID bigint not null,
  ROLE_ID bigint not null,
  PERMISSION_ID bigint,
  constraint TEMPLATE_PERMISSIONS_PK primary key (TEMPLATE_ID, ROLE_ID),
  constraint TEMPLATE_PERMISSIONS_PROFILE_FK foreign key (TEMPLATE_ID) references TEMPLATE (ID),
  constraint TEMPLATE_PERMISSIONS_ROLE_FK foreign key (ROLE_ID) references ROLE (ID),
  constraint TEMPLATE_PERMISSIONS_PERMISSION_FK foreign key (PERMISSION_ID) references PERMISSION (ID)
);

create table ATTRIBUTE_PERMISSIONS (
  ATTRIBUTE_ID bigint not null,
  ROLE_ID bigint not null,
  PERMISSION_ID bigint,
  constraint ATTRIBUTE_PERMISSIONS_PK primary key (ATTRIBUTE_ID, ROLE_ID),
  constraint ATTRIBUTE_PERMISSIONS_ATTRIBUTE_FK foreign key (ATTRIBUTE_ID) references ATTRIBUTE (ID),
  constraint ATTRIBUTE_PERMISSIONS_ROLE_FK foreign key (ROLE_ID) references ROLE (ID),
  constraint ATTRIBUTE_PERMISSIONS_PERMISSION_FK foreign key (PERMISSION_ID) references PERMISSION (ID)
);

create table LINK_PERMISSIONS (
  LINK_ID bigint not null,
  ROLE_ID bigint not null,
  PERMISSION_ID bigint,
  constraint LINK_PERMISSIONS_PK primary key (LINK_ID, ROLE_ID),
  constraint LINK_PERMISSIONS_FK foreign key (LINK_ID) references LINK (ID),
  constraint LINK_PERMISSIONS_ROLE_FK foreign key (ROLE_ID) references ROLE (ID),
  constraint LINK_PERMISSIONS_PERMISSION_FK foreign key (PERMISSION_ID) references PERMISSION (ID)
);

create table ATTRIBUTE_GRANTS (
  ATTRIBUTE_ID bigint not null,
  STATUS_ID bigint not null,
  PERMISSION_ID bigint,
  constraint ATTRIBUTE_GRANTS_PK primary key (ATTRIBUTE_ID, STATUS_ID),
  constraint ATTRIBUTE_GRANTS_ATTRIBUTE_FK foreign key (ATTRIBUTE_ID) references ATTRIBUTE (ID),
  constraint ATTRIBUTE_GRANTS_STATUS_FK foreign key (STATUS_ID) references STATUS (ID),
  constraint ATTRIBUTE_GRANTS_PERMISSION_FK foreign key (PERMISSION_ID) references PERMISSION (ID)
);

create table LINK_GRANTS (
  LINK_ID bigint not null,
  STATUS_ID bigint not null,
  PERMISSION_ID bigint,
  constraint LINK_GRANTS_PK primary key (LINK_ID, STATUS_ID),
  constraint LINK_GRANTS_LINK_FK foreign key (LINK_ID) references LINK (ID),
  constraint LINK_GRANTS_STATUS_FK foreign key (STATUS_ID) references STATUS (ID),
  constraint LINK_GRANTS_PERMISSION_FK foreign key (PERMISSION_ID) references PERMISSION (ID)
);

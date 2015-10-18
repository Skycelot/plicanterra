# Ojbect management system.

### Description
The system operates with definable objects. Every object consists of attributes set and statuses. Objects can be linked together. Status can apply restrictions on attributes. There is a security mechanism of roles, that restrict object, attribute manipulations and status changes.

The system operates with two schemas: metadata schema (contains object definitions and restrictions) and data schema (contains actual object data).

### Architecture
* Metamodel module - loads object definitions, reflects metadata definitions on data schema.
* CRUD module - persistence api for actual object data.
* Security module - restricts data access based on role model.
* Client web interface with REST api - user interface.

### Platform
JRE 1.8, Java 7 EE application server, SQL server.<br/>
Security login module implemeted only for wildfly.<br/>
Database dialect implemented only for Postgres.<br/>
Requires 2 datasources:<br/>
metadata: jndi-name - "java:/jdbc/metamodel"<br/>
data: jndi-name - "java:/jdbc/data"<br/>
Requires setting custom security realm with name "plicanterra-domain" with login module ru.petrosoft.erratum.security.login.WildflyLoginModule<br/>
Requires application.properties file with setting its location as java property "property.file.path"<br/>

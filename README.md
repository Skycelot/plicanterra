# Ojbect management system.

### Description
The system operates with definable objects. Every object consists of attributes set and statuses. Objects can be linked together. Status can apply restrictions on attributes. There is a security mechanism of roles, that restrict object, attribute manipulations and status changes.

The system operates with two schemas: metadata schema (contains object definitions and restrictions) and data schema (contains actual object data).

### Architecture
* Metamodel module - loads object definitions, reflects metadata definitions on data schema.
* CRUD module - persistence api for actual object data.
* Security module - restricts data access based on role model.
* Client web interface with REST api - user interface.

package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.skycelot.plicanterra.metamodel.ElementPermission;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AttributePermissions.class)
public abstract class AttributePermissions_ {

	public static volatile SingularAttribute<AttributePermissions, Role> role;
	public static volatile SingularAttribute<AttributePermissions, ElementPermission> permission;
	public static volatile SingularAttribute<AttributePermissions, Attribute> attribute;

}


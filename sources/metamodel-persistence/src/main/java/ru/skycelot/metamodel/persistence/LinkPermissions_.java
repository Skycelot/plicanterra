package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.skycelot.plicanterra.metamodel.ElementPermission;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LinkPermissions.class)
public abstract class LinkPermissions_ {

	public static volatile SingularAttribute<LinkPermissions, Role> role;
	public static volatile SingularAttribute<LinkPermissions, Link> link;
	public static volatile SingularAttribute<LinkPermissions, ElementPermission> permission;

}


package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.skycelot.plicanterra.metamodel.ElementPermission;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LinkGrants.class)
public abstract class LinkGrants_ {

	public static volatile SingularAttribute<LinkGrants, Link> link;
	public static volatile SingularAttribute<LinkGrants, ElementPermission> permission;
	public static volatile SingularAttribute<LinkGrants, Status> status;

}


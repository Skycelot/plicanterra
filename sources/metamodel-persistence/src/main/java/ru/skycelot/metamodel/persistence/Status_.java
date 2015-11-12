package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Status.class)
public abstract class Status_ {

	public static volatile SingularAttribute<Status, Template> template;
	public static volatile SingularAttribute<Status, String> code;
	public static volatile SingularAttribute<Status, Boolean> initial;
	public static volatile SingularAttribute<Status, String> name;
	public static volatile SingularAttribute<Status, String> description;
	public static volatile SingularAttribute<Status, Long> id;

}


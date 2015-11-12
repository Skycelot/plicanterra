package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transition.class)
public abstract class Transition_ {

	public static volatile SingularAttribute<Transition, Status> sourceStatus;
	public static volatile ListAttribute<Transition, Role> roles;
	public static volatile SingularAttribute<Transition, Long> id;
	public static volatile SingularAttribute<Transition, Status> destinationStatus;

}


package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Profile.class)
public abstract class Profile_ {

	public static volatile ListAttribute<Profile, Role> roles;
	public static volatile SingularAttribute<Profile, Project> project;
	public static volatile SingularAttribute<Profile, Long> id;
	public static volatile SingularAttribute<Profile, User> user;

}


package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.skycelot.plicanterra.metamodel.LinkType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Link.class)
public abstract class Link_ {

	public static volatile SingularAttribute<Link, Template> rightTemplate;
	public static volatile SingularAttribute<Link, String> code;
	public static volatile SingularAttribute<Link, Template> leftTemplate;
	public static volatile SingularAttribute<Link, String> name;
	public static volatile SingularAttribute<Link, String> description;
	public static volatile SingularAttribute<Link, Long> id;
	public static volatile SingularAttribute<Link, LinkType> type;

}


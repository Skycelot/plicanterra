package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Template.class)
public abstract class Template_ {

	public static volatile SingularAttribute<Template, String> code;
	public static volatile SingularAttribute<Template, String> name;
	public static volatile SingularAttribute<Template, String> description;
	public static volatile SingularAttribute<Template, Project> project;
	public static volatile SingularAttribute<Template, Long> id;

}


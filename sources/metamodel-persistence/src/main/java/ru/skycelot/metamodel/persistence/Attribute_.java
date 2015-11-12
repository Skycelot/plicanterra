package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.skycelot.plicanterra.metamodel.AttributeType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Attribute.class)
public abstract class Attribute_ {

	public static volatile SingularAttribute<Attribute, Template> template;
	public static volatile SingularAttribute<Attribute, String> code;
	public static volatile SingularAttribute<Attribute, String> name;
	public static volatile SingularAttribute<Attribute, String> description;
	public static volatile SingularAttribute<Attribute, Long> id;
	public static volatile SingularAttribute<Attribute, AttributeType> type;

}


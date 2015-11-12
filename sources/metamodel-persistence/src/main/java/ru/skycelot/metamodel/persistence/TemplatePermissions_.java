package ru.skycelot.metamodel.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.skycelot.plicanterra.metamodel.TemplatePermission;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TemplatePermissions.class)
public abstract class TemplatePermissions_ {

	public static volatile SingularAttribute<TemplatePermissions, Template> template;
	public static volatile SingularAttribute<TemplatePermissions, Role> role;
	public static volatile SingularAttribute<TemplatePermissions, TemplatePermission> permission;

}


package ru.skycelot.plicanterra.util.orm.loader;

import org.junit.Assert;
import org.junit.Test;
import ru.skycelot.plicanterra.metamodel.Project;

/**
 *
 */
public class MetamodelLoader {

    @Test
    public void testFindEntity() {
        EntityManager em = new EntityManager(new MyDataSource());
        Project project = em.find(Project.class, 1);
        Assert.assertNotNull(project);
    }
}

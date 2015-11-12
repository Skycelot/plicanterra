package ru.skycelot.metamodel.service;

import java.util.List;
import javax.ejb.Stateless;
import ru.skycelot.plicanterra.metamodel.Project;
import ru.skycelot.plicanterra.metamodel.User;

/**
 *
 */
@Stateless
public class MetamodelCrud {

    public Project loadProject(String projectCode) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public List<User> loadUsers(String projectCode) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}

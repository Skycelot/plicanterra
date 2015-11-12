package ru.skycelot.plicanterra.security.core.loader;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

/**
 *
 */
public class ReflectionHelper {

    public static List<Field> searchTableColumnFields(Class entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields()).
                filter(field -> field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class)).
                collect(Collectors.toList());
    }
}

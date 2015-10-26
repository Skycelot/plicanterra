package ru.skycelot.plicanterra.util.orm.loader;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
public class OrmReflectionHelper {

    public static void checkTableAnnotation(Class entityClass) {
        if (entityClass != null) {
            if (!entityClass.isAnnotationPresent(Table.class)) {
                throw new IllegalStateException("Class " + entityClass.getCanonicalName() + " doesn't have @Table annotation!");
            }
        } else {
            throw new IllegalArgumentException("Argument is null!");
        }
    }

    public static Field findIdField(Class entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new IllegalStateException("Class doesn't have a field with @Id annotation!");
    }

    public static Field findFieldByName(Class entityClass, String fieldName) {
        Optional<Field> result = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .findAny();
        return result.orElseThrow(() -> new IllegalStateException("Class doesn't have a field with @Id annotation!"));
    }

    public static void setFieldValue(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }
}

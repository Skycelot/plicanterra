package ru.petrosoft.erratum.util;

/**
 *
 */
public class ArgumentsChecker {

    public static void notNull(Argument... arguments) {
        for (Argument argument : arguments) {
            if (argument.value == null) {
                throw new IllegalArgumentException(argument.name + " can not be null");
            }
        }
    }
}

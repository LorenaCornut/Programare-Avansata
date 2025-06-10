package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Responsible for analyzing and printing the prototype of Java classes.
 * <p>
 * This includes printing class modifiers, constructors, and methods with their signatures.
 */
public class ClassAnalyzer {

    /**
     * Prints the class declaration including modifiers, class name,
     * constructors, and methods.
     *
     * @param cls the Class object to analyze and print
     */
    public void printPrototype(Class<?> cls) {
        System.out.println(Modifier.toString(cls.getModifiers()) + " class " + cls.getName());
        printConstructors(cls);
        printMethods(cls);
        System.out.println();
    }

    /**
     * Prints all declared constructors of the given class,
     * including their modifiers and parameter types.
     *
     * @param cls the Class object whose constructors to print
     */
    private void printConstructors(Class<?> cls) {
        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            System.out.println("  " + Modifier.toString(constructor.getModifiers()) +
                    " " + constructor.getName() + formatParameters(constructor.getParameterTypes()));
        }
    }

    /**
     * Prints all declared methods of the given class,
     * including their modifiers, return types, names, and parameter types.
     *
     * @param cls the Class object whose methods to print
     */
    private void printMethods(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            System.out.println("  " + Modifier.toString(method.getModifiers()) +
                    " " + method.getReturnType().getSimpleName() +
                    " " + method.getName() + formatParameters(method.getParameterTypes()));
        }
    }

    /**
     * Formats the parameter types of a method or constructor as a comma-separated list.
     *
     * @param parameterTypes array of parameter Class objects
     * @return a formatted string representing the parameter list, e.g. "(int, String)"
     */
    private String formatParameters(Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameterTypes[i].getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }
}

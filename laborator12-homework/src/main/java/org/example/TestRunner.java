package org.example;

import org.example.MockValueGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Responsible for executing test methods annotated with @Test.
 * <p>
 * It detects test methods, creates instances if necessary, generates mock arguments,
 * executes the tests, and records the results in the provided TestStatistics.
 */
public class TestRunner {

    private final MockValueGenerator mockGenerator = new MockValueGenerator();

    /**
     * Runs all test methods marked with @Test in the given class,
     * updating the provided statistics object with pass/fail results.
     *
     * @param cls the Class object containing test methods
     * @param statistics the TestStatistics instance for recording test outcomes
     */
    public void runTests(Class<?> cls, TestStatistics statistics) {
        for (Method method : cls.getDeclaredMethods()) {
            if (isTestMethod(method)) {
                executeTest(cls, method, statistics);
            }
        }
    }

    /**
     * Checks if a method is annotated with @Test.
     *
     * @param method the Method to check
     * @return true if the method has the @Test annotation, false otherwise
     */
    private boolean isTestMethod(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Test")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes a single test method, creating an instance if the method is non-static,
     * generating mock arguments, invoking the method, and recording the result.
     *
     * @param cls the Class containing the method
     * @param method the test Method to execute
     * @param statistics the TestStatistics to update with the test result
     */
    private void executeTest(Class<?> cls, Method method, TestStatistics statistics) {
        try {
            Object instance = createInstanceIfNeeded(cls, method);
            Object[] args = generateMethodArguments(method);

            method.setAccessible(true);
            method.invoke(instance, args);

            statistics.recordPassed();
            System.out.println("[PASS] " + cls.getName() + "." + method.getName() + "\n");

        } catch (Exception ex) {
            statistics.recordFailed();
            System.out.println("[FAIL] " + cls.getName() + "." + method.getName() +
                    " -> " + ex.getCause());
        }
    }

    /**
     * Creates an instance of the class if the test method is non-static.
     * Assumes the class has a no-argument constructor.
     *
     * @param cls the Class to instantiate
     * @param method the Method to check for static modifier
     * @return an instance of cls or null if the method is static
     * @throws Exception if instantiation fails
     */
    private Object createInstanceIfNeeded(Class<?> cls, Method method) throws Exception {
        if (Modifier.isStatic(method.getModifiers())) {
            return null;
        }

        Constructor<?> constructor = cls.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * Generates mock argument values for the parameters of the test method.
     * Uses the MockValueGenerator to produce values matching each parameter's type.
     *
     * @param method the Method whose parameters need mock values
     * @return an array of mock argument values
     */
    private Object[] generateMethodArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = mockGenerator.generateMockValue(parameterTypes[i]);
        }

        return args;
    }
}

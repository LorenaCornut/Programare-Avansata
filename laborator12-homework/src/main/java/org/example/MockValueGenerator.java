package org.example;

import java.util.Random;

/**
 * Generates mock values for method parameters during testing.
 * <p>
 * Supports primitive types and their wrappers, as well as String and char.
 * For other complex types, returns null.
 */
public class MockValueGenerator {

    private final Random random = new Random();

    /**
     * Generates a mock value corresponding to the given type.
     * <p>
     * Supported types:
     * <ul>
     *   <li>int / Integer</li>
     *   <li>long / Long</li>
     *   <li>boolean / Boolean</li>
     *   <li>double / Double</li>
     *   <li>float / Float</li>
     *   <li>String</li>
     *   <li>char / Character</li>
     * </ul>
     * Returns null for unsupported or complex types.
     *
     * @param type the Class object representing the parameter type
     * @return a randomly generated mock value matching the type, or null if unsupported
     */
    public Object generateMockValue(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return random.nextInt();
        }
        if (type == long.class || type == Long.class) {
            return random.nextLong();
        }
        if (type == boolean.class || type == Boolean.class) {
            return random.nextBoolean();
        }
        if (type == double.class || type == Double.class) {
            return random.nextDouble();
        }
        if (type == float.class || type == Float.class) {
            return random.nextFloat();
        }
        if (type == String.class) {
            return random.nextInt() + " - mock string";
        }
        if (type == char.class || type == Character.class) {
            return (char) ('A' + random.nextInt(26));
        }

        // For complex types, return null by default
        return null;
    }
}

package edu.kit.ipd.sdq.visualj.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a static class, which contians utility methods for dealing with
 * reflection.
 */
public final class ReflectionUtils {
    
    private static Map<Class<?>, Class<?>> primitiveToWrapper0 = new HashMap<>();
    private static Map<Class<?>, Class<?>> wrapperToPrimitive0 = new HashMap<>();
    static {
        primitiveToWrapper0.put(byte.class, Byte.class);
        primitiveToWrapper0.put(short.class, Short.class);
        primitiveToWrapper0.put(int.class, Integer.class);
        primitiveToWrapper0.put(long.class, Long.class);
        primitiveToWrapper0.put(float.class, Float.class);
        primitiveToWrapper0.put(double.class, Double.class);
        primitiveToWrapper0.put(boolean.class, Boolean.class);
        primitiveToWrapper0.put(char.class, Character.class);
        
        wrapperToPrimitive0.put(Byte.class, byte.class);
        wrapperToPrimitive0.put(Short.class, short.class);
        wrapperToPrimitive0.put(Integer.class, int.class);
        wrapperToPrimitive0.put(Long.class, long.class);
        wrapperToPrimitive0.put(Float.class, float.class);
        wrapperToPrimitive0.put(Double.class, double.class);
        wrapperToPrimitive0.put(Boolean.class, boolean.class);
        wrapperToPrimitive0.put(Character.class, char.class);
        
        primitiveToWrapper = Collections.unmodifiableMap(primitiveToWrapper0);
        wrapperToPrimitive = Collections.unmodifiableMap(wrapperToPrimitive0);
    }
    
    public static Map<Class<?>, Class<?>> primitiveToWrapper;
    
    public static Map<Class<?>, Class<?>> wrapperToPrimitive;
    
    /**
     * This returns all methods in a specified class that are compatible with a
     * certain parameter tuple.
     * 
     * <p>
     * For example, for the following class:
     * 
     * <pre>
     * <code>
     *      class Foo {
     *      
     *          public void m1(int i, Object o) { }
     *          public void m2(boolean b, String s) { }
     *          public void m3(Integer i, String s) { }
     *      }
     *  </code>
     * </pre>
     * 
     * the call
     * {@code getCompatibleMethods(Foo.class, null, Integer.class, String.class)}
     * will return the set
     * 
     * <pre>
     * <code>{m1,m3}</code>
     * </pre>
     * </p>
     * 
     * @param type
     *            the class to search for compatible methods.
     * @param name
     *            the name of the returned methods, or {@code null} if methods
     *            with any name should be returned
     * @param parameterTypes
     *            the parameters.
     * 
     * @return every static or non-static method with the specified name in
     *         {@code type} that can be called with the specified parameter
     *         types.
     * 
     * @see #getDeclaredOrCompatibleMethod()
     */
    public static Method[] getCompatibleMethods(Class<?> type, String name, Class<?>... parameterTypes) {
        Set<Method> result = new HashSet<>();
        
        outerLoop: for (Method method : type.getMethods()) {
            Class<?>[] methodParameterTypes = method.getParameterTypes();
            
            if (methodParameterTypes.length != parameterTypes.length
                    || (name != null && !name.equals(method.getName()))) {
                continue outerLoop;
            }
            
            for (int i = 0; i < parameterTypes.length; ++i) {
                if (!(methodParameterTypes[i].isAssignableFrom(parameterTypes[i])
                        || methodParameterTypes[i].equals(primitiveToWrapper.get(parameterTypes[i]))
                        || methodParameterTypes[i].equals(wrapperToPrimitive.get(parameterTypes[i])))) {
                    continue outerLoop;
                }
            }
            
            result.add(method);
        }
        
        return result.toArray(new Method[0]);
    }
    
    /**
     * Returns a method with the specified name that is compatible with the
     * specified parameter types.
     * 
     * <p>
     * If a method that takes exactly {@code parameterTypes} exists, that one is
     * returned. If no compatible method exists, a {@link NoSuchMethodException}
     * is thrown.
     * </p>
     * 
     * @param type
     * @param name
     * @param parameterTypes
     * 
     * @return a method with the specified name that is compatible with the
     *         specified parameter types.
     * 
     * @throws NoSuchMethodException
     *             if no compatible method exists.
     * @throws SecurityException
     * 
     * @see Class#getDeclaredMethod(String, Class...)
     * @see #getCompatibleMethods(Class, boolean, String, Class...)
     */
    public static Method getDeclaredOrCompatibleMethod(Class<?> type, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        Method method = null;
        
        try {
            method = type.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            // The method with the exact signature was not found.
            // Try and look for another compatible method with the same name.
            Method[] compatibleMethods = ReflectionUtils.getCompatibleMethods(type, name, parameterTypes);
            
            if (compatibleMethods.length == 0) {
                throw e;
            }
            
            method = compatibleMethods[0];
        }
        
        return method;
    }
    
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }
    
    private ReflectionUtils() {
    }
}

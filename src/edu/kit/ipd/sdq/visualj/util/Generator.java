package edu.kit.ipd.sdq.visualj.util;

/**
 * A generator for objects of a specified type.
 *
 * @param <T>
 *            the type to generate.
 */
@FunctionalInterface
public interface Generator<T> {
    
    T yield();
}

package edu.kit.ipd.sdq.visualj.util;

import java.util.Objects;

/**
 * A generic pair of objects.
 *
 * @param <T1>
 *            the type of the first object.
 * @param <T2>
 *            the type of the second object.
 */
public class Pair<T1, T2> {
    
    private T1 first;
    private T2 second;
    
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    
    public T1 getFirst() {
        return first;
    }
    
    public void setFirst(T1 first) {
        this.first = first;
    }
    
    public T2 getSecond() {
        return second;
    }
    
    public void setSecond(T2 second) {
        this.second = second;
    }
    
    @Override
    public String toString() {
        return "[" + first + ", " + second + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair<?, ?> p = (Pair<?, ?>) obj;
            return Objects.equals(first, p.first) && Objects.equals(second, p.second);
        }
        
        return false;
    }
}

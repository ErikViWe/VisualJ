package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.lang.reflect.Method;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.ArrayVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;

/**
 * A logger for arbitrary arrays.
 * 
 * If you want to log primitive arrays or arrays of primitive wrapper objects,
 * you should use one of the subclasses.
 */
public class ArrayLogger extends Logger<Object[]> {
    
    protected ArrayLogger() {
    }
    
    /**
     * Create a new {@code ArrayLogger}.
     * 
     * @param arr
     *            the initial value of the array to be logged.
     */
    public ArrayLogger(Object[] arr) {
        update(arr);
    }
    
    @Override
    protected Object[] cloneValue(Object[] t) {
        if (t == null)
            return null;
        Object[] clone = new Object[t.length];
        
        try {
            for (int i = 0; i < t.length; ++i) {
                Method cloneMethod = t[i].getClass().getMethod("clone");
                clone[i] = cloneMethod.invoke(t[i]);
            }
        } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
            // If we cannot clone all elements, just make a shallow copy of the
            // array.
            System.arraycopy(t, 0, clone, 0, t.length);
        }
        
        return clone;
    }
    
    @Override
    public final Visualizer createDefaultVisualizer() {
        return new ArrayVisualizer(this);
    }
}

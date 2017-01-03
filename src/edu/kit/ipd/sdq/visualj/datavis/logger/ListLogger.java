package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.ListVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;

/**
 * A logger for lists, i.e. objects of classes that implement the {@link List}
 * interface.
 *
 * @param <T>
 *            the type of elements in the list being logged.
 */
public class ListLogger<T> extends Logger<List<T>> {
    
    /**
     * Create a new {@code ListLogger}.
     * 
     * @param arr
     *            the initial value of the list to be logged.
     */
    public ListLogger(List<T> list) {
        update(list);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected List<T> cloneValue(List<T> t) {
        if (t == null)
            return null;
        List<T> clone = new ArrayList<>();
        
        try {
            for (T element : t) {
                Method cloneMethod = element.getClass().getMethod("clone");
                clone.add((T) cloneMethod.invoke(element));
            }
        } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
            // If we cannot clone all elements, just make a shallow copy.
            clone = new ArrayList<>(t);
        }
        
        return clone;
    }
    
    @Override
    public Visualizer createDefaultVisualizer() {
        return new ListVisualizer<T>(this);
    }
}

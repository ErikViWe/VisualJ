package edu.kit.ipd.sdq.visualj.datavis.logger;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.SimpleVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;

/**
 * A logger for immutable objects, such as strings and instances of the
 * primitive wrapper classes (Integer, Byte, etc.).
 * 
 * If the object you're logging is mutable, use another kind of logger.
 */
public class ImmutableLogger<T> extends Logger<T> {
    
    /**
     * Create a new {@code ImmutableLogger}.
     * 
     * @param arr
     *            the initial value of the object to be logged.
     */
    public ImmutableLogger(T t) {
        update(t);
    }
    
    @Override
    protected T cloneValue(T t) {
        return t;
    }
    
    @Override
    public Visualizer createDefaultVisualizer() {
        return new SimpleVisualizer<>(this);
    }
}

package edu.kit.ipd.sdq.visualj.datavis.logger;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.SimpleVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;

/**
 * A logger for objects of classes that implement {@link DeepCloneable}.
 * 
 * <p>
 * If your class does not implement {@link DeepCloneable}, or you do not have
 * access to the class's source code, check if one of the other subclasses works
 * for you.
 * </p>
 *
 * @param <T>
 *            the type of the object being logged.
 */
public class DeepCloneableLogger<T> extends Logger<DeepCloneable<T>> {
    
    /**
     * Create a new {@code DeepCloneableLogger}.
     * 
     * @param t
     *            the initial value of the object to be logged.
     */
    public DeepCloneableLogger(DeepCloneable<T> t) {
        update(t);
    }
    
    @Override
    protected DeepCloneable<T> cloneValue(DeepCloneable<T> t) {
        return t.clone();
    }
    
    @Override
    public Visualizer createDefaultVisualizer() {
        return new SimpleVisualizer<>(this);
    }
    
}

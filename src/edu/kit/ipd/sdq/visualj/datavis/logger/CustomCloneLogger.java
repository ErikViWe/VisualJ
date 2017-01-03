package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.util.function.UnaryOperator;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.SimpleVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;

/**
 * A logger for arbitrary objects which can be cloned using a custom lambda
 * method.
 * 
 * <p>
 * Most other logger subclasses provide their own cloning functionality; so you
 * should only use this class as a last resort if no other subclass suits you
 * and you do not want to write your own subclass for some reason.
 * </p>
 *
 * @param <T>
 *            the type of the object being logged.
 */
public class CustomCloneLogger<T> extends Logger<T> {
    
    private UnaryOperator<T> cloneMethod;
    
    /**
     * Create a new {@code CustomCloneLogger}.
     * 
     * @param t
     *            the initial value of the object to be logged.
     * @param cloneMethod
     *            a lambda method used to clone objects of type {@code T}.
     */
    public CustomCloneLogger(T t, UnaryOperator<T> cloneMethod) {
        this.cloneMethod = cloneMethod;
        update(t);
    }
    
    @Override
    protected T cloneValue(T t) {
        return cloneMethod.apply(t);
    }
    
    @Override
    public Visualizer createDefaultVisualizer() {
        return new SimpleVisualizer<>(this);
    }
    
}

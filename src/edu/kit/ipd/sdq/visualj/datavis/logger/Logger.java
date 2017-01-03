package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Window;

/**
 * Base class for all loggers.
 * 
 * A logger logs the changing states of an object (via its
 * {@link Logger#update(Object)} method).
 * 
 * Those states may then be visualized in a {@link Window} via a fitting
 * {@link Visualizer}.
 *
 * @param <T>
 *            the type of the object being logged.
 */
public abstract class Logger<T> {
    
    /**
     * The history of previous values.
     * 
     * At every breakpoint, the {@link currentValue} is appended to this list.
     * 
     * <p>
     * When changing the type, make sure that the type you choose allows
     * insertion of {@code null} elements.
     * </p>
     */
    private final List<T> history = new ArrayList<>();
    
    /**
     * The current value, i.e. (a clone of) the last value passed to
     * {@link update}.
     */
    private T currentValue;
    
    public Logger() {
        for (int i = 0; i <= LogManager.getInstance().getBreakpointViewer().getLatestBreakpointId(); ++i) {
            history.add(null);
        }
        
        LogManager.getInstance().addLogger(this);
    }
    
    /**
     * This method may return any of the following:
     * <ul>
     * <li>a deep copy of {@code t} if is is mutable, and a deep copy is
     * possible
     * <li>a shallow copy of {@code t} if it is mutable, and a deep copy is
     * impossible
     * <li>{@code t} itself otherwise.
     * </ul>
     * 
     * @param value
     *            the value to clone.
     * @return a deep or shallow copy, or {@code t} itself. See description.
     */
    protected abstract T cloneValue(T value);
    
    /**
     * Sets the current value of the object being logged.
     * 
     * Note that this value is not saved to this logger's history until you call
     * {@link LogManager#breakpoint()}.
     * 
     * @param newValue
     *            the current value of the object being logged.
     * @see #getCurrentValue()
     */
    public final void update(T newValue) {
        // In order to save memory, we only clone the new value if necessary.
        if (!Objects.equals(currentValue, newValue)) {
            currentValue = cloneValue(newValue);
        }
    }
    
    /**
     * Appends the {@link #currentValue} to the history.
     */
    final void saveCurrentValue() {
        history.add(currentValue);
    }
    
    /**
     * Gets the current value of the object being logged.
     * 
     * @return the current value of the object being logged, i.e. the last value
     *         passed to {@link update}.
     */
    public final T getCurrentValue() {
        // Return a clone, so the caller cannot rewrite history.
        return cloneValue(currentValue);
    }
    
    /**
     * Gets a previous value of the object being logged from this logger's
     * history.
     * 
     * @param breakpoint
     *            the index of a past breakpoint.
     * @return the value of this logger at the specified breakpoint.
     * @throws IndexOutOfBoundsException
     *             if {@code breakpoint} does not refer to a past breakpoint.
     */
    public final T getValue(int breakpoint) throws IndexOutOfBoundsException {
        // Return a clone, so the caller cannot rewrite history.
        return cloneValue(history.get(breakpoint));
    }
    
    /**
     * Creates a new default visualizer for this logger.
     * 
     * If you want a custom visualizer, create one yourself and pass this logger
     * in the constructor.
     * 
     * @return a new default visualizer for this logger
     */
    public abstract Visualizer createDefaultVisualizer();
    
    @Override
    public String toString() {
        StringJoiner historyStr = new StringJoiner("\r\n\t", "History:\r\n\t", "\r\n");
        
        for (T element : history) {
            historyStr.add(element.toString());
        }
        
        return "Current:\r\n\t" + currentValue + "\r\n" + historyStr;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentValue == null) ? 0 : currentValue.hashCode());
        result = prime * result + ((history == null) ? 0 : history.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Logger) {
            Logger<?> l = (Logger<?>) obj;
            return Objects.equals(currentValue, l.currentValue) && Objects.equals(history, l.history);
        }
        
        return false;
    }
}

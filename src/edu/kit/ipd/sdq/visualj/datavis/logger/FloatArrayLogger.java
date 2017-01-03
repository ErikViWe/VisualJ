package edu.kit.ipd.sdq.visualj.datavis.logger;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A logger for arrays of {@code float}s or {@code Float}s.
 */
public class FloatArrayLogger extends PrimitiveArrayLogger {
    
    /**
     * Create a new {@code FloatArrayLogger}.
     * 
     * @param arr
     *            the initial value of the array to be logged.
     */
    public FloatArrayLogger(float[] arr) {
        update(arr);
    }
    
    /**
     * Sets the current value of the object being logged.
     * 
     * Note that this value is not saved to this logger's history until you call
     * {@link LogManager#breakpoint()}.
     * 
     * @param newValue
     *            the current value of the object being logged.
     * @see #update(Object[])
     */
    public void update(float[] newValue) {
        update(ArrayUtils.toObject(newValue));
    }
}

package edu.kit.ipd.sdq.visualj.datavis.logger;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A logger for arrays of {@code double}s or {@code Double}s.
 */
public class DoubleArrayLogger extends PrimitiveArrayLogger {
    
    /**
     * Create a new {@code DoubleArrayLogger}.
     * 
     * @param arr
     *            the initial value of the array to be logged.
     */
    public DoubleArrayLogger(double[] arr) {
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
    public void update(double[] newValue) {
        update(ArrayUtils.toObject(newValue));
    }
}

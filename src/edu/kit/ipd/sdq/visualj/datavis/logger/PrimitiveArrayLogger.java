package edu.kit.ipd.sdq.visualj.datavis.logger;

/**
 * Base class for all loggers of arrays of primitives (or primitive wrappers).
 */
public abstract class PrimitiveArrayLogger extends ArrayLogger {
    
    @Override
    protected final Object[] cloneValue(Object[] t) {
        if (t == null)
            return null;
        Object[] clone = new Object[t.length];
        System.arraycopy(t, 0, clone, 0, t.length);
        return clone;
    }
}

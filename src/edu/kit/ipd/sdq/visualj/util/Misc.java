package edu.kit.ipd.sdq.visualj.util;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Utility class with miscellaneous static methods.
 */
public final class Misc {
    
    /**
     * Scales a dimension's width and height by the specified factor
     * 
     * @param dimension
     *            a dimension.
     * @param scale
     *            the factor by which to scale the dimension's width and height.
     * @return a new, rescaled dimension.
     * 
     * @see #scaleDimension(Dimension, double, double)
     */
    public static Dimension scaleDimension(Dimension dimension, double scale) {
        return scaleDimension(dimension, scale, scale);
    }
    
    /**
     * Scales a dimension's width and height by the specified factors
     * 
     * @param dimension
     *            a dimension.
     * @param scaleWidth
     *            the factor by which to scale the dimension's width.
     * @param scaleHeight
     *            the factor by which to scale the dimension's height.
     * @return a new, rescaled dimension.
     * 
     * @see #scaleDimension(Dimension, double)
     */
    public static Dimension scaleDimension(Dimension dimension, double scaleWidth, double scaleHeight) {
        return new Dimension((int) (dimension.width * scaleWidth), (int) (dimension.height * scaleHeight));
    }
    
    /**
     * 
     * @param min
     * @param max
     * @return a random integer between {@code min} and {@code max} (inclusive).
     */
    public static int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    /**
     * Returns a clone of an array. The Array is clone as deeply as possible.
     * 
     * @param arr
     *            an array.
     * @return a deep clone of the array.
     */
    public static Object[] cloneArray(Object[] arr) {
        Object[] result = new Object[arr.length];
        
        for (int j = 0; j < arr.length; ++j) {
            Object obj = arr[j];
            
            if (obj instanceof Serializable) {
                result[j] = SerializationUtils.clone((Serializable) obj);
            } else if (obj instanceof Object[]) {
                result[j] = cloneArray((Object[]) obj);
            } else {
                result[j] = ObjectUtils.cloneIfPossible(obj);
            }
        }
        
        return result;
    }
    
    private Misc() {
    }
}

package edu.kit.ipd.sdq.visualj.datavis.logger;

/**
 * Classes implementing this interface must provide a public {@link #clone()}
 * method which returns a deep clone of the object it is invoked on.
 * 
 * <p>
 * Objects of classes implementing this interface may be used in a
 * {@link DeepCloneableLogger}.
 * </p>
 */
public interface DeepCloneable<T> extends Cloneable {
    
    /**
     * 
     * @return a deep clone of this object.
     */
    DeepCloneable<T> clone();
}

package edu.kit.ipd.sdq.visualj.util;

/**
 * An interface that represents a class that can be observed by a
 * {@link BreakpointObserver} for new breakpoints.
 * 
 * @see BreakpointObserver
 */
public interface BreakpointObservable {
    
    /**
     * Adds a new observer to this observable that is notified about new
     * breakpoints. If this observable already contains the observer, this
     * method has no effect.
     * 
     * @param observer
     *            the observer to add.
     * @throws NullPointerException
     *             if the parameter {@code observer} is {@code null}
     * 
     * @see BreakpointObserver#update(int)
     */
    void addBreakpointObserver(BreakpointObserver observer) throws NullPointerException;
    
    /**
     * Removes an observer from this observable, so it is no longer notified
     * about new breakpoints. If this observable does not contain the observer,
     * this method has no effect.
     * 
     * @param observer
     *            the observer to remove.
     * 
     * @see BreakpointObserver#update(int)
     */
    void removeBreakpointObserver(BreakpointObserver observer);
}

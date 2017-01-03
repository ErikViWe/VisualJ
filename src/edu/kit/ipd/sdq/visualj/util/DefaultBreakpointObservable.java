package edu.kit.ipd.sdq.visualj.util;

import java.util.Set;
import java.util.HashSet;

/**
 * A simple implementation of the {@link BreakpointObservable} interface that
 * can be used to encapsulate Observable logic.
 * 
 * <p>
 * Note that this class should only be used with composition because of the
 * public {@link #notifyObservers(int) notifyObservers} method.
 * </p>
 */
public class DefaultBreakpointObservable implements BreakpointObservable {
    
    private Set<BreakpointObserver> observers = new HashSet<>();
    
    /**
     * Notifies all observers previously added via
     * {@link #addBreakpointObserver(BreakpointObserver)} by calling their
     * {@link BreakpointObserver#update(int) update(int)} with the newly added
     * breakpoint id.
     * 
     * @param breakpointId
     *            the new breakpoint id
     */
    public void notifyObservers(int breakpointId) {
        for (BreakpointObserver observer : observers) {
            observer.update(breakpointId);
        }
    }
    
    @Override
    public void addBreakpointObserver(BreakpointObserver observer) throws NullPointerException {
        if (observer == null) {
            throw new NullPointerException("observer cannot be null.");
        }
        
        observers.add(observer);
    }
    
    @Override
    public void removeBreakpointObserver(BreakpointObserver observer) {
        observers.remove(observer);
    }
}

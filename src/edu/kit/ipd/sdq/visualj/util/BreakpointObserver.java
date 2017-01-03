package edu.kit.ipd.sdq.visualj.util;

/**
 * An interface that represents a class that can be notified by a
 * {@link BreakpointObservable} of new breakpoints.
 * 
 * @see BreakpointObservable
 */
public interface BreakpointObserver {
    /**
     * This method is called by a {@link BreakpointObservable} whenever a new
     * breakpoint is added.
     * 
     * @param breakpointId
     *            the breakpoint id
     */
    void update(int breakpointId);
}

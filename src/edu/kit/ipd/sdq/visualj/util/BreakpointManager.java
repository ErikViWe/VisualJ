package edu.kit.ipd.sdq.visualj.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of the {@link BreakpointViewer} interface that can be
 * used to encapsulate Breakpoint information.
 */
public class BreakpointManager implements BreakpointViewer {
    
    /**
     * [breakpointId] = breakpointLevel
     */
    private List<Integer> breakpoints = new ArrayList<Integer>();
    
    private DefaultBreakpointObservable observable = new DefaultBreakpointObservable();
    
    /**
     * Adds a new breakpoint with the given level and then notifies all
     * observers about the new breakpoint.
     * 
     * @param level
     *            the level of the breakpoint
     */
    public void addBreakpoint(int level) {
        breakpoints.add(level);
        
        observable.notifyObservers(getLatestBreakpointId());
    }
    
    @Override
    public int getBreakpointLevel(int breakpointId) throws IndexOutOfBoundsException {
        return this.breakpoints.get(breakpointId);
    }
    
    @Override
    public int getLatestBreakpointId() {
        return this.breakpoints.size() - 1;
    }
    
    @Override
    public int getNextBreakpointId(int fromBreakpointId, int maxLevel) {
        if (fromBreakpointId < 0 || fromBreakpointId >= this.breakpoints.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        for (int i = fromBreakpointId; i < this.breakpoints.size(); ++i) {
            if (this.breakpoints.get(i) <= maxLevel) {
                return i;
            }
        }
        
        return -1;
    }
    
    @Override
    public int getPrevBreakpointId(int fromBreakpointId, int maxLevel) {
        if (fromBreakpointId < 0 || fromBreakpointId >= this.breakpoints.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        for (int i = fromBreakpointId; i >= 0; --i) {
            if (this.breakpoints.get(i) <= maxLevel) {
                return i;
            }
        }
        
        return -1;
    }
    
    @Override
    public void addBreakpointObserver(BreakpointObserver observer) throws NullPointerException {
        observable.addBreakpointObserver(observer);
    }
    
    @Override
    public void removeBreakpointObserver(BreakpointObserver observer) {
        observable.removeBreakpointObserver(observer);
    }
}

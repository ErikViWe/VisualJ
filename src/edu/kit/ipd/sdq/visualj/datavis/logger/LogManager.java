package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.Window;
import edu.kit.ipd.sdq.visualj.util.BreakpointManager;
import edu.kit.ipd.sdq.visualj.util.BreakpointViewer;
import edu.kit.ipd.sdq.visualj.util.ThreadStatus;

/**
 * This class contains methods for managing all loggers in the program.
 */
public final class LogManager {
    private final static LogManager instance = new LogManager();
    
    public static LogManager getInstance() {
        return LogManager.instance;
    }
    
    private LogManager() {
    }
    
    private ThreadStatus threadStatus;
    
    private BreakpointManager breakpointManager = new BreakpointManager();
    
    private final Set<Logger<?>> allLoggers = new HashSet<>();
    
    /**
     * Called in the constructor of every {@link Logger} to let that logger be
     * managed by the {@code LogManager}.
     * 
     * @param l
     *            the logger to add.
     */
    void addLogger(Logger<?> l) {
        allLoggers.add(l);
    }
    
    /**
     * 
     * @return an unmodifiable set of every logger in the program.
     */
    public Set<Logger<?>> getAllLoggers() {
        return Collections.unmodifiableSet(allLoggers);
    }
    
    /**
     * Returns a {@link BreakpointViewer} that can be used e.g to search for
     * specific breakpoints.
     * 
     * @return the breakpoint manager
     */
    public BreakpointViewer getBreakpointViewer() {
        return breakpointManager;
    }
    
    /**
     * Resets all static state related to breakpoints. Namely, this method
     * 
     * <ul>
     * <li>resets the current breakpoint id to {@code -1}, and the next
     * breakpoint id to {@code 0}.
     * <li>unregisters all {@link Logger}s from the {@link LogManager}.
     * </ul>
     * 
     * <p>
     * Objects from the {@code datavis} package created before this method was
     * called may not work properly anymore
     * </p>
     * 
     * <p>
     * Note that this method differs from {@link Breakpoints#reset()} as it does not close any {@link Window windows}.
     * </p>
     */
    public void reset() {
        breakpointManager = new BreakpointManager();
        allLoggers.clear();
    }
    
    // TODO javadoc
    public void init() {
        threadStatus = new ThreadStatus(Thread.currentThread());
    }
    
    // TODO javadoc
    public ThreadStatus getThreadStatus() {
        return threadStatus;
    }
    
    /**
     * Sets a breakpoint with a default level of {@code 0}.
     * 
     * @see {@link #getMaxBlockingLevel()}
     * @see {@link #setMaxBlockingLevel(int)}
     */
    public void breakpoint() {
        breakpoint(0);
    }
    
    /**
     * Sets a breakpoint, and blocks execution if
     * {@code level <= getMaxBlockingLevel()}.
     * 
     * @param level
     *            the breakpoint level.
     * @see #breakpoint()
     * @see {@link #getMaxBlockingLevel()}
     * @see {@link #setMaxBlockingLevel(int)}
     */
    public void breakpoint(int level) {
        for (Logger<?> l : allLoggers) {
            l.saveCurrentValue();
        }
        
        // add new breakpoint and notify all observers about it
        breakpointManager.addBreakpoint(level);
    }
}

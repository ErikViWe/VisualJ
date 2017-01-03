package edu.kit.ipd.sdq.visualj.datavis.logger;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.Window;

/**
 * This class contains static methods for setting breakpoints.
 * 
 * <p>
 * It mostly provides a simpler interface to {@link LogManager#getInstance()}, and should be preferred to the latter if
 * possible. Note that {@link #reset()} is the only exception here because it adds additional functionality.
 * </p>
 */
public final class Breakpoints {
    
    /**
     * Sets a breakpoint with a default level of {@code 0}.
     * 
     * @see {@link #getMaxBlockingLevel()}
     * @see {@link #setMaxBlockingLevel(int)}
     */
    public static void breakpoint() {
        LogManager.getInstance().breakpoint();
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
    public static void breakpoint(int level) {
        LogManager.getInstance().breakpoint(level);
    }
    
    /**
     * Resets all static state related to breakpoints. Namely, this method
     * 
     * <ul>
     * <li>resets the current breakpoint id to 0.
     * <li>closes all {@link Window}s.
     * <li>unregisters all {@link Logger}s from the {@link LogManager}.
     * </ul>
     * 
     * <p>
     * Objects from the {@code datavis} package created before this method was
     * called may not work properly anymore
     * </p>
     * 
     * <p>
     * Note that this method differs from {@link LogManager#reset()} as it closes all {@link Window windows} in
     * addition.
     * </p>
     */
    public static void reset() {
        LogManager.getInstance().reset();
        Window.closeAll();
    }
    
    // TODO javadoc
    public static void init() {
        LogManager.getInstance().init();
    }
    
    private Breakpoints() {
    }
}

package edu.kit.ipd.sdq.visualj.efficiency.measure;

/**
 * This class contains static methods for counting steps, to be used as a kind
 * of time measurement.
 * 
 * @see EfficiencyTest
 */
public final class StepCounter {
    
    private static volatile long steps = 0;
    
    /**
     * Increments the step counter and returns the value before the
     * incrementation.
     * 
     * @return the step count before this method was called.
     */
    public synchronized static long step() {
        return steps++;
    }
    
    /**
     * Resets the step counter to {@code 0}.
     */
    public synchronized static void reset() {
        steps = 0;
    }
    
    /**
     * 
     * @return the current step count.
     */
    public synchronized static long getSteps() {
        return steps;
    }
    
    private StepCounter() {
    }
}

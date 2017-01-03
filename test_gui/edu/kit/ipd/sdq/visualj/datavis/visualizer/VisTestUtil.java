package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import edu.kit.ipd.sdq.visualj.datavis.logger.LogManager;

/**
 * Various utility methods for testing visualizers.
 */
public class VisTestUtil {
    
    public static void simulateBreakpoint() {
        LogManager.getInstance().breakpoint();
    }
    
    public static void waitFor(Condition condition) {
        while (!condition.eval()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                /* not so important */}
        }
    }
    
    public interface Condition {
        
        public boolean eval();
    }
}

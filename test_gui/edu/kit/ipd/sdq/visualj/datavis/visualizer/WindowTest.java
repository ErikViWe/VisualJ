package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import edu.kit.ipd.sdq.visualj.datavis.logger.Breakpoints;
import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.LogManager;

public class WindowTest {
    
    /**
     * Opens a {@link Window} to be tested interactively.
     * 
     * @throws InterruptedException
     */
    public static void test() {
        Breakpoints.reset();
        Breakpoints.init();
        
        LogManager.getInstance().getBreakpointViewer().addBreakpointObserver((breakpointId) -> {
            System.out.println("Breakpoint: " + breakpointId);
        });
        
        IntArrayLogger l1 = new IntArrayLogger(new int[]{1, 2, 3});
        IntArrayLogger l2 = new IntArrayLogger(new int[]{4, 5, 6});
        
        Window w = new Window();
        w.addVisualizer(l1, "L1:");
        w.addVisualizer(l2, "L2:");
        w.setVisible(true);
        
        Window w2 = new Window();
        w2.setVisible(true);
        w2.makeMainWindow();
        
        Breakpoints.breakpoint();
        
        for (int i = 2; i < 50; ++i) {
            l1.update(new int[]{i, 2 * i, 3 * i});
            Breakpoints.breakpoint();
        }
        
        throw new IllegalArgumentException();
    }
    
    public static void main(String[] args) {
        test();
    }
}

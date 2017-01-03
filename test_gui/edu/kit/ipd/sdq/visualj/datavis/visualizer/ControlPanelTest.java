package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Dimension;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.util.BreakpointManager;
import edu.kit.ipd.sdq.visualj.util.ThreadStatus;

public class ControlPanelTest {
    
    /**
     * Opens a {@link TestFrame} containing a {@link ControlPanel}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void test() {
        TestFrame frame
                = new TestFrame(new ControlPanel(new BreakpointManager(), new ThreadStatus(Thread.currentThread())));
        frame.setSize(new Dimension(500, 200));
        frame.waitForClose();
    }
    
}

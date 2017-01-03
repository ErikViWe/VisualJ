package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ImmutableLogger;

public class VisualizerWrapperTest {
    
    /**
     * Opens a {@link TestFrame} containing a {@link Visualizer} wrapped in a scroll pane.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testWrapper() {
        ImmutableLogger<String> logger = new ImmutableLogger<>("Hello world!");
        SimpleVisualizer<String> visualizer = new SimpleVisualizer<>(logger, "Test:");
        
        VisTestUtil.simulateBreakpoint();
        visualizer.update(0);
        
        new TestFrame(visualizer.getWrapper()).waitForClose();
    }
    
}

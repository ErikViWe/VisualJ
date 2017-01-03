package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ImmutableLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.SimpleVisualizer;

public class SimpleVisualizerTest {
    
    /**
     * Opens a {@link TestFrame} with a {@link SimpleVisualizer}.
     */
    @Test
    public void testVisualization() {
        ImmutableLogger<String> logger = new ImmutableLogger<String>("Hello world!");
        SimpleVisualizer<String> visualizer = new SimpleVisualizer<>(logger);
        
        VisTestUtil.simulateBreakpoint();
        visualizer.update(0);
        
        new TestFrame(visualizer).waitForClose();
    }
    
    /**
     * Opens a {@link TestFrame} with a {@link SimpleVisualizer}.
     * 
     * <p>
     * The visualizer must not throw an exception when trying to visualize
     * {@code null}.
     * </p>
     */
    @Test
    public void testNull() {
        ImmutableLogger<String> logger = new ImmutableLogger<String>(null);
        SimpleVisualizer<String> visualizer = new SimpleVisualizer<>(logger);
        
        VisTestUtil.simulateBreakpoint();
        visualizer.update(0);
        
        new TestFrame(visualizer).waitForClose();
    }
}

package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.MapLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.MapVisualizer;

public class MapVisualizerTest {
    
    HashMap<Integer, String> map;
    MapLogger<Integer, String> logger;
    MapVisualizer<Integer, String> visualizer;
    
    @Before
    public void setUp() {
        map = new HashMap<>();
        
        for (int i = 0; i < 50; i++) {
            map.put(i, generateRandomString());
        }
        
        map.put(51, "b��m");
        
        logger = new MapLogger<>(map);
        visualizer = new MapVisualizer<>(logger);
        VisTestUtil.simulateBreakpoint();
        
    }
    
    private String generateRandomString() {
        return String.valueOf(Math.random() * 100);
    }
    
    /**
     * Opens a {@link TestFrame} containing a {@link MapVisualizer}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateInt() throws InterruptedException {
        visualizer.update(0);
        TestFrame testFrame = new TestFrame(visualizer);
        Thread.sleep(5000);
        
        for (int i = 40; i <= 60; i += 2) {
            map.put(i, generateRandomString());
        }
        
        logger.update(map);
        VisTestUtil.simulateBreakpoint();
        visualizer.update(1);
        testFrame.setPreferredSize(visualizer.getPreferredSize());
        testFrame.add(visualizer);
        testFrame.pack();
        // Thread.sleep(3000);
        
        testFrame.waitForClose();
    }
    
}

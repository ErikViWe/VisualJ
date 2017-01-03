package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ListLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.ListVisualizer;

public class ListVisualizerStringTest {
    
    String[] exampleArray;
    ArrayList<String> list = new ArrayList<String>();
    Random random = new Random();
    ListLogger<String> logger;
    ListVisualizer<String> visualizer;
    
    @Before
    public void setUp() {
        exampleArray = new String[20];
        logger = new ListLogger<String>(list);
        for (int i = 0; i < 10; i++) {
            int x = (random.nextBoolean() ? 1 : -1);
            exampleArray = new String[Math.abs(20 + x * random.nextInt(20 - 0))];
            if (i == 4) {
                list = new ArrayList<String>();
            }
            list.clear();
            for (int j = 0; j < exampleArray.length; j++) {
                exampleArray[j] = j + "#" + random.nextInt(3 - 0) * 100;
                list.add(exampleArray[j]);
            }
            // if (i == random.nextInt(exampleArray.length * 10 - -1) || i == 5)
            
            if (i == 3) {
                list = null;
            }
            if (i == 6) {
                list.clear();
            }
            
            logger.update(list);
            VisTestUtil.simulateBreakpoint();
        }
        
        visualizer = new ListVisualizer<String>(logger);
        VisTestUtil.simulateBreakpoint();
        
    }
    
    /**
     * Opens a {@link TestFrame} containing a {@link ListVisualizer}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateInt() throws InterruptedException {
        TestFrame testFrame = new TestFrame(visualizer);
        testFrame.setSize(700, 250);
        testFrame.setMinimumSize(new Dimension(250, 250));
        testFrame.setVisible(true);
        for (int i = 0; i < 10; i++) {
            visualizer.update(i);
            // testFrame.add(visualizer).setBackground(new Color(240, 240,
            // 240));
            testFrame.repaint();
            testFrame.setVisible(true);
            Thread.sleep(500);
        }
        testFrame.waitForClose();
    }
}

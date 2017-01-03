package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ListLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.ListVisualizer;

public class ListVisualizerIntTest {
    
    int[] exampleArray;
    ArrayList<Integer> list = new ArrayList<Integer>();
    Random random = new Random();
    ListLogger<Integer> logger;
    ListVisualizer<Integer> visualizer;
    
    @Before
    public void setUp() {
        exampleArray = new int[40];
        logger = new ListLogger<Integer>(list);
        for (int i = 0; i < 10; i++) {
            int x = (random.nextBoolean() ? 1 : -1);
            exampleArray = new int[Math.abs(40 + x * random.nextInt(20 - 0))];
            if (i == 4) {
                list = new ArrayList<Integer>();
            }
            list.clear();
            for (int j = 0; j < exampleArray.length; j++) {
                exampleArray[j] = random.nextInt(3 - 0) * 100;
                list.add(exampleArray[j]);
            }
            if (i == 3) {
                list = null;
            }
            if (i == 6) {
                list.clear();
            }
            
            logger.update(list);
            VisTestUtil.simulateBreakpoint();
        }
        
        visualizer = new ListVisualizer<Integer>(logger);
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

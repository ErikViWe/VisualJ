package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.TreeLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.TreeVisualizer;
import edu.uci.ics.jung.graph.DelegateTree;

public class TreeVisualizerTest {
    
    // String and Double are just example Classes..
    TreeVisualizer<String, Double> visualizer;
    TreeLogger<String, Double> logger;
    DelegateTree<String, Double> tree;
    String[] vertices;
    static String option;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        String[] options = {"short", "thisIsALongText", "thisIsAVeryVeryVeryVeryVeryVeryVeryLongText"};
        int i = JOptionPane.showOptionDialog(null, "choose the vertices name prefix", "Name Prefix",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        option = options[i];
    }
    
    @Before
    public void setUp() {
        tree = new DelegateTree<>();
        vertices = new String[9];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = option + i;
        }
        tree.setRoot(vertices[0]);
    }
    
    /**
     * Opens a {@link TestFrame} containing a {@link TreeVisualizer}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateInt() {
        tree.addChild(0.0, vertices[0], vertices[1]);
        tree.addChild(0.1, vertices[0], vertices[2]);
        tree.addChild(0.2, vertices[1], vertices[3]);
        tree.addChild(0.3, vertices[1], vertices[4]);
        tree.addChild(0.4, vertices[2], vertices[5]);
        tree.addChild(0.5, vertices[2], vertices[6]);
        tree.addChild(0.6, vertices[0], vertices[7]);
        try {
            testEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void testEnd() throws InterruptedException {
        logger = new TreeLogger<>(tree);
        visualizer = new TreeVisualizer<>(logger);
        TestFrame testFrame = new TestFrame(visualizer);
        
        // first value, the built tree
        VisTestUtil.simulateBreakpoint();
        updateVisualizer(0, testFrame);
        Thread.sleep(2000);
        
        // add a child to the tree
        tree.addChild(0.7, vertices[6], vertices[8]);
        logger.update(tree);
        VisTestUtil.simulateBreakpoint();
        updateVisualizer(1, testFrame);
        Thread.sleep(2000);
        
        // remove a child (and its ancestors), add a new one in its place
        tree.removeChild(vertices[1]);
        tree.addChild(0.25, vertices[0], "neue 1");
        logger.update(tree);
        VisTestUtil.simulateBreakpoint();
        updateVisualizer(2, testFrame);
        Thread.sleep(2000);
        
        // test null
        tree = null;
        logger.update(tree);
        VisTestUtil.simulateBreakpoint();
        updateVisualizer(3, testFrame);
        Thread.sleep(2000);
        
        testFrame.waitForClose();
    }
    
    private void updateVisualizer(int i, TestFrame testFrame) {
        visualizer.update(i);
        testFrame.repaint();
        testFrame.setMinimumSize(visualizer.getPreferredSize());
        testFrame.setVisible(true);
    }
}

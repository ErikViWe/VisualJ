package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.GraphLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.GraphVisualizer;
import edu.kit.ipd.sdq.visualj.util.Misc;
import edu.uci.ics.jung.graph.SparseGraph;

public class GraphVisualizerTest {
    
    // String and Double are just example Classes..
    GraphVisualizer<String, Double> visualizer;
    GraphLogger<String, Double> logger;
    SparseGraph<String, Double> graph;
    
    @Before
    public void setUp() {
        graph = new SparseGraph<String, Double>();
        String[] vertices = new String[20];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = "test" + i;
            graph.addVertex(vertices[i]);
        }
        // add 20 edges
        for (int i = 0; i < 20; i++) {
            String v1 = vertices[Misc.getRandomInteger(0, vertices.length - 1)];
            String v2 = vertices[Misc.getRandomInteger(0, vertices.length - 1)];
            graph.addEdge(new Double(i), v1, v2);
        }
        logger = new GraphLogger<String, Double>(graph);
        visualizer = new GraphVisualizer<String, Double>(logger);
        
        VisTestUtil.simulateBreakpoint();
    }
    
    /**
     * Opens a {@link TestFrame} containing a {@link GraphVisualizer}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateInt() throws InterruptedException {
        
        visualizer.update(0);
        TestFrame testFrame = new TestFrame(visualizer);
        Thread.sleep(5000);
        // test null
        graph = null;
        logger.update(graph);
        VisTestUtil.simulateBreakpoint();
        visualizer.update(1);
        testFrame.setPreferredSize(visualizer.getPreferredSize());
        testFrame.add(visualizer);
        testFrame.pack();
        new TestFrame(visualizer).waitForClose();
    }
    
}

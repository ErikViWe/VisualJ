package edu.kit.ipd.sdq.visualj.datavis.logger;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.GraphLogger;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GraphLoggerTest extends BaseLoggerTest {
    
    /**
     * When passed a graph containing uncloneable values, the
     * {@link GraphLogger#cloneValue(Graph)} method should return a shallow
     * copy.
     */
    @Test
    public void testCloneValueUncloneable() {
        Graph<TestClass, TestClass> archetype = new SparseMultigraph<>();
        
        TestClass v0 = new TestClass(0);
        TestClass v1 = new TestClass(1);
        TestClass edge = new TestClass(42);
        
        archetype.addVertex(v0);
        archetype.addVertex(v1);
        archetype.addEdge(edge, v0, v1);
        
        Graph<TestClass, TestClass> clone = new GraphLogger<>(archetype).cloneValue(archetype);
        
        assertGraphEquals(archetype, clone);
        
        archetype.removeEdge(edge);
        assertGraphDoesNotEqual(archetype, clone);
    }
    
    /**
     * When passed a graph containing only cloneable values, the
     * {@link GraphLogger#cloneValue(Graph)} method should return a deep copy.
     */
    @Test
    public void testCloneValueCloneable() {
        Graph<TestClassCloneable, TestClassCloneable> archetype = new SparseMultigraph<>();
        
        TestClassCloneable v0 = new TestClassCloneable(0);
        TestClassCloneable v1 = new TestClassCloneable(1);
        TestClassCloneable edge = new TestClassCloneable(42);
        
        archetype.addVertex(v0);
        archetype.addVertex(v1);
        archetype.addEdge(edge, v0, v1);
        
        Graph<TestClassCloneable, TestClassCloneable> clone = new GraphLogger<>(archetype).cloneValue(archetype);
        
        assertGraphEquals(archetype, clone);
        
        edge.attribute = 21;
        assertGraphDoesNotEqual(archetype, clone);
    }
}

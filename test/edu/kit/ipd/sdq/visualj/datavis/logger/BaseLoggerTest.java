package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import edu.kit.ipd.sdq.visualj.datavis.logger.DeepCloneable;
import edu.kit.ipd.sdq.visualj.datavis.logger.Logger;
import edu.uci.ics.jung.graph.Graph;

public abstract class BaseLoggerTest {
    
    /**
     * 
     * @param expected
     *            the expected graph.
     * @param actual
     *            the actual graph.
     * @return {@code true} if and only if {@code expected} and {@code actual}
     *         contain equal edges and vertives.
     */
    public static <V, E> boolean graphEquals(Graph<V, E> expected, Graph<? super V, ? super E> actual) {
        if (expected.getVertexCount() != actual.getVertexCount() || expected.getEdgeCount() != actual.getEdgeCount()) {
            return false;
        }
        
        for (V vertex : expected.getVertices()) {
            if (!actual.containsVertex(vertex)) {
                return false;
            }
        }
        
        for (E edge : expected.getEdges()) {
            if (!actual.containsEdge(edge)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static <V, E> void assertGraphEquals(Graph<V, E> expected, Graph<? super V, ? super E> actual) {
        assertTrue(graphEquals(expected, actual));
    }
    
    public static <V, E> void assertGraphDoesNotEqual(Graph<V, E> expected, Graph<? super V, ? super E> actual) {
        assertFalse(graphEquals(expected, actual));
    }
    
    /**
     * Used to test {@link Logger}s. This class is not cloneable.
     */
    static class TestClass {
        
        int attribute = 0;
        
        TestClass() {
        }
        
        TestClass(int attribute) {
            this.attribute = attribute;
        }
        
        @Override
        public String toString() {
            return "TestClass(" + attribute + ")";
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + attribute;
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestClass) {
                return attribute == ((TestClass) obj).attribute;
            }
            
            return false;
        }
    }
    
    /**
     * Used to test {@link Logger}s. This class is cloneable.
     */
    static class TestClassCloneable extends TestClass implements DeepCloneable<TestClassCloneable> {
        
        TestClassCloneable() {
        }
        
        TestClassCloneable(int attribute) {
            super(attribute);
        }
        
        @Override
        public String toString() {
            return "TestClassCloneable(" + attribute + ")";
        }
        
        @Override
        public DeepCloneable<TestClassCloneable> clone() {
            return new TestClassCloneable(attribute);
        }
    }
}

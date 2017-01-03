package edu.kit.ipd.sdq.visualj.datavis.logger;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.TreeLogger;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;

public class TreeLoggerTest extends BaseLoggerTest {
    
    /**
     * When passed a tree containing uncloneable values, the
     * {@link TreeLogger#cloneValue(Tree)} method should return a shallow copy.
     */
    @Test
    public void testCloneValueUncloneable() {
        DelegateTree<TestClass, TestClass> archetype = new DelegateTree<>();
        
        TestClass v0 = new TestClass(0);
        TestClass v1 = new TestClass(1);
        TestClass edge = new TestClass(42);
        
        archetype.addVertex(v0);
        archetype.addChild(edge, v0, v1);
        
        Tree<TestClass, TestClass> clone = new TreeLogger<>(archetype).cloneValue(archetype);
        
        assertGraphEquals(archetype, clone);
        
        archetype.removeChild(v1);
        assertGraphDoesNotEqual(archetype, clone);
    }
    
    /**
     * When passed a tree containing only cloneable values, the
     * {@link TreeLogger#cloneValue(Tree)} method should return a deep copy.
     */
    @Test
    public void testCloneValueCloneable() {
        DelegateTree<TestClassCloneable, TestClassCloneable> archetype = new DelegateTree<>();
        
        TestClassCloneable v0 = new TestClassCloneable(0);
        TestClassCloneable v1 = new TestClassCloneable(1);
        TestClassCloneable edge = new TestClassCloneable(42);
        
        archetype.addVertex(v0);
        archetype.addChild(edge, v0, v1);
        
        Tree<TestClassCloneable, TestClassCloneable> clone = new TreeLogger<>(archetype).cloneValue(archetype);
        
        assertGraphEquals(archetype, clone);
        
        edge.attribute = 21;
        assertGraphDoesNotEqual(archetype, clone);
    }
}

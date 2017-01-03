package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ListLogger;

public class ListLoggerTest extends BaseLoggerTest {
    
    /**
     * When passed a list containing uncloneable values, the
     * {@link ListLogger#cloneValue(List)} method should return a shallow copy.
     */
    @Test
    public void testCloneValueUncloneable() {
        List<TestClass> archetype = new LinkedList<>();
        archetype.add(new TestClass(0));
        archetype.add(new TestClass(1));
        
        List<TestClass> clone = new ListLogger<>(archetype).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.add(0, new TestClass(42));
        assertNotEquals(archetype, clone);
    }
    
    /**
     * When passed a list containing only cloneable values, the
     * {@link ListLogger#cloneValue(List)} method should return a deep copy.
     */
    @Test
    public void testCloneValueCloneable() {
        List<TestClassCloneable> archetype = new LinkedList<>();
        archetype.add(new TestClassCloneable(0));
        archetype.add(new TestClassCloneable(1));
        
        List<TestClassCloneable> clone = new ListLogger<>(archetype).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.get(0).attribute = 42;
        assertNotEquals(archetype, clone);
    }
}

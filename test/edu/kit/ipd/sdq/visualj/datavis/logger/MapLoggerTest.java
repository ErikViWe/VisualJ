package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.MapLogger;

public class MapLoggerTest extends BaseLoggerTest {
    
    /**
     * When passed a map containing uncloneable values, the
     * {@link MapLogger#cloneValue(Map)} method should return a shallow copy.
     */
    @Test
    public void testCloneValueUncloneable() {
        Map<Integer, TestClass> archetype = new TreeMap<>();
        archetype.put(0, new TestClass(0));
        archetype.put(1, new TestClass(1));
        
        Map<Integer, TestClass> clone = new MapLogger<>(archetype).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.put(0, new TestClass(42));
        assertNotEquals(archetype, clone);
    }
    
    /**
     * When passed a map containing only cloneable values, the
     * {@link MapLogger#cloneValue(Map)} method should return a deep copy.
     */
    @Test
    public void testCloneValueCloneable() {
        Map<Integer, TestClass> archetype = new TreeMap<>();
        archetype.put(0, new TestClassCloneable(0));
        archetype.put(1, new TestClassCloneable(1));
        
        Map<Integer, TestClass> clone = new MapLogger<>(archetype).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.get(0).attribute = 42;
        assertNotEquals(archetype, clone);
    }
}

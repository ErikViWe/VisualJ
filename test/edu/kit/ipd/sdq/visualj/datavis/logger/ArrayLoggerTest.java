package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ArrayLogger;

public class ArrayLoggerTest extends BaseLoggerTest {
    
    /**
     * When passed an array containing uncloneable values, the
     * {@link ArrayLogger#cloneValue(Object[])} method should return a shallow
     * copy.
     */
    @Test
    public void testCloneValueUncloneable() {
        TestClass[] archetype = {new TestClass(0), new TestClass(1)};
        
        Object[] clone = new ArrayLogger().cloneValue(archetype);
        
        assertTrue(Arrays.equals(archetype, clone));
        
        archetype[0] = new TestClass(42);
        assertFalse(Arrays.equals(archetype, clone));
    }
    
    /**
     * When passed an array containing only cloneable values, the
     * {@link ArrayLogger#cloneValue(Object[])} method should return a deep
     * copy.
     */
    @Test
    public void testCloneValueCloneable() {
        TestClass[] archetype = {new TestClassCloneable(0), new TestClassCloneable(1)};
        
        Object[] clone = new ArrayLogger().cloneValue(archetype);
        
        assertTrue(Arrays.equals(archetype, clone));
        
        archetype[0].attribute = 42;
        assertFalse(Arrays.equals(archetype, clone));
    }
}

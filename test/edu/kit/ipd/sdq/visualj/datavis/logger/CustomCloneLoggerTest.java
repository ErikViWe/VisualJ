package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.CustomCloneLogger;

public class CustomCloneLoggerTest extends BaseLoggerTest {
    
    /**
     * The cloning lambda method must be called correctly.
     */
    @Test
    public void testCloneValue() {
        TestClass archetype = new TestClass(42);
        TestClass clone = new CustomCloneLogger<>(archetype, obj -> new TestClass(obj.attribute)).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.attribute = 21;
        assertNotEquals(archetype, clone);
    }
    
    /**
     * Exceptions thrown by the lambda method should not be caught by the
     * logger.
     */
    @Test(expected = AssertionError.class)
    public void testCloneValueExcsption() {
        TestClass archetype = new TestClass(42);
        TestClass clone = new CustomCloneLogger<>(archetype, obj -> {
            throw new AssertionError();
        }).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.attribute = 21;
        assertNotEquals(archetype, clone);
    }
}

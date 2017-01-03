package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.DeepCloneable;
import edu.kit.ipd.sdq.visualj.datavis.logger.DeepCloneableLogger;

public class DeepCloneableLoggerTest extends BaseLoggerTest {
    
    /**
     * The clone method must be called correctly.
     */
    @Test
    public void testCloneValue() {
        TestClassCloneable archetype = new TestClassCloneable(42);
        DeepCloneable<TestClassCloneable> clone = new DeepCloneableLogger<>(archetype).cloneValue(archetype);
        
        assertEquals(archetype, clone);
        
        archetype.attribute = 21;
        assertNotEquals(archetype, clone);
    }
}

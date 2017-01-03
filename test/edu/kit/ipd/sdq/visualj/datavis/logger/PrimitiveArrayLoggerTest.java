package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;

public class PrimitiveArrayLoggerTest extends BaseLoggerTest {
    
    /**
     * The clone method must return a copy of the array.
     */
    @Test
    public void testCloneValue() {
        Integer[] archetype = {0, 1};
        Object[] clone = new IntArrayLogger(ArrayUtils.toPrimitive(archetype)).cloneValue(archetype);
        
        assertTrue(Arrays.equals(archetype, clone));
        
        archetype[0] = 42;
        assertFalse(Arrays.equals(archetype, clone));
    }
}

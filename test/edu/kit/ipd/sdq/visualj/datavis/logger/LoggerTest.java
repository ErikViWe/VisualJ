package edu.kit.ipd.sdq.visualj.datavis.logger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LoggerTest extends BaseLoggerTest {
    
    /**
     * Tests if loggers are initialized correctly.
     * 
     * <p>
     * In particular, loggers initialized after one or multiple breakpoints must
     * fill up their history with {@code null}.
     * </p>
     */
    @Test
    public void testInitialization() {
        ImmutableLogger<Integer> log1 = new ImmutableLogger<>(42);
        Breakpoints.breakpoint();
        log1.update(21);
        Breakpoints.breakpoint();
        
        assertEquals(Integer.valueOf(42), log1.getValue(0));
        assertEquals(Integer.valueOf(21), log1.getValue(1));
        
        ImmutableLogger<Integer> log2 = new ImmutableLogger<>(42);
        Breakpoints.breakpoint();
        log2.update(21);
        Breakpoints.breakpoint();
        
        assertEquals((Integer) null, log2.getValue(0));
        assertEquals((Integer) null, log2.getValue(1));
        
        assertEquals(Integer.valueOf(42), log2.getValue(2));
        assertEquals(Integer.valueOf(21), log2.getValue(3));
    }
}

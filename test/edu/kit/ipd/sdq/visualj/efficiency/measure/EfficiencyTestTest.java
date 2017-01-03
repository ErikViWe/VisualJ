package edu.kit.ipd.sdq.visualj.efficiency.measure;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;

/**
 * I heard you liked tests, so I put a test in your test...
 */
public class EfficiencyTestTest extends MeasureTest {
    
    /**
     * Trivial {@link EfficiencyTest} using a two-dimensional array for the
     * arguments.
     */
    @Test
    public void testDefaultGenerator() {
        try {
            TestClassFoo obj = new TestClassFoo();
            
            EfficiencyTest test
                    = new EfficiencyTest(obj, TestClassFoo.reflectNonStaticOneArg(), new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Trivial {@link EfficiencyTest} using a custom
     * {@link EfficiencyTest.ArgumentTupleGenerator} for the arguments.
     */
    @Test
    public void testCustomGenerator() {
        try {
            TestClassFoo obj = new TestClassFoo();
            
            EfficiencyTest test = new EfficiencyTest(obj, TestClassFoo.reflectNonStaticOneArg(),
                    new EfficiencyTest.ArgumentTupleGenerator() {
                        
                        int count = 0;
                        
                        @Override
                        public Object[] yield() {
                            if (count >= 3) {
                                return null;
                            }
                            
                            return new Integer[]{count++};
                        }
                    }, 5);
            
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * A {@link NullPointerException} should be thrown if {@code null} is passed
     * as a method.
     */
    @Test(expected = NullPointerException.class)
    public void testNullMethod() {
        try {
            TestClassFoo obj = new TestClassFoo();
            
            EfficiencyTest test = new EfficiencyTest(obj, null, new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * An {@link NullPointerException} should be thrown if the array of
     * specified argument tuples is {@code null}.
     * 
     * <p>
     * To ensure consistency with {@link TestRun}, the argument tuples
     * themselves may be {@code null} if the method takes no arguments.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void testIllegalNullArgs() {
        try {
            TestClassFoo obj = new TestClassFoo();
            
            EfficiencyTest test = new EfficiencyTest(obj, TestClassFoo.reflectNonStaticOneArg(), (Object[][]) null, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * A {@link NullPointerException} should be thrown if {@code null} is passed
     * as a custom generator.
     */
    @Test(expected = NullPointerException.class)
    public void testNullCustomGenerator() {
        try {
            TestClassFoo obj = new TestClassFoo();
            
            EfficiencyTest test = new EfficiencyTest(obj, TestClassFoo.reflectNonStaticNoArgs(),
                    (EfficiencyTest.ArgumentTupleGenerator) null, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * An {@link IllegalArgumentException} should be thrown if the method cannot
     * be called with the specified arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() {
        try {
            TestClassFoo obj = new TestClassFoo();
            
            EfficiencyTest test
                    = new EfficiencyTest(obj, TestClassFoo.reflectNonStaticNoArgs(), new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If {@code null} is passed as the object on which to invoke a non-static
     * method, the class's default constructor should be used to construct an
     * object.
     */
    @Test
    public void testDefaultConstruction() {
        try {
            EfficiencyTest test = new EfficiencyTest(null, TestClassFoo.reflectNonStaticOneArg(),
                    new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
            
            assertTrue("" + MeasureTest.TestClassFoo.getInstanceCounter() + " new objects were constructed",
                    MeasureTest.TestClassFoo.getInstanceCounter() == 3 * 5);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If {@code null} is passed as the object on which to invoke a static
     * method, and the class has no default constructor, no exception should be
     * thrown.
     */
    @Test
    public void testNoDefaultConstructorStatic() {
        try {
            EfficiencyTest test
                    = new EfficiencyTest(null, TestClassBar.reflectStatic(), new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If {@code null} is passed as the object on which to invoke a non-static
     * method, and the class has no default constructor, a
     * {@link NoDefaultConstructorException} should be thrown.
     */
    @Test(expected = NoDefaultConstructorException.class)
    public void testNoDefaultConstructorNonStatic() {
        try {
            EfficiencyTest test = new EfficiencyTest(null, TestClassBar.reflectNonStaticOneArg(),
                    new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If the method to test is not visible to {@link EfficiencyTest}, an
     * {@link IllegalAccessException} should be thrown.
     * 
     * @throws IllegalAccessException
     */
    @Test(expected = IllegalAccessException.class)
    public void testIllegalAccess() throws IllegalAccessException {
        try {
            EfficiencyTest test = new EfficiencyTest(new TestClassFoo(), TestClassFoo.reflectNonStaticPrivate(),
                    new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If the method to test throws an exception, an
     * {@link InvocationTargetException} should be thrown.
     * 
     * @throws InvocationTargetException
     */
    @Test(expected = InvocationTargetException.class)
    public void testInvocationTargetMethod() throws InvocationTargetException {
        try {
            EfficiencyTest test = new EfficiencyTest(new TestClassThrows(false),
                    TestClassThrows.reflectNonStaticThrows(), new Integer[][]{{}}, 5);
            test.run();
        } catch (NoDefaultConstructorException | IllegalAccessException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If the class's default constructor throws an exception, an
     * {@link InvocationTargetException} should be thrown.
     * 
     * @throws InvocationTargetException
     */
    @Test(expected = InvocationTargetException.class)
    public void testInvocationTargetConstructor() throws InvocationTargetException {
        try {
            EfficiencyTest test = new EfficiencyTest(null, TestClassThrows.reflectNonStaticThrows(),
                    new Integer[][]{{0}, {1}, {2}}, 5);
            TestRun.Result[] results = test.run();
            
            assertEquals(results.length, 3);
            for (int i = 0; i < 3; ++i) {
                assertArrayEquals(results[i].getArgs(), new Integer[]{i});
                assertEquals(results[i].getAccuracy(), 5);
            }
        } catch (NoDefaultConstructorException | IllegalAccessException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
}

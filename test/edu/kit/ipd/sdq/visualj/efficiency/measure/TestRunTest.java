package edu.kit.ipd.sdq.visualj.efficiency.measure;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;

public class TestRunTest extends MeasureTest {
    
    /**
     * Trivial {@link TestRun} for a static method.
     */
    @Test
    public void testStaticMethod() {
        try {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectStatic(), new Integer[]{42}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Trivial {@link TestRun} for a non-static method.
     */
    @Test
    public void testNonStaticMethod() {
        try {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectNonStaticOneArg(), new Integer[]{42}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
            
            assertTrue("The object was not cloned properly", !obj.isChanged());
            assertTrue("" + (MeasureTest.TestClassFoo.getInstanceCounter() - 1) + " new objects were constructed",
                    MeasureTest.TestClassFoo.getInstanceCounter() == 1);
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
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, null, new Integer[]{42, 21}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * An {@link IllegalArgumentException} should be thrown if the method takes
     * at least one argument, but the array of specified arguments is
     * {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNullArgs() {
        try {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectNonStaticOneArg(), null, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If the method takes no arguments, and the array of specified arguments is
     * {@code null}, the test run should complete successfully.
     */
    @Test
    public void testNullArgs() {
        try {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectNonStaticNoArgs(), null, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
            
            assertTrue("The object was not cloned properly", !obj.isChanged());
            assertTrue("" + (MeasureTest.TestClassFoo.getInstanceCounter() - 1) + " new objects were constructed",
                    MeasureTest.TestClassFoo.getInstanceCounter() == 1);
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
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectNonStaticOneArg(), new Integer[]{42, 21}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If {@code null} is passed as the object on which to invoke the non-static
     * method, the class's default constructor should be used to construct an
     * object.
     */
    @Test
    public void testDefaultConstruction() {
        try {
            TestRun tr = new TestRun(null, MeasureTest.TestClassFoo.reflectNonStaticOneArg(), new Integer[]{42}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
            
            assertTrue("" + MeasureTest.TestClassFoo.getInstanceCounter() + " new objects were constructed",
                    MeasureTest.TestClassFoo.getInstanceCounter() == 5);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If {@code null} is passed as the object on which to invoke the method,
     * the class has no default constructor, and the method to test is static,
     * no exception should be thrown.
     */
    @Test
    public void testNoDefaultConstructorStatic() {
        try {
            TestRun tr = new TestRun(null, MeasureTest.TestClassBar.reflectStatic(), new Integer[]{42}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If {@code null} is passed as the object on which to invoke the method,
     * the class has no default constructor, and the method to test is not
     * static, a {@link NoDefaultConstructorException} should be thrown.
     */
    @Test(expected = NoDefaultConstructorException.class)
    public void testNoDefaultConstructorNonStatic() {
        try {
            TestRun tr = new TestRun(null, MeasureTest.TestClassBar.reflectNonStaticOneArg(), new Integer[]{42}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If the method to test is not visible to {@link TestRun}, an
     * {@link IllegalAccessException} should be thrown.
     * 
     * @throws IllegalAccessException
     */
    @Test(expected = IllegalAccessException.class)
    public void testIllegalAccess() throws IllegalAccessException {
        try {
            TestRun tr = new TestRun(null, MeasureTest.TestClassFoo.reflectNonStaticPrivate(), new Integer[]{}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
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
            TestClassThrows obj = new TestClassThrows(false);
            TestRun tr = new TestRun(obj, MeasureTest.TestClassThrows.reflectNonStaticThrows(), new Integer[]{}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
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
            TestRun tr = new TestRun(null, MeasureTest.TestClassThrows.reflectNonStaticThrows(), new Integer[]{}, 5);
            TestRun.Result res = tr.run();
            System.out.println(res);
        } catch (NoDefaultConstructorException | IllegalAccessException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }
}

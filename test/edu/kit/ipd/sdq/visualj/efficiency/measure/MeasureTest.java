package edu.kit.ipd.sdq.visualj.efficiency.measure;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.StepCounter;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;

public abstract class MeasureTest {
    
    @Before
    public void setUp() {
        TestClassFoo.instanceCounter = 0;
    }
    
    @After
    public void cleanUp() {
    }
    
    /**
     * Used to test valid {@link TestRun}s and {@link EfficiencyTest}s. This
     * class has a public default constructor and clone method, and it is
     * serializable.
     */
    public static class TestClassFoo implements Cloneable, Serializable {
        
        private static final long serialVersionUID = 6531533612111436230L;
        
        private static int instanceCounter = 0;
        
        public static int getInstanceCounter() {
            return instanceCounter;
        }
        
        private boolean changed = false;
        
        public TestClassFoo() {
            ++instanceCounter;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestClassFoo) {
                return ((TestClassFoo) obj).changed == changed;
            }
            
            return false;
        }
        
        @Override
        public int hashCode() {
            return changed ? -1 : 0;
        }
        
        @Override
        public String toString() {
            return "TestClassFoo(changed == " + changed + ")";
        }
        
        @Override
        public TestClassFoo clone() {
            // An actual clone would be more expensive than this,
            // so we just simulate it.
            --instanceCounter;
            TestClassFoo clone = new TestClassFoo();
            clone.changed = changed;
            return clone;
        }
        
        public boolean isChanged() {
            return changed;
        }
        
        public static Method reflectStatic() {
            try {
                return TestClassFoo.class.getDeclaredMethod("staticFoo", int.class);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public static Method reflectNonStaticOneArg() {
            try {
                return TestClassFoo.class.getDeclaredMethod("nonStaticFoo", int.class);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public static Method reflectNonStaticNoArgs() {
            try {
                return TestClassFoo.class.getDeclaredMethod("nonStaticFoo");
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public static Method reflectNonStaticPrivate() {
            try {
                return TestClassFoo.class.getDeclaredMethod("nonStaticPrivate");
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public static void staticFoo(int a) {
            StepCounter.step();
            StepCounter.step();
            StepCounter.step();
            
            System.out.println("static foo: " + a);
        }
        
        public void nonStaticFoo(int a) {
            System.out.println("non-static foo: " + a);
            System.out.println("\tchanged == " + changed);
            changed = true;
        }
        
        public void nonStaticFoo() {
            System.out.println("non-static foo: no arg");
            System.out.println("\tchanged == " + changed);
            changed = true;
        }
        
        @SuppressWarnings("unused")
        private void nonStaticPrivate() {
        }
    }
    
    /**
     * Used to test valid {@link TestRun}s and {@link EfficiencyTest}s. This
     * class does not have a public default constructor and clone method, and it
     * is not serializable.
     */
    public static class TestClassBar {
        
        private TestClassBar() {
        }
        
        public static Method reflectStatic() {
            try {
                return TestClassBar.class.getDeclaredMethod("staticBar", int.class);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public static Method reflectNonStaticOneArg() {
            try {
                return TestClassBar.class.getDeclaredMethod("nonStaticBar", int.class);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public static void staticBar(int a) {
            System.out.println("static bar: " + a);
        }
        
        public void nonStaticBar(int a) {
            System.out.println("non-static bar: " + a);
        }
    }
    
    /**
     * Used to test {@link TestRun}s and {@link EfficiencyTest}s when their
     * invocation targets throw exceptions or errors.
     */
    public static class TestClassThrows {
        
        public TestClassThrows() {
            this(true);
        }
        
        public TestClassThrows(boolean throwError) {
            if (throwError) {
                throw new AssertionError();
            }
        }
        
        public static Method reflectNonStaticThrows() {
            try {
                return TestClassThrows.class.getDeclaredMethod("nonStaticThrows");
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        public void nonStaticThrows() {
            throw new AssertionError();
        }
    }
}

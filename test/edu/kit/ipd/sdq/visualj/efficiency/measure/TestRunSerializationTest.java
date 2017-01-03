package edu.kit.ipd.sdq.visualj.efficiency.measure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;

public class TestRunSerializationTest extends SerializationTest {
    
    /**
     * Serialize a {@link TestRun} for running a non-static method on a
     * specified object.
     */
    @Test
    public void testSerializationNonStaticMethod() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectNonStaticOneArg(), new Integer[]{42}, 5);
            
            os.writeObject(tr);
            TestRun tr2 = (TestRun) is.readObject();
            
            System.out.println("\r\nBefore serialization:\r\n" + tr + "\r\nAfter:\r\n" + tr2);
            assertEquals(tr, tr2);
        } catch (IOException | NoDefaultConstructorException | IllegalArgumentException | ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Serialize a {@link TestRun} for running a static method.
     */
    @Test
    public void testSerializationStaticMethod() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            TestRun tr = new TestRun(null, MeasureTest.TestClassFoo.reflectStatic(), new Integer[]{42}, 5);
            
            os.writeObject(tr);
            TestRun tr2 = (TestRun) is.readObject();
            
            System.out.println("\r\nBefore serialization:\r\n" + tr + "\r\nAfter:\r\n" + tr2);
            assertEquals(tr, tr2);
        } catch (IOException | NoDefaultConstructorException | IllegalArgumentException | ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * If the arguments passed to the method are not serializable, a
     * {@link NotSerializableException} should be thrown.
     */
    @Test
    public void testNotSerializableArgs() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            TestRun tr = new TestRun(null, SerializationTest.class.getDeclaredMethod("method", Object.class),
                    new Object[]{new Object()}, 5);
            
            os.writeObject(tr);
            
            fail("No exception thrown");
        } catch (NotSerializableException e) {
            // Test successful.
        } catch (IOException | NoDefaultConstructorException | IllegalArgumentException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * If the object on which to run the method is not serializable, a
     * {@link NotSerializableException} should be thrown.
     */
    @Test
    public void testNotSerializableObject() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            TestRun tr = new TestRun(new Object(), Object.class.getDeclaredMethod("toString"), new Object[]{}, 5);
            
            os.writeObject(tr);
            
            fail("No exception thrown");
        } catch (NotSerializableException e) {
            // Test successful.
        } catch (IOException | NoDefaultConstructorException | IllegalArgumentException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}

package edu.kit.ipd.sdq.visualj.efficiency.measure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun.Result;

public class TestRunResultSerializationTest extends SerializationTest {
    
    /**
     * Serialize a {@link TestRun.Result} for a non-static method on a specified
     * object.
     */
    @Test
    public void testSerializationNonStaticMethod() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            TestRun tr = new TestRun(obj, MeasureTest.TestClassFoo.reflectNonStaticOneArg(), new Integer[]{42}, 5);
            TestRun.Result result = tr.run();
            
            os.writeObject(result);
            TestRun.Result result2 = (Result) is.readObject();
            
            System.out.println("\r\nBefore serialization:\r\n" + result + "\r\nAfter:\r\n" + result2);
            assertEquals(result, result2);
        } catch (IOException | NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Serialize a {@link TestRun.Result} for a static method.
     */
    @Test
    public void testSerializationStaticMethod() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            TestRun tr = new TestRun(null, MeasureTest.TestClassFoo.reflectStatic(), new Integer[]{42}, 5);
            TestRun.Result result = tr.run();
            
            os.writeObject(result);
            TestRun.Result result2 = (Result) is.readObject();
            
            System.out.println("\r\nBefore serialization:\r\n" + result + "\r\nAfter:\r\n" + result2);
            assertEquals(result, result2);
        } catch (IOException | NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * If the arguments passed to the method are not serializable, a
     * {@link NotSerializableException} should be thrown.
     */
    @Test
    public void testNotSerializable() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            TestRun tr = new TestRun(null, SerializationTest.class.getDeclaredMethod("method", Object.class),
                    new Object[]{new Object()}, 5);
            TestRun.Result result = tr.run();
            
            os.writeObject(result);
            
            fail("No exception thrown");
        } catch (NotSerializableException e) {
            // Test successful.
        } catch (IOException | NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}

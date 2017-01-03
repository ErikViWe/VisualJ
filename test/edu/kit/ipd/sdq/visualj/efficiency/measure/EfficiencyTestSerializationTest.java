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

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;

public class EfficiencyTestSerializationTest extends SerializationTest {
    
    /**
     * Serialize an {@link EfficiencyTest} for running a non-static method on a
     * specified object.
     */
    @Test
    public void testSerializationNonStaticMethod() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            MeasureTest.TestClassFoo obj = new MeasureTest.TestClassFoo();
            EfficiencyTest et = new EfficiencyTest(obj, MeasureTest.TestClassFoo.reflectNonStaticOneArg(),
                    new Integer[][]{{42}}, 5);
            
            os.writeObject(et);
            EfficiencyTest et2 = (EfficiencyTest) is.readObject();
            
            System.out.println("\r\nBefore serialization:\r\n" + et + "\r\nAfter:\r\n" + et2);
            assertEquals(et, et2);
        } catch (IOException | NoDefaultConstructorException | IllegalArgumentException | ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Serialize an {@link EfficiencyTest} for running a static method.
     */
    @Test
    public void testSerializationStaticMethod() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(testFile.toFile()));
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
            EfficiencyTest et
                    = new EfficiencyTest(null, MeasureTest.TestClassFoo.reflectStatic(), new Integer[][]{{42}}, 5);
            
            os.writeObject(et);
            EfficiencyTest et2 = (EfficiencyTest) is.readObject();
            
            System.out.println("\r\nBefore serialization:\r\n" + et + "\r\nAfter:\r\n" + et2);
            assertEquals(et, et2);
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
            EfficiencyTest et
                    = new EfficiencyTest(null, SerializationTest.class.getDeclaredMethod("method", Object.class),
                            new Object[][]{{new Object()}}, 5);
            
            os.writeObject(et);
            
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
            EfficiencyTest et = new EfficiencyTest(new Object(), Object.class.getDeclaredMethod("toString"),
                    new Object[][]{{}}, 5);
            
            os.writeObject(et);
            
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

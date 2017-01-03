package edu.kit.ipd.sdq.visualj.efficiency.measure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;

public abstract class SerializationTest extends MeasureTest {
    
    public static final Path testFile = Paths.get("testData", "testResult.vjr");
    
    @Before
    @Override
    public void setUp() {
        super.setUp();
        
        try {
            Files.createDirectories(testFile.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            Files.createFile(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @After
    @Override
    public void cleanUp() {
        super.cleanUp();
        
        try {
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test method. This method's argument is not serializable.
     * 
     * @param arg
     */
    public static void method(Object arg) {
    }
}

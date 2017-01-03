package edu.kit.ipd.sdq.visualj.efficiency.view;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.measure.SerializationTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;
import edu.kit.ipd.sdq.visualj.efficiency.measure.MeasureTest.TestClassFoo;
import edu.kit.ipd.sdq.visualj.efficiency.view.EfficiencyResultWindow;

public class EfficiencyResultWindowTest {
    
    /**
     * Opens an {@link EfficiencyResultWindow} to be tested interactively.
     */
    @Test
    public void showWindow() {
        try (ObjectOutputStream stream
                = new ObjectOutputStream(new FileOutputStream(SerializationTest.testFile.toFile()));) {
            TestClassFoo obj = new TestClassFoo();
            
            List<TestRun.Result[]> resultsList = new LinkedList<>();
            resultsList.add(
                    new EfficiencyTest(obj, TestClassFoo.reflectNonStaticOneArg(), new Integer[][]{{42}, {21}, {-1}}, 5)
                            .run());
            
            stream.writeObject(
                    new EfficiencyTest(null, TestClassFoo.reflectStatic(), new Integer[][]{{42}, {21}}, 5).run());
            
            EfficiencyResultWindow win = new EfficiencyResultWindow(resultsList);
            
            win.setVisible(true);
            
            while (win.isVisible()) {
                try {
                    wait(1000);
                } catch (InterruptedException | IllegalMonitorStateException e) {
                }
            }
            
            Files.delete(SerializationTest.testFile);
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException
                | IllegalArgumentException | IOException e) {
            fail(e.getMessage());
        }
    }
}

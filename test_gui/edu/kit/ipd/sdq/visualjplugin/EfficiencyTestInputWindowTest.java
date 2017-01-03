package edu.kit.ipd.sdq.visualjplugin;

import java.awt.Dimension;
import java.lang.reflect.Method;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.TestFrame;
import edu.kit.ipd.sdq.visualjplugin.efficiency.EfficiencyTestInputWindow;

public class EfficiencyTestInputWindowTest {
    
    Method method;
    
    @Before
    public void setUp() {
        method = TestClass.class.getDeclaredMethods()[0];
    }
    
    @Test
    public void test() {
        (new EfficiencyTestInputWindow(method)).setVisible(true);
        
        // this frame is only used for letting the tester decide to exit execution.
        TestFrame f = new TestFrame(new JPanel());
        f.setPreferredSize(new Dimension(300, 0));
        f.pack();
        f.waitForClose();
    }
    
}

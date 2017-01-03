package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.commons.lang3.mutable.MutableBoolean;

/**
 * This class is used as a simpler alternative to {@link Window} when testing visualizers.
 */
public class TestFrame extends JFrame {
    
    private static final long serialVersionUID = 218844298096900599L;
    
    private MutableBoolean continueIndicator = new MutableBoolean(false);
    
    /**
     * Creates a new {@link TestFrame} with the given panel.
     * 
     * @param panel
     *            the panel shown within the {@link TestFrame}.
     * @param title
     *            the frame's title.
     */
    public TestFrame(JComponent panel, String title) {
        super(title);
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                TestFrame.this.continueIndicator.setTrue();
                TestFrame.this.setVisible(false);
                TestFrame.this.dispose();
            }
        });
        
        this.getContentPane().add(panel);
        this.pack();
        
        this.setVisible(true);
    }
    
    /**
     * Creates a new {@link TestFrame} with the given panel.
     * 
     * @param panel
     *            the panel shown within the {@link TestFrame}
     */
    public TestFrame(JComponent panel) {
        this(panel, panel.getClass().getName());
    }
    
    /**
     * Blocks until the window is closed.
     */
    public void waitForClose() {
        VisTestUtil.waitFor(() -> this.continueIndicator.booleanValue());
    }
}

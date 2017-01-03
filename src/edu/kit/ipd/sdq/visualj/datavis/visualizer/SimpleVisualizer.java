package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import edu.kit.ipd.sdq.visualj.datavis.logger.Logger;

/**
 * A visualizer that visualizes objects by simply calling their {@code toString}
 * method.
 * 
 * If you want more fancy visualization, use one of the other subclasses.
 */
public class SimpleVisualizer<T> extends Visualizer {
    
    private static final long serialVersionUID = -3422235124901521872L;
    
    private JLabel label;
    
    private Logger<T> logger;
    
    /**
     * Creates a new {@code SimpleVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public SimpleVisualizer(Logger<T> logger, String title) {
        this.logger = logger;
        setTitle(title);
        
        setLayout(new BorderLayout());
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
    
    /**
     * Creates a new {@code SimpleVisualizer} without a title.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     */
    public SimpleVisualizer(Logger<T> logger) {
        this(logger, null);
    }
    
    @Override
    protected void update(int breakpointId) {
        final T value = logger.getValue(breakpointId);
        label.setText(Objects.toString(value));
    }
}

package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import edu.kit.ipd.sdq.visualj.datavis.logger.Logger;

/**
 * Base class for all visualizers.
 * 
 * Visualizers visualize the changing states of objects in {@link Logger}s.
 * 
 * For ease of use, a visualizer with default settings may also be created via
 * {@link Logger#createVisualizer()}.
 */
public abstract class Visualizer extends JPanel {
    
    private static final long serialVersionUID = -31971556430323074L;
    
    private String title = null;
    private JComponent wrapper = null;
    
    /**
     * Returns the title of the visualizer.
     * 
     * @return the title of the visualizer, or {@code null} if the title field
     *         is hidden.
     * 
     * @see #setTitle(String)
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Sets the title of the visualizer. If the title is {@code null}, the title
     * field is hidden.
     * 
     * @param title
     *            the new title.
     * 
     * @see #getTitle()
     */
    public void setTitle(String title) {
        this.title = title;
        
        onTitleChanged();
    }
    
    /**
     * Removes all components from this visualizer and just shows the string
     * "null".
     */
    protected void showNull() {
        this.removeAll();
        setLayout(new BorderLayout());
        JLabel label = new JLabel("null");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
    
    /**
     * Returns a wrapper around this visualizer that may provide additional
     * features such as scrolling or displaying the {@link #setTitle(String)
     * title}. This wrapper is used in {@link Window} instead of the visualizer
     * itself. <br>
     * <br>
     * Note that this method returns the same wrapper every time. <br>
     * <br>
     * If you want to override the default wrapper implementation, note:<br>
     * This method only ensures that the same wrapper is returned every call. To
     * initially create the wrapper the protected {@link #createWrapper()}
     * method is used internally so you probably want to override that method
     * instead.
     * 
     * @return the wrapper
     */
    public JComponent getWrapper() {
        if (wrapper != null) {
            return wrapper;
        }
        
        wrapper = createWrapper();
        return wrapper;
    }
    
    /**
     * This method is used internally by {@link #getWrapper()} to create a
     * wrapper around the visualizer. By default the visualizer is wrapped
     * within a {@link JScrollPane} that is wrapped within a {@link TitlePane}.
     * 
     * @return the created wrapper
     * 
     * @see #onTitleChanged()
     */
    protected JComponent createWrapper() {
        return new TitlePane(new JScrollPane(this), this.getTitle());
    }
    
    /**
     * This method is called if the title is set via {@link #setTitle(String)}.
     * By default this method is used to update the title in the wrapping
     * {@link TitlePane} accordingly but this behavior can be overridden if
     * needed.
     */
    protected void onTitleChanged() {
        if (wrapper != null && wrapper instanceof TitlePane) {
            ((TitlePane) wrapper).setTitle(this.getTitle());
        }
    }
    
    /**
     * Update the visualization to the state associated with the given
     * breakpoint id.
     * 
     * @param breakpointId
     *            the breakpoint id.
     */
    protected abstract void update(int breakpointId);
}

enum StateOfCell {
    
    NOTCHANGED("NOTCHANGED"), CHANGED("CHANGED"), NEW("NEW");
    
    private final String name;
    
    private StateOfCell(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

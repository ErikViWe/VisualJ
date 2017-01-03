package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * A simple wrapper for any {@link JComponent} that displays a title above it.
 */
public class TitlePane extends JPanel {
    
    private static final long serialVersionUID = -5962977661528530440L;
    
    private JLabel titleLabel;
    private String title;
    
    /**
     * Creates a new {@link TitlePane} wrapper with the given component and no title.
     * 
     * @param comp
     *            the component to wrap
     */
    public TitlePane(JComponent comp) {
        this(comp, null);
    }
    
    /**
     * Creates a new {@link TitlePane} wrapper with the given component and title.
     * 
     * @param comp
     *            the component to wrap
     * @param title
     *            the title
     */
    public TitlePane(JComponent comp, String title) {
        this.setLayout(new MigLayout("flowy,insets 0", "[grow,fill]", "[][grow,fill]"));
        
        titleLabel = new JLabel();
        this.add(titleLabel);
        this.add(comp);
        
        setTitle(title);
    }
    
    /**
     * Returns the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title. If the given title is {@code null}, the title is hidden.
     * 
     * @param title
     *            the title
     */
    public void setTitle(String title) {
        this.title = title;
        titleLabel.setText(title);
        titleLabel.setVisible(title != null);
    }
}

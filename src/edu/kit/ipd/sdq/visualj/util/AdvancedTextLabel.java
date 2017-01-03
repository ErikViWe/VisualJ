package edu.kit.ipd.sdq.visualj.util;

import javax.swing.JTextArea;

/**
 * A text label that is capable of displaying multi-line text with automatic line wrapping using a {@link JTextArea}
 * internally.
 */
public class AdvancedTextLabel extends JTextArea {
    
    private static final long serialVersionUID = -5905769252837728094L;
    
    /**
     * Creates a new text label with the given text.
     * 
     * @param text
     *            the text to display
     */
    public AdvancedTextLabel(String text) {
        super(text);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
        this.setOpaque(false);
        this.setHighlighter(null);
    }
}

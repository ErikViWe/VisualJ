package edu.kit.ipd.sdq.visualj.util;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * A text field with a placeholder text. The placeholder text is shown when the text field does not have focus and is
 * empty.
 */
public class PlaceholderTextField extends JTextField {
    
    private static final long serialVersionUID = -176537386541743017L;
    
    private String placeholder;
    
    /**
     * Creates a new empty text field.
     */
    public PlaceholderTextField() {
        init();
    }
    
    /**
     * Creates a new text field initialized with the given text.
     * 
     * @param text
     *            the initial text
     */
    public PlaceholderTextField(String text) {
        super(text);
        init();
    }
    
    /**
     * Init placeholder functionality by binding focus listeners.
     */
    private void init() {
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (PlaceholderTextField.this.getText().equals("")) {
                    PlaceholderTextField.this.setText(PlaceholderTextField.this.placeholder);
                }
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                if (PlaceholderTextField.this.getText().equals(PlaceholderTextField.this.placeholder)) {
                    PlaceholderTextField.this.setText("");
                }
            }
        });
    }
    
    /**
     * Returns the placeholder text.
     * 
     * @return the placeholder text
     */
    public String getPlaceholder() {
        return placeholder;
    }
    
    /**
     * Sets the placeholder text.
     * 
     * @param placeholder
     *            the placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        
        if (this.getText().equals("")) {
            this.setText(this.placeholder);
        }
    }
}

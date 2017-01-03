package edu.kit.ipd.sdq.visualj.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

/**
 * This utility class contains static methods for creating dialogs of various types:
 * 
 * <ul>
 * <li>{@link #TYPE_INFORMATION}</li>
 * <li>{@link #TYPE_WARNING}</li>
 * <li>{@link #TYPE_ERROR}</li>
 * <li>{@link #TYPE_QUESTION}</li>
 * <li>{@link #TYPE_PLAIN}</li>
 * </ul>
 */
public final class DialogUtils {
    
    /**
     * See {@link JOptionPane#INFORMATION_MESSAGE}.
     */
    public static final int TYPE_INFORMATION = JOptionPane.INFORMATION_MESSAGE;
    
    /**
     * See {@link JOptionPane#WARNING_MESSAGE}.
     */
    public static final int TYPE_WARNING = JOptionPane.WARNING_MESSAGE;
    
    /**
     * See {@link JOptionPane#ERROR_MESSAGE}.
     */
    public static final int TYPE_ERROR = JOptionPane.ERROR_MESSAGE;
    
    /**
     * See {@link JOptionPane#QUESTION_MESSAGE}.
     */
    public static final int TYPE_QUESTION = JOptionPane.QUESTION_MESSAGE;
    
    /**
     * See {@link JOptionPane#PLAIN_MESSAGE}.
     */
    public static final int TYPE_PLAIN = JOptionPane.PLAIN_MESSAGE;
    
    /**
     * The default size for dialogs.
     */
    private static final Dimension DEFAULT_DIALOG_SIZE = new Dimension(300, 140);
    
    /**
     * Creates a dialog window displaying the message and stack trace of the
     * given throwable.
     * <br>
     * <br>
     * This is a convenience method for {@link #showExceptionDialog(Component, Throwable, String, String)} that sets the
     * parameter {@link title} to the throwable's class name.
     * 
     * @param parent
     *            the parent component the dialog is shown in front of or
     *            {@code null} if there is no parent (see {@link JOptionPane}
     *            for an explanation)
     * @param throwable
     *            the throwable whose message and stack trace should be
     *            displayed
     * @param infoMessage
     *            a short info text displayed above the throwable's message and
     *            stack trace or {@code null} to not show any info text
     */
    public static void showExceptionDialog(Component parent, Throwable throwable, String infoMessage) {
        showExceptionDialog(parent, throwable, throwable.getClass().getSimpleName(), infoMessage);
    }
    
    /**
     * Creates a dialog window displaying the message and stack trace of the
     * given throwable.
     * <br>
     * <br>
     * This is a convenience method for {@link #showExceptionDialog(Component, Throwable, String, String, boolean)} that
     * sets the parameter {@link error} to true.
     * 
     * @param parent
     *            the parent component the dialog is shown in front of or
     *            {@code null} if there is no parent (see {@link JOptionPane}
     *            for an explanation)
     * @param throwable
     *            the throwable whose message and stack trace should be
     *            displayed
     * @param title
     *            the title of the dialog window
     * @param infoMessage
     *            a short info text displayed above the throwable's message and
     *            stack trace or {@code null} to not show any info text
     */
    public static void showExceptionDialog(Component parent, Throwable throwable, String title, String infoMessage) {
        showExceptionDialog(parent, throwable, title, infoMessage, true);
    }
    
    /**
     * Creates a dialog window displaying the message and stack trace of the
     * given throwable.
     * 
     * @param parent
     *            the parent component the dialog is shown in front of or
     *            {@code null} if there is no parent (see {@link JOptionPane}
     *            for an explanation)
     * @param throwable
     *            the throwable whose message and stack trace should be
     *            displayed
     * @param title
     *            the title of the dialog window
     * @param infoMessage
     *            a short info text displayed above the throwable's message and
     *            stack trace or {@code null} to not show any info text
     * @param error
     *            {@code true} to show an {@link #TYPE_ERROR error} dialog, {@code false} to show a
     *            {@link #TYPE_WARNING warning} dialog
     */
    public static void showExceptionDialog(Component parent, Throwable throwable, String title, String infoMessage,
            boolean error) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(DEFAULT_DIALOG_SIZE);
        panel.setLayout(new MigLayout("flowy,insets 0", "[grow,fill]"));
        {
            if (infoMessage != null) {
                // as a JLabel can only show single line text
                JTextArea infoLabel = new AdvancedTextLabel(infoMessage);
                panel.add(infoLabel);
            }
            
            String message = throwable.getClass().getSimpleName() + ": " + throwable.getMessage() + "\n\nStack-Trace:\n"
                    + getStackTrace(throwable);
            JTextArea textArea = new JTextArea(message);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.addFocusListener(new FocusListener() {
                @Override
                public void focusLost(FocusEvent e) {
                    textArea.select(0, 0); // remove selection
                }
                
                @Override
                public void focusGained(FocusEvent e) {
                    textArea.selectAll();
                }
            });
            panel.add(new JScrollPane(textArea));
        }
        
        JOptionPane.showMessageDialog(null, panel, title, error ? TYPE_ERROR : TYPE_WARNING);
    }
    
    /**
     * Creates a information dialog window with the given title displaying the given message.
     * 
     * @param parent
     *            the parent component the dialog is shown in front of or
     *            {@code null} if there is no parent (see {@link JOptionPane}
     *            for an explanation)
     * @param title
     *            the title of the dialog window
     * @param message
     *            the message to show
     */
    public static void showInfoDialog(Component parent, String title, String message) {
        showMessageDialog(parent, title, message, TYPE_INFORMATION);
    }
    
    /**
     * Creates a dialog window of the given type with the given title displaying the given message.
     * 
     * @param parent
     *            the parent component the dialog is shown in front of or
     *            {@code null} if there is no parent (see {@link JOptionPane}
     *            for an explanation)
     * @param title
     *            the title of the dialog window
     * @param message
     *            the message to show
     * @param type
     *            the type of the message dialog (See {@link DialogUtils} for available types)
     */
    public static void showMessageDialog(Component parent, String title, String message, int type) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(DEFAULT_DIALOG_SIZE);
        panel.setLayout(new MigLayout("flowy,insets 0", "[grow,fill]"));
        {
            JTextArea infoLabel = new AdvancedTextLabel(message);
            panel.add(infoLabel);
        }
        
        JOptionPane.showMessageDialog(parent, panel, title, type);
    }
    
    private static String getStackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
    
    private DialogUtils() {
    }
}

package edu.kit.ipd.sdq.visualjplugin.efficiency;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.view.EfficiencyResultWindow;
import edu.kit.ipd.sdq.visualj.util.DialogUtils;
import net.miginfocom.swing.MigLayout;

/**
 * The window in which the user enters the test data. Displays all test cases,
 * generated by the user or by the generator. It gives the ability to set the
 * accuracy of the tests and to start the test
 */
public class EfficiencyTestInputWindow extends JFrame {
    
    private static final long serialVersionUID = -2220806676499337095L;
    
    private static final String EXPORT_FILE_EXTENSION = "vji";
    private static final String EXPORT_FILE_DESCRIPTION = "VisualJ Test Input";
    
    private JTextField tfAccuracy;
    private Method method;
    private JPanel parameterTupleContainer;
    private JScrollPane scrollPane;
    
    /**
     * Creates a new widow.
     * 
     * @param method
     *            the method to test
     */
    public EfficiencyTestInputWindow(Method method) {
        this.method = method;
        
        String methodSignature = method.toString().split("throws")[0].trim();
        this.setTitle(Messages.getString("EfficiencyTestInputWindow.title") + methodSignature);
        
        this.setPreferredSize(new Dimension(700, 600));
        this.pack();
        
        this.getContentPane().setLayout(new MigLayout("fill,flowy,gapy 10", "[grow,fill]", "[grow,fill][]"));
        {
            parameterTupleContainer = new JPanel(new MigLayout("flowy,gapy 10", "[grow,fill]"));
            
            scrollPane = new JScrollPane(parameterTupleContainer);
            scrollPane.getVerticalScrollBar().setUnitIncrement(15);
            this.add(scrollPane);
            
            JPanel buttonPane = new JPanel(new MigLayout("flowy,insets 0", "[grow,fill]"));
            {
                JPanel firstRow = new JPanel(new MigLayout("flowx,gapx 10,insets 0", "[]push[][]"));
                {
                    JButton generatorButton = new JButton(Messages.getString("EfficiencyTestInputWindow.generator"));
                    generatorButton.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            if (paramsContainsString(method)) {
                                DialogUtils.showInfoDialog(EfficiencyTestInputWindow.this, "Unsupported operation",
                                        Messages.getString("EfficiencyTestInputWindow.stringGenError"));
                                return;
                            }
                            
                            TupleGeneratorWindow generatorWindow
                                    = new TupleGeneratorWindow(method, EfficiencyTestInputWindow.this);
                            generatorWindow.setVisible(true);
                            String[][] generatedArgs = generatorWindow.getArguments();
                            
                            // if arguments is null, the user clicked cancel
                            if (generatedArgs != null) {
                                for (String[] args : generatedArgs) {
                                    ParameterTuplePane pane
                                            = new ParameterTuplePane(method, EfficiencyTestInputWindow.this);
                                    
                                    for (int i = 0; i < args.length; ++i) {
                                        pane.setTextFieldText(i, args[i]);
                                    }
                                    
                                    addParameterTuplePane(pane);
                                }
                            }
                        }
                    });
                    firstRow.add(generatorButton);
                    
                    JButton button = new JButton("+");
                    button.addActionListener((event) -> {
                        ParameterTuplePane pane = new ParameterTuplePane(method, EfficiencyTestInputWindow.this);
                        addParameterTuplePane(pane);
                    });
                    firstRow.add(button);
                    
                    JButton btnDeleteAll = new JButton(Messages.getString("EfficiencyTestInputWindow.DeleteAll"));
                    btnDeleteAll.addActionListener((event) -> {
                        removeAllParameterTuplePanes();
                    });
                    firstRow.add(btnDeleteAll);
                }
                buttonPane.add(firstRow);
                
                JPanel secondRow = new JPanel(new MigLayout("flowx,gapx 10,insets 0", "[][]10:push[][]"));
                {
                    JButton btnImport = new JButton(Messages.getString("EfficiencyTestInputWindow.Import"));
                    btnImport.addActionListener((event) -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.setFileFilter(
                                new FileNameExtensionFilter(EXPORT_FILE_DESCRIPTION, EXPORT_FILE_EXTENSION));
                        
                        int returnVal = fileChooser.showOpenDialog(EfficiencyTestInputWindow.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            
                            String[] inputArray = null;
                            
                            // read file
                            try (final BufferedReader reader
                                    = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                                inputArray = reader.lines().toArray(String[]::new);
                            } catch (IOException e) {
                                DialogUtils.showExceptionDialog(this, e,
                                        Messages.getString("EfficiencyTestInputWindow.readingFileFailed"));
                            }
                            
                            // parse the input
                            for (int i = 0; i < inputArray.length; i++) {
                                ParameterTuplePane pane = new ParameterTuplePane(method, this);
                                /*
                                 * split after every occurance of ";" with no backslash as prefix,
                                 * because those would break the splitting.
                                 */
                                String[] values = inputArray[i].split(";;");
                                for (int j = 0; j < values.length; j++) {
                                    // change back the escape done by saving.
                                    values[j] = values[j].replace("\\;", ";");
                                    pane.setTextFieldText(j, values[j]);
                                }
                                addParameterTuplePane(pane);
                            }
                        }
                    });
                    secondRow.add(btnImport);
                    
                    JButton btnExport = new JButton(Messages.getString("EfficiencyTestInputWindow.Export"));
                    btnExport.addActionListener((event) -> {
                        // Generate Array
                        String[][] testCasesArr = getValues();
                        
                        // Save File
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.setFileFilter(
                                new FileNameExtensionFilter(EXPORT_FILE_DESCRIPTION, EXPORT_FILE_EXTENSION));
                        int returnVal = fileChooser.showSaveDialog(buttonPane);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            // Check for valid file ending
                            if (!file.getAbsolutePath().endsWith(".vji")) {
                                file = new File(file.getAbsolutePath() + ".vji");
                            }
                            // add test cases
                            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));) {
                                for (int i = 0; i < testCasesArr.length; i++) {
                                    String line = "";
                                    boolean first = true;
                                    
                                    for (int j = 0; j < testCasesArr[i].length; j++) {
                                        if (first) {
                                            first = false;
                                        } else {
                                            line += ";;";
                                        }
                                        // allow escaping of semicolon, if anyone wishes to use it:
                                        testCasesArr[i][j] = testCasesArr[i][j].replace(";", "\\;");
                                        // replace one backslash with two..
                                        // testCasesArr[i][j] = testCasesArr[i][j].replace("\\", "\\\\");
                                        line += testCasesArr[i][j];
                                    }
                                    
                                    bufferedWriter.write(line);
                                    bufferedWriter.newLine();
                                }
                                bufferedWriter.close();
                            } catch (IOException e) {
                                DialogUtils.showExceptionDialog(this, e,
                                        Messages.getString("EfficiencyTestInputWindow.exportError"));
                            }
                        }
                    });
                    secondRow.add(btnExport);
                    
                    JLabel lblRepetitions = new JLabel(Messages.getString("EfficiencyTestInputWindow.repetitions"));
                    secondRow.add(lblRepetitions);
                    
                    tfAccuracy = new JTextField();
                    tfAccuracy.setText("10");
                    tfAccuracy.setColumns(10);
                    secondRow.add(tfAccuracy);
                    
                    JButton btnStart = new JButton(Messages.getString("EfficiencyTestInputWindow.start"));
                    btnStart.addActionListener((event) -> {
                        try {
                            executeTest();
                        } catch (IllegalArgumentException ex) {
                            DialogUtils.showExceptionDialog(this, ex,
                                    Messages.getString("EfficiencyTestInputWindow.invalidArgument"));
                        }
                    });
                    secondRow.add(btnStart);
                }
                buttonPane.add(secondRow);
            }
            this.add(buttonPane);
            
        }
    }
    
    private static boolean paramsContainsString(Method method) {
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i].getName().equals(String.class.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds the given parameter pane.
     * 
     * @param pane
     *            the parameter pane to add
     */
    void addParameterTuplePane(ParameterTuplePane pane) {
        parameterTupleContainer.add(pane);
        validateAndRepaint();
    }
    
    void removeParameterTuplePane(ParameterTuplePane pane) {
        parameterTupleContainer.remove(pane);
        validateAndRepaint();
    }
    
    void removeAllParameterTuplePanes() {
        parameterTupleContainer.removeAll();
        validateAndRepaint();
    }
    
    private void validateAndRepaint() {
        scrollPane.validate();
        scrollPane.repaint();
    }
    
    /**
     * Gets the Accuracy the user has set. The default accuracy is 10.
     * 
     * @return the accuracy for the tests.
     * 
     * @throws NumberFormatException
     *             if the user did not enter a valid number.
     */
    private int getAccuracy() throws NumberFormatException {
        return Integer.parseInt(tfAccuracy.getText());
    }
    
    /**
     * Gets an Object[][] containing the test cases in their primitive data types.
     * The Object[][] format is [test case][test case attributes]
     * 
     * @return
     *         Object[][] containing all test instances
     */
    private Object[][] getTestCases() throws IllegalArgumentException {
        int amountOfTestCases = parameterTupleContainer.getComponentCount();
        if (parameterTupleContainer.getComponentCount() == 0)
            throw new IllegalArgumentException();
        int variables = ((ParameterTuplePane) parameterTupleContainer.getComponent(0)).getAmountOfVariables();
        Object[][] testCases = new Object[amountOfTestCases][variables];
        for (int i = 0; i < amountOfTestCases; i++) {
            ParameterTuplePane pane = (ParameterTuplePane) parameterTupleContainer.getComponent(i);
            Object[] values = pane.getValue();
            for (int j = 0; j < values.length; j++) {
                testCases[i][j] = values[j];
            }
        }
        return testCases;
    }
    
    /**
     * Generates a resultWindow and executes the test run. Results will be displayed in this
     * window after successful tests.
     * 
     * @param values
     *            Object[][] containing the test cases
     * @param accuracy
     *            amount of reputations a test case is tested
     */
    private void executeTest() {
        Object[][] testCases = getTestCases();
        int accuracy = getAccuracy();
        
        EfficiencyTest efficiencyTest = new EfficiencyTest(null, method, testCases, accuracy);
        try {
            EfficiencyResultWindow resultWindow = new EfficiencyResultWindow(efficiencyTest.run());
            resultWindow.setVisible(true);
        } catch (IllegalArgumentException e) {
            DialogUtils.showInfoDialog(this, "Illegal Argument",
                    Messages.getString("EfficiencyTestInputWindow.invalidArgument"));
        } catch (NoDefaultConstructorException | InvocationTargetException | IllegalAccessException e) {
            // no custom message for these..
            DialogUtils.showExceptionDialog(this, e, "");
        }
    }
    
    /**
     * Returns a String[][] containing the test case text field content.
     * 
     * @return
     *         String[][] containing the test case text field content
     */
    private String[][] getValues() {
        int amountOfTestCases = parameterTupleContainer.getComponentCount();
        int variables = ((ParameterTuplePane) parameterTupleContainer.getComponent(0)).getAmountOfVariables();
        String[][] textFields = new String[amountOfTestCases][variables];
        for (int i = 0; i < amountOfTestCases; i++) {
            ParameterTuplePane pane = (ParameterTuplePane) parameterTupleContainer.getComponent(i);
            String[] values = pane.getTextFieldValues();
            for (int j = 0; j < values.length; j++) {
                textFields[i][j] = values[j];
            }
        }
        return textFields;
    }
}

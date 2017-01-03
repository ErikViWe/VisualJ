package edu.kit.ipd.sdq.visualjplugin.efficiency;

import java.awt.Dimension;
import java.awt.Frame;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.ArrayUtils;

import edu.kit.ipd.sdq.visualj.util.DialogUtils;
import edu.kit.ipd.sdq.visualj.util.Generator;
import net.miginfocom.swing.MigLayout;

public class TupleGeneratorWindow extends JDialog {
    
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(670, 550);
    
    private static final long serialVersionUID = -7494235366362890436L;
    private Parameter[] parameters;
    private Class<?>[] parameterTypes;
    private static final Class<?>[] primitiveTypes
            = {int.class, long.class, short.class, double.class, float.class, byte.class, char.class, boolean.class};
    private static final Class<?>[] primitiveArrayTypes = {int[].class, long[].class, short[].class, double[].class,
            float[].class, byte[].class, char[].class, boolean[].class};
    private JTextField amountTextField;
    private GeneratorPane[] generatorPanes;
    private String[][] generatedArguments;
    
    /**
     * Create the application.
     */
    public TupleGeneratorWindow(Method method, Frame owner) {
        super(owner);
        this.parameters = method.getParameters();
        this.parameterTypes = method.getParameterTypes();
        this.generatorPanes = new GeneratorPane[parameterTypes.length];
        
        initGUI(parameterTypes);
        
        this.pack();
        this.setModalityType(ModalityType.DOCUMENT_MODAL);
    }
    
    private void initGUI(Class<?>[] parameterTypes) {
        this.setTitle(Messages.getString("TupleGeneratorWindow.title"));
        this.setPreferredSize(DEFAULT_WINDOW_SIZE);
        this.getContentPane().setLayout(new MigLayout("flowy,fill", "[grow,fill]", "[grow,fill][]"));
        {
            JPanel scrollInnerPannel = new JPanel();
            scrollInnerPannel.setLayout(new MigLayout("flowy", "[grow,fill]"));
            {
                boolean first = true;
                
                for (int i = 0; i < parameterTypes.length; ++i) {
                    Parameter parameter = parameters[i];
                    Class<?> parameterType = parameterTypes[i];
                    
                    if (first) {
                        first = false;
                    } else {
                        scrollInnerPannel.add(new JSeparator(SwingConstants.HORIZONTAL));
                    }
                    
                    String title = parameterType.getSimpleName() + " " + parameter.getName() + ":";
                    
                    if (ArrayUtils.contains(primitiveTypes, parameterType)) {
                        TupleGeneratorNumberPane numberPane
                                = new TupleGeneratorNumberPane(title);
                        scrollInnerPannel.add(numberPane);
                        generatorPanes[i] = numberPane;
                    } else if (ArrayUtils.contains(primitiveArrayTypes, parameterType)) {
                        TupleGeneratorArrayPane arrayPane = new TupleGeneratorArrayPane(title);
                        scrollInnerPannel.add(arrayPane);
                        generatorPanes[i] = arrayPane;
                    } else {
                        // here should be the object parts but they can't be
                        // realized at the moment.
                    }
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(scrollInnerPannel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(15);
            this.getContentPane().add(scrollPane);
            
            JPanel footerPanel = new JPanel(new MigLayout("", "[][]rel:push[][]"));
            {
                JLabel amoutLabel = new JLabel(Messages.getString("TupleGeneratorHeaderPane.count"));
                footerPanel.add(amoutLabel);
                
                amountTextField = new JTextField();
                amountTextField.setText("10");
                footerPanel.add(amountTextField, "width 50:100:");
                
                JButton generateButton = new JButton(Messages.getString("TupleGeneratorWindow.generate"));
                generateButton.addActionListener((event) -> {
                    try {
                        generate();
                    } catch (IllegalArgumentException ex) {
                        DialogUtils.showMessageDialog(this, "Bad Input",
                                Messages.getString("TupleGeneratorWindow.badInput"), DialogUtils.TYPE_ERROR);
                        return;
                    }
                    
                    this.setVisible(false);
                    this.dispose();
                });
                footerPanel.add(generateButton);
                
                JButton abortButton = new JButton(Messages.getString("TupleGeneratorWindow.cancel"));
                abortButton.addActionListener((event) -> {
                    this.setVisible(false);
                    this.dispose();
                });
                footerPanel.add(abortButton);
            }
            this.getContentPane().add(footerPanel);
        }
    }
    
    /**
     * Returns the generated arguments as String[][]
     * 
     * @return
     *         String[][] containing the generated arguments
     */
    public String[][] getArguments() {
        return generatedArguments;
    }
    
    /**
     * Generates the test cases
     * 
     * @throws IllegalArgumentException
     */
    private void generate() throws NumberFormatException {
        int amount = Integer.parseInt(amountTextField.getText());
        
        // retrieve generators
        Generator<?>[] generators = new Generator<?>[generatorPanes.length];
        for (int i = 0; i < generatorPanes.length; ++i) {
            generators[i] = generatorPanes[i].getGenerator();
        }
        
        // init argument array
        generatedArguments = new String[amount][parameterTypes.length];
        for (int i = 0; i < amount; ++i) {
            generatedArguments[i] = new String[parameterTypes.length];
        }
        
        // generate argument tuples
        for (int i = 0; i < parameterTypes.length; ++i) {
            Generator<String> generator = generatorPanes[i].getGenerator();
            
            for (int j = 0; j < amount; ++j) {
                generatedArguments[j][i] = generator.yield();
            }
        }
    }
}

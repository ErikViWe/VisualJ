package edu.kit.ipd.sdq.visualjplugin.efficiency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import edu.kit.ipd.sdq.visualj.util.Generator;
import edu.kit.ipd.sdq.visualj.util.PlaceholderTextField;
import net.miginfocom.swing.MigLayout;

/**
 * Randomizer pane for arrays of number types (int, float, etc.), booleans, or
 * Strings to allow users to randomize those arrays.
 * 
 * See sketch.
 */
class TupleGeneratorArrayPane extends JPanel implements GeneratorPane {
    
    private static final long serialVersionUID = 626342759371972820L;
    
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton rbLengthStatic;
    private JRadioButton rbLengthIncrement;
    private PlaceholderTextField tfLengthStatic;
    private PlaceholderTextField tfLengthIncMin;
    private PlaceholderTextField tfLengthIncStep;
    private TupleGeneratorNumberPane contentGeneratorPane;
    
    TupleGeneratorArrayPane() {
        this(null);
    }
    
    /**
     * Generates the GUI components
     * 
     * @param title
     *            title of the GUI
     */
    TupleGeneratorArrayPane(String title) {
        this.setLayout(new MigLayout("flowx,insets 0", "", "[align top]"));
        {
            if (title != null) {
                JLabel titleLabel = new JLabel(title);
                this.add(titleLabel, "wrap");
            }
            
            JPanel lengthPane = new JPanel(new MigLayout("flowy,insets 0"));
            {
                JLabel label = new JLabel(Messages.getString("TupleGeneratorArrayPane.length"));
                lengthPane.add(label);
                
                JPanel lengthConfigPane = new JPanel(new MigLayout("flowx,insets 0", "[][80!,fill][80!,fill]"));
                {
                    // STATIC
                    rbLengthStatic = new JRadioButton(Messages.getString("TupleGeneratorArrayPane.static"));
                    rbLengthStatic.setSelected(true);
                    buttonGroup.add(rbLengthStatic);
                    rbLengthStatic.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tfLengthIncMin.setVisible(false);
                            tfLengthIncStep.setVisible(false);
                            tfLengthStatic.setVisible(true);
                        }
                    });
                    lengthConfigPane.add(rbLengthStatic);
                    
                    tfLengthStatic = new PlaceholderTextField();
                    tfLengthStatic.setPlaceholder("length");
                    lengthConfigPane.add(tfLengthStatic, "wrap");
                    
                    // INCREMENT
                    rbLengthIncrement = new JRadioButton(Messages.getString("TupleGeneratorArrayPane.increment"));
                    buttonGroup.add(rbLengthIncrement);
                    rbLengthIncrement.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tfLengthIncMin.setVisible(true);
                            tfLengthIncStep.setVisible(true);
                            tfLengthStatic.setVisible(false);
                        }
                    });
                    lengthConfigPane.add(rbLengthIncrement);
                    
                    tfLengthIncMin = new PlaceholderTextField();
                    tfLengthIncMin.setPlaceholder("min");
                    tfLengthIncMin.setVisible(false);
                    lengthConfigPane.add(tfLengthIncMin);
                    
                    tfLengthIncStep = new PlaceholderTextField();
                    tfLengthIncStep.setPlaceholder("step");
                    tfLengthIncStep.setVisible(false);
                    lengthConfigPane.add(tfLengthIncStep);
                }
                lengthPane.add(lengthConfigPane);
            }
            this.add(lengthPane);
            
            this.add(new JSeparator(SwingConstants.VERTICAL), "pushy,growy");
            
            JPanel contentPane = new JPanel(new MigLayout("flowy,insets 0"));
            {
                JLabel label = new JLabel(Messages.getString("TupleGeneratorArrayPane.contents"));
                contentPane.add(label);
                
                contentGeneratorPane = new TupleGeneratorNumberPane();
                contentPane.add(contentGeneratorPane);
            }
            this.add(contentPane);
        }
    }
    
    @Override
    public Generator<String> getGenerator() throws NumberFormatException {
        Generator<String> generator;
        
        if (rbLengthStatic.isSelected()) {
            int length = Integer.parseInt(tfLengthStatic.getText());
            
            generator = () -> {
                Generator<String> contentGenerator = this.contentGeneratorPane.getGenerator();
                
                return TupleGeneratorArrayPane.makeArrayString(contentGenerator, length);
            };
        } else {
            int min = Integer.parseInt(tfLengthIncMin.getText());
            int step = Integer.parseInt(tfLengthIncStep.getText());
            
            generator = new Generator<String>() {
                private int currLength = min;
                
                @Override
                public String yield() {
                    Generator<String> contentGenerator
                            = TupleGeneratorArrayPane.this.contentGeneratorPane.getGenerator();
                    
                    String arrString = TupleGeneratorArrayPane.makeArrayString(contentGenerator, currLength);
                    
                    currLength += step;
                    
                    return arrString;
                }
            };
        }
        
        return generator;
    }
    
    /**
     * Generates a String in the valid array format to be placed in the specific text field.
     * 
     * @param contentGenerator
     *            array content
     * @param length
     *            array length
     * @return
     *         valid String for array text field
     */
    private static String makeArrayString(Generator<String> contentGenerator, int length) {
        String arrString = "[";
        
        boolean first = true;
        for (int i = 0; i < length; ++i) {
            if (first) {
                first = false;
            } else {
                arrString += ", ";
            }
            
            arrString += contentGenerator.yield();
        }
        
        return arrString + "]";
    }
}

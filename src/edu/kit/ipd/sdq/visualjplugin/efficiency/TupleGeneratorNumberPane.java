package edu.kit.ipd.sdq.visualjplugin.efficiency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.kit.ipd.sdq.visualj.util.Generator;
import edu.kit.ipd.sdq.visualj.util.Misc;
import edu.kit.ipd.sdq.visualj.util.PlaceholderTextField;
import net.miginfocom.swing.MigLayout;

/**
 * Randomizer pane for parameters of number types (int, float, etc.) to allow
 * users to randomize those numbers.
 * 
 * See sketch.
 */
class TupleGeneratorNumberPane extends JPanel implements GeneratorPane {
    
    private static final long serialVersionUID = 8909904757906211567L;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton rbStatic;
    private JRadioButton rbIncrement;
    private JRadioButton rbRandom;
    private PlaceholderTextField tfStatic;
    private PlaceholderTextField tfIncMin;
    private PlaceholderTextField tfIncStep;
    private PlaceholderTextField tfRandMin;
    private PlaceholderTextField tfRandMax;
    
    TupleGeneratorNumberPane() {
        this(null);
    }
    
    /**
     * Generates the GUI components
     * 
     * @param title
     *            title of GUI
     */
    TupleGeneratorNumberPane(String title) {
        this.setLayout(new MigLayout("flowx,insets 0,gapx 10", "[][80!,fill][80!,fill]"));
        {
            if (title != null) {
                JLabel titleLabel = new JLabel(title);
                this.add(titleLabel, "wrap");
            }
            
            // STATIC
            rbStatic = new JRadioButton(Messages.getString("TupleGeneratorArrayPane.static"));
            rbStatic.setSelected(true);
            buttonGroup.add(rbStatic);
            rbStatic.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tfStatic.setVisible(true);
                    tfIncMin.setVisible(false);
                    tfIncStep.setVisible(false);
                    tfRandMin.setVisible(false);
                    tfRandMax.setVisible(false);
                }
            });
            this.add(rbStatic);
            
            tfStatic = new PlaceholderTextField();
            tfStatic.setPlaceholder(Messages.getString("TupleGeneratorNumberPane.value"));
            this.setVisible(true);
            this.add(tfStatic, "wrap");
            
            // INCREMENT
            rbIncrement = new JRadioButton(Messages.getString("TupleGeneratorArrayPane.increment"));
            buttonGroup.add(rbIncrement);
            rbIncrement.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tfStatic.setVisible(false);
                    tfIncMin.setVisible(true);
                    tfIncStep.setVisible(true);
                    tfRandMin.setVisible(false);
                    tfRandMax.setVisible(false);
                }
            });
            this.add(rbIncrement);
            
            tfIncMin = new PlaceholderTextField();
            tfIncMin.setPlaceholder(Messages.getString("TupleGeneratorNumberPane.min"));
            tfIncMin.setVisible(false);
            this.add(tfIncMin);
            
            tfIncStep = new PlaceholderTextField();
            tfIncStep.setPlaceholder(Messages.getString("TupleGeneratorNumberPane.step"));
            tfIncStep.setVisible(false);
            this.add(tfIncStep, "wrap");
            
            // RANDOM
            rbRandom = new JRadioButton(Messages.getString("TupleGeneratorArrayPane.random"));
            buttonGroup.add(rbRandom);
            rbRandom.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    tfStatic.setVisible(false);
                    tfIncMin.setVisible(false);
                    tfIncStep.setVisible(false);
                    tfRandMin.setVisible(true);
                    tfRandMax.setVisible(true);
                }
            });
            this.add(rbRandom);
            
            tfRandMin = new PlaceholderTextField();
            tfRandMin.setPlaceholder(Messages.getString("TupleGeneratorNumberPane.min"));
            tfRandMin.setVisible(false);
            this.add(tfRandMin);
            
            tfRandMax = new PlaceholderTextField();
            tfRandMax.setPlaceholder(Messages.getString("TupleGeneratorNumberPane.max"));
            tfRandMax.setVisible(false);
            this.add(tfRandMax);
        }
    }
    
    @Override
    public Generator<String> getGenerator() throws NumberFormatException {
        Generator<String> generator;
        
        if (rbStatic.isSelected()) {
            int val = Integer.parseInt(tfStatic.getText());
            generator = () -> String.valueOf(val);
        } else if (rbIncrement.isSelected()) {
            int min = Integer.parseInt(tfIncMin.getText());
            int step = Integer.parseInt(tfIncStep.getText());
            generator = new Generator<String>() {
                private int curr = min;
                
                @Override
                public String yield() {
                    int val = curr;
                    curr += step;
                    return String.valueOf(val);
                }
            };
        } else {
            int min = Integer.parseInt(tfRandMin.getText());
            int max = Integer.parseInt(tfRandMax.getText());
            generator = () -> String.valueOf(Misc.getRandomInteger(min, max));
        }
        
        return generator;
    }
}

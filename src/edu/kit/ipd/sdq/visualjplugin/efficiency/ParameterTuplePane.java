package edu.kit.ipd.sdq.visualjplugin.efficiency;

import java.awt.Color;
import java.awt.Cursor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.kit.ipd.sdq.visualj.util.ResourceLoader;
import net.miginfocom.swing.MigLayout;

/**
 * This pane contains input fields for every parameter.
 * 
 * In particular: - for numbers (including chars), arrays of numbers, arrays of
 * booleans, arrays of Strings, and Strings: an input field - for other objects:
 * a dropdown list containing all objects on the BlueJ Object Bench
 */
class ParameterTuplePane extends JPanel {
    
    private static final long serialVersionUID = -8841870312155188619L;
    private ArrayList<JTextField> textFields;
    private Class<?>[] types;
    private Parameter[] parameters;
    private EfficiencyTestInputWindow window;
    
    ParameterTuplePane(Method method, EfficiencyTestInputWindow efficiencyWindow) {
        parameters = method.getParameters();
        window = efficiencyWindow;
        textFields = new ArrayList<JTextField>();
        types = method.getParameterTypes();
        initialize();
    }
    
    /**
     * Generates the GUI components
     */
    private void initialize() {
        this.setLayout(new MigLayout("flowx", "[grow,fill][]", "[grow,fill]"));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JPanel informationPanel = new JPanel(new MigLayout("flowx,insets 0,wrap 2", "[][grow,fill]"));
        {
            for (int i = 0; i < types.length; ++i) {
                {
                    JLabel label = new JLabel(types[i].getSimpleName() + " " + parameters[i].getName() + ":");
                    informationPanel.add(label);
                    
                    JTextField textField = new JTextField();
                    textFields.add(textField);
                    informationPanel.add(textField);
                }
            }
        }
        this.add(informationPanel);
        
        JPanel deleteButtonPanel = new JPanel(new MigLayout("flowx,insets 0", "", "[align top]"));
        {
            ImageIcon deleteIcon = ResourceLoader.loadIcon("close.png");
            JButton btnDelete;
            if (deleteIcon != null) {
                btnDelete = new JButton(deleteIcon);
            } else {
                btnDelete = new JButton("X");
            }
            btnDelete.addActionListener((event) -> {
                window.removeParameterTuplePane(this);
            });
            
            btnDelete.setContentAreaFilled(false);
            btnDelete.setBorder(null);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteButtonPanel.add(btnDelete);
        }
        this.add(deleteButtonPanel);
        
    }
    
    /**
     * Gets the Values of the pane which the user has set.
     * 
     * @return and object array containing the values.
     */
    Object[] getValue() {
        Object[] values = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            values[i] = parseTextFieldString(textFields.get(i).getText(), types[i]);
        }
        return values;
    }
    
    private Object parseTextFieldString(String input, Class<?> type) {
        Object ret = null;
        if (type.isArray()) {
            String[] arr = parseArrayInput(input);
            ret = createArray(arr, type.getComponentType());
        } else if (type.getName().equals(String.class.getName())) {
            return input;
        } else {
            ret = createPrimitive(input, type);
        }
        
        return ret;
    }
    
    private Object createPrimitive(String str, Class<?> type) {
        String className = type.getName();
        Object ret = null;
        if (className.equals(Integer.class.getName()) || className.equals(int.class.getName())) {
            ret = Integer.parseInt(str);
        } else if (className.equals(Short.class.getName()) || className.equals(short.class.getName())) {
            ret = Short.parseShort(str);
        } else if (className.equals(Long.class.getName()) || className.equals(long.class.getName())) {
            ret = Long.parseLong(str);
        } else if (className.equals(Float.class.getName()) || className.equals(float.class.getName())) {
            ret = Float.parseFloat(str);
        } else if (className.equals(Double.class.getName()) || className.equals(double.class.getName())) {
            ret = Double.parseDouble(str);
        } else if (className.equals(Character.class.getName()) || className.equals(char.class.getName())) {
            // not very likely but..
            ret = new Character(str.toCharArray()[0]);
        } else if (className.equals(Byte.class.getName()) || className.equals(byte.class.getName())) {
            ret = Byte.parseByte(str);
        } else if (className.equals(String.class.getName())) {
            ret = str;
        } else {
            // not planned, let's throw!
            throw new UnsupportedOperationException(Messages.getString("ParameterTuplePane.noObjects"));
        }
        return ret;
    }
    
    /*
     * return type is Object (instead of Object[]) because of primitive types
     */
    private Object createArray(String[] arr, Class<?> componentType) {
        // ignore if its wrapped or not.
        String className = componentType.getName();
        Object[] ret = new Object[arr.length];
        
        /*
         * check for the various primitives, wrappers and String. also check if
         * the textfield displayed an empty array (like "[]" or ""). If it did,
         * return an empty array of length 0
         */
        
        if (className.equals(Integer.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Integer[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = Integer.parseInt(arr[i]);
        } else if (className.equals(int.class.getName())) {
            // these might be evitable, but I don't know how..
            if (arr.length == 1 && arr[0].equals(""))
                return new int[0];
            int[] primRet = new int[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = Integer.parseInt(arr[i]);
            return primRet;
        } else if (className.equals(Short.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Short[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = Short.parseShort(arr[i]);
        } else if (className.equals(short.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new short[0];
            short[] primRet = new short[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = Short.parseShort(arr[i]);
            return primRet;
        } else if (className.equals(Long.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Long[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = Long.parseLong(arr[i]);
        } else if (className.equals(long.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new long[0];
            long[] primRet = new long[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = Long.parseLong(arr[i]);
            return primRet;
        } else if (className.equals(Float.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Float[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = Float.parseFloat(arr[i]);
        } else if (className.equals(float.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new float[0];
            float[] primRet = new float[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = Float.parseFloat(arr[i]);
            return primRet;
        } else if (className.equals(Double.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Double[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = Double.parseDouble(arr[i]);
        } else if (className.equals(double.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new double[0];
            double[] primRet = new double[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = Double.parseDouble(arr[i]);
            return primRet;
        } else if (className.equals(Character.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Character[0];
            // not very likely but..
            for (int i = 0; i < ret.length; i++)
                ret[i] = new Character(arr[i].toCharArray()[0]);
        } else if (className.equals(char.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new char[0];
            char[] primRet = new char[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = arr[i].toCharArray()[0];
            return primRet;
        } else if (className.equals(Byte.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new Byte[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = Byte.parseByte(arr[i]);
        } else if (className.equals(byte.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new byte[0];
            byte[] primRet = new byte[arr.length];
            for (int i = 0; i < ret.length; i++)
                primRet[i] = Byte.parseByte(arr[i]);
            return primRet;
        } else if (className.equals(String.class.getName())) {
            if (arr.length == 1 && arr[0].equals(""))
                return new String[0];
            for (int i = 0; i < ret.length; i++)
                ret[i] = arr[i];
        } else {
            // not planned, let's throw!
            throw new UnsupportedOperationException(Messages.getString("ParameterTuplePane.noArrays"));
        }
        return ret;
    }
    
    /**
     * 
     * @param str
     *            must be matching e.g [1,2,3] or 1,2,3
     * @return
     */
    private String[] parseArrayInput(String str) {
        if (str.startsWith("[") && str.endsWith("]")) {
            return trimArray((str.substring(1, str.length() - 1)).split(","));
        }
        return trimArray(str.trim().split(","));
    }
    
    private String[] trimArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim();
        }
        return arr;
    }
    
    /**
     * Returns the amount of attributes in the method
     * 
     * @return
     *         amount of variables
     */
    public int getAmountOfVariables() {
        return types.length;
    }
    
    /**
     * Sets tthe given text field text to the given text
     * 
     * @param index
     *            index of text field
     * @param text
     *            text to set to the text field
     */
    void setTextFieldText(int index, String text) {
        textFields.get(index).setText(text);
    }
    
    /**
     * Returns String[] containing the text field values from top to bottom
     * 
     * @return
     *         String[] with the text field values
     */
    String[] getTextFieldValues() {
        String[] values = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            values[i] = textFields.get(i).getText();
        }
        return values;
    }
}

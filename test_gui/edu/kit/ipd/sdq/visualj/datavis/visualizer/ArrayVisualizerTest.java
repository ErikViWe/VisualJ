package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.ArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.BooleanArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.ByteArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.CharArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.DoubleArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.FloatArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.LongArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.ShortArrayLogger;
import edu.uci.ics.jung.graph.Graph;

public class ArrayVisualizerTest {
    
    boolean[] booleanArray = new boolean[50];
    byte[] byteArray = new byte[50];
    char[] charArray = new char[50];
    double[] doubleArray = new double[50];
    float[] floatArray = new float[50];
    int[] intArray = new int[50];
    long[] longArray = new long[50];
    short[] shortArray = new short[50];
    String[] stringArray = new String[50];
    BooleanArrayLogger booleanLogger = new BooleanArrayLogger(booleanArray);
    ByteArrayLogger byteLogger = new ByteArrayLogger(byteArray);
    CharArrayLogger charLogger = new CharArrayLogger(charArray);
    DoubleArrayLogger doubleLogger = new DoubleArrayLogger(doubleArray);
    FloatArrayLogger floatLogger = new FloatArrayLogger(floatArray);
    IntArrayLogger intLogger = new IntArrayLogger(intArray);
    LongArrayLogger longLogger = new LongArrayLogger(longArray);
    ShortArrayLogger shortLogger = new ShortArrayLogger(shortArray);
    ArrayLogger stringLogger;
    ArrayVisualizer visualizer;
    Graph<String, String> gr;
    Random random = new Random();
    static String option;
    int x;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        String[] options = {"boolean", "byte", "char", "double", "float", "int", "long", "short", "String"};
        int i = JOptionPane.showOptionDialog(null, "messagee", "Choose type of array", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        option = options[i];
    }
    
    @Before
    public void setUp() {
        if (option.equals("boolean")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                booleanArray = new boolean[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < booleanArray.length; j++) {
                    booleanArray[j] = random.nextBoolean();
                }
                if (i == 5) {
                    booleanArray = null;
                }
                booleanLogger.update(booleanArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(booleanLogger);
        } else if (option.equals("byte")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                byteArray = new byte[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < byteArray.length; j++) {
                    byteArray[j] = (byte) (random.nextInt(3 - 0));
                }
                if (i == 5) {
                    byteArray = null;
                }
                byteLogger.update(byteArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(byteLogger);
        } else if (option.equals("char")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                charArray = new char[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < charArray.length; j++) {
                    charArray[j] = (char) (random.nextInt(3 - 0) + 'a');
                }
                if (i == 5) {
                    charArray = null;
                }
                charLogger.update(charArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(charLogger);
        } else if (option.equals("double")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                doubleArray = new double[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < doubleArray.length; j++) {
                    doubleArray[j] = random.nextInt(3 - 0) + 0.001;
                }
                if (i == 5) {
                    doubleArray = null;
                }
                doubleLogger.update(doubleArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(doubleLogger);
        } else if (option.equals("float")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                floatArray = new float[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < floatArray.length; j++) {
                    floatArray[j] = (float) (random.nextInt(3 - 0) + 0.001);
                }
                if (i == 5) {
                    floatArray = null;
                }
                floatLogger.update(floatArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(floatLogger);
        } else if (option.equals("int")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                intArray = new int[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < intArray.length; j++) {
                    intArray[j] = random.nextInt(3 - 0) * 100;
                }
                if (i == 5) {
                    intArray = null;
                }
                intLogger.update(intArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(intLogger);
        } else if (option.equals("long")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                longArray = new long[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < longArray.length; j++) {
                    longArray[j] = random.nextInt(3 - 0) * 100;
                }
                if (i == 5) {
                    longArray = null;
                }
                longLogger.update(longArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(longLogger);
        } else if (option.equals("short")) {
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                shortArray = new short[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < shortArray.length; j++) {
                    shortArray[j] = (short) (random.nextInt(3 - 0) * 100);
                }
                if (i == 5) {
                    shortArray = null;
                }
                shortLogger.update(shortArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(shortLogger);
        } else if (option.equals("String")) {
            Arrays.fill(stringArray, "");
            stringLogger = new ArrayLogger(stringArray);
            for (int i = 0; i < 10; i++) {
                x = (random.nextBoolean() ? 1 : -1);
                stringArray = new String[Math.abs(50 + x * random.nextInt(20 - 0))];
                for (int j = 0; j < stringArray.length; j++) {
                    stringArray[j] = (random.nextInt(3 - 0) * 100) + " String";
                }
                if (i == 5) {
                    stringArray = null;
                }
                stringLogger.update(stringArray);
                VisTestUtil.simulateBreakpoint();
            }
            visualizer = new ArrayVisualizer(stringLogger);
        }
        VisTestUtil.simulateBreakpoint();
        
    }
    
    /**
     * Opens a {@link TestFrame} containing an {@link ArrayVisualizer}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateInt() throws InterruptedException {
        TestFrame testFrame = new TestFrame(visualizer);
        // testFrame.setSize(700, 250);
        // testFrame.setMinimumSize(new Dimension(250, 250));
        testFrame.setVisible(true);
        for (int i = 0; i < 10; i++) {
            visualizer.update(i);
            testFrame.repaint();
            testFrame.setVisible(true);
            Thread.sleep(1000);
        }
        testFrame.waitForClose();
    }
}

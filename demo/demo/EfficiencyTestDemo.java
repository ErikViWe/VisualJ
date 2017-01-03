package demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.NoDefaultConstructorException;
import edu.kit.ipd.sdq.visualj.efficiency.measure.StepCounter;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;
import edu.kit.ipd.sdq.visualj.efficiency.view.EfficiencyResultWindow;

/**
 * This class contains a comparison of multiple simple sorting algorithms using the
 * VisualJ library.
 */
public final class EfficiencyTestDemo {
    
    /**
     * Creates an {@link EfficiencyTestResultWindow} to compare the performance
     * of some sorting algorithms.
     */
    @Test
    public void compareSortingAlgorithms() {
        List<TestRun.Result[]> results = new LinkedList<>();
        
        Object[][] arguments = new Object[][]{{new int[]{3, 1, 2}}, {new int[]{5, 4, 5, 6, 2, 3, -42, 21}}};
        
        try {
            EfficiencyTest bubbleSortTest = new EfficiencyTest(null,
                    EfficiencyTestDemo.class.getDeclaredMethod("bubbleSort", int[].class), arguments, 10);
            
            EfficiencyTest selectionSortTest = new EfficiencyTest(null,
                    EfficiencyTestDemo.class.getDeclaredMethod("selectionSort", int[].class), arguments, 10);
            
            EfficiencyTest bogoSortTest = new EfficiencyTest(null,
                    EfficiencyTestDemo.class.getDeclaredMethod("bogoSort", int[].class), arguments, 10);
            
            results.add(bubbleSortTest.run());
            results.add(selectionSortTest.run());
            results.add(bogoSortTest.run());
        } catch (IllegalArgumentException | NoSuchMethodException | SecurityException | NoDefaultConstructorException
                | InvocationTargetException | IllegalAccessException e) {
            System.err.println("Exception while running efficiency tests: " + e.getMessage());
            e.printStackTrace();
        }
        
        EfficiencyResultWindow win = new EfficiencyResultWindow(results);
        win.setVisible(true);
        waitForWindowToClose(win);
    }
    
    /**
     * Creates an {@link EfficiencyTestResultWindow} to compare the performance
     * of some sorting algorithms using a custom generator.
     */
    @Test
    public void compareSortingAlgorithmsAdvanced() {
        List<TestRun.Result[]> results = new LinkedList<>();
        
        try {
            EfficiencyTest bubbleSortTest = new EfficiencyTest(null,
                    EfficiencyTestDemo.class.getDeclaredMethod("bubbleSort", int[].class), new MyGenerator(), 10);
            
            EfficiencyTest selectionSortTest
                    = new EfficiencyTest(null, EfficiencyTestDemo.class.getDeclaredMethod("selectionSort", int[].class),
                            // Take care to pass a new generator to every
                            // EfficiencyTest.
                            // (Technically VisualJ tries to use a clone of your
                            // generator if possible,
                            // but that's not obvious to someone just reading your code,
                            // and only works if your generator has a clone method.
                            // Passing two different
                            // generators makes it clear what's going on.)
                            new MyGenerator(), 10);
            
            results.add(bubbleSortTest.run());
            results.add(selectionSortTest.run());
        } catch (IllegalArgumentException | NoSuchMethodException | SecurityException | NoDefaultConstructorException
                | InvocationTargetException | IllegalAccessException e) {
            System.err.println("Exception while running efficiency tests: " + e.getMessage());
            e.printStackTrace();
        }
        
        EfficiencyResultWindow win = new EfficiencyResultWindow(results);
        win.setVisible(true);
        waitForWindowToClose(win);
    }
    
    public class MyGenerator implements EfficiencyTest.ArgumentTupleGenerator {
        
        private final int NUMBER_OF_TUPLES = 50;
        private int count = 1;
        
        @Override
        public Object[] yield() {
            if (count > NUMBER_OF_TUPLES) {
                return null;
            }
            
            Random rand = new Random();
            int[] arr = new int[count * 10];
            
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = rand.nextInt();
            }
            
            ++count;
            return new Object[]{arr};
        }
    };
    
    public static void bubbleSort(int[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            for (int j = 0; j < arr.length - i; ++j) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
                
                StepCounter.step();
            }
        }
    }
    
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; ++i) {
            int min = i;
            
            for (int j = i + 1; j < arr.length; ++j) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
                
                StepCounter.step();
            }
            
            swap(arr, i, min);
        }
    }
    
    public static void bogoSort(int[] arr) {
        while (!isSorted(arr)) {
            shuffle(arr);
            
            StepCounter.step();
        }
    }
    
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; ++i) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        
        return true;
    }
    
    private static void shuffle(int[] arr) {
        // Fisher-Yates shuffle algorithm
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; --i) {
            swap(arr, i, rand.nextInt(i + 1));
        }
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private void waitForWindowToClose(EfficiencyResultWindow win) {
        Thread t = new Thread(() -> {
            synchronized (this) {
                while (win.isVisible()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        win.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosed(WindowEvent e) {
                notifyAll();
            }
        });
        
        t.run();
    }
}

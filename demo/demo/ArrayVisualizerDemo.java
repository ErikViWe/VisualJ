package demo;

import java.util.Random;

import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.Breakpoints;
import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.ImplicitHeapVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Window;

/**
 * This class contains multiple simple sorting algorithms visualized using the
 * VisualJ library.
 */
public final class ArrayVisualizerDemo {
    
    /**
     * Shows a visualization of the bubble sort algorithm using an example
     * array.
     */
    @Test
    public void showBubbleSort() {
        bubbleSort(new int[]{42, 4, 2, 5, 1, 3, 2});
    }
    
    /**
     * Shows a visualization of the selection sort algorithm using an example
     * array.
     */
    @Test
    public void showSelectionSort() {
        selectionSort(new int[]{42, 4, 2, 5, 1, 3, 2});
    }
    
    /**
     * Shows a visualization of the insertion sort algorithm using an example
     * array.
     */
    @Test
    public void showInsertionSort() {
        insertionSort(new int[]{42, 4, 2, 5, 1, 3, 2});
    }
    
    /**
     * Shows a visualization of the heap sort algorithm using an example array.
     */
    @Test
    public void showHeapSort() {
        heapSort(new int[]{42, 4, 2, 5, 1, 3, 2});
    }
    
    /**
     * Shows a visualization of the bogo sort algorithm using an example array.
     */
    @Test
    public void showBogoSort() {
        bogoSort(new int[]{42, 4, 2, 5, 1, 3, 2});
    }
    
    public static void bubbleSort(int[] arr) {
        Breakpoints.reset();
        Window win = new Window();
        
        IntArrayLogger log = new IntArrayLogger(arr);
        win.addVisualizer(log);
        win.setVisible(true);
        Breakpoints.breakpoint();
        
        for (int i = 1; i < arr.length; ++i) {
            for (int j = 0; j < arr.length - i; ++j) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    
                    log.update(arr);
                }
                
                Breakpoints.breakpoint();
            }
        }
    }
    
    public static void selectionSort(int[] arr) {
        Breakpoints.reset();
        Window win = new Window();
        
        IntArrayLogger log = new IntArrayLogger(arr);
        win.addVisualizer(log);
        win.setVisible(true);
        Breakpoints.breakpoint();
        
        for (int i = 0; i < arr.length - 1; ++i) {
            int min = i;
            
            for (int j = i + 1; j < arr.length; ++j) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            
            swap(arr, i, min);
            
            log.update(arr);
            Breakpoints.breakpoint();
        }
    }
    
    public static void insertionSort(int[] arr) {
        Breakpoints.reset();
        Window win = new Window();
        
        IntArrayLogger log = new IntArrayLogger(arr);
        win.addVisualizer(log);
        win.setVisible(true);
        Breakpoints.breakpoint();
        
        for (int i = 1; i < arr.length; ++i) {
            for (int j = i; j > 0 && arr[j - 1] > arr[j]; --j) {
                swap(arr, j - 1, j);
                
                log.update(arr);
                Breakpoints.breakpoint();
            }
        }
    }
    
    public static void heapSort(int[] arr) {
        Breakpoints.reset();
        Window win = new Window();
        
        IntArrayLogger log = new IntArrayLogger(arr);
        win.addVisualizer(log);
        win.addVisualizer(new ImplicitHeapVisualizer(log));
        win.setVisible(true);
        Breakpoints.breakpoint(0);
        
        for (int i = (arr.length / 2) - 1; i >= 0; i--) {
            siftDown(arr, i, arr.length, log);
        }
        
        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, i, 0);
            siftDown(arr, 0, i, log);
            
            log.update(arr);
            Breakpoints.breakpoint(0);
        }
    }
    
    private static void siftDown(int[] arr, int i, int n, IntArrayLogger log) {
        while (i <= (n / 2) - 1) {
            int childIndex = ((i + 1) * 2) - 1;
            
            if (childIndex + 1 <= n - 1) {
                if (arr[childIndex] < arr[childIndex + 1]) {
                    childIndex++;
                }
            }
            
            if (arr[i] < arr[childIndex]) {
                swap(arr, i, childIndex);
                i = childIndex;
            } else {
                break;
            }
            
            log.update(arr);
            Breakpoints.breakpoint(1);
        }
    }
    
    public static void bogoSort(int[] arr) {
        Breakpoints.reset();
        Window win = new Window();
        
        IntArrayLogger log = new IntArrayLogger(arr);
        win.addVisualizer(log);
        win.setVisible(true);
        Breakpoints.breakpoint();
        
        while (!isSorted(arr)) {
            shuffle(arr);
            
            log.update(arr);
            Breakpoints.breakpoint();
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
}

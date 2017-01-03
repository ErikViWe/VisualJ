import java.util.Random;

import edu.kit.ipd.sdq.visualj.datavis.logger.Breakpoints;
import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.ImplicitHeapVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Window;

/**
 * Beispielmethoden für die Visualizer.
 * 
 * Führe einfach mit BlueJ eine der Methoden aus und schau dir an, wie der Algorithmus visualisiert wird.
 */
public final class ArrayVisualizerDemo {
    
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
        
        // Hier wird absichtlich eine ArrayIndexOutOfBoundsException ausgelöst,
        // um zu zeigen, dass VisualJ auch nach dem Wurf einer Exception funktioniert.
        for (int i = 1; i <= arr.length; ++i) {
        // Richtig wäre:
        //for (int i = 1; i < arr.length; ++i) {
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
        Breakpoints.breakpoint();
        
        for (int i = (arr.length / 2) - 1; i >= 0; i--) {
            siftDown(arr, i, arr.length, log);
        }
        
        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, i, 0);
            siftDown(arr, 0, i, log);

            log.update(arr);
            Breakpoints.breakpoint();
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
            Breakpoints.breakpoint();
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

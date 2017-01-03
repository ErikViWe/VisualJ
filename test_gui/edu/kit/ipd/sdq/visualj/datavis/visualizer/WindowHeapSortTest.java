package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import edu.kit.ipd.sdq.visualj.datavis.logger.Breakpoints;
import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;

public class WindowHeapSortTest {
    
    private IntArrayLogger logger;
    private ImplicitHeapVisualizer heapVis;
    private ArrayVisualizer arrVis;
    private int[] arr = {3, 5, 667, 3, 6, 55, 3, 2, 4345, 6, 7, 34, 5, 7, 7, 882, 89,};
    
    /**
     * Opens a {@link Window} containing an {@link ArryVisualizer} and a {@link HeapVisualizer}.
     * 
     * <p>
     * Both visualizers show the same array being sorted using heap sort.
     * </p>
     * 
     * @throws InterruptedException
     */
    public void run() {
        logger = new IntArrayLogger(arr);
        heapVis = new ImplicitHeapVisualizer(logger);
        arrVis = new ArrayVisualizer(logger);
        
        Window w = new Window();
        w.addVisualizer(heapVis);
        w.addVisualizer(arrVis);
        w.setVisible(true);
        
        Breakpoints.breakpoint();
        
        heapSort(arr);
    }
    
    public void heapSort(int[] a) {
        generateMaxHeap(a);
        
        for (int i = a.length - 1; i > 0; i--) {
            swap(a, i, 0);
            siftDown(a, 0, i);
        }
    }
    
    private void generateMaxHeap(int[] a) {
        // starte von der Mitte rückwärts.
        for (int i = (a.length / 2) - 1; i >= 0; i--) {
            siftDown(a, i, a.length);
        }
    }
    
    private void siftDown(int[] a, int i, int n) {
        while (i <= (n / 2) - 1) {
            int kindIndex = ((i + 1) * 2) - 1;
            
            if (kindIndex + 1 <= n - 1) {
                if (a[kindIndex] < a[kindIndex + 1]) {
                    kindIndex++;
                }
            }
            
            if (a[i] < a[kindIndex]) {
                swap(a, i, kindIndex);
                i = kindIndex;
            } else
                break;
        }
    }
    
    private void swap(int[] a, int i, int kindIndex) {
        int z = a[i];
        a[i] = a[kindIndex];
        a[kindIndex] = z;
        // breakpoint goes here
        logger.update(a);
        
        Breakpoints.breakpoint();
    }
    
    public static void main(String[] args) {
        WindowHeapSortTest test = new WindowHeapSortTest();
        test.run();
    }
}

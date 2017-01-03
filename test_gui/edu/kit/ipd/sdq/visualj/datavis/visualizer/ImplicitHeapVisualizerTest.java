package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.visualj.datavis.logger.IntArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.ImplicitHeapVisualizer;
import edu.uci.ics.jung.graph.DelegateTree;

public class ImplicitHeapVisualizerTest {
    
    int[] treeArray;
    DelegateTree<String, String> tree;
    IntArrayLogger logger;
    ImplicitHeapVisualizer visualizer;
    
    @Before
    public void setUp() {
        logger = new IntArrayLogger(treeArray);
        VisTestUtil.simulateBreakpoint();
        treeArray = new int[15];
        
        for (int i = 1; i < treeArray.length; i++) {
            treeArray[i] = i;
        }
        visualizer = new ImplicitHeapVisualizer(logger);
    }
    
    /**
     * Opens a {@link TestFrame} containing an {@link ImplicitHeapVisualizer}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateInt() throws InterruptedException {
        TestFrame testFrame = new TestFrame(visualizer);
        
        int steps = heapSort(treeArray);
        
        for (int i = 0; i < steps + 1; i++) {
            
            visualizer.update(i);
            testFrame.repaint();
            testFrame.setMinimumSize(visualizer.getPreferredSize());
            testFrame.setVisible(true);
            Thread.sleep(900);
        }
        
        testFrame.waitForClose();
    }
    
    private int heapSort(int[] a) {
        generateMaxHeap(a);
        int steps = 0;
        
        for (int i = a.length - 1; i > 0; i--) {
            swap(a, i, 0);
            siftDown(a, 0, i);
            
            logger.update(a);
            VisTestUtil.simulateBreakpoint();
            steps++;
        }
        return steps;
    }
    
    private void generateMaxHeap(int[] a) {
        // starte von der Mitte r�ckw�rts.
        for (int i = (a.length / 2) - 1; i >= 0; i--) {
            siftDown(a, i, a.length);
        }
    }
    
    private void siftDown(int[] a, int i, int n) {
        while (i <= (n / 2) - 1) {
            int childIndex = ((i + 1) * 2) - 1;
            
            if (childIndex + 1 <= n - 1) {
                if (a[childIndex] < a[childIndex + 1]) {
                    childIndex++;
                }
            }
            
            if (a[i] < a[childIndex]) {
                swap(a, i, childIndex);
                i = childIndex;
            } else
                break;
        }
    }
    
    private void swap(int[] a, int i, int childIndex) {
        int z = a[i];
        a[i] = a[childIndex];
        a[childIndex] = z;
    }
    
}

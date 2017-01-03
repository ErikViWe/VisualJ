package edu.kit.ipd.sdq.visualjplugin;

import edu.kit.ipd.sdq.visualj.efficiency.measure.StepCounter;

/**
 * Objects of this class are used in {@link EfficiencyTestInputWindowTest}.
 */
public class TestClass {
    
    public TestClass() {
    }
    
    @SuppressWarnings("unused")
    public void foo(int a, byte[] b, int[] c, int d, int e, String str) throws InterruptedException {
        // bubblesort
        for (int n = c.length; n > 1; n--) {
            for (int i = 0; i < n - 1; i++) {
                if (c[i] > c[i + 1]) {
                    StepCounter.step();
                    int tmp = c[i];
                    c[i] = c[i + 1];
                    c[i + 1] = tmp;
                }
            }
        }
    }
}

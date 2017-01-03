import java.util.Random;

import edu.kit.ipd.sdq.visualj.efficiency.measure.StepCounter;

/**
 * Beispielmethoden für den Effizienztest.
 * 
 * Klicke im BlueJ-Fenster rechts auf diese Klasse und wähle unter dem Menüpunkt "Effizienzbetrachtung" die Methode aus, deren Effizienz du messen willst.
 */
public final class EfficiencyTestDemo {
    
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
}

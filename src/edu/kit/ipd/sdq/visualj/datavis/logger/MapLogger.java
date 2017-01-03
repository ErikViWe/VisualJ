package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.MapVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;

/**
 * A logger for maps.
 *
 * @param <K>
 *            the map's key type.
 * @param <V>
 *            the map's value type
 */
public class MapLogger<K, V> extends Logger<Map<K, V>> {
    
    /**
     * Create a new {@code MapLogger}.
     * 
     * @param arr
     *            the initial value of the map to be logged.
     */
    public MapLogger(Map<K, V> map) {
        update(map);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected Map<K, V> cloneValue(Map<K, V> map) {
        if (map == null)
            return null;
        Map<K, V> result = new TreeMap<>();
        
        try {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                Method cloneMethod = entry.getValue().getClass().getMethod("clone");
                result.put(entry.getKey(), (V) cloneMethod.invoke(entry.getValue()));
            }
        } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
            // If we cannot clone all elements, just make a shallow copy of the
            // map.
            return new TreeMap<>(map);
        }
        
        return result;
    }
    
    @Override
    public Visualizer createDefaultVisualizer() {
        return new MapVisualizer<>(this);
    }
    
}

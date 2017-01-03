package edu.kit.ipd.sdq.visualj.datavis.logger;

import org.apache.commons.lang3.ObjectUtils;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.GraphVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.ImplicitHeapVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.Visualizer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * A logger for graphs, i.e. objects of classes that implement the {@link Graph}
 * interface from the Jung library.
 * 
 * <p>
 * For trees, use a {@link TreeLogger} instead.
 * </p>
 * 
 * <p>
 * If you want to visualize an array as a implicit binary heap, use an
 * {@link ArrayLogger} with an {@link ImplicitHeapVisualizer} instead.
 * </p>
 *
 * @param <V>
 *            the type of the vertices in the graph.
 * @param <E>
 *            the type of the edges in the graph.
 */
public class GraphLogger<V, E> extends Logger<Graph<V, E>> {
    
    /**
     * Create a new {@code GraphLogger}.
     * 
     * @param arr
     *            the initial value of the graph to be logged.
     */
    public GraphLogger(Graph<V, E> graph) {
        update(graph);
    }
    
    @Override
    protected Graph<V, E> cloneValue(Graph<V, E> graph) {
        if (graph == null)
            return null;
        
        Graph<V, E> result = new SparseMultigraph<>();
        
        for (V vertex : graph.getVertices()) {
            result.addVertex(ObjectUtils.cloneIfPossible(vertex));
        }
        
        for (E edge : graph.getEdges()) {
            result.addEdge(edge, graph.getIncidentVertices(ObjectUtils.cloneIfPossible(edge)));
        }
        
        return result;
    }
    
    @Override
    public Visualizer createDefaultVisualizer() {
        return new GraphVisualizer<>(this);
    }
}

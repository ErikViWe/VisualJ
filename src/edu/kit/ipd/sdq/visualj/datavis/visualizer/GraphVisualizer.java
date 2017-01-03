package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import edu.kit.ipd.sdq.visualj.datavis.logger.GraphLogger;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * A visualizer for {@link GraphLogger}s.
 *
 * @param <V>
 *            the type of the vertices in the graph.
 * @param <E>
 *            the type of the edges in the graph.
 */
public class GraphVisualizer<V, E> extends Visualizer {
    
    private static final int vertexCellFrameLength = 20;
    
    private static final long serialVersionUID = 2822555396959936233L;
    private static final Font font = new Font("Dialog", Font.CENTER_BASELINE, 15);
    private static final FontMetrics metrics = new FontMetrics(font) {
        
        private static final long serialVersionUID = -5665064634200300808L;
    };
    
    private GraphLogger<V, E> logger;
    private Rectangle2D bounds;
    private int widthInPixels;
    
    /**
     * Creates a new {@code GraphVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public GraphVisualizer(GraphLogger<V, E> logger, String title) {
        this.logger = logger;
        this.setTitle(title);
    }
    
    /**
     * Creates a new {@code GraphVisualizer} without a title.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     */
    public GraphVisualizer(GraphLogger<V, E> logger) {
        this(logger, null);
    }
    
    @Override
    protected void update(int breakpointId) {
        
        Graph<V, E> currentGraph = logger.getValue(breakpointId);
        if (currentGraph == null) {
            this.showNull();
            return;
        }
        Collection<V> vertices = currentGraph.getVertices();
        
        int longestWord = 0;
        for (V vertex : vertices) {
            bounds = metrics.getStringBounds(vertex.toString(), null);
            widthInPixels = (int) bounds.getWidth();
            if (longestWord < widthInPixels)
                longestWord = widthInPixels;
        }
        
        int cellLength = longestWord + vertexCellFrameLength;
        
        CircleLayout<V, E> currentLayout = new CircleLayout<V, E>(currentGraph);
        // vv fits the layout to the dimension but doesn't care about the vertex
        // size so it cuts some vertices..
        VisualizationViewer<V, E> vv = new VisualizationViewer<V, E>(currentLayout);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<V, E>());
        (new VertexRenderer()).renderVertexEllipse(vv, cellLength);
        this.removeAll();
        this.add(vv);
    }
}

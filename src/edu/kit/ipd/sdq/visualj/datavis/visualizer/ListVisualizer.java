package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.kit.ipd.sdq.visualj.datavis.logger.ListLogger;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A visualizer for {@link ListLogger}s.
 *
 * @param <T>
 *            the list's element type.
 */
public class ListVisualizer<T> extends Visualizer {
    
    private static final long serialVersionUID = -1954122646801149070L;
    
    private ListLogger<T> logger;
    private String[] oldList = null;
    private String[] changeIndexes;
    private static final int paneHeight = 50;
    private static final int xOffset = 1;
    private static final int yOffset = 21;
    private static final int cellLengthOffset = 10;
    private static final Font font = new Font("Dialog", Font.CENTER_BASELINE, 15);
    private static final FontMetrics metrics = new FontMetrics(font) {
        
        private static final long serialVersionUID = -1032846070123560147L;
    };
    private Rectangle2D bounds;
    private int widthInPixels;
    
    /**
     * Creates a new {@code ListVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public ListVisualizer(ListLogger<T> logger, String title) {
        this.logger = logger;
        this.setTitle(title);
    }
    
    /**
     * Creates a new {@code ListVisualizer} without a title.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     */
    public ListVisualizer(ListLogger<T> logger) {
        this(logger, null);
    }
    
    @Override
    protected void update(int breakpointId) {
        List<T> currentList = logger.getValue(breakpointId);
        if (currentList == null) {
            this.showNull();
            oldList = null;
            return;
        }
        
        // create array of vertices, add vertices and edges to the created graph
        // and find the length of the longest word
        Graph<String, String> currentGraph = new SparseMultigraph<String, String>();
        int longestWord = 0;
        String[] newList = new String[currentList.size()];
        Iterator<T> it = currentList.iterator();
        int j = 0;
        while (it.hasNext()) {
            String str = it.next().toString();
            bounds = metrics.getStringBounds(str, null);
            widthInPixels = (int) bounds.getWidth();
            if (longestWord < widthInPixels)
                longestWord = widthInPixels;
            newList[j] = j + "#" + str;
            currentGraph.addVertex(newList[j]);
            j++;
        }
        for (int i = 0; i < newList.length - 1; i++) {
            currentGraph.addEdge(newList[i], newList[i], newList[i + 1]);
        }
        int cellLength = longestWord + cellLengthOffset;
        int distance = longestWord + cellLengthOffset * 2;
        int lengthOfLayout = distance * (newList.length - 1) + cellLength + xOffset * 2;
        
        // find the changes compared to the last given breakpointId
        changeIndexes = new String[newList.length];
        Arrays.fill(changeIndexes, StateOfCell.NOTCHANGED.getName());
        if (oldList != null) {
            for (int i = 0; i < newList.length; i++) {
                if (oldList.length > i && !newList[i].equals(oldList[i])) {
                    changeIndexes[i] = StateOfCell.CHANGED.getName();
                } else if (oldList.length <= i) {
                    changeIndexes[i] = StateOfCell.NEW.getName();
                }
            }
            
        } else {
            for (int i = 0; i < newList.length; i++) {
                changeIndexes[i] = StateOfCell.NEW.getName();
            }
        }
        
        // visualize graph as an list
        StaticLayout<String, String> layout = new StaticLayout<String, String>(currentGraph);
        for (int i = 0; i < newList.length; i++) {
            if (i != 0) {
                layout.setLocation(newList[i], distance * i + xOffset, yOffset);
            }
            if (i == 0) {
                layout.setLocation(newList[i], xOffset, yOffset);
            }
        }
        
        this.removeAll();
        VisualizationViewer<String, String> vv
                = new VisualizationViewer<String, String>(layout, new Dimension(lengthOfLayout, paneHeight));
        
        (new VertexRenderer()).renderVertexListRectangle(vv, cellLength, changeIndexes);
        
        setLayout(new BorderLayout());
        this.add(vv);
        this.oldList = newList;
    }
}

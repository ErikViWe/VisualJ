package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import edu.kit.ipd.sdq.visualj.datavis.logger.ArrayLogger;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A visualizer for {@link ArrayLogger}s.
 * 
 * @see ImplicitHeapVisualizer
 */
public class ArrayVisualizer extends Visualizer {
    
    private static final long serialVersionUID = 4788004116712393679L;
    
    private ArrayLogger logger;
    private String[] oldArray = null;
    private String[] changeIndexes;
    private static final Font font = new Font("Dialog", Font.CENTER_BASELINE, 15);
    private static final int paneHeight = 60;
    private static final int offset = 1;
    private static final int indexYPos = 50;
    private static final int cellLengthOffset = 10;
    private static final FontMetrics metrics = new FontMetrics(font) {
        
        private static final long serialVersionUID = -1672013785405370027L;
    };
    private Rectangle2D bounds;
    private int widthInPixels;
    
    /**
     * Creates a new {@code ArrayVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public ArrayVisualizer(ArrayLogger logger, String title) {
        this.setTitle(title);
        this.logger = logger;
    }
    
    /**
     * Creates a new {@code ArrayVisualizer} without a title.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     */
    public ArrayVisualizer(ArrayLogger logger) {
        this(logger, null);
    }
    
    @Override
    protected final void update(int breakpointId) {
        Object[] currentArr = logger.getValue(breakpointId);
        if (currentArr == null) {
            this.showNull();
            oldArray = null;
            return;
        }
        
        // create array of vertices, add them to the created graph and find the
        // length of the longest word
        Graph<String, String> currentGraph = new SparseMultigraph<String, String>();
        int longestWord = 0;
        String[] newArray = new String[currentArr.length];
        for (int i = 0; i < newArray.length; i++) {
            bounds = metrics.getStringBounds(currentArr[i].toString(), null);
            widthInPixels = (int) bounds.getWidth();
            if (longestWord < widthInPixels)
                longestWord = widthInPixels;
            newArray[i] = i + "#" + currentArr[i].toString();
            currentGraph.addVertex(newArray[i]);
            currentGraph.addVertex(Integer.toString(i));
        }
        bounds = metrics.getStringBounds("" + newArray.length, null);
        widthInPixels = (int) bounds.getWidth();
        if (longestWord < widthInPixels)
            longestWord = widthInPixels;
        
        int cellLength = longestWord + cellLengthOffset;
        int lengthOfLayout = cellLength * newArray.length + offset * 2;
        
        // find the changes compared to the last given breakpointId
        changeIndexes = new String[newArray.length];
        Arrays.fill(changeIndexes, StateOfCell.NOTCHANGED.getName());
        if (oldArray != null) {
            for (int i = 0; i < newArray.length; i++) {
                if (oldArray.length > i && !newArray[i].equals(oldArray[i])) {
                    changeIndexes[i] = StateOfCell.CHANGED.getName();
                } else if (oldArray.length <= i) {
                    changeIndexes[i] = StateOfCell.NEW.getName();
                }
            }
        } else {
            for (int i = 0; i < newArray.length; i++) {
                changeIndexes[i] = StateOfCell.NEW.getName();
            }
        }
        
        // visualize graph as an array
        StaticLayout<String, String> layout = new StaticLayout<String, String>(currentGraph);
        for (int i = 0; i < newArray.length; i++) {
            layout.setLocation(newArray[i], cellLength * i + offset, offset);
            layout.setLocation(Integer.toString(i), cellLength * i + offset + cellLength / 2, indexYPos);
        }
        
        this.removeAll();
        VisualizationViewer<String, String> vv
                = new VisualizationViewer<String, String>(layout, new Dimension(lengthOfLayout, paneHeight));
        
        (new VertexRenderer()).renderVertexArrayRectangle(vv, cellLength, changeIndexes);
        vv.setLayout(new BorderLayout());
        this.add(vv);
        this.oldArray = newArray;
    }
}

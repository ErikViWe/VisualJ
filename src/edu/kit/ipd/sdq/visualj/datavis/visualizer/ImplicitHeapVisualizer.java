package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import edu.kit.ipd.sdq.visualj.datavis.logger.ArrayLogger;
import edu.kit.ipd.sdq.visualj.datavis.logger.TreeLogger;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A visualizer for {@link ArrayLogger}s that visualizes arrays as implicit
 * binary heaps.
 * 
 * For other kinds of trees, use {@link TreeLogger} with {@link TreeVisualizer}.
 * 
 * @see ArrayVisualizer
 */
public class ImplicitHeapVisualizer extends Visualizer {
    
    private static final long serialVersionUID = -9203222490013771943L;
    
    private ArrayLogger logger;
    private Tree<String, String> oldTree;
    
    /**
     * Creates a new {@code ImplicitHeapVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public ImplicitHeapVisualizer(ArrayLogger logger, String title) {
        this.logger = logger;
        this.setTitle(title);
    }
    
    /**
     * Creates a new {@code ImplicitHeapVisualizer} without a title..
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public ImplicitHeapVisualizer(ArrayLogger logger) {
        this(logger, null);
    }
    
    @Override
    protected final void update(int breakpointId) {
        Object[] currentArr = logger.getValue(breakpointId);
        if (currentArr == null) {
            this.showNull();
            oldTree = null;
            return;
        }
        // create vertices
        String[] vertices = new String[currentArr.length];
        for (int i = 0; i < currentArr.length; i++)
            vertices[i] = i + "#" + currentArr[i].toString();
        
        // build heap
        DelegateTree<String, String> newTree
                = new DelegateTree<String, String>(new DirectedOrderedSparseMultigraph<String, String>());
        newTree.setRoot(vertices[0]);
        for (int i = 0; ((2 * i + 1) < vertices.length); i++) {
            newTree.addChild(vertices[i] + "-" + vertices[2 * i + 1], vertices[i], vertices[2 * i + 1]);
            if ((2 * i + 2) < vertices.length) {
                newTree.addChild(vertices[i] + "-" + vertices[2 * i + 2], vertices[i], vertices[2 * i + 2]);
            }
        }
        TreeRenderer<String, String> rend = new TreeRenderer<String, String>(newTree);
        VisualizationViewer<String, String> vv = rend.buildTreeVisualization();
        rend.markChangedAndNew(vv, oldTree, newTree);
        
        this.removeAll();
        this.add(vv);
        oldTree = newTree;
    }
}

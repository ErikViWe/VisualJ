package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import edu.kit.ipd.sdq.visualj.datavis.logger.TreeLogger;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A visualizer for {@link TreeLogger}s.
 *
 * @param <V>
 *            the type of the vertices in the tree.
 * @param <E>
 *            the type of the edges in the tree.
 */
public class TreeVisualizer<V, E> extends Visualizer {
    
    private static final long serialVersionUID = -2160126363063534885L;
    private TreeLogger<V, E> logger;
    private Tree<V, E> oldTree;
    
    /**
     * Creates a new {@code TreeVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public TreeVisualizer(TreeLogger<V, E> logger, String title) {
        this.logger = logger;
        this.setTitle(title);
    }
    
    /**
     * Creates a new {@code TreeVisualizer} without a title.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     */
    public TreeVisualizer(TreeLogger<V, E> logger) {
        this(logger, null);
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected final void update(int breakpointId) {
        Tree<V, E> currentTree = logger.getValue(breakpointId);
        if (currentTree == null) {
            this.showNull();
            oldTree = null;
            return;
        }
        
        TreeRenderer rend = new TreeRenderer(currentTree);
        VisualizationViewer<V, E> vv = rend.buildTreeVisualization();
        rend.markChangedAndNew(vv, oldTree, currentTree);
        
        this.removeAll();
        this.add(vv);
        oldTree = currentTree;
    }
}

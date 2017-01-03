package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A class to render vertices within the JUNG-Library context.
 */
class TreeRenderer<V, E> {
    
    private static final int vertexCellFrameLength = 20;
    private static final int distanceOffset = 50;
    private static final int levelDistancePx = 50;
    private static final int panelHeightOffset = 150;
    
    private static final Font font = new Font("Dialog", Font.CENTER_BASELINE, 15);
    private static final FontMetrics metrics = new FontMetrics(font) {
        
        private static final long serialVersionUID = 6370030719010609607L;
    };
    
    private Tree<V, E> tree;
    private Rectangle2D bounds;
    private int widthInPixels;
    
    protected TreeRenderer(Tree<V, E> t) {
        this.tree = t;
    }
    
    /**
     * @return a Tree VisualizationViewer with appropriate vertex distances
     */
    protected VisualizationViewer<String, String> buildTreeVisualization() {
        // get cell length
        Collection<V> vertices = tree.getVertices();
        
        int longestWord = 0;
        for (V vertex : vertices) {
            bounds = metrics.getStringBounds(vertex.toString(), null);
            widthInPixels = (int) bounds.getWidth();
            if (longestWord < widthInPixels)
                longestWord = widthInPixels;
        }
        
        int cellLength = longestWord + vertexCellFrameLength;
        int distance = longestWord + distanceOffset;
        
        /*
         * The layout needs to be set after the dummmies are in the tree.
         * Therefore, this takes place in the distance setting method. To make
         * that algorithm exchangable, it also generates the
         * VisualizationViewer.
         */
        VisualizationViewer<String, String> vv = setDistanceAndLayout(distance, cellLength);
        // render the Vertices using the calculated cell length
        (new VertexRenderer()).renderVertexEllipse(vv, cellLength);
        return vv;
    }
    
    /**
     * This method has two functionalities:
     * <ol>
     * <li>For every leaf that has not the maximum depth, generate vertices
     * until every leaf has max depth.</li>
     * <li>It returns the Added vertices for later removal.</li>
     * </ol>
     * 
     * @param maxDepth
     * @return
     */
    private ArrayList<V> fillTree(int maxDepth) {
        ArrayList<V> dummyVertices = new ArrayList<V>();
        
        // we need to put the current vertices into a new list as the iterator
        // of tree.getVertices() does not allow
        // adding new vertices during iteration
        ArrayList<V> vertices = new ArrayList<>(tree.getVertices());
        
        // generate the dummies, add them to the tree, add them to the return
        // value dummyVertices.
        for (V vertex : vertices) {
            // choose only the leaves which "hang too high"
            if (tree.getChildCount(vertex) == 0 && tree.getDepth(vertex) < maxDepth) {
                dummyVertices.addAll(addChildren(vertex, maxDepth));
            }
        }
        
        return dummyVertices;
    }
    
    /**
     * This method generates children for leafs until this vertex's children
     * contain exactly one leaf, and that has maxDepth.
     * 
     * @param vertex
     *            the vertex which needs to get children
     * @param maxDepth
     *            the maximum depth from where no more children are generated
     * @return the added vertices
     */
    @SuppressWarnings("unchecked")
    private ArrayList<V> addChildren(V vertex, int maxDepth) {
        ArrayList<V> vertices = new ArrayList<V>();
        if (tree.getChildCount(vertex) == 0 && tree.getDepth(vertex) < maxDepth) {
            // no double namings..
            String newChild = String.valueOf(System.nanoTime());
            String newEdge = String.valueOf(System.nanoTime());
            ((DelegateTree<String, String>) tree).addChild(newEdge, (String) vertex, newChild);
            vertices.add((V) newChild);
            vertices.addAll(addChildren((V) newChild, maxDepth));
        }
        
        return vertices;
    }
    
    /**
     * 
     * @param dummies
     *            these are removed from this Tree tree.
     */
    private void removeDummies(ArrayList<V> dummies) {
        
        for (V dummy : dummies)
            // ((DelegateTree<V, E>) tree).removeChild(tree.getParent(dummy));
            tree.removeVertex(dummy);
    }
    
    /**
     * This method manages the layout of a tree concerning the distance two
     * vertices should have.
     * 
     * @param distance
     *            the distance between two vertices
     * @param cellLength
     *            the length of a vertex's cell.
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private VisualizationViewer<String, String> setDistanceAndLayout(int distance, int cellLength) {
        int maxDepth = getMaxDepth(tree);
        
        /*
         * Cheap trick: "fill" the tree with vertices, so every leaf has the
         * same depth. This isn't very unperformant since we need to add
         * O((n²/4)) (worst case.. avg should be lower) new vertices per vertex
         * and it makes the resizing algorithm almost trivial: equidistant
         * vertices on the lowest level and centering parents..
         */
        ArrayList<V> dummies = fillTree(maxDepth);
        
        // visualize tree as a tree
        TreeLayout<V, E> layout = new TreeLayout<V, E>(tree);
        
        ArrayList<V>[] verticesByDepth = new ArrayList[maxDepth + 1];
        // order the vertices by depth
        for (V vertex : tree.getVertices()) {
            int depth = tree.getDepth(vertex);
            if (verticesByDepth[depth] == null)
                verticesByDepth[depth] = new ArrayList<V>();
            verticesByDepth[tree.getDepth(vertex)].add(vertex);
        }
        
        // this is needed in building the VisualizationViewer
        int dummyTreeMaxDepthVertexCount = verticesByDepth[maxDepth].size();
        
        /*
         * the building is based on the last level of vertices This guarantees
         * appropriate distances for the upper levels too! condition: every leaf
         * has the same level
         * 
         */
        // this needs to be done because the vertices can be arranged
        // differently based on their names...
        // if we find a better layout, this becomes deprecated.
        verticesByDepth[maxDepth].sort(new Comparator<V>() {
            @Override
            public int compare(V o1, V o2) {
                return Double.compare(layout.transform(o1).getX(), layout.transform(o2).getX());
            }
        });
        
        int i = 0;
        // relocate all the leafs (which all have the same depth)
        for (V vertex : verticesByDepth[maxDepth]) {
            Point2D prevLocation = layout.transform(vertex);
            layout.setLocation(vertex, new Point2D.Double(cellLength + distance * i, prevLocation.getY()));
            i++;
        }
        /*
         * place parents(and their parents..) in a pleasant position (in the
         * middle of their children) This is done level by level (bottom-1 to
         * top): The level-List is sorted by their position in x-direction
         * (necessary?)
         */
        for (int j = maxDepth - 1; j >= 0; j--) {
            
            // place the parent in the middle of its children (in x-direction)
            for (V vertex : verticesByDepth[j]) {
                // calculate the middle and set the new location.
                Point2D prevLocation = layout.transform(vertex);
                double xMiddle = calculateXMiddle(layout, vertex);
                layout.setLocation(vertex, new Point2D.Double(xMiddle, prevLocation.getY()));
            }
        }
        // Cheap trick, continuation: we need to remove the dummies we added
        // before
        removeDummies(dummies);
        
        // Panel sizing
        int treeWidth = dummyTreeMaxDepthVertexCount * distance + cellLength;
        Dimension dim = new Dimension(treeWidth, maxDepth * levelDistancePx + panelHeightOffset);
        VisualizationViewer vv = new VisualizationViewer(layout, dim);
        return vv;
    }
    
    private double calculateXMiddle(TreeLayout<V, E> layout, V vertex) {
        double xMin = Integer.MAX_VALUE;
        double xMax = Integer.MIN_VALUE;
        for (V child : tree.getChildren(vertex)) {
            double actualX = layout.transform(child).getX();
            if (actualX < xMin)
                xMin = actualX;
            if (actualX > xMax)
                xMax = actualX;
        }
        return xMin + (xMax - xMin) / 2;
    }
    
    private int getMaxDepth(Tree<V, E> t) {
        int maxDepth = 0;
        for (V vertex : t.getVertices())
            if (maxDepth < t.getDepth(vertex))
                maxDepth = t.getDepth(vertex);
        return maxDepth;
    }
    
    @SuppressWarnings("unchecked")
    public void markChangedAndNew(VisualizationViewer<String, String> vv, Tree<V, E> oldTree, Tree<V, E> newTree) {
        if (oldTree == null)
            return;
        int oldTreeMaxDepth = getMaxDepth(oldTree);
        int newTreeMaxDepth = getMaxDepth(newTree);
        ArrayList<V>[] oldTreeVerticesByDepth = new ArrayList[oldTreeMaxDepth + 1];
        ArrayList<V>[] newTreeVerticesByDepth = new ArrayList[newTreeMaxDepth + 1];
        
        // fill lists
        fillTreeVerticesListArrayByDepth(oldTree, oldTreeVerticesByDepth);
        fillTreeVerticesListArrayByDepth(newTree, newTreeVerticesByDepth);
        
        ArrayList<String>[] changeIndexes = new ArrayList[newTreeMaxDepth + 1];
        // fill the indexes with "not changed", similar to ListVisualizer
        for (int i = 0; i < newTreeVerticesByDepth.length; i++) {
            for (int j = 0; j < newTreeVerticesByDepth[i].size(); j++) {
                if (changeIndexes[i] == null)
                    changeIndexes[i] = new ArrayList<String>();
                changeIndexes[i].add(StateOfCell.NOTCHANGED.getName());
            }
        }
        /*
         * This tries to tell new from changed vertices but it can't. Maybe
         * someone finds a solution, but from my point of view: changed vertices
         * are also new..
         * 
         * if (oldTree != null) { for (int i = 0; i <
         * newTreeVerticesByDepth.length; i++) {
         * 
         * if (i < oldTreeVerticesByDepth.length) { //make sure this level
         * exists in both for (int j = 0; j < newTreeVerticesByDepth[i].size();
         * j++) { if (oldTreeVerticesByDepth[i].size() > j &&
         * !newTreeVerticesByDepth[i].get(j).equals(oldTreeVerticesByDepth[i].
         * get(j))) { changeIndexes[i].set(j,
         * StateOfCell.CHANGED.getStateOfCell()); } else if
         * (oldTreeVerticesByDepth[i].size() <= j) { changeIndexes[i].set(j,
         * StateOfCell.NEW.getStateOfCell());
         * System.out.println("NEW gooten here"); } } } else { for (int j = 0; j
         * < newTreeVerticesByDepth[i].size(); j++) { changeIndexes[i].set(j,
         * StateOfCell.NEW.getStateOfCell()); } }
         * 
         * } }
         */
        // put the change indexes in a hashmap to make the transformer able to
        // find it..
        HashMap<String, String> changeMap = new HashMap<String, String>();
        for (int i = 0; i < newTreeVerticesByDepth.length; i++) {
            for (int j = 0; j < newTreeVerticesByDepth[i].size(); j++) {
                changeMap.put(newTreeVerticesByDepth[i].get(j).toString(), changeIndexes[i].get(j));
            }
        }
        
        Collection<V> oldTreeVertices = oldTree.getVertices();
        for (V vertex : newTree.getVertices()) {
            if (!oldTreeVertices.contains(vertex)) {
                // either new or changed,
                changeMap.put(vertex.toString(), StateOfCell.NEW.getName());
            }
        }
        
        Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
            
            @Override
            public Paint transform(String arg0) {
                if (changeMap.get(arg0).equals(StateOfCell.CHANGED.getName())) {
                    // just implemented if anyone wants to add a distincition
                    // between changed and new..
                    return new Color(255, 127, 127);
                } else if (changeMap.get(arg0).equals(StateOfCell.NEW.getName())) {
                    return new Color(127, 255, 127);
                } else {
                    return new Color(255, 255, 255);
                }
            }
            
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
    }
    
    private void fillTreeVerticesListArrayByDepth(Tree<V, E> t, ArrayList<V>[] verticesByDepth) {
        for (V vertex : t.getVertices()) {
            int depth = t.getDepth(vertex);
            if (verticesByDepth[depth] == null)
                verticesByDepth[depth] = new ArrayList<V>();
            verticesByDepth[t.getDepth(vertex)].add(vertex);
        }
    }
}

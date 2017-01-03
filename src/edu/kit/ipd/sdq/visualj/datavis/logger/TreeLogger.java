package edu.kit.ipd.sdq.visualj.datavis.logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Stack;

import edu.kit.ipd.sdq.visualj.datavis.visualizer.ImplicitHeapVisualizer;
import edu.kit.ipd.sdq.visualj.datavis.visualizer.TreeVisualizer;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;

/**
 * A logger for trees, i.e. objects of classes that implement the {@link Tree}
 * interface from the Jung library.
 * 
 * <p>
 * If you want to visualize an array as a implicit binary heap, use an
 * {@link ArrayLogger} with an {@link ImplicitHeapVisualizer} instead.
 * </p>
 *
 * @param <V>
 *            the type of the vertices in the tree.
 * @param <E>
 *            the type of the edges in the tree.
 */
public class TreeLogger<V, E> extends Logger<Tree<V, E>> {
    
    /**
     * Create a new {@code TreeLogger}.
     * 
     * @param arr
     *            the initial value of the tree to be logged.
     */
    public TreeLogger(Tree<V, E> tree) {
        update(tree);
    }
    
    @Override
    public TreeVisualizer<V, E> createDefaultVisualizer() {
        return new TreeVisualizer<>(this);
    }
    
    @Override
    protected Tree<V, E> cloneValue(Tree<V, E> t) {
        if (t == null)
            return null;
        DelegateTree<V, E> result = new DelegateTree<>();
        
        // iterative DFS
        // init visited map
        HashMap<V, Boolean> visited = new HashMap<V, Boolean>();
        for (V vertex : t.getVertices()) {
            visited.put(vertex, false);
        }
        
        Stack<V> stack = new Stack<V>();
        visited.put(t.getRoot(), true);
        stack.push(t.getRoot());
        result.setRoot(t.getRoot());
        
        while (!stack.isEmpty()) {
            if (allChildrenVisited(stack.peek(), t, visited)) {
                stack.pop();
            } else {
                V parent = stack.peek();
                V child = getFirstUnvisitedChild(parent, t, visited);
                E edge = t.findEdge(parent, child);
                // do the cloning
                try {
                    // reflection cloning
                    Method vertexCloneMethod = parent.getClass().getMethod("clone");
                    
                    @SuppressWarnings("unchecked")
                    V newParent = (V) vertexCloneMethod.invoke(parent);
                    @SuppressWarnings("unchecked")
                    V newChild = (V) vertexCloneMethod.invoke(child);
                    
                    Method edgeCloneMethod = edge.getClass().getMethod("clone");
                    
                    @SuppressWarnings("unchecked")
                    E newEdge = (E) edgeCloneMethod.invoke(edge);
                    result.addChild(newEdge, newParent, newChild);
                } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
                    // no cloning
                    result.addChild(edge, parent, child);
                }
                visited.put(child, true);
                stack.push(child);
            }
        }
        return result;
    }
    
    private boolean allChildrenVisited(V vertex, Tree<V, E> t, HashMap<V, Boolean> visited) {
        for (V child : t.getChildren(vertex)) {
            if (!visited.get(child))
                return false;
        }
        return true;
    }
    
    private V getFirstUnvisitedChild(V vertex, Tree<V, E> t, HashMap<V, Boolean> visited) {
        for (V child : t.getChildren(vertex)) {
            if (!visited.get(child))
                return child;
        }
        // invariant in DFS: we can't get here..
        return null;
    }
}

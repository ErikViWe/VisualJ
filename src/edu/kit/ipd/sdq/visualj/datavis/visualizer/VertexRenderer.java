package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * A class to render vertices within the JUNG-Library context.
 */
class VertexRenderer {
    
    private static final Font font = new Font("Dialog", Font.CENTER_BASELINE, 15);
    private static final Color green = new Color(127, 255, 127);
    private static final Color red = new Color(255, 127, 127);
    private static final Color white = new Color(255, 255, 255);
    private static final int CellHeight = 40;
    
    protected VertexRenderer() {
        
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void renderVertexEllipse(VisualizationViewer vv, int cellLength) {
        // edit the size of the cells (vertices), their color, the name and the
        // font
        
        // create a vertex shape from the given cellLength
        Transformer<String, Shape> vertexSize = new Transformer<String, Shape>() {
            
            @Override
            public Shape transform(String i) {
                Ellipse2D circle
                        = new Ellipse2D.Double(-(cellLength/2), -(CellHeight / 2), cellLength, CellHeight);
                return circle;
            }
        };
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        
        // set standard color: white
        Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
            
            @Override
            public Paint transform(String i) {
                
                return white;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
        Transformer<String, String> name = new Transformer<String, String>() {
            
            @Override
            public String transform(String arg0) {
                return arg0.substring(arg0.indexOf('#') + 1);
            }
        };
        vv.getRenderContext().setVertexLabelTransformer(name);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        vv.getRenderContext().setVertexFontTransformer(new Transformer<String, Font>() {
            
            @Override
            public Font transform(String authorNode) {
                return font;
            }
        });
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void renderVertexListRectangle(VisualizationViewer vv, int cellLength, String[] changeIndexes) {
        // edit the size and shape of the cells (vertices)
        Transformer<String, Shape> vertexShape = new Transformer<String, Shape>() {
            
            @Override
            public Shape transform(String i) {
                Rectangle2D circle = new Rectangle2D.Double(0, -1 * (CellHeight / 2), cellLength, CellHeight);
                return circle;
            }
        };
        vv.getRenderContext().setVertexShapeTransformer(vertexShape);
        
        // edit shape of edges
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String, String>());
        
        // add color to the cells (vertices)
        Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
            
            @Override
            public Paint transform(String arg0) {
                if (changeIndexes[Integer.parseInt(arg0.substring(0, arg0.indexOf('#')))]
                        .equals(StateOfCell.CHANGED.getName())) {
                    return red;
                } else if (changeIndexes[Integer.parseInt(arg0.substring(0, arg0.indexOf('#')))]
                        .equals(StateOfCell.NEW.getName())) {
                    return green;
                } else {
                    return white;
                }
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
        // display the content of the cells as name of vertices
        Transformer<String, String> name = new Transformer<String, String>() {
            @Override
            public String transform(String arg0) {
                return arg0.substring(arg0.indexOf('#') + 1);
            }
        };
        vv.getRenderContext().setVertexLabelTransformer(name);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        // edit font
        vv.getRenderContext().setVertexFontTransformer(new Transformer<String, Font>() {
            
            @Override
            public Font transform(String arg0) {
                return font;
            }
        });
        vv.setForeground(Color.BLACK);
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void renderVertexArrayRectangle(VisualizationViewer vv, int cellLength, String[] changeIndexes) {
        // edit the size and shape of the cells (vertices)
        Transformer<String, Shape> vertexShape = new Transformer<String, Shape>() {
            
            @Override
            public Shape transform(String arg0) {
                if (arg0.contains("#")) {
                    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, cellLength, CellHeight);
                    return rectangle;
                } else {
                    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 0, 0);
                    return rectangle;
                }
            }
        };
        vv.getRenderContext().setVertexShapeTransformer(vertexShape);
        
        // add color to the cells (vertices)
        Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
            
            @Override
            public Paint transform(String arg0) {
                if (arg0.contains("#")) {
                    if (changeIndexes[Integer.parseInt(arg0.split("#", 2)[0])].equals(StateOfCell.CHANGED.getName())) {
                        return red;
                    } else if (changeIndexes[Integer.parseInt(arg0.split("#", 2)[0])]
                            .equals(StateOfCell.NEW.getName())) {
                        return green;
                    } else {
                        return white;
                    }
                }
                return vv.getBackground();
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
        // display the content of the cells as name of vertices
        Transformer<String, String> name = new Transformer<String, String>() {
            
            @Override
            public String transform(String arg0) {
                if (arg0.contains("#")) {
                    return arg0.split("#", 2)[1];
                } else {
                    return arg0;
                }
            }
        };
        vv.getRenderContext().setVertexLabelTransformer(name);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        // edit font
        vv.getRenderContext().setVertexFontTransformer(new Transformer<String, Font>() {
            
            @Override
            public Font transform(String arg0) {
                return font;
            }
        });
        vv.setForeground(Color.BLACK);
    }
}

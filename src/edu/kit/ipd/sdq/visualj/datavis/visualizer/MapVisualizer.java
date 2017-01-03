package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.collections15.ComparatorUtils;
import org.apache.commons.lang3.math.NumberUtils;

import edu.kit.ipd.sdq.visualj.datavis.logger.MapLogger;

/**
 * A visualizer for {@link MapLogger}s.
 *
 * @param <K>
 *            the map's key type.
 * @param <V>
 *            the map's value type
 */
public class MapVisualizer<K, V> extends Visualizer {
    
    private static final long serialVersionUID = 3789357275301038005L;
    
    private MapLogger<K, V> logger;
    private JTable oldTable;
    private JTable oldTableSave;
    
    /**
     * Creates a new {@code MapVisualizer}.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     * @param title
     *            the title of this visualizer. If the title is {@code null},
     *            the title field is hidden.
     */
    public MapVisualizer(MapLogger<K, V> logger, String title) {
        this.logger = logger;
        this.setTitle(title);
        this.setLayout(new BorderLayout());
    }
    
    /**
     * Creates a new {@code MapVisualizer} without a title.
     * 
     * @param logger
     *            the logger belonging to this visualizer.
     */
    public MapVisualizer(MapLogger<K, V> logger) {
        this(logger, null);
        this.setLayout(new BorderLayout());
    }
    
    @Override
    public JComponent getWrapper() {
        return this;
    }
    
    @Override
    protected void update(int breakpointId) {
        Map<K, V> currentMap = logger.getValue(breakpointId);
        if (currentMap == null) {
            this.showNull();
            return;
        }
        Object[][] values = new Object[currentMap.size()][2];
        Object[] keyArr = currentMap.keySet().toArray();
        Object[] valueArr = currentMap.values().toArray();
        
        for (int i = 0; i < currentMap.size(); i++) {
            values[i][0] = keyArr[i];
            values[i][1] = valueArr[i];
        }
        String[] header = {"Key", "Value"};
        JTable table = new JTable(values, header);
        this.removeAll();
        
        // add sorting option
        TableModel model = table.getModel();
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
        sorter.setModel(model);
        table.setRowSorter(sorter);
        // numerical comparator
        Comparator<Object> numberComp = new Comparator<Object>() {
            
            @Override
            public int compare(Object arg0, Object arg1) {
                Double d = (NumberUtils.toDouble(arg0.toString()));
                return d.compareTo(NumberUtils.toDouble(arg1.toString()));
            }
            
        };
        
        // check if values and keys are numbers
        boolean valueIsNumber = true;
        boolean keyIsNumber = true;
        for (int i = 0; i < valueArr.length; i++) {
            if (!NumberUtils.isNumber(valueArr[i].toString()))
                valueIsNumber = false;
            if (!NumberUtils.isNumber(keyArr[i].toString()))
                keyIsNumber = false;
        }
        // set comparators
        if (keyIsNumber) {
            sorter.setComparator(0, numberComp);
        } else {
            sorter.setComparator(0, ComparatorUtils.naturalComparator());
        }
        
        if (valueIsNumber) {
            sorter.setComparator(1, numberComp);
        } else {
            sorter.setComparator(1, ComparatorUtils.naturalComparator());
        }
        
        setCellColor(oldTable, table);
        // save the old Table for the mouseListener!
        this.oldTableSave = oldTable;
        // table.getTableHeader().getMouseListeners()[0].
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    // don't need to do anything if interrupted..
                }
                setCellColor(oldTableSave, table);
            }
            
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        this.oldTable = table;
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setCellColor(JTable oldTable, JTable newTable) {
        if (oldTable != null) {
            
            String[][] changedArrArr = getChanged(oldTable, newTable);
            newTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                
                private static final long serialVersionUID = 4473010082033987167L;
                
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    final Component c
                            = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    String status = changedArrArr[column][row];
                    if (status.equals(StateOfCell.NOTCHANGED.getName())) {
                        c.setBackground(new Color(255, 255, 255));
                    } else if (status.equals(StateOfCell.CHANGED.getName())) {
                        c.setBackground(new Color(255, 127, 127));
                    } else { // new
                        c.setBackground(new Color(127, 255, 127));
                    }
                    return c;
                }
            });
        }
    }
    
    private String[][] getChanged(JTable oldTable, JTable newTable) {
        
        HashMap<Object, Object> containedMap = new HashMap<Object, Object>();
        for (int i = 0; i < oldTable.getRowCount(); i++) {
            // 0 for key, 1 for value
            containedMap.put(oldTable.getValueAt(i, 0), oldTable.getValueAt(i, 1));
        }
        
        String[][] changedArrArr = new String[newTable.getColumnCount()][newTable.getRowCount()];
        for (int j = 0; j < newTable.getRowCount(); j++) {
            Object actualKey = newTable.getValueAt(j, 0);
            Object actualValue = newTable.getValueAt(j, 1);
            if (!containedMap.containsKey(actualKey)) {
                // new cell
                changedArrArr[0][j] = StateOfCell.NEW.getName();
                changedArrArr[1][j] = StateOfCell.NEW.getName();
            } else if (containedMap.containsKey(actualKey) && !containedMap.get(actualKey).equals(actualValue)) {
                // changed value
                // keys cannot be changed, only deleted with their values..
                changedArrArr[0][j] = StateOfCell.NOTCHANGED.getName();
                changedArrArr[1][j] = StateOfCell.CHANGED.getName();
            } else {
                // unchanged
                changedArrArr[0][j] = StateOfCell.NOTCHANGED.getName();
                changedArrArr[1][j] = StateOfCell.NOTCHANGED.getName();
            }
        }
        return changedArrArr;
    }
    
}

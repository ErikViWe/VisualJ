package edu.kit.ipd.sdq.visualj.efficiency.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYDataset;

import com.sun.glass.events.KeyEvent;

import edu.kit.ipd.sdq.visualj.efficiency.measure.EfficiencyTest;
import edu.kit.ipd.sdq.visualj.efficiency.measure.StepCounter;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun;
import edu.kit.ipd.sdq.visualj.efficiency.measure.TestRun.Result;
import edu.kit.ipd.sdq.visualj.util.DialogUtils;
import edu.kit.ipd.sdq.visualj.util.Messages;
import edu.kit.ipd.sdq.visualj.util.Pair;

/**
 * A window to visualize the results of a call to {@link EfficiencyTest#run()}.
 */
public class EfficiencyResultWindow {
    
    private static final Pair<ChartType, DataType> BR = new Pair<>(ChartType.BAR_CHART, DataType.RUNTIME);
    private static final Pair<ChartType, DataType> BS = new Pair<>(ChartType.BAR_CHART, DataType.STEPS);
    private static final Pair<ChartType, DataType> BM = new Pair<>(ChartType.BAR_CHART, DataType.MEMORY);
    private static final Pair<ChartType, DataType> GR = new Pair<>(ChartType.GRAPH, DataType.RUNTIME);
    private static final Pair<ChartType, DataType> GS = new Pair<>(ChartType.GRAPH, DataType.STEPS);
    private static final Pair<ChartType, DataType> GM = new Pair<>(ChartType.GRAPH, DataType.MEMORY);
    
    private static final String EXPORT_FILE_EXTENSION = "vjr";
    private static final String EXPORT_FILE_DESCRIPTION = "VisualJ Test Result";
    
    private final String measurementMsg = Messages.getString("EfficiencyResultWindow.measurement");
    private final String fileMsg = Messages.getString("EfficiencyResultWindow.file");
    private final String exitMsg = Messages.getString("EfficiencyResultWindow.exit");
    private final String runtimeMsg = Messages.getString("EfficiencyResultWindow.runtime");
    private final String stepsMsg = Messages.getString("EfficiencyResultWindow.steps");
    private final String memoryMsg = Messages.getString("EfficiencyResultWindow.memory");
    private final String parametersMsg = Messages.getString("EfficiencyResultWindow.parameters");
    private final String nsMsg = Messages.getString("EfficiencyResultWindow.ns");
    private final String bytesMsg = Messages.getString("EfficiencyResultWindow.bytes");
    private final String addMsg = Messages.getString("EfficiencyResultWindow.add");
    private final String saveMsg = Messages.getString("EfficiencyResultWindow.save");
    private final String removeMsg = Messages.getString("EfficiencyResultWindow.remove");
    private final String saveBeforeMsg = Messages.getString("EfficiencyResultWindow.saveBefore");
    
    private final DefaultCategoryDataset runtimeCategoryDataset = new DefaultCategoryDataset();
    private final DefaultCategoryDataset stepsCategoryDataset = new DefaultCategoryDataset();
    private final DefaultCategoryDataset memoryCategoryDataset = new DefaultCategoryDataset();
    private final DefaultXYDataset runtimeXYDataset = new DefaultXYDataset();
    private final DefaultXYDataset stepsXYDataset = new DefaultXYDataset();
    private final DefaultXYDataset memoryXYDataset = new DefaultXYDataset();
    
    private JFrame frame = new JFrame();
    
    private JComboBox<ChartType> chartTypeBox = new JComboBox<>(ChartType.values());
    private JComboBox<DataType> dataTypeBox = new JComboBox<>(DataType.values());
    
    private JMenuItem addResultsMenu = new JMenuItem(addMsg);
    private JMenu saveResultsMenu = new JMenu(saveMsg);
    private JMenu removeResultsMenu = new JMenu(removeMsg);
    
    private ActionListener saveResultsListener = (ActionEvent e) -> {
        JMenuItem source = (JMenuItem) e.getSource();
        saveResults(source.getText());
    };
    
    private ActionListener removeResultsListener = (ActionEvent e) -> {
        JMenuItem source = (JMenuItem) e.getSource();
        String method = source.getText();
        
        int input = JOptionPane.showConfirmDialog(frame, saveBeforeMsg, "", JOptionPane.YES_NO_OPTION);
        
        if (input == JOptionPane.YES_OPTION) {
            saveResults(method);
        }
        
        removeResults(method);
    };
    
    private ChartType chartTypeShown = ChartType.BAR_CHART;
    private DataType dataTypeShown = DataType.RUNTIME;
    
    private Map<Pair<ChartType, DataType>, Dataset> datasets = new HashMap<>();
    private Map<Pair<ChartType, DataType>, JFreeChart> charts = new HashMap<>();
    
    private ChartPanel chartPanel;
    
    private Map<String, TestRun.Result[]> resultsMap = new HashMap<>();
    private Map<String, JMenuItem> saveMenuItems = new HashMap<>();
    private Map<String, JMenuItem> removeMenuItems = new HashMap<>();
    
    /**
     * Creates a new {@code EfficiencyResultWindow} for visualizing the results
     * of just one {@link EfficiencyTest}.
     * 
     * @param results
     *            the results to visualize
     */
    public EfficiencyResultWindow(TestRun.Result[] results) {
        List<TestRun.Result[]> resultsList = new LinkedList<>();
        resultsList.add(results);
        init(resultsList);
    }
    
    /**
     * Creates a new {@code EfficiencyResultWindow} for visualizing the results
     * of multiple {@link EfficiencyTest}s.
     * 
     * @param resultsList
     *            a list containting the results of each {@link EfficiencyTest}.
     */
    public EfficiencyResultWindow(List<TestRun.Result[]> resultsList) {
        init(resultsList);
    }
    
    private void init(List<TestRun.Result[]> resultsList) {
        /* Initialize datasets and charts */
        
        datasets.put(BR, runtimeCategoryDataset);
        datasets.put(BS, stepsCategoryDataset);
        datasets.put(BM, memoryCategoryDataset);
        datasets.put(GR, runtimeXYDataset);
        datasets.put(GS, stepsXYDataset);
        datasets.put(GM, memoryXYDataset);
        
        charts.put(BR, ChartFactory.createBarChart(runtimeMsg, parametersMsg, nsMsg, runtimeCategoryDataset));
        charts.put(BS, ChartFactory.createBarChart(stepsMsg, parametersMsg, stepsMsg, stepsCategoryDataset));
        charts.put(BM, ChartFactory.createBarChart(memoryMsg, parametersMsg, bytesMsg, memoryCategoryDataset));
        charts.put(GR, ChartFactory.createScatterPlot(runtimeMsg, parametersMsg, nsMsg, runtimeXYDataset));
        charts.put(GS, ChartFactory.createScatterPlot(stepsMsg, parametersMsg, stepsMsg, stepsXYDataset));
        charts.put(GM, ChartFactory.createScatterPlot(memoryMsg, parametersMsg, bytesMsg, memoryXYDataset));
        
        Deque<String> methodNames = new LinkedList<>();
        
        for (TestRun.Result[] results : resultsList) {
            final String methodName = results.length == 0 ? "" : results[0].getMethod();
            methodNames.add(methodName);
            
            addResults(results);
        }
        
        /* Set chart style */
        
        for (Entry<Pair<ChartType, DataType>, JFreeChart> entry : charts.entrySet()) {
            if (entry.getKey().getFirst().equals(ChartType.GRAPH)) {
                XYPlot plot = entry.getValue().getXYPlot();
                NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
                NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
                
                // Set axis range and units.
                xAxis.setTickUnit(new NumberTickUnit(1.0, NumberFormat.getIntegerInstance(), 0), true, true);
                yAxis.setRangeType(RangeType.POSITIVE);
                
                // Draw lines between data points.
                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                for (int i = 0; i < plot.getDataset(0).getSeriesCount(); ++i) {
                    renderer.setSeriesLinesVisible(i, true);
                }
                plot.setRenderer(renderer);
            } else if (entry.getKey().getFirst().equals(ChartType.BAR_CHART)) {
                // Make the bars in the bar charts look nicer.
                CategoryPlot plot = entry.getValue().getCategoryPlot();
                BarRenderer renderer = (BarRenderer) plot.getRenderer();
                renderer.setBarPainter(new StandardBarPainter());
            }
        }
        
        /* Initialize frame */
        
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width / 4 * 3, screenSize.height / 4 * 3);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setTitle(measurementMsg);
        
        chartPanel = new ChartPanel(charts.get(BR));
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        
        /* Initialize controls */
        
        chartTypeBox.addActionListener((ActionEvent e) -> {
            @SuppressWarnings("unchecked")
            JComboBox<ChartType> source = (JComboBox<ChartType>) e.getSource();
            chartTypeShown = (ChartType) source.getSelectedItem();
            update();
        });
        
        dataTypeBox.addActionListener((ActionEvent e) -> {
            @SuppressWarnings("unchecked")
            JComboBox<DataType> source = (JComboBox<DataType>) e.getSource();
            dataTypeShown = (DataType) source.getSelectedItem();
            update();
        });
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(fileMsg);
        menuBar.add(menu);
        menu.add(addResultsMenu);
        menu.add(saveResultsMenu);
        menu.add(removeResultsMenu);
        menu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem(exitMsg);
        menu.add(exitMenuItem);
        frame.setJMenuBar(menuBar);
        
        menu.setMnemonic(KeyEvent.VK_F);
        addResultsMenu.setMnemonic(KeyEvent.VK_L);
        saveResultsMenu.setMnemonic(KeyEvent.VK_S);
        removeResultsMenu.setMnemonic(KeyEvent.VK_R);
        exitMenuItem.setMnemonic(KeyEvent.VK_Q);
        
        exitMenuItem.addActionListener((ActionEvent e) -> {
            setVisible(false);
            frame.dispose();
        });
        
        addResultsMenu.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(EXPORT_FILE_DESCRIPTION, EXPORT_FILE_EXTENSION));
            fileChooser.showOpenDialog(frame);
            File file = fileChooser.getSelectedFile();
            
            if (file != null) {
                try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));) {
                    TestRun.Result[] results = (Result[]) stream.readObject();
                    addResults(results);
                } catch (Exception ex) {
                    DialogUtils.showExceptionDialog(frame, ex,
                            Messages.getString("EfficiencyResultWindow.IOErrorTitle"),
                            Messages.getString("EfficiencyResultWindow.IErrorInfo"));
                }
            }
        });
        
        final Dimension buttonPanelSize = new Dimension(frame.getWidth(), frame.getHeight() / 10);
        final JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(chartTypeBox);
        buttonPanel.add(dataTypeBox);
        buttonPanel.setPreferredSize(buttonPanelSize);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        showBarChart();
        showRuntime();
    }
    
    /**
     * Shows or hides this {@code EfficiencyResultWindow} depending on the value
     * of parameter {@code visible}.
     * 
     * @param visible
     * 
     * @see #isVisible()
     * @see JFrame#setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
    
    /**
     * @return {@code true} if the window is visible, {@code false} otherwise.
     * 
     * @see #setVisible(boolean)
     * @see JFrame#isVisible()
     */
    public boolean isVisible() {
        return frame.isVisible();
    }
    
    /**
     * Adds the specified window listener to receive window events from this
     * window.
     *
     * @param l
     *            the window listener
     * @see #removeWindowListener(WindowListener)
     * @see JFrame#addWindowListener(WindowListener)
     */
    public synchronized void addWindowListener(WindowListener l) {
        frame.addWindowListener(l);
    }
    
    /**
     * Removes the specified window listener so that it no longer receives
     * window events from this window.
     *
     * @param l
     *            the window listener
     * @see #addWindowListener(WindowListener)
     * @see JFrame#removeWindowListener(WindowListener)
     */
    public synchronized void removeWindowListener(WindowListener l) {
        frame.removeWindowListener(l);
    }
    
    /**
     * Update the window to show {@link #chartTypeShown} and
     * {@link #dataTypeShown}.
     */
    private void update() {
        chartPanel.setChart(charts.get(new Pair<>(chartTypeShown, dataTypeShown)));
        
        chartTypeBox.setSelectedIndex(ArrayUtils.indexOf(ChartType.values(), chartTypeShown));
        dataTypeBox.setSelectedIndex(ArrayUtils.indexOf(DataType.values(), dataTypeShown));
        
        frame.validate();
        frame.repaint();
    }
    
    /**
     * Adds results every dataset.
     * 
     * <p>
     * If the datasets already contain the results, this method has no effect.
     * </p>
     * 
     * @param results
     *            the results to add.
     */
    private void addResults(TestRun.Result[] results) {
        final String method = results.length == 0 ? "" : results[0].getMethod();
        
        if (resultsMap.containsKey(method)) {
            return;
        }
        
        double[][] runtimesSeries = new double[2][];
        runtimesSeries[0] = new double[results.length];
        runtimesSeries[1] = new double[results.length];
        
        double[][] stepsSeries = new double[2][];
        stepsSeries[0] = new double[results.length];
        stepsSeries[1] = new double[results.length];
        
        double[][] memorySeries = new double[2][];
        memorySeries[0] = new double[results.length];
        memorySeries[1] = new double[results.length];
        
        for (int i = 0; i < results.length; ++i) {
            runtimeCategoryDataset.addValue(results[i].getMedianRuntime(), method, Integer.valueOf(i));
            runtimesSeries[0][i] = i;
            runtimesSeries[1][i] = results[i].getMedianRuntime();
            
            stepsCategoryDataset.addValue(results[i].getMedianSteps(), method, Integer.valueOf(i));
            stepsSeries[0][i] = i;
            stepsSeries[1][i] = results[i].getMedianSteps();
            
            memoryCategoryDataset.addValue(results[i].getMedianMemoryFootprint(), method, Integer.valueOf(i));
            memorySeries[0][i] = i;
            memorySeries[1][i] = results[i].getMedianMemoryFootprint();
        }
        
        runtimeXYDataset.addSeries(method, runtimesSeries);
        stepsXYDataset.addSeries(method, stepsSeries);
        memoryXYDataset.addSeries(method, memorySeries);
        
        JMenuItem saveItem = new JMenuItem(method);
        saveMenuItems.put(method, saveItem);
        saveResultsMenu.add(saveItem);
        saveItem.addActionListener(saveResultsListener);
        JMenuItem removeItem = new JMenuItem(method);
        removeMenuItems.put(method, removeItem);
        removeResultsMenu.add(removeItem);
        removeItem.addActionListener(removeResultsListener);
        resultsMap.put(method, results);
    }
    
    /**
     * Removes results from every dataset.
     * 
     * <p>
     * If the datasets do not contain the results, this method has no effect.
     * </p>
     * 
     * @param method
     *            the method whose results to remove.
     */
    private void removeResults(String method) {
        for (Dataset dataset : datasets.values()) {
            if (dataset instanceof DefaultCategoryDataset) {
                DefaultCategoryDataset dcd = (DefaultCategoryDataset) dataset;
                dcd.removeRow(method);
            } else if (dataset instanceof DefaultXYDataset) {
                DefaultXYDataset dxd = (DefaultXYDataset) dataset;
                dxd.removeSeries(method);
            }
        }
        
        saveResultsMenu.remove(saveMenuItems.get(method));
        removeResultsMenu.remove(removeMenuItems.get(method));
        resultsMap.remove(method);
    }
    
    /**
     * Opens a {@link JFileChooser} and saves results to a file.
     * 
     * @param method
     *            the method whose results to save.
     */
    private void saveResults(String method) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(EXPORT_FILE_DESCRIPTION, EXPORT_FILE_EXTENSION));
        fileChooser.setSelectedFile(new File("results.vjr"));
        fileChooser.showSaveDialog(frame);
        File file = fileChooser.getSelectedFile();
        
        if (file != null) {
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));) {
                stream.writeObject(resultsMap.get(method));
            } catch (Exception ex) {
                DialogUtils.showExceptionDialog(frame, ex, Messages.getString("EfficiencyResultWindow.IOErrorTitle"),
                        Messages.getString("EfficiencyResultWindow.OErrorInfo"));
            }
        }
    }
    
    /**
     * Shows a bar chart (one bar per parameter tuple).
     * 
     * @see #showGraph()
     */
    public void showBarChart() {
        chartTypeShown = ChartType.BAR_CHART;
        update();
    }
    
    /**
     * Shows approximated graph with the parameter tuples on the x- and the time
     * on the y-axis.
     * 
     * @see #showBarChart()
     */
    public void showGraph() {
        chartTypeShown = ChartType.GRAPH;
        update();
    }
    
    /**
     * Shows the runtime in calls to {@link StepCounter#step()}.
     * 
     * @see #showRuntime()
     * @see #showMemory()
     */
    public void showSteps() {
        dataTypeShown = DataType.STEPS;
        update();
    }
    
    /**
     * Shows the runtime in nanoseconds.
     * 
     * @see #showSteps()
     * @see #showMemory()
     */
    public void showRuntime() {
        dataTypeShown = DataType.RUNTIME;
        update();
    }
    
    /**
     * Shows the memory usage in bytes.
     * 
     * @see #showSteps()
     * @see #showRuntime()
     */
    public void showMemory() {
        dataTypeShown = DataType.MEMORY;
        update();
    }
    
    private static enum ChartType {
        BAR_CHART("barChart"), GRAPH("graph");
        
        private String subkey;
        
        private ChartType(String subkey) {
            this.subkey = subkey;
        }
        
        @Override
        public String toString() {
            return Messages.getString("EfficiencyResultWindow." + subkey);
        }
    }
    
    private static enum DataType {
        RUNTIME("runtime"), STEPS("steps"), MEMORY("memory");
        
        private String subkey;
        
        private DataType(String subkey) {
            this.subkey = subkey;
        }
        
        @Override
        public String toString() {
            return Messages.getString("EfficiencyResultWindow." + subkey);
        }
    }
}

package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import edu.kit.ipd.sdq.visualj.datavis.logger.Breakpoints;
import edu.kit.ipd.sdq.visualj.datavis.logger.LogManager;
import edu.kit.ipd.sdq.visualj.datavis.logger.Logger;
import edu.kit.ipd.sdq.visualj.util.BreakpointObserver;
import edu.kit.ipd.sdq.visualj.util.ThreadStatus;
import net.miginfocom.swing.MigLayout;

/**
 * A window that contains arbitrarily many {@link Visualizer}s.
 * 
 * There is also exactly one main window ({@link #getMainWindow()} that contains
 * the {@link ControlPanel}.
 */
public final class Window extends JFrame {
    
    private static final long serialVersionUID = 7791761916684430801L;
    
    private static final Set<Window> windows = new HashSet<Window>();
    
    private static ControlPanel controlPanel;
    private static JSeparator controlPanelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
    
    /**
     * The main window which contains the controls and, by default, all
     * visualizers.
     */
    private static Window mainWindow;
    
    private JPanel visualizerContainer;
    private JPanel controlPanelContainer;
    
    private Set<Visualizer> visualizers = new HashSet<>();
    
    /**
     * Creates a new window.
     * 
     * <p>
     * If no windows was previously created, or {@link Breakpoints#reset()} was called, this window becomes
     * the main window ({@link #getMainWindow()}.
     * </p>
     */
    public Window() {
        Window.windows.add(this);
        this.setTitle("VisualJ");
        
        initGUI();
        
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Window.windows.remove(Window.this);
                if (Window.this.isMainWindow()) {
                    Window.setMainWindow(null);
                }
                
                Window.this.setVisible(false);
                Window.this.dispose();
            }
        });
        
        if (controlPanel == null) {
            // Init control panel here and not in the static part to ensure that the LogManager and its BreakpointViewer
            // instance already exist. In addition the controlPanel is only needed if there is at least one window.
            
            ThreadStatus threadStatus;
            if (LogManager.getInstance().getThreadStatus() != null) {
                threadStatus = LogManager.getInstance().getThreadStatus();
            } else {
                threadStatus = new ThreadStatus(Thread.currentThread());
            }
            controlPanel = new ControlPanel(LogManager.getInstance().getBreakpointViewer(), threadStatus);
            
            LogManager.getInstance().getBreakpointViewer().addBreakpointObserver(controlPanel);
            
            controlPanel.addBreakpointObserver(new BreakpointObserver() {
                @Override
                public void update(int breakpointId) {
                    SwingUtilities.invokeLater(() -> {
                        Window.updateWindows(breakpointId);
                    });
                }
            });
        }
        
        if (getMainWindow() == null) {
            this.makeMainWindow();
        }
        
        pack();
    }
    
    private void initGUI() {
        this.getContentPane().setLayout(new MigLayout("flowy,insets 0,hidemode 3,gap 0", "[grow,fill]"));
        {
            visualizerContainer = new JPanel(new MigLayout("flowy", "[grow,fill]"));
            visualizerContainer.setVisible(false);
            this.getContentPane().add(visualizerContainer, "pushy,growy");
            
            controlPanelContainer = new JPanel(new MigLayout("flowy,hidemode 3", "[grow,fill]"));
            controlPanelContainer.setVisible(false);
            this.getContentPane().add(controlPanelContainer);
        }
    }
    
    /**
     * Updates every visualizer in every window to show the state at the
     * breakpoint with id {@code breakpointId}.
     * 
     * @param breakpointId
     *            the id of the breakpoint whose state to visualize.
     */
    static void updateWindows(int breakpointId) {
        for (Window w : Window.windows) {
            w.updateVisualizers(breakpointId);
        }
    }
    
    /**
     * Hides and disposes every window created.
     * 
     * <p>
     * The next window instantiated after this method was called will
     * automatically become the new {@link #getMainWindow()}.
     * </p>
     */
    public static void closeAll() {
        for (Window win : windows) {
            win.setVisible(false);
            win.dispose();
        }
        
        mainWindow = null;
        controlPanel = null;
    }
    
    /**
     * 
     * @return the main window that contains the {@link ControlPanel}.
     * @see #makeMainWindow()
     */
    public static Window getMainWindow() {
        return mainWindow;
    }
    
    public boolean isMainWindow() {
        return this == getMainWindow();
    }
    
    /**
     * Makes this window the main window.
     * 
     * @see #getMainWindow()
     */
    public void makeMainWindow() {
        Window.setMainWindow(this);
    }
    
    public static void setMainWindow(Window newMainWindow) {
        if (mainWindow == newMainWindow) {
            return;
        }
        
        // remove control panel from current main window if it exists
        if (mainWindow != null) {
            final Window finalOldMainWindow = mainWindow;
            SwingUtilities.invokeLater(() -> {
                finalOldMainWindow.controlPanelContainer.removeAll();
                finalOldMainWindow.controlPanelContainer.setVisible(false);
            });
        }
        
        if (newMainWindow == null && Window.windows.size() > 0) {
            newMainWindow = Window.windows.iterator().next();
        }
        
        if (newMainWindow != null) {
            final Window finalNewMainWindow = newMainWindow;
            
            SwingUtilities.invokeLater(() -> {
                finalNewMainWindow.controlPanelContainer.add(controlPanelSeparator, "gapbefore 0");
                
                // needed to separate visualizers from the control panel and therefore only visible if there is at least
                // one visualizer to separate the control panel from
                controlPanelSeparator.setVisible(!finalNewMainWindow.visualizers.isEmpty());
                
                finalNewMainWindow.controlPanelContainer.add(controlPanel);
                finalNewMainWindow.controlPanelContainer.setVisible(true);
                finalNewMainWindow.pack();
            });
            
        }
        
        mainWindow = newMainWindow;
    }
    
    /**
     * Adds the given visualizer to this window.
     * 
     * @param visualizer
     *            the visualizer to add
     * @see #addVisualizer(Logger)
     */
    public synchronized void addVisualizer(Visualizer visualizer) {
        SwingUtilities.invokeLater(() -> {
            if (!visualizers.isEmpty()) {
                // only add the separator if there are already other visualizers because the first visualizer does not
                // need to be separated from anything
                visualizerContainer.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
            visualizerContainer.add(visualizer.getWrapper(), "pushy,growy");
            
            // show the visualizerContainer as soon as the first visualizer is added
            visualizerContainer.setVisible(true);
            
            if (isMainWindow()) {
                controlPanelSeparator.setVisible(true);
            }
            
            pack();
            
            visualizers.add(visualizer);
            
            // update visualizer to the current breakpoint if the visualization already started
            if (controlPanel.getCurrentBreakpoint() != -1) {
                visualizer.update(controlPanel.getCurrentBreakpoint());
            }
        });
    }
    
    /**
     * Adds the given visualizer to this window and sets the title of the visualizer.
     * 
     * @param visualizer
     *            the visualizer to add
     * @param title
     *            the title
     */
    public void addVisualizer(Visualizer visualizer, String title) {
        visualizer.setTitle(title);
        this.addVisualizer(visualizer);
    }
    
    /**
     * Creates a new visualizer for the specified logger and adds that to this
     * window.
     * 
     * @param logger
     *            the logger to visualize.
     * @see #addVisualizer(Visualizer)
     * @see Logger#createVisualizer()
     */
    public void addVisualizer(Logger<?> logger) {
        addVisualizer(logger, null);
    }
    
    /**
     * Creates a new visualizer for the specified logger and adds that to this
     * window
     * 
     * @param logger
     *            the logger to visualize.
     * @param title
     *            the title to be displayed within the visualizer.
     * 
     * @see Visualizer#setTitle()
     * @see #addVisualizer(Visualizer)
     */
    public void addVisualizer(Logger<?> logger, String title) {
        Visualizer visualizer = logger.createDefaultVisualizer();
        visualizer.setTitle(title);
        this.addVisualizer(visualizer);
    }
    
    /**
     * Updates every visualizer in this window to show the state at the
     * breakpoint with id {@code breakpointId}.
     * 
     * @param breakpointId
     *            the id of the breakpoint whose state to visualize.
     */
    private synchronized void updateVisualizers(int breakpointId) {
        for (Visualizer v : this.visualizers) {
            v.update(breakpointId);
        }
        
        // pack();
        validate();
        repaint();
    }
}

package edu.kit.ipd.sdq.visualj.datavis.visualizer;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import edu.kit.ipd.sdq.visualj.util.BreakpointObserver;
import edu.kit.ipd.sdq.visualj.util.BreakpointViewer;
import edu.kit.ipd.sdq.visualj.util.DefaultBreakpointObservable;
import edu.kit.ipd.sdq.visualj.util.Messages;
import edu.kit.ipd.sdq.visualj.util.Misc;
import edu.kit.ipd.sdq.visualj.util.ResourceLoader;
import edu.kit.ipd.sdq.visualj.util.ThreadStatus;
import net.miginfocom.swing.MigLayout;

/**
 * The control panel that is displayed in the main window ({@link Window#getMainWindow()}.
 */
class ControlPanel extends JPanel implements BreakpointObserver {
    
    private static final long serialVersionUID = 4497669516518659873L;
    
    private static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(50, 30);
    
    // GUI components
    private JButton firstButton;
    private JButton prevButton;
    private JButton playPauseButton;
    private JButton nextButton;
    private JButton lastButton;
    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    
    private JSlider speedSlider;
    private JSpinner silentSpinner;
    
    private JLabel statusLabel;
    private JLabel breakpointInfoLabel;
    
    private DefaultBreakpointObservable observable = new DefaultBreakpointObservable();
    private boolean playing = false;
    private int currentBreakpoint = -1;
    private BreakpointViewer breakpointViewer;
    private ThreadStatus threadStatus;
    private ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> task;
    
    public ControlPanel(BreakpointViewer breakpointViewer, ThreadStatus threadStatus) {
        if (breakpointViewer == null) {
            throw new NullPointerException("breakpointViewer");
        }
        this.breakpointViewer = breakpointViewer;
        
        if (threadStatus == null) {
            throw new NullPointerException("threadStatus");
        }
        this.threadStatus = threadStatus;
        threadStatus.setChangedListener(() -> {
            updateControls();
        });
        
        initGUI();
        
        // init with latest breakpoint if it exists
        if (breakpointViewer.getLatestBreakpointId() > -1) {
            setCurrentBreakpoint(breakpointViewer.getLatestBreakpointId());
        }
    }
    
    private void initGUI() {
        this.setLayout(new MigLayout("flowy,insets 0", "[grow,fill]", "[]15![][][]"));
        {
            JPanel controlButtonsPane = new JPanel();
            controlButtonsPane.setLayout(new MigLayout("gapx 5,insets 0,align center"));
            {
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            setPlaying(false);
                            goToNextBreakpoint();
                        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            setPlaying(false);
                            goToPrevBreakpoint();
                        }
                    }
                };
                
                firstButton = createButton("first.png", "|<", Messages.getString("ControlPanel.toStart"));
                firstButton.addActionListener((event) -> {
                    setPlaying(false);
                    goToFirstBreakpoint();
                });
                firstButton.addKeyListener(keyListener);
                controlButtonsPane.add(firstButton);
                
                prevButton = createButton("prev.png", "<", Messages.getString("ControlPanel.previous"));
                prevButton.addActionListener((event) -> {
                    setPlaying(false);
                    goToPrevBreakpoint();
                });
                prevButton.addKeyListener(keyListener);
                controlButtonsPane.add(prevButton);
                
                playPauseButton = createButton("play.png", "Play / Pause", Messages.getString("ControlPanel.autoplay"));
                playPauseButton.setPreferredSize(Misc.scaleDimension(playPauseButton.getPreferredSize(), 1.5));
                playPauseButton.addActionListener((event) -> {
                    toggleAutoPlay();
                });
                playPauseButton.addKeyListener(keyListener);
                controlButtonsPane.add(playPauseButton);
                playIcon = ResourceLoader.loadIcon("play.png");
                pauseIcon = ResourceLoader.loadIcon("pause.png");
                
                nextButton = createButton("next.png", ">", Messages.getString("ControlPanel.next"));
                nextButton.addActionListener((event) -> {
                    setPlaying(false);
                    goToNextBreakpoint();
                });
                nextButton.addKeyListener(keyListener);
                controlButtonsPane.add(nextButton);
                
                lastButton = createButton("last.png", ">|", Messages.getString("ControlPanel.toEnd"));
                lastButton.addActionListener((event) -> {
                    setPlaying(false);
                    goToLastBreakpoint();
                });
                lastButton.addKeyListener(keyListener);
                controlButtonsPane.add(lastButton);
            }
            this.add(controlButtonsPane);
            
            JPanel optionsPane = new JPanel();
            optionsPane.setLayout(new MigLayout("flowy,insets 0,align center,wrap 2", "[grow,::500][][]", ""));
            {
                JLabel speedLabel = new JLabel(Messages.getString("ControlPanel.speed"));
                speedLabel.setToolTipText(Messages.getString("ControlPanel.delay"));
                optionsPane.add(speedLabel);
                
                speedSlider = new JSlider(0, 5000, 2000);
                speedSlider.setLabelTable(speedSlider.createStandardLabels(1000));
                speedSlider.setPaintLabels(true);
                speedSlider.setMajorTickSpacing(1000);
                speedSlider.setMinorTickSpacing(250);
                speedSlider.setPaintTicks(true);
                speedSlider.addChangeListener((ChangeEvent event) -> {
                    JSlider source = (JSlider) event.getSource();
                    
                    // If the user adjusts the speed while the visualization is playing, restart the auto playing, so
                    // that it can fetch the new speed value.
                    if (!source.getValueIsAdjusting() && isPlaying()) {
                        setPlaying(false);
                        setPlaying(true);
                    }
                });
                optionsPane.add(speedSlider, "grow");
                
                JSeparator seperator = new JSeparator(SwingConstants.VERTICAL);
                optionsPane.add(seperator, "spany 2,growy");
                
                JLabel silentLabel = new JLabel(Messages.getString("ControlPanel.maxBlockingLevel")); // TODO rename
                silentLabel.setToolTipText(Messages.getString("ControlPanel.maxBlockingLevelTooltip")); // TODO rename
                optionsPane.add(silentLabel);
                
                silentSpinner = new JSpinner();
                silentSpinner.setValue(Integer.MAX_VALUE);
                silentSpinner.addChangeListener((event) -> {
                    updateControls();
                });
                optionsPane.add(silentSpinner, "width 150!,align center center");
            }
            this.add(optionsPane);
            
            JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
            this.add(seperator);
            
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new MigLayout("insets 0", "[]push[]"));
            {
                statusLabel = new JLabel("Initialisierung..."); // TODO lang
                statusLabel.setToolTipText("Status der Ausführung.");
                statusPanel.add(statusLabel);
                
                JPanel breakpointPanel = new JPanel(new MigLayout("insets 0"));
                // TODO lang
                breakpointPanel.setToolTipText(
                        "Aktueller Breakpoint: <Breakpoint Nummer> (<Level>) / <Nummer des letzten Breakpoints>");
                {
                    JLabel breakpointInfoTitle = new JLabel("Breakpoint:"); // TODO lang
                    breakpointPanel.add(breakpointInfoTitle);
                    
                    breakpointInfoLabel = new JLabel("?");
                    breakpointPanel.add(breakpointInfoLabel);
                }
                statusPanel.add(breakpointPanel);
            }
            this.add(statusPanel);
        }
        
        updateControls();
    }
    
    /**
     * Create a new {@link JButton} with the image resource given by {@code imageFileName}. Uses {@code altText} as
     * alternative text if the image file does not exist. Also sets the buttons tooltip to {@code toolTipText}.
     * 
     * @param imageFileName
     *            the file name for the image resource
     * @param altText
     *            the alternative text displayed if the image resource does not exist
     * @param toolTipText
     *            the tooltip text
     * @return the created {@link JButton}
     */
    private JButton createButton(String imageFileName, String altText, String toolTipText) {
        ImageIcon icon = ResourceLoader.loadIcon(imageFileName);
        
        JButton button;
        if (icon != null) {
            button = new JButton(icon);
        } else {
            button = new JButton(altText);
        }
        button.setPreferredSize(DEFAULT_BUTTON_SIZE);
        
        button.setToolTipText(toolTipText);
        
        return button;
    }
    
    private void updateControls() {
        SwingUtilities.invokeLater(() -> {
            firstButton.setEnabled(this.currentBreakpoint > 0);
            lastButton.setEnabled(currentBreakpoint < breakpointViewer.getLatestBreakpointId());
            
            prevButton.setEnabled(currentBreakpoint > 0
                    && breakpointViewer.getPrevBreakpointId(currentBreakpoint - 1, getMaxBreakpointLevel()) != -1);
            nextButton.setEnabled(currentBreakpoint < breakpointViewer.getLatestBreakpointId()
                    && breakpointViewer.getNextBreakpointId(currentBreakpoint + 1, getMaxBreakpointLevel()) != -1);
            
            // TODO do something usefull (-> icons)
            if (currentBreakpoint == breakpointViewer.getLatestBreakpointId()) {
                if (threadStatus.isRunning()) {
                    // still loading
                } else if (threadStatus.hasThrown()) {
                    // error before next breakpoint
                } else {
                    // normal
                }
            }
            
            // TODO lang
            if (threadStatus.isRunning()) {
                statusLabel.setText("Running...");
            } else if (threadStatus.hasThrown()) {
                statusLabel.setText("Error: " + threadStatus.getThrowable().getClass().getSimpleName());
            } else {
                statusLabel.setText("Completed.");
            }
            
            if (currentBreakpoint != -1) {
                breakpointInfoLabel.setText(
                        currentBreakpoint + " (" + breakpointViewer.getBreakpointLevel(currentBreakpoint) + ") / "
                                + breakpointViewer.getLatestBreakpointId());
            } else {
                breakpointInfoLabel.setText("?");
            }
        });
    }

    public int getCurrentBreakpoint() {
        return currentBreakpoint;
    }
    
    /**
     * Returns whether the visualization is auto-playing or not.
     * 
     * @return {@code true} if the visualization is auto-playing, {@code false} otherwise.
     * @see #setPlaying(boolean)
     */
    public synchronized boolean isPlaying() {
        return playing;
    }
    
    /**
     * Starts or pauses the auto-play of the visualization depending on the value of {@code playing}.
     * 
     * @param playing
     *            {@code true} to start auto-playing, {@code false} to stop
     * @see #isPaused()
     */
    public synchronized void setPlaying(boolean playing) {
        if (this.playing == playing) {
            return;
        }
        
        this.playing = playing;
        
        if (playing) {
            int delay = speedSlider.getValue();
            
            if (delay <= 0) {
                // a delay of 0 is not permitted for scheduleWithFixedDelay and the difference should not be noticeable
                delay = 1;
            }
            
            task = timer.scheduleWithFixedDelay(() -> {
                if (!canGoToNext()) {
                    setPlaying(false);
                } else {
                    goToNextBreakpoint();
                }
            }, 0, delay, TimeUnit.MILLISECONDS);
            
            SwingUtilities.invokeLater(() -> {
                playPauseButton.setIcon(pauseIcon);
            });
        } else {
            if (task != null) {
                task.cancel(false);
                task = null;
            }
            
            SwingUtilities.invokeLater(() -> {
                playPauseButton.setIcon(playIcon);
            });
        }
    }
    
    private synchronized void toggleAutoPlay() {
        setPlaying(!isPlaying());
    }
    
    private synchronized void setCurrentBreakpoint(int breakpointId) {
        if (breakpointId < 0 || breakpointId > breakpointViewer.getLatestBreakpointId()) {
            throw new IllegalArgumentException("breakpointId out of bounds");
        }
        
        currentBreakpoint = breakpointId;
        observable.notifyObservers(breakpointId);
        
        updateControls();
    }
    
    private boolean canGoToFirst() {
        return breakpointViewer.getLatestBreakpointId() != -1;
    }
    
    private boolean canGoToPrev() {
        return currentBreakpoint > 0
                && breakpointViewer.getPrevBreakpointId(currentBreakpoint - 1, getMaxBreakpointLevel()) != -1;
    }

    private boolean canGoToNext() {
        return currentBreakpoint < breakpointViewer.getLatestBreakpointId()
                && breakpointViewer.getPrevBreakpointId(currentBreakpoint + 1, getMaxBreakpointLevel()) != -1;
    }
    
    private boolean canGoToLast() {
        return breakpointViewer.getLatestBreakpointId() != -1;
    }
    
    private synchronized void goToFirstBreakpoint() {
        if (canGoToFirst()) {
            setCurrentBreakpoint(0);
        }
    }
    
    private synchronized void goToPrevBreakpoint() {
        if (canGoToPrev()) {
            setCurrentBreakpoint(breakpointViewer.getPrevBreakpointId(currentBreakpoint - 1, getMaxBreakpointLevel()));
            
        }
    }
    
    private synchronized void goToNextBreakpoint() {
        if (canGoToNext()) {
            setCurrentBreakpoint(breakpointViewer.getNextBreakpointId(currentBreakpoint + 1, getMaxBreakpointLevel()));
        }
    }
    
    private synchronized void goToLastBreakpoint() {
        if (canGoToLast()) {
            setCurrentBreakpoint(breakpointViewer.getLatestBreakpointId());
        }
    }
    
    /**
     * Adds an observer for this object, which will be notified when there is a new state the user wants visualized.
     *
     * @param o
     *            an observer to be added.
     * @throws NullPointerException
     *             if {@code o} is null.
     */
    public void addBreakpointObserver(BreakpointObserver observer) {
        observable.addBreakpointObserver(observer);
    }
    
    /**
     * Removes an observer. Passing {@code null} to this method will have no effect.
     * 
     * @param o
     *            the observer to be removed
     */
    public void removeBreakpointObserver(BreakpointObserver observer) {
        observable.removeBreakpointObserver(observer);
    }
    
    @Override
    public synchronized void update(int breakpointId) {
        if (currentBreakpoint == -1 && breakpointViewer.getLatestBreakpointId() > -1) {
            setCurrentBreakpoint(0);
        }
        
        updateControls();
    }
    
    private int getMaxBreakpointLevel() {
        return (int) silentSpinner.getValue();
    }
}

package edu.kit.ipd.sdq.visualj.util;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadStatus {
    private UncaughtExceptionHandler defaultUEH;
    
    private Thread thread;
    private Throwable throwable;
    private boolean shouldLog;
    
    private Runnable changedListener;
    
    public ThreadStatus(Thread thread) {
        this(thread, true);
    }
    
    public ThreadStatus(Thread thread, boolean shouldLog) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.thread = thread;
        this.shouldLog = shouldLog;
        
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                ThreadStatus.this.throwable = throwable;
                
                if (ThreadStatus.this.defaultUEH != null) {
                    ThreadStatus.this.defaultUEH.uncaughtException(thread, throwable);
                }
                
                if (ThreadStatus.this.shouldLog) {
                    System.err.print("Exception in thread \"" + ThreadStatus.this.thread.getName() + "\" ");
                    throwable.printStackTrace();
                }
                
                notifyChangeListener();
            }
        });
        
        (new Thread(() -> {
            try {
                ThreadStatus.this.thread.join();
            } catch (InterruptedException e) {
            }
            
            ThreadStatus.this.notifyChangeListener();
        })).start();
    }
    
    public boolean isRunning() {
        return this.thread.isAlive();
    }
    
    public boolean hasThrown() {
        return this.throwable != null;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public void setChangedListener(Runnable changedListener) {
        this.changedListener = changedListener;
    }
    
    private void notifyChangeListener() {
        if (this.changedListener != null) {
            this.changedListener.run();
        }
    }
}

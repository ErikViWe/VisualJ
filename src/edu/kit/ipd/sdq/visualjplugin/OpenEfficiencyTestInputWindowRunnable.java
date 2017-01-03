package edu.kit.ipd.sdq.visualjplugin;

import java.lang.reflect.Method;
import java.util.Locale;

import edu.kit.ipd.sdq.visualjplugin.efficiency.EfficiencyTestInputWindow;
import edu.kit.ipd.sdq.visualjplugin.efficiency.Messages;

/**
 * This class is used by the {@link MenuBuilder} to create a new thread in which
 * to open the {@link EfficiencyTestInputWindow}.
 * 
 * This class must be public!
 * 
 * @see MenuBuilder.OpenEfficiencyTestInputWindowAction#actionPerformed(java.awt.event.ActionEvent)
 */
public class OpenEfficiencyTestInputWindowRunnable implements Runnable {
    
    private Locale locale;
    private Method method;
    
    public OpenEfficiencyTestInputWindowRunnable(Method method, Locale locale) {
        this.locale = locale;
        this.method = method;
    }
    
    @Override
    public void run() {
        Messages.setLocale(locale);
        edu.kit.ipd.sdq.visualj.util.Messages.setLocale(locale);
        
        new EfficiencyTestInputWindow(method).setVisible(true);
    }
}

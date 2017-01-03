package edu.kit.ipd.sdq.visualjplugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import bluej.extensions.BlueJ;
import bluej.extensions.Extension;
import bluej.extensions.event.PackageEvent;
import bluej.extensions.event.PackageListener;
import edu.kit.ipd.sdq.visualjplugin.efficiency.Messages;

public class VisualJExtension extends Extension implements PackageListener {
    
    private MenuBuilder menuBuilder;
    
    /**
     * The startup method called to inizialize the extension.
     * 
     * @param bluej
     *            the BlueJ object needed to setup this extension
     */
    @Override
    public void startup(BlueJ bluej) {        
        bluej.addPackageListener(this);
        menuBuilder = new MenuBuilder(bluej);
        bluej.setMenuGenerator(menuBuilder);
        
        // Set the locale of VisualJ's GUIs:
        Locale locale
                = bluej.getBlueJPropertyString("bluej.language", "").equals("german") ? Locale.GERMAN : Locale.ENGLISH;
        
        Messages.setLocale(locale);
        edu.kit.ipd.sdq.visualj.util.Messages.setLocale(locale);
    }
    
    /**
     * Dummy main method to mark this class as the main class.
     * 
     * @param args
     */
    public static void main(String[] args) {
    }
    
    /**
     * When a package is closing in BlueJ, this method gets invoked.
     * 
     * @param event
     *            the event describing what package has closed etc.
     */
    @Override
    public void packageClosing(PackageEvent event) {
    }
    
    /**
     * When a package is opening in BlueJ, this method gets invoked.
     * 
     * @param event
     *            the event describing what package has opened etc.
     */
    @Override
    public void packageOpened(PackageEvent event) {
    }
    
    /**
     * Checks if this extension is compatible with the current BlueJ and Java versions.
     * 
     * @return the result of the compatibility-check.
     */
    @Override
    public boolean isCompatible() {
        return Double.parseDouble(System.getProperty("java.specification.version")) > 1.7
                // These version numbers refer to the version of the BlueJ Extensions API,
                // NOT the version of BlueJ itself!
                && VERSION_MAJOR == 2 && VERSION_MINOR >= 10;
    }
    
    /**
     * @return this extension's version.
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    /**
     * @return this extension's name.
     */
    @Override
    public String getName() {
        return "VisualJ";
    }
    
    /**
     * @return this extension's description.
     */
    @Override
    public String getDescription() {
        return "VisualJ Extension for BlueJ";
    }
    
    /**
     * @return this extension's URL
     */
    @Override
    public URL getURL() {
        try {
            return new URL("http://www.kit.edu/");
        } catch (MalformedURLException e) {
            System.err.println("The site is no longer available.");
            return null;
        }
    }
}

package edu.kit.ipd.sdq.visualjplugin.efficiency;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;

/**
 * This class contains static methods for setting the locale for this plugin's
 * GUIs.
 * 
 * <p>
 * The default value is {@link Locale#GERMAN}.
 * </p>
 */
public class Messages {
    
    private static final String BUNDLE_NAME = "edu.kit.ipd.sdq.visualjplugin.res.messages";
    
    private static final Locale[] supportedLocales = {Locale.GERMAN, Locale.ENGLISH};
    
    private static Locale locale = Locale.GERMAN;
    
    /**
     * 
     * @return an array of every locale the library supports.
     */
    public static Locale[] getSupportedLocales() {
        return supportedLocales;
    }
    
    /**
     * 
     * @return the current locale for.
     */
    public static Locale getLocale() {
        return locale;
    }
    
    /**
     * Sets the current locale.
     * 
     * @param locale
     * @throws IllegalArgumentException
     *             if {@code locale} is not in {@link #getSupportedLocales()}.
     */
    public static void setLocale(Locale locale) throws IllegalArgumentException {
        if (!ArrayUtils.contains(supportedLocales, locale)) {
            throw new IllegalArgumentException(locale.toString());
        }
        
        Messages.locale = locale;
    }
    
    /**
     * 
     * @param key
     *            a key.
     * @return the localized message with the specified key.
     */
    public static String getString(String key) {
        try {
            return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    private Messages() {
    }
}

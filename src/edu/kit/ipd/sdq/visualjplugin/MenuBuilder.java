package edu.kit.ipd.sdq.visualjplugin;

import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.lang3.ArrayUtils;

import bluej.extensions.BClass;
import bluej.extensions.BlueJ;
import bluej.extensions.ClassNotFoundException;
import bluej.extensions.MenuGenerator;
import bluej.extensions.ProjectNotOpenException;
import edu.kit.ipd.sdq.visualj.util.DialogUtils;
import edu.kit.ipd.sdq.visualjplugin.efficiency.Messages;

/**
 * This class generates our extension's context menu when right-clicking on a class.
 */
class MenuBuilder extends MenuGenerator {
    
    private static final Class<?>[] allowedTypes = {
            // @formatter:off
            int.class, long.class, short.class, double.class, float.class, byte.class, char.class, boolean.class,
            int[].class, long[].class, short[].class, double[].class, float[].class, byte[].class, char[].class, boolean[].class,
            String.class
            // @formatter:on
    };
    private BlueJ bluej;
    
    MenuBuilder(BlueJ bluej) {
        this.bluej = bluej;
    }
    
    /**
     * Returns a menu item to start efficiency tests that contains a sub entry for every testable method of the given
     * class. A method is considered testable if it is
     * <ol>
     * <li>not private and</li>
     * <li>static or its class has a default constructor</li>
     * <li>its parameter types are only primitives, array of primitives or strings.
     * </ol>
     * 
     * @param bClass
     *            the class whose methods are to be tested
     * 
     * @return a JMenuItem for efficiency tests that is added to the right-click
     *         menu of a class by BlueJ
     */
    @Override
    public JMenuItem getClassMenuItem(BClass bClass) {
        JMenu menu = new JMenu(Messages.getString("MenuBuilder.efficiency"));
        
        // try to get the underlying java class
        Class<?> jClass = null;
        try {
            jClass = bClass.getJavaClass();
        } catch (ProjectNotOpenException | ClassNotFoundException e1) {
            // don't add methods if the class can't be accessed.
            return menu;
        }
        
        boolean hasDefaultConstructor = hasDefaultConstructor(jClass);
        
        try {
            for (Method method : jClass.getDeclaredMethods()) {
                // method modifiers should not be private, have a default
                // constructor or should be static and
                
                if (!Modifier.isPrivate(method.getModifiers())
                        && (Modifier.isStatic(method.getModifiers()) || hasDefaultConstructor)
                        && parameterTypesValid(method)) {
                    menu.add(new JMenuItem(new OpenEfficiencyTestInputWindowAction(method)));
                }
            }
        } catch (SecurityException e) {
            // just don't add those methods then..
        }
        
        // dont display the plugin entry if there are no methods to be tested.
        if (menu.getItemCount() == 0)
            return null;
        return menu;
    }
    
    /**
     * Check if the method's parameter types are valid. Allowed types are:
     * <ul>
     * <li>primitive
     * <li>primitive arrays
     * <li>Strings
     * </ul>
     * 
     * @param method
     *            the method to check
     * @return {@code true} if the parameter types are valid, {@code false} otherwise
     */
    private static boolean parameterTypesValid(Method method) {
        for (Class<?> type : method.getParameterTypes()) {
            if (!ArrayUtils.contains(allowedTypes, type)) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean hasDefaultConstructor(Class<?> c) {
        for (Constructor<?> co : c.getConstructors()) {
            if (co.getParameterTypes().length == 0) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * This class instantiates the different menus for each testable method.
     */
    class OpenEfficiencyTestInputWindowAction extends AbstractAction {
        
        private static final long serialVersionUID = -4896498988779657558L;
        
        private Method method;
        
        /**
         * 
         * @param method
         *            the Method which is to be tested
         */
        public OpenEfficiencyTestInputWindowAction(Method method) {
            // example
            putValue(Action.NAME, method.getName());
            this.method = method;
        }
        
        /**
         * When this is called, the EfficeincyTestInputWindow is opened with this
         * method so the user may choose his test values.
         */
        @Override
        public void actionPerformed(ActionEvent anEvent) {
            // Make sure the ClassLoader used by the EfficiencyTestInputWindow is a child of the current BlueJ Project
            // ClassLoader. This prevents classes being loaded multiple times (i.e. once by the BlueJ project and once
            // more by the VisualJ library), which would lead to static variables having multiple instances.
            try {
                URLClassLoader bluejClassLoader = bluej.getCurrentPackage().getProject().getClassLoader();
                URL jarFileLocation
                        = VisualJExtension.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFileLocation}, bluejClassLoader);
                
                Class<?> runnableClass = classLoader.loadClass(OpenEfficiencyTestInputWindowRunnable.class.getName());
                
                // The locale has to be set again because we've reloaded the classes.
                Locale locale = bluej.getBlueJPropertyString("bluej.language", "").equals("german")
                        ? Locale.GERMAN
                        : Locale.ENGLISH;
                
                Thread thread = new Thread((Runnable) runnableClass.getDeclaredConstructor(Method.class, Locale.class)
                        .newInstance(method, locale));
                thread.setContextClassLoader(classLoader);
                thread.start();
            } catch (ReflectiveOperationException | ProjectNotOpenException | MalformedURLException | URISyntaxException
                    | IllegalArgumentException | SecurityException ex) {
                DialogUtils.showExceptionDialog(null, ex, "VisualJ Error", Messages.getString("MenuBuilder.loadError"));
            }
        }
    }
}

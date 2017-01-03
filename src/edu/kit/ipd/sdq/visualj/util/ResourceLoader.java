package edu.kit.ipd.sdq.visualj.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class contains static methods for loading images and icons used by this
 * library's GUIs.
 */
public class ResourceLoader {
    /**
     * Resource folder.
     */
    public static final String RESOURCE_DIR = "edu/kit/ipd/sdq/visualj/res/";
    
    /**
     * Image folder within the {@link #RESOURCE_DIR resource folder}.
     */
    public static final String IMAGE_DIR = "img/";
    
    /**
     * Loads an image resource from the image resource folder.
     * 
     * @param fileName
     *            name of the image file
     * @return the image as a {@link BufferedImage} or {@code null} if it does
     *         not exist or could not be read
     */
    public static BufferedImage loadImage(String fileName) {
        String path = RESOURCE_DIR + IMAGE_DIR + fileName;
        InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
        
        BufferedImage image;
        if (stream != null) {
            try {
                image = ImageIO.read(stream);
            } catch (IOException e) {
                image = null;
            }
        } else {
            image = null;
        }
        
        return image;
    }
    
    /**
     * Loads an {@link ImageIcon} resource from the image resource folder.
     * 
     * @param fileName
     *            name of the image file
     * @return the image as a {@link ImageIcon} or {@code null} if it does not
     *         exist or could not be read
     */
    public static ImageIcon loadIcon(String fileName) {
        BufferedImage image = loadImage(fileName);
        return (image == null ? null : new ImageIcon(image));
    }
}

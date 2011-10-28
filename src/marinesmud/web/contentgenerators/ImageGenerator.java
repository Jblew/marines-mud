/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.contentgenerators;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import marinesmud.web.StreamContentGenerator;

/**
 *
 * @author jblew
 */
public class ImageGenerator implements StreamContentGenerator {
    private final static int IMG_WIDTH = 640;
    private final static int IMG_HEIGTH = 480;

    public void generate(String[] urlParts, OutputStream os) {
        BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGTH,  BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGTH);

        g.setColor(Color.WHITE);
        g.fillRect(10, 10, IMG_WIDTH-20, IMG_HEIGTH-20);
        try {
            ImageIO.write(img, "jpg", os);
        } catch (IOException ex) {
            Logger.getLogger(ImageGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        g.dispose();
    }
}

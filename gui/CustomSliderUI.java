package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class CustomSliderUI extends BasicSliderUI {

    private BufferedImage thumbImage;
    private JSlider currentSlider;

    public CustomSliderUI(JSlider s) {
        super(s);
        currentSlider = s;

        try {
            thumbImage = ImageIO.read(getClass().getResourceAsStream("/assets/slider_thumb.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics gCopy = g.create();
        Graphics2D g2D = (Graphics2D) gCopy;
        g2D.setColor(Color.white);

        int trackX = trackRect.x;
        int trackY = trackRect.y;
        int trackW = trackRect.width;
        int trackH = trackRect.height;

        g2D.fillRoundRect(trackX, trackY, trackW, trackH, 25, 25);
        g2D.setColor(Color.black);
        g2D.setStroke(new BasicStroke(7));
        g2D.drawRoundRect(trackX, trackY, trackW, trackH, 25, 25);

        gCopy.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics gCopy = g.create();
        
        gCopy.drawImage(thumbImage, thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, currentSlider);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(32, 32);
    }

}
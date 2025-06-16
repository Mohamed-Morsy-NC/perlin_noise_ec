package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class BackgroundPanel extends JPanel {
    private BufferedImage bgImage;

    public BackgroundPanel(BufferedImage i) {
        this.bgImage = i;
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, this);
        }
    }
}

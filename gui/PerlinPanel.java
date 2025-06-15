package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import backend.MainFrame;
import backend.MediaHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PerlinPanel extends JPanel implements ActionListener, KeyListener {

    private JButton exitBtn;
    private JButton startBtn;

    private BufferedImage bgImage;
    private JPanel screenPanel = new JPanel();

    private GenerationPanel genPanel = new GenerationPanel();

    private static CardLayout cardLayout = new CardLayout();
    private static JPanel cardPanel = new JPanel(cardLayout);
    private static int btnScaleFactor = 1;

    // Creates the components on the screen, including buttons and sets up layout
    public void createMainComponents() {
        this.requestFocusInWindow();
        this.setLayout(new BorderLayout());
        screenPanel.setLayout(null);

        screenPanel.setOpaque(false);
        cardPanel.setOpaque(false);

        cardPanel.add(genPanel, "Generation");

        try {
            startBtn = createButton("START", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/start_btn.png")), 860, 450, this);
            exitBtn = createButton("QUIT", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/quit_btn.png")), 860, 775, this);

            // Align Buttons
            addButtons();
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/bgimage.png"));

            this.add(screenPanel, BorderLayout.WEST);

            cardPanel.add(screenPanel, "Menu");
            cardPanel.add(genPanel, "Generation");
            this.add(cardPanel, BorderLayout.CENTER);
            cardLayout.show(cardPanel, "Menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Aligns buttons on screen
    private void addButtons() {
        screenPanel.add(exitBtn);
        screenPanel.add(startBtn);
    }

    // Creates a button with certain properties
    public static JButton createButton(String txt, int scale, Image bImage, int posX, int posY, ActionListener manager) {
        // Helped me figure out transparent buttons: https://stackoverflow.com/questions/5654208/making-a-jbutton-invisible-but-clickable
        JButton b = new JButton();
        b.setBounds(posX, posY, (int) MainFrame.UNIVERSAL_BUTTON_DIMENSION_MAX.getWidth(), (int) MainFrame.UNIVERSAL_BUTTON_DIMENSION_MAX.getHeight());
        bImage = bImage.getScaledInstance(bImage.getWidth(null) / scale, bImage.getHeight(null) / scale, Image.SCALE_SMOOTH);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setBorder(null);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setIcon(new ImageIcon(bImage));
        b.addActionListener(manager);
        b.setVisible(true);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFont(MainFrame.UNIVERSAL_FONT);
        b.setMaximumSize(MainFrame.UNIVERSAL_BUTTON_DIMENSION_MAX);
        b.setPreferredSize(MainFrame.UNIVERSAL_BUTTON_DIMENSION_PREFERRED);
        return b;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object sourceObject = e.getSource();

        if (sourceObject == exitBtn) {
            System.exit(0);
        } else if (sourceObject == startBtn) {

            SwingUtilities.invokeLater(() -> {
                cardLayout.show(cardPanel, "Generation");
                genPanel.setFocusable(true);
                genPanel.requestFocusInWindow();
            });
        }
    }

}

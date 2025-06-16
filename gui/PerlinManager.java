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

import backend.AudioHandler;
import backend.MainFrame;
import backend.MediaHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PerlinManager extends JPanel implements ActionListener, KeyListener {

    private JButton exitBtn;
    private JButton startBtn;

    private BufferedImage bgImage;
    private JPanel screenPanel = new JPanel();

    private static GenerationPanel genPanel = new GenerationPanel();
    private static ImagesPanel imagePanel = new ImagesPanel();

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
        cardPanel.add(imagePanel, "Images");

        try {
            startBtn = createButton("START", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/start_btn.png")), 860, 100, this);
            exitBtn = createButton("QUIT", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/quit_btn.png")), 860, 550, this);

            // Align Buttons
            addButtons();
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/bgimage.png"));

            this.add(screenPanel, BorderLayout.WEST);

            cardPanel.add(screenPanel, "Menu");
            cardPanel.add(imagePanel, "Images");
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

    public static void playPop() {
        try {
            //Sound Effect by <a href="https://pixabay.com/users/creatorshome-49707711/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=328170">CreatorsHome</a> from <a href="https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=328170">Pixabay</a>
            //Sound Effect by <a href="https://pixabay.com/users/creatorshome-49707711/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=328167">CreatorsHome</a> from <a href="https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=328167">Pixabay</a>
            //Sound Effect by <a href="https://pixabay.com/users/freesound_community-46691455/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=39222">freesound_community</a> from <a href="https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=39222">Pixabay</a>
            int sfxChooser = (int) (Math.random() * 3) + 1;
            AudioHandler sfxHandler;

            switch (sfxChooser) {
                case 1 -> {
                    sfxHandler = new AudioHandler("/assets/music/pop1.wav");
                    break;
                }
                case 2 -> {
                    sfxHandler = new AudioHandler("/assets/music/pop2.wav");
                    break;
                }
                case 3 -> {
                    sfxHandler = new AudioHandler("/assets/music/pop3.wav");
                    break;
                }
                default -> {
                    sfxHandler = new AudioHandler("/assets/music/pop3.wav");
                    break;
                }
            }

            if (sfxHandler != null) {
                sfxHandler.playAudio();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public static JSlider createSlider(int direction, int min, int max, int value, Rectangle bounds, String minT, String maxT, String valueT, ChangeListener l) {
        JSlider slider = new JSlider(direction, min, max, value);
        slider.setFont(MainFrame.UNIVERSAL_FONT_SMALL);
        Hashtable strengthLabelTable = new Hashtable();
        slider.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        strengthLabelTable.put(min, new JLabel(minT));
        strengthLabelTable.put(value, new JLabel(valueT));
        strengthLabelTable.put(max, new JLabel(maxT));

        for (Object label : strengthLabelTable.values()) {
            JLabel labelT = (JLabel) label;
            labelT.setFont(MainFrame.UNIVERSAL_FONT_BIG);
            labelT.setForeground(Color.red);
            labelT.setBackground(Color.black);
            labelT.setOpaque(true);
        }

        slider.setLabelTable(strengthLabelTable);
        slider.setPaintLabels(true);
        slider.setPaintTicks(false);
        slider.setFocusable(false);
        slider.setBackground(new Color(0, 0, 0, 0));
        slider.addChangeListener(l);

        slider.setOpaque(false);
        slider.setUI(new CustomSliderUI(slider));

        return slider;
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

        playPop();

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

    public static void goToMain() {
        cardLayout.show(cardPanel, "Menu");
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public static void goToImages() {
        cardLayout.show(cardPanel, "Images");
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public static void goToGenerate() {
        cardLayout.show(cardPanel, "Generation");
        genPanel.setFocusable(true);
        genPanel.requestFocusInWindow();
    }

}

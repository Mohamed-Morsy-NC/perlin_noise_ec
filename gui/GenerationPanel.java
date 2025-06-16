package gui;

import backend.MediaHandler;
import backend.PerlinNoiseGenerator;
import backend.SaveState;
import backend.StateManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GenerationPanel extends JPanel implements KeyListener, ActionListener {

    private static GenerationPanel instance;
    private static JButton returnBtn;
    private static JButton regenBtn;
    private static JButton biomeBtn;
    private static JButton imagesBtn;

    private static PerlinNoiseGenerator generator;
    private static JSlider freqSlider; // 0.01f is default, goes between 0.001 and 0.5

    private static final String SAVE_FILE_DIRECTORY = "save_data/app_data.json";

    public GenerationPanel() {
        instance = this;
        this.setBackground(Color.white);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        MediaHandler.initSSCount(SAVE_FILE_DIRECTORY);

        createComponents();
        initGeneration();
    }

    public static GenerationPanel setElementVisibility(boolean state) {
        returnBtn.setVisible(state);
        biomeBtn.setVisible(state);
        regenBtn.setVisible(state);
        freqSlider.setVisible(state);
        imagesBtn.setVisible(state);
        return instance;
    }

    public static GenerationPanel getInstance() {
        return instance;
    }

    private void createComponents() {
        try {
            returnBtn = PerlinManager.createButton("RETURN", 2, ImageIO.read(getClass().getResourceAsStream("/assets/return_btn.png")), -75, -75, this);
            this.add(returnBtn);
            regenBtn = PerlinManager.createButton("REGEN", 1, ImageIO.read(getClass().getResourceAsStream("/assets/regen_btn.png")), 850, 750, this);
            this.add(regenBtn);

            biomeBtn = PerlinManager.createButton("BIOME", 1, ImageIO.read(getClass().getResourceAsStream("/assets/biome_btn.png")), 1500, 100, this);
            this.add(biomeBtn);

            imagesBtn = PerlinManager.createButton("IMAGES", 1, ImageIO.read(getClass().getResourceAsStream("/assets/gallery_btn.png")), 1500, 700, this);
            this.add(imagesBtn);

            ChangeListener changeListener = new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    freqSlider.repaint();
                    float v = (float) freqSlider.getValue() / 10000;
                    PerlinNoiseGenerator.setFrequency(v);
                    generator.start();
                    repaint();
                }
            };

            freqSlider = PerlinManager.createSlider(JSlider.HORIZONTAL, 1, 5000, 1000, new Rectangle(600, 100, 750, 128), "blobby", "dots", "dotted", changeListener);
            this.add(freqSlider);

            generator = new PerlinNoiseGenerator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGeneration() {
        generator.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_F2) {
            saveGame();
            SaveState loadCount = StateManager.loadState(SAVE_FILE_DIRECTORY);

            if (loadCount != null) {
                MediaHandler.captureScreen("screenshot_" + loadCount.getSsNum());
                MediaHandler.updateSSCount(SAVE_FILE_DIRECTORY);
            } else {
                System.err.println("Save data failed to load. Screenshot file will be overwritten.");
            }
        } else if (key == KeyEvent.VK_Q) {
            System.exit(0);
        }
    }

    private void saveGame() {
        File f = new File(SAVE_FILE_DIRECTORY);

        if (!f.exists() || f.length() == 0) {
            SaveState state = new SaveState(1);
            StateManager.saveState(state, SAVE_FILE_DIRECTORY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object sourceObject = e.getSource();
        PerlinManager.playPop();

        if (sourceObject == returnBtn) {
            PerlinManager.goToMain();
        } else if (sourceObject == regenBtn) {
            generator.start();
            repaint();
        } else if (sourceObject == biomeBtn) {
            generator.cycleBiomeType();
            generator.start();
            repaint();
        } else if (sourceObject == imagesBtn) {
            PerlinManager.goToImages();
            repaint();
        }

        this.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color[][] pixels = generator.getPixelGrid();

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                g.setColor(pixels[i][j]);
                g.drawRect(i, j, 1, 1);
            }
        }

    }
}

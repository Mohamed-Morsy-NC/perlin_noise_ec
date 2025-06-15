package backend;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import gui.PerlinPanel;

// GOAL:
// Perlin Noise Terrain Map
// Simulate 2D Perlin noise (or a simplified noise function) to generate elevation values across a grid.
// Use colors to represent elevation (e.g., blue = water, green = grass, white = snow).
// Focus: Randomness, smoothing/interpolation, procedural generation.


// In charge of the actual window holding the game, and starts up the GUI thread
// Has the screen dimension, font, button dimensions, pause state, debug mode status,
// and main panel
public class MainFrame extends JFrame {

    private final Dimension SCREEN_DIMENSION = new Dimension(1920, 1080);
    public static final Dimension UNIVERSAL_BUTTON_DIMENSION_MAX = new Dimension(250, 250);
    public static final Dimension UNIVERSAL_BUTTON_DIMENSION_PREFERRED = new Dimension(250, 250);
    public static final Font UNIVERSAL_FONT = new Font("Comic Sans MS", Font.BOLD, 18);
    public static final Font UNIVERSAL_FONT_SMALL = new Font("Comic Sans MS", Font.BOLD, 14);
    public static final Font UNIVERSAL_FONT_BIG = new Font("Comic Sans MS", Font.BOLD, 25);
    public static final Font UNIVERSAL_FONT_LARGEST = new Font("Comic Sans MS", Font.BOLD, 72);
    static PerlinPanel perlinPanel = new PerlinPanel();

    private BufferedImage pointerCursorImage;
    public static Cursor pointerCursor;

    public static void init(MainFrame GUI) {
        SwingUtilities.invokeLater(() -> GUI.createFrame(GUI));
    }

    // Creates the window frame with given properties
    public void createFrame(Object semaphore) {
        this.setTitle("Perlin Noise Generator");
        this.setSize(SCREEN_DIMENSION);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        perlinPanel.createMainComponents();
        perlinPanel.setVisible(true);
        this.add(perlinPanel, BorderLayout.CENTER);
        this.setVisible(true);

        // Set up app icon & cursor
        try {
            this.setIconImage(ImageIO.read(getClass().getResourceAsStream("/assets/perlin_icon.png")));
            pointerCursorImage = ImageIO.read(getClass().getResourceAsStream("/assets/cursor.png"));
            pointerCursor = Toolkit.getDefaultToolkit().createCustomCursor(pointerCursorImage, new Point(0, 0), "pointer cursor");
            this.setCursor(pointerCursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
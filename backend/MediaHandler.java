package backend;

import gui.GenerationPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

public class MediaHandler {
    public static int screenShotCount;

    public MediaHandler() {
    }

    public static void initSSCount(String savePath) {
        SaveState state = StateManager.loadState(savePath);

        File mediaFolder = new File("media");

        if (mediaFolder.exists() && mediaFolder.isDirectory()) {
            String[] screenshots = mediaFolder.list();

            if (screenshots == null || screenshots.length == 0) {
                System.out.println("No screenshots yet");

                screenShotCount = 1;
                StateManager.saveState(new SaveState(screenShotCount), savePath);
            }

        }

        if (state != null) {
            screenShotCount = state.getSsNum();
        } else {
            screenShotCount = 1;
            // StateManager.saveState(new SaveState(screenShotCount), savePath);
        }
    }

    public static void captureScreen(String fileName) {
        new Thread(() -> {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    GenerationPanel gP = GenerationPanel.setElementVisibility(false);
                    gP.revalidate();
                    gP.paintImmediately(gP.getBounds());
                });

                GenerationPanel tempPanel = GenerationPanel.getInstance();

                Robot robot = new Robot();
                JRootPane rootPane = SwingUtilities.getRootPane(tempPanel);
                Point p = rootPane.getLocationOnScreen();
                Dimension size = rootPane.getSize();

                Rectangle screenRect = new Rectangle(p, size);
                BufferedImage screenImage = robot.createScreenCapture(screenRect);

                File temp = new File("media");

                if (temp.exists()) {
                    ImageIO.write(screenImage, "PNG", new File("media/" + fileName + ".png"));
                } else {
                    temp.mkdirs();
                    ImageIO.write(screenImage, "PNG", new File("media/" + fileName + ".png"));
                }

                SwingUtilities.invokeLater(() -> {
                    GenerationPanel gP = GenerationPanel.setElementVisibility(true);
                    gP.revalidate();
                    gP.repaint();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static int getSSCount() {
        return screenShotCount;
    }

    public static void updateSSCount(String saveFilePath) {
        SaveState loaded = StateManager.loadState(saveFilePath);
        if (loaded != null) {
            screenShotCount = loaded.getSsNum();
        } else {
            screenShotCount = 1;
        }

        screenShotCount++;

        SaveState newState = new SaveState(screenShotCount);
        StateManager.saveState(newState, saveFilePath);
    }
}

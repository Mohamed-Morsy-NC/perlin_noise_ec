package backend;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MediaHandler {
    private static int screenShotCount = 1;

    public static void captureScreen(String fileName) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenImage = robot.createScreenCapture(screenRect);

            ImageIO.write(screenImage, "PNG", new File("media/" + fileName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSSCount() {
        return screenShotCount;
    }

    public static void updateSSCount() {
        screenShotCount++;
    }
}
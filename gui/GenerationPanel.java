package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import backend.MediaHandler;

public class GenerationPanel extends JPanel implements KeyListener {
    public GenerationPanel() {
        
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_F2) {
            System.out.println("SS taken");
            MediaHandler.captureScreen("screenshot_" + MediaHandler.getSSCount());
            MediaHandler.updateSSCount();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

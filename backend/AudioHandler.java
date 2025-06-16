package backend;

import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class AudioHandler {

    private Clip clip;
    private AudioInputStream audioInputStream;
    private String filePath;


    public AudioHandler(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.filePath = filePath;

        InputStream inputStream = getClass().getResourceAsStream(filePath);
        try {
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            bufferedIn.mark(Integer.MAX_VALUE);

            this.audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            this.clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            FileWriter writer = new FileWriter("output.txt");
            writer.write(e.getMessage());
            writer.close();
        }
    }

    public void playAudio() {
        clip.start();
    }

    public Clip getClip() {
        return this.clip;
    }

    // Makes audio play forever (like background music)
    public void playAudioLooped() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void stopAudio() {
        clip.close();
    }

    // Gets the status of the audio
    public String getAudioStatus() {
        return "Currently Playing: " + filePath;
    }
}

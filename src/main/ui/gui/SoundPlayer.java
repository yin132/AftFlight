package ui.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundPlayer {

    // EFFECTS: plays sound file in data folder with fileName
    public static void playSound(String fileName) {
        String filePath = "./data/" + fileName;
        AudioInputStream audioInputStream;
        Clip clip;
        try {
            File file = new File(filePath);
            audioInputStream = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            PopupWindow.showMessage("unable to play sound");
        }
    }
}

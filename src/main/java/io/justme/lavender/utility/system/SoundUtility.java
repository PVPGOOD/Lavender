package io.justme.lavender.utility.system;

import lombok.experimental.UtilityClass;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import java.util.Objects;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
@UtilityClass
public class SoundUtility {

    public synchronized void playSound(String url,float gain) {
        new Thread(() -> {
            try {
                var clip = AudioSystem.getClip();
                var inputStream = AudioSystem.getAudioInputStream(
                        Objects.requireNonNull(SoundUtility.class.getResourceAsStream("/assets/minecraft/la/sound/" + url)));

                clip.open(inputStream);
                var gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gain);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage() + " " + url);
            }
        }).start();
    }

}

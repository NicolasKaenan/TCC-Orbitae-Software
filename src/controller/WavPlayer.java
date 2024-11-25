package controller;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.FloatControl;

public class WavPlayer {
    private Clip clip = null;
    public static FloatControl gainControl;

    public WavPlayer() {

    }

    public void playWavFile(String filePath, float volume) {
        try {
        InputStream in = getClass().getResourceAsStream(filePath);
        
        if (in == null) {
            System.err.println("Arquivo de áudio não encontrado: " + filePath);
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float final_volume = (volume * (gainControl.getMaximum() - gainControl.getMinimum()) / 100f) + gainControl.getMinimum();
        gainControl.setValue(final_volume);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo de áudio: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void VolumeAtual(float volume) {
        float minGain = gainControl.getMinimum();
        float maxGain = gainControl.getMaximum();
        float range = maxGain - minGain;
    
        volume = Math.max(0, Math.min(volume, 100)); 
        float adjustedVolume = (volume / 100f) * range + minGain;
    
        gainControl.setValue(adjustedVolume);
    }
    
    

    public float readVolumeFromFile(String filePath) {
        float text = 0.0f;

        try (BufferedReader leitorFiler = new BufferedReader(new FileReader(filePath))) {
            text = Float.valueOf(leitorFiler.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }


    public void StopMusic() {
        clip.stop();
    }

    public void StartMusic() {
        clip.start();
    }

}

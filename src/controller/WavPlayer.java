package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sound.sampled.FloatControl;

public class WavPlayer {
    private Clip clip = null;

    public WavPlayer() {

    }

    public void playWavFile(String filePath, float volume) {
        try {
            // Carrega o arquivo de música como um InputStream
        InputStream in = getClass().getResourceAsStream(filePath);
        
        if (in == null) {
            System.err.println("Arquivo de áudio não encontrado: " + filePath);
            return;
        }

        // Copia o InputStream para um ByteArrayInputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // Usa o ByteArrayInputStream para criar o AudioInputStream
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        // Ajusta o volume
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float final_volume = (volume * (gainControl.getMaximum() - gainControl.getMinimum()) / 100f) + gainControl.getMinimum();
        gainControl.setValue(final_volume);

        // Reproduz o áudio
        clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproduzir indefinidamente
        clip.start();
        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo de áudio: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float readVolumeFromFile(String filePath) {
        float volume = 150.0f; // Valor padrão de volume
        InputStream in = getClass().getResourceAsStream(filePath);
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        try {
            volume = Float.valueOf(input.readLine());
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        return volume;
    }

    public void StopMusic() {
        clip.stop();
    }

    public void StartMusic() {
        clip.start();
    }

}

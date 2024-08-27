package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.FloatControl;

public class WavPlayer {
    private Clip clip = null;
    public WavPlayer(){

    }

    public void playWavFile(String filePath, float volume) {
        try {
            File wavFile = new File(filePath); // Representa o arquivo .wav
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Ajustar o volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float final_volume = (volume*(gainControl.getMaximum()-gainControl.getMinimum())/100f)+gainControl.getMinimum();

            gainControl.setValue(final_volume); // volume em decibéis

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
        try {
            File file = new File(filePath);
            Scanner scan = new Scanner(file);

            while(scan.hasNextLine()){
                return Float.valueOf(scan.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo de volume não encontrado: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao ler o volume: " + e.getMessage());
        }
        return volume;
    }

    public void StopMusic(){
        clip.stop();
    }

    public void StartMusic(){
        clip.start();
    }

}

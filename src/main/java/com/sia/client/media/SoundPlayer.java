package com.sia.client.media;

import com.sia.client.config.Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import java.io.File;
import java.net.URL;

public class SoundPlayer {

	private static long lasttimesoundplayed = 1;

	public SoundPlayer(String file) {
		this(file,false);

	}
	public SoundPlayer(String file,boolean illcatch) {

			playSound(Utils.getMediaResource(file),illcatch);
	}
	
	public static void playSound(URL file, boolean illcatch) {
		long now = new java.util.Date().getTime();
		if(1000L < (now - lasttimesoundplayed)) { // this makes sure sounds dont play over each other
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file.toURI()).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (Exception ex) {
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
				if (illcatch) JOptionPane.showMessageDialog(null, "Error Playing File!");

			}
		}
	}
}
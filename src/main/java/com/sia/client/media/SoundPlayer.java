package com.sia.client.media;

import com.sia.client.config.Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import java.net.URL;

import static com.sia.client.config.Utils.log;

public class SoundPlayer {

	private static long lasttimesoundplayed = 1;
	public SoundPlayer(String file) {
		this(file,false);

	}
	public SoundPlayer(String file,boolean illcatch) {

			playSound(Utils.getMediaResource(file),illcatch);
	}
	
	public static synchronized void playSound(URL fileUrl, boolean illcatch) {
		long now = new java.util.Date().getTime();
		if(1000L < (now - lasttimesoundplayed)) { // this makes sure sounds dont play over each other
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileUrl);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (Exception ex) {
				log("Error with playing sound. sourfile url="+fileUrl);
				log(ex);
				if (illcatch) JOptionPane.showMessageDialog(null, "Error Playing Sound File!");

			}
		}
	}
}
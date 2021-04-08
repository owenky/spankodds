import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Date;
import javax.swing.JOptionPane;

public class SoundPlayer
{
	static long lasttimesoundplayed = 1;
	String file = "";
	boolean illcatch = false;
	
	public SoundPlayer(String file)
	{
		this(file,false);

	}
	
	public SoundPlayer(String file,boolean illcatch) 
	{
		this.file = file;
		this.illcatch = illcatch;
		long now = new java.util.Date().getTime();
		if(now-lasttimesoundplayed > 1000) // this makes sure sounds dont play over eachother
		{
			playSound(file); 
			lasttimesoundplayed = now;	
		}
		
	}
	
	public void playSound(String file) {
		
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch(Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
			if(illcatch) JOptionPane.showMessageDialog(null, "Error Playing File!");
			
		}
	}
	
		
	
	
	
}
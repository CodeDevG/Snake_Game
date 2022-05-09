import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
public class Game_Window extends JFrame{
	
	public Game_Window() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
	
		this.add(new Game_Panel()); 
		this.setResizable(false);
		this.setTitle("SNAKE");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(430,100);
		this.pack();
		this.setVisible(true);
	}

}

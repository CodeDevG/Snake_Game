import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.util.Random;
import java.io.IOException;
import javax.sound.sampled.*;

public class Game_Panel extends JPanel implements ActionListener {

	// ----------All attributes----------\\

	static final int PANEL_WIDTH = 500;
	static final int PANEL_HEIGHT = 500;
	static final int UNIT_DIMENSION = 25;
	static final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT) / (UNIT_DIMENSION * UNIT_DIMENSION);
	static int SPEED = 120;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int body_parts = 2;
	int apple_score = 0;
	int appleX;
	int appleY;
	char direction = 'D';
	boolean running = false;
	Timer timer;
	Random rand;
	File file01, file02;
	AudioInputStream audio01, audio02;
	Clip clip01, clip02;
	
	// ----------Constructor----------\\

	public Game_Panel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		// ----------Music and sound----------\\
		
		file01 = new File("SnakeMusic.wav");
		file02 = new File("GameOver.wav");

		audio01 = AudioSystem.getAudioInputStream(file01);
		audio02 = AudioSystem.getAudioInputStream(file02);

		clip01 = AudioSystem.getClip();
		clip02 = AudioSystem.getClip();

		clip01.open(audio01);
		clip02.open(audio02);

		// ----------Random----------\\

		rand = new Random();

		MoveKey keys = new MoveKey();

		// ----------Methods of panel----------\\

		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBackground(new Color(0, 153, 0));
		this.setFocusable(true);
		this.addKeyListener(keys);

		// ----------Start method----------\\

		Gamestart();
	}

	// ----------Method to start the game----------\\

	public void Gamestart() {

		newApple();
		running = true;
		timer = new Timer(SPEED, this);
		timer.start();
		clip01.start();

	}

	// ----------Draw the component----------\\

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	// ----------Draw apple,snake's body and snake's head----------\\

	public void draw(Graphics g) {

		// ----------If running is true----------\\

		if (running) {

			// ----------Optional : shows the grid of panel----------\\

			/*
			 * for(int i=0;i<PANEL_HEIGHT/UNIT_DIMENSION;i++) {
			 * g.drawLine(i*UNIT_DIMENSION,0,i*UNIT_DIMENSION,PANEL_HEIGHT);
			 * g.drawLine(0,i*UNIT_DIMENSION,PANEL_WIDTH,i*UNIT_DIMENSION); }
			 */

			// ----------Set the color of apple and creates it whit x and y position x and y
			// size----------\\

			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_DIMENSION, UNIT_DIMENSION);

			// ----------A for loop to create head green when i equals 0 and create body
			// with another shade of green ----------\\

			for (int i = 0; i < body_parts; i++) {
				if (i == 0) {
					g.setColor(new Color(51, 153, 255));
					g.fillRect(x[i], y[i], UNIT_DIMENSION, UNIT_DIMENSION);
				} else {
					g.setColor(new Color(0, 0, 255));
					g.fillRect(x[i], y[i], UNIT_DIMENSION, UNIT_DIMENSION);
				}
			}

			// ----------Set the score's text with Font,color and position----------\\

			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: " + apple_score, (PANEL_WIDTH - metrics.stringWidth("SCORE: " + apple_score)) / 2,
					g.getFont().getSize());

		} else {

			// ----------Call the GameOver method when running is false----------\\

			GameOver(g);
		}
	}
	// ----------Method to create an apple with random postion----------\\

	public void newApple() {
		appleX = rand.nextInt((int) (PANEL_WIDTH / UNIT_DIMENSION)) * UNIT_DIMENSION;
		appleY = rand.nextInt((int) (PANEL_HEIGHT / UNIT_DIMENSION)) * UNIT_DIMENSION;

		/*
		 * OPTIONAL : if you want to increment the difficulty 
		 * if(apple_score==5) {
		 * SPEED=110; 
		 * timer.setDelay(SPEED);
		 * }if(apple_score==10) { 
		 * SPEED=90;
		 * timer.setDelay(SPEED); 
		 * }if(apple_score==20) { 
		 * SPEED=70;
		 * timer.setDelay(SPEED); 
		 * }if(apple_score==30) {
		 *  SPEED=50;
		 * timer.setDelay(SPEED);
		 * }
		 */
	}

	// ----------Method to move the snake----------\\

	public void Move() {

		// ----------Shift body of snake----------\\

		for (int i = body_parts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		// ----------Switch to move the snake----------\\

		switch (direction) {

		case 'W':
			y[0] = y[0] - UNIT_DIMENSION;
			break;
		case 'S':
			y[0] = y[0] + UNIT_DIMENSION;
			break;
		case 'A':
			x[0] = x[0] - UNIT_DIMENSION;
			break;
		case 'D':
			x[0] = x[0] + UNIT_DIMENSION;
			break;
		}
	}

	// ----------Method to check if the apple have been eaten----------\\

	public void checkApple() {

		// ----------If the condition is true,add 1 to body and to score and spawns a
		// new apple randomly----------\\

		if ((x[0] == appleX) && (y[0] == appleY)) {
			body_parts++;
			apple_score++;
			newApple();
		}
	}

	// ----------Method to check the collisions----------\\

	public void checkCollisions() {

		// ----------Collision with body----------\\

		for (int i = body_parts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}

		// ----------Collision with left border----------\\

		if (x[0] < 0) {
			running = false;
		}

		// ----------Collision with right border----------\\

		if (x[0] > PANEL_WIDTH - UNIT_DIMENSION) {
			running = false;
		}

		// ----------Collision with top border----------\\

		if (y[0] < 0) {
			running = false;
		}

		// ----------Collision with bottom border----------\\

		if (y[0] > PANEL_HEIGHT - UNIT_DIMENSION) {
			running = false;
		}

		// ----------If running is false,the game stops----------\\

		if (!running) {
			timer.stop();
		}
	}

	// ----------Method to stop the game if you lose----------\\

	public void GameOver(Graphics g) {

		// ----------Final score----------\\

		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("SCORE: " + apple_score, (PANEL_WIDTH - metrics1.stringWidth("SCORE: " + apple_score)) / 2,
				g.getFont().getSize());

		// ----------Text "Game Over"----------\\

		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (PANEL_WIDTH - metrics2.stringWidth("GAME OVER")) / 2, PANEL_HEIGHT / 2);
		clip01.stop();
		clip02.start();
	}

	// ----------ActionPerformed that calls the methods
	// move,checkApple,checkCollision----------\\
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			Move();
			checkApple();
			checkCollisions();
		}

		// ----------Repaint method----------\\

		repaint();
	}

	// ----------A class that extends KeyAdapter to take the keys of your
	// keyboard----------\\

	public class MoveKey extends KeyAdapter {

		@Override

		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {

			case KeyEvent.VK_A:
				if (direction != 'D') {
					direction = 'A';
				}
				break;
			case KeyEvent.VK_D:
				if (direction != 'A') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_W:
				if (direction != 'S') {
					direction = 'W';
				}
				break;
			case KeyEvent.VK_S:
				if (direction != 'W') {
					direction = 'S';
				}
				break;
			}
		}
	}
}
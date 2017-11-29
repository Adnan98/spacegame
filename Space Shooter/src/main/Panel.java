package main;

import java.awt.Cursor;
import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GameState.GameStateManager;
//import GameState.MouseInput;


@SuppressWarnings({ "unused", "serial", "deprecation" })
public class Panel extends JPanel implements Runnable, KeyListener, MouseListener{
	
	/*
	 * THIS CLASS CONTAINS THE PANEL IN WHICH EVERYTHING IS DRAWN AND IT ALSO CREATES AND
	 * RUNS THE GAME LOOP. 
	 */
	
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//Används för att läsa in skärmens storlek
	//Följande kod läser in skärmens STORLEK
	public static final int WIDTH = (int) screenSize.getWidth();
	public static final int HEIGHT = (int) screenSize.getHeight();
	private Thread thread;//An object from the runnable class
	private boolean running;
	private int FPS = 30;//Antalet uppdatering per sekund
	private long targetTime = 1000 / FPS;//Antalet sekunder som går mellan varje uppdatering av skärmen

	private BufferedImage image;
	private Graphics2D g2d;

	private GameStateManager GSM;//see class GameStateManager for more information

	//Variabler för Muspekare och typsnitt 
	public static Font titleFont;
	public static Font regularFont;

	public Panel() {
		/*KONSTRUKTÖR*/
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));//Sätter storleken på panelen (fönstret)
			
		addMouseListener(this);
	
		BufferedImage cursorImage;
		try {
			cursorImage = ImageIO.read(getClass().getResourceAsStream("/UI/cursor.png"));//Read the image for the custom cursor
			Point cursorHotSpot = new Point(0,0);
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			//The following creates the custom cursor at the given point and then sets it as the cursor of this panel
			Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot ,"Cursor");
			this.setCursor(customCursor);


			java.io.InputStream stream;

			try {
				//Load the custom fonts
				stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Bonus/kenvector_future.ttf");
				titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
				
				stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Bonus/kenvector_future_thin.ttf");
				regularFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
				
			} catch (FontFormatException e) {e.printStackTrace();}

		} catch (IOException e) {e.printStackTrace();}
		
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();

		if (thread == null) {
			//Create a new thread for this panel and then start it
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void init() {
		//initialize the game loop and create the GSM object
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) image.getGraphics();
		running = true;
		GSM = new GameStateManager();
	}

	public void run() {// DETTA ÄR GAME LOOPEN, metoden run(); är ärvd från klassen "Runnable"
		init();
		long start;
		long elapsed;
		long wait;
		while (running) {
			start = System.nanoTime();
			update();
			draw();
			wait = targetTime;//Sleep this many milliseconds between each iteration of the loop
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	private void update() {
		GSM.update();
	}

	private void draw() {
		GSM.draw(g2d);
		Graphics graphics = getGraphics();
		graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		graphics.dispose();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		GSM.keyPressed(key.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent key) {
		GSM.keyReleased(key.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}


	public void mouseClicked(MouseEvent e) {
		GSM.mouseClick(e.getX(),e.getY());
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	

}

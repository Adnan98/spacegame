package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Background;
import main.Panel;

public class MenuState extends GameState {
	/*
	 * This is the MenuState. It is here the user starts and then chooses what to do
	 */

	private Background bg;
	public Font font;

	public BufferedImage button;
	public static int buttonWidth = 222;
	public static int buttonHeight = 39;

	public String[] options = {"Start","Help","Exit"};

	public MenuState(GameStateManager GSM){       
		this.GSM = GSM;

		try{
			bg = new Background("/Backgrounds/Space.png", 1);
			bg.setVector(-1, 0);
			button = ImageIO.read(getClass().getResourceAsStream("/UI/buttonYellow.png"));

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void init() {}

	@Override
	public void update() {bg.update();}

	@Override
	public void draw(Graphics2D g2d) {
		bg.draw(g2d);

		//Draws out the heading of the game
		g2d.setColor(Color.WHITE);
		g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 3F));
		drawCenteredText(g2d,"SPACE SHOOTER GAME", Panel.WIDTH/2, 250);

		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1.5F));
		g2d.setColor(Color.BLACK);

		int marginTop = 0;//Starting position for the buttons
		for(int i = 0; i < options.length; i++){
			//This loop goes through the options array and draws a button whith the option for each iteration
			g2d.drawImage(button, (Panel.WIDTH/2 - buttonWidth/2), (Panel.HEIGHT/2) + marginTop, null);
			drawCenteredText(g2d, options[i], Panel.WIDTH/2, Panel.HEIGHT/2 + marginTop + 30);
			marginTop += buttonHeight * 2;//Adds margin to the top of the next button
		}


	}

	protected void drawCenteredText(Graphics2D g2, String s, float centerX, float baselineY) {
		//This method occurs frequently throughout the program and it is used to draw text at the center of the panel
		FontRenderContext frc = g2.getFontRenderContext();
		Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
		float width = (float) bounds.getWidth();
		g2.drawString(s, centerX - width / 2, baselineY);
	}


	@Override
	public void keyPressed(int key) {		
	}
	@Override
	public void keyReleased(int key) {}

	public void mouseClick(int x, int y) {
		int mouseX = x;
		int mouseY = y;

		//First we only check if the click coordinates are within the possible X-postions of the buttons since they're all in the same vertival line, we only need to check this once
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			//Following if statements check if click was within the y-coordinates of a button. 
			
			if(mouseY > Panel.HEIGHT/2 && mouseY < Panel.HEIGHT/2 + MenuState.buttonHeight)
				GSM.setState(GameStateManager.LEVELSTATE);

			//Check if second button is pressed: "Help"
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*2 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*2))
				GSM.setState(GameStateManager.HELPSTATE);

			//Check if third button is pressed: "Exit"
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*4 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*4))
				System.exit(0);
			
		}

	}

}

package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Background;
import main.Panel;

public class MenuState extends GameState {

	private Background bg;
	public Font font;

	public BufferedImage button;
	public static int buttonWidth = 222;
	public static int buttonHeight = 39;

	public String[] options = {"Start","Help", "Highscores", "Exit"};

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
	public void init() {

	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public void draw(Graphics2D g2d) {
		bg.draw(g2d);

		g2d.setColor(Color.WHITE);
		g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 3F));
		drawCenteredText(g2d,"SPACE SHOOTER GAME", Panel.WIDTH/2, 250);

		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1.5F));
		g2d.setColor(Color.BLACK);

		int marginTop = 0;
		for(int i = 0; i < options.length; i++){
			g2d.drawImage(button, (Panel.WIDTH/2 - buttonWidth/2), (Panel.HEIGHT/2) + marginTop, null);
			drawCenteredText(g2d, options[i], Panel.WIDTH/2, Panel.HEIGHT/2 + marginTop + 30);
			marginTop += buttonHeight * 2;
		}


	}

	protected void drawCenteredText(Graphics2D g2, String s,
			float centerX, float baselineY) {
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

		//Check if first button is pressed: "Play"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 && mouseY < Panel.HEIGHT/2 + MenuState.buttonHeight){
				//System.out.println("Play");
				GSM.setState(GameStateManager.LEVELSTATE);
			}
		}

		//Check if second button is pressed: "Help"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*2 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*2)){
				GSM.setState(GameStateManager.HELPSTATE);
			}
		}

		//Check if third button is pressed: "HighScore"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*4 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*4)){
				GSM.setState(GameStateManager.SCORESTATE);
			}
		}

		//Check if third button is pressed: "Exit"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*6 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*6)){
				System.exit(0);
			}
		}

	}

}

package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import GameState.GameStateManager;
import main.Background;
import main.Panel;

public class PauseMenu {

	GameStateManager GSM;
	BufferedImage button, panel;
	int buttonWidth = 300;
	int buttonHeight = 39;
	public String[] options = {"Return to game", "Restart game", "Main menu", "exit"};
	
	public PauseMenu(GameStateManager gsm) {
		this.GSM = gsm;
		
		try{
			button = ImageIO.read(getClass().getResourceAsStream("/UI/buttonGreen.png"));
			panel = ImageIO.read(getClass().getResource("/uipack-space/PNG/glassPanel.png"));			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g2d) {
		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1.5F));
		g2d.setColor(Color.BLACK);
		
		AffineTransform at = AffineTransform.getTranslateInstance( Panel.WIDTH/2 - buttonWidth + 50,Panel.HEIGHT/2 - 200);
		at.scale(5.0, 5.0);
		g2d.drawImage(panel, at, null);
		int marginTop = -100;
		for(int i = 0; i < options.length; i++){
			g2d.drawImage(button, (Panel.WIDTH/2 - buttonWidth/2), (Panel.HEIGHT/2) + marginTop, buttonWidth, buttonHeight, null);
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

}

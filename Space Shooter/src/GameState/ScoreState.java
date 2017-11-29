package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import util.Scores;
import main.Background;
import main.Panel;

public class ScoreState extends GameState {

	GameStateManager GSM;
	Background bg;
	Scores scoresutil;
	Object[] scores;

	public ScoreState(GameStateManager gsm) {
		/*
		 * Here the player can view the top 10 highscores
		 */
		this.GSM = gsm;

		try{
			bg = new Background("/Backgrounds/Space.png", 1);
			bg.setVector(-1, 0);

		}
		catch(Exception e){
			e.printStackTrace();
		}

		scoresutil = new Scores();
		scores = scoresutil.loadScores();
	}

	@Override
	public void init() {}

	@Override
	public void update() {
		bg.update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(Graphics2D g2d) {
		bg.draw(g2d);

		g2d.setColor(Color.WHITE);
		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1F));
		drawCenteredText(g2d,"press [ESC] to go back", Panel.WIDTH/2, 30);
		drawCenteredText(g2d,"press [F5] to start game", Panel.WIDTH/2, 60);
		
		g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 4F));
		drawCenteredText(g2d,"HIGHSCORES", Panel.WIDTH/2, 300);
		
		g2d.setFont(Panel.titleFont.deriveFont(Panel.regularFont.getSize() * 1.5F));
		for (int i = 0; i < 10; i++) {
			drawCenteredText(g2d,
					((Map.Entry<String, Integer>) scores[i]).getKey() + ": " + Integer.toString(((Map.Entry<String, Integer>) scores[i]).getValue()), 

					Panel.WIDTH/2, 
					400 + i * 40);
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
		if(key == KeyEvent.VK_ESCAPE) GSM.setState(GSM.MENUSTATE);
		if(key == KeyEvent.VK_F5) GSM.setState(GSM.LEVELSTATE);
	}
	@Override
	public void keyReleased(int key) {}

	@Override
	public void mouseClick(int x, int y) {
		// TODO Auto-generated method stub

	}
}
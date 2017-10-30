package GameState;

public abstract class GameState {
	protected GameStateManager GSM;
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g2d);
	public abstract void keyPressed(int key);
	public abstract void keyReleased(int key);
	public abstract void mouseClick(int x, int y);
}

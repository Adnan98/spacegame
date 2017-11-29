package GameState;

import java.util.ArrayList;

public class GameStateManager {
	/*
	 * This is the most important class of the game. 
	 * It is responsible for invoking update and draw functions at the current running class
	 */
	
	private GameState[] gameStates;
	public int currentState;
	
	public static final int NUMGAMESTATES = 4;
	public static final int MENUSTATE = 0;
	public static final int LEVELSTATE = 1;
	public static final int HELPSTATE = 2;
	public static final int SCORESTATE = 3;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		currentState = MENUSTATE;
		loadState(currentState);//Load the current state
	}
	
	public void loadState(int state) {
		//Create an object for the respective GameState and add it to the 
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVELSTATE)
			gameStates[state] = new LevelState(this);
		if(state == HELPSTATE)
			gameStates[state] = new HelpState(this);
		if(state == SCORESTATE)
			gameStates[state] = new ScoreState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	
	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}
	
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	
	public void mouseClick(int x, int y){
		gameStates[currentState].mouseClick(x,y);
	}
	
}










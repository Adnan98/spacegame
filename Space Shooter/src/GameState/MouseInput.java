package GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.Panel;

public class MouseInput implements MouseListener {

	private GameStateManager GSM;

	public MouseInput(GameStateManager GSM){
		this.GSM = GSM;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();

		//Check if first button is pressed: "Play"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 && mouseY < Panel.HEIGHT/2 + MenuState.buttonHeight){
				System.out.println("Play");
			}
		}

		//Check if second button is pressed: "Help"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*2 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*2)){
				System.out.println("Help");
				GSM.setState(GameStateManager.LEVELSTATE);
			}
		}

		//Check if third button is pressed: "Exit"
		if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){
			if(mouseY > Panel.HEIGHT/2 + MenuState.buttonHeight*4 && mouseY < (Panel.HEIGHT/2 + MenuState.buttonHeight + MenuState.buttonHeight*4)){
				System.exit(0);
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

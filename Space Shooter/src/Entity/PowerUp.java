package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GameState.LevelState;

public class PowerUp {
	int xPos, yPos, width, height, type; 
	public ArrayList <BufferedImage> powerups;
	LevelState levelState;

	public PowerUp(LevelState l, int t, int x, int y){
		type = t;//The type ranges from 0 - 6
		xPos = x;
		yPos = y;
		levelState = l;

		powerups = new ArrayList<>();

		try {
			for(int i = 0; i <= 6; i++)
				powerups.add(ImageIO.read(getClass().getResource("/PNG/powerups/powerup"+i+".png")));
		} catch (IOException e) {e.printStackTrace();}


	}	

	public void collision(){
		//what the different powerups do when picked up
		switch(type){

		case 0:
			levelState.coins++;	
			break;

		case 1:
			levelState.player.boost = levelState.player.maxBoost;	
			break;
			
		case 2:
			levelState.player.health += levelState.player.maxHealth/4;
			break;
			
		case 3:
			levelState.player.fire += levelState.player.maxFire;
			break;
			
		case 4:
			levelState.player.shield = new Shield(levelState.player, 1);
			break;
			
		case 5:
			levelState.player.shield = new Shield(levelState.player, 2);
			break;

		case 6:
			levelState.player.shield = new Shield(levelState.player, 3);
			break;
		}
	}

	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.scale(0.75, 0.75);
		g2d.drawImage(powerups.get(type), at , null);
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, (int)(powerups.get(type).getWidth() * 0.75), (int)(powerups.get(type).getHeight() * 0.75));
	}

}

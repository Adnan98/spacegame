package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.Panel;

import util.CollisionDetection;

public class Station {

	BufferedImage image;

	public int width;
	public int height;

	public double xPos,yPos;

	public double rotate;
	
	public CollisionDetection CD;
	
	public Station(){
		try {
			image = ImageIO.read(getClass().getResource("/PNG/Station/spaceStation_024.png"));
		} catch (IOException e) {e.printStackTrace();}
		width = 276; 
		height = 400;
		xPos = (Panel.WIDTH/2) - width;
		yPos = Panel.HEIGHT/2- height;
		rotate = 0;
		
		CD = new CollisionDetection();
	}
	

	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(Math.toRadians(rotate), width/2, height/2);
		at.scale(2.0, 2.0);
		g2d.drawImage(image,at,null);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, width, height);
	}
	


}

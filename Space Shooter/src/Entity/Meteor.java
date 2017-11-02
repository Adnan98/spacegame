package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Meteor {

	BufferedImage image;

	public int type, color;

	public int width;
	public int height;

	public double xPos,yPos;
	public double vx, vy;

	public double rotate;
	String meteor_color;

	public Meteor(int c,int t, int x, int y){
		color = c;
		type = t;
		xPos = x;
		yPos = y;
		
		if(color == 0) meteor_color = "Brown";
		if(color == 1) meteor_color = "Grey";

		
		switch (type){
		
		
		case 4:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Meteors/meteor"+meteor_color+"_big3.png"));
			} catch (IOException e) {e.printStackTrace();}
			width = 89; height = 82;
			vx = randInt(-2,2);
			vy = randInt(-2,2);
			break;

		case 3:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Meteors/meteor"+meteor_color+"_med3.png"));
			} catch (IOException e) {e.printStackTrace();}
			width = 45; height = 40;
			vx = randInt(-4,4);
			vy = randInt(-4,4);
			break;

		case 2:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Meteors/meteor"+meteor_color+"_small2.png"));
			} catch (IOException e) {e.printStackTrace();}
			width = 29; height = 26;
			vx = randInt(-6,6);
			vy = randInt(-6,6);
			break;
			
		case 1:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Meteors/meteor"+meteor_color+"_tiny2.png"));
			} catch (IOException e) {e.printStackTrace();}
			width = 16; height = 15;
			vx = randInt(-8,8);
			vy = randInt(-8,8);
			break;
		}

	}
	
	public void update(){
		move();
	}
	
	public void move(){
		xPos += vx;
		yPos += vy;
		rotate += 1;
		if(rotate > 720)rotate -= 1;
		if(rotate < -720)rotate +=1;
	}

	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(Math.toRadians(rotate), width/2, height/2);
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

package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bullet {

	private boolean hit;
	private boolean remove;
	BufferedImage image;
	private int moveSpeed = 15;

	int width = 9;
	int height = 54;

	int playerWidth;
	int playerHeight;
	double xPos;
	double yPos;
	double rotate;
	double vX;
	double vY;

	public Bullet(int w, int h, double x, double y, double rotateDegree){
		this.playerWidth = w;
		this.playerHeight = h;
		this.rotate = rotateDegree;
		this.xPos = x;
		this.yPos = y;

		try{
			image = ImageIO.read(getClass().getResourceAsStream("/PNG/Lasers/laserBlue01.png"));
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setHit(){
		//This is the function that gets called to check if the FIREBALL has hit something
		if(hit) return;//If it has already hit, don't bother!
		hit = true;
	}

	public boolean shouldRemove(){ return remove; }

	public void update(){
		move();
	}

	public void move(){
		vX = moveSpeed * Math.sin(Math.toRadians(rotate));
		vY = -(moveSpeed * Math.cos(Math.toRadians(rotate)));

		xPos += vX;
		yPos += vY;

	}

	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(Math.toRadians(rotate), playerWidth/2, playerHeight/2);
		g2d.drawImage(image, at, null);

	}

	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, width, height);
	}


}

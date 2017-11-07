package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {

	private boolean hit;
	private boolean remove;
	BufferedImage image;
	int width;
	int height;
	int speed;
	int playerWidth;
	int playerHeight;
	public double xPos;
	public double yPos;
	double rotate;
	double vX;
	double vY;
	public int type;
	public int damage;
	int cost;//The cost of fire to shoot one bullet for the player

	public Bullet(int t, int w, int h, double x, double y, double rotateDegree){
		this.playerWidth = w;
		this.playerHeight = h;
		this.rotate = rotateDegree;
		this.xPos = x;
		this.yPos = y;
		this.type = t;

		try {
			image = ImageIO.read(getClass().getResource("/PNG/bullets/bullet"+type+".png"));
		} catch (IOException e) {e.printStackTrace();}
		
		width = image.getWidth();
		height = image.getHeight();
		damage = (type == 6 ? 400 : 100 * type);
		speed = 30 - type;
		cost = 100 * type;
		
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
		vX = speed * Math.sin(Math.toRadians(rotate));
		vY = -(speed * Math.cos(Math.toRadians(rotate)));

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

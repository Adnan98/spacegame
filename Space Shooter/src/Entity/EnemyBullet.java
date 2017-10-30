package Entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class EnemyBullet {

	private boolean hit;
	private boolean remove;
	BufferedImage image;
	private int moveSpeed = 15;

	int width = 9;
	int height = 54;

	double playerX;
	double playerY;
	double xPos;
	double yPos;

	double vx;
	double vy;

	public EnemyBullet(double x, double y, double px, double py){
		this.xPos = x;
		this.yPos = y;
		this.playerX = px;
		this.playerY = py;

		try{
			image = ImageIO.read(getClass().getResourceAsStream("/PNG/Lasers/laserRed08.png"));
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
		if(xPos < playerX)xPos+=5;
		if(yPos < playerY)yPos+=5;
		if(playerX < xPos)xPos -= 5;
		if(playerY < yPos)yPos -= 5;
	}

	public void draw(Graphics2D g2d){
		g2d.drawImage(image, (int) xPos, (int)yPos, null);

	}


}

package Entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnemyBullet {

	private boolean hit;
	private boolean remove;
	BufferedImage image;

	double playerX, playerY, xPos, yPos, vx, vy;
	int type, rotate, width, height, damage, speed;
	
	public EnemyBullet(int t, double x, double y, int angle){
		this.xPos = x;
		this.yPos = y;
		//this.playerX = px;
		//this.playerY = py;
		this.type = t;
		this.rotate = angle;
		
		switch (type){
		case 7:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Lasers/laserRed"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 48;
			height =  46;
			damage = 10;
			speed = 15;
			break;

		}

	}

	public void update(){
		move();
		rotate();
	}

	public void move(){
		xPos += Math.sin(rotate) * speed;
		yPos += Math.cos(rotate) * speed;
		
	}
	
	public void rotate() {
		
	}

	public void draw(Graphics2D g2d){
		g2d.drawImage(image, (int)xPos, (int)yPos, null);

	}


}

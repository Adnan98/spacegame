package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Panel;

public class EnemyBullet {

	private boolean hit;
	private boolean remove;
	BufferedImage image;
	Player player;
	Enemy enemy;
	double xPos, yPos, vx, vy, rotate;
	int type, width, height, damage, speed;
	
	public EnemyBullet(Enemy enemy, int t, double x, double y, Player  player){
		this.xPos = x;
		this.yPos = y;
		this.player = player;
		this.type = t;
		this.enemy = enemy;
		
		damage = 100 * type;
		speed = 15 - type;
		
		try {
			image = ImageIO.read(getClass().getResource("/PNG/enemybullets/bullet"+type+".png"));
		} catch (IOException e) {e.printStackTrace();}
		
		width = image.getWidth();
		height = image.getHeight();
		
		double dx = player.xPos - xPos;
		double dy = player.yPos - yPos;
		double theta = Math.atan(dx/dy);
		if(dy > 0) {
			vx = Math.sin(theta) * speed;
			vy = Math.cos(theta) * speed;
		}
		
		else {
			vx = -Math.sin(theta) * speed;
			vy = -Math.cos(theta) * speed;
		}
		
		double angle = Math.atan(Math.abs(dx)/Math.abs(dy));
			
			if(dy > 0) {
				if(dx > 0 ) rotate = Math.PI - angle;
				else if(dx < 0) rotate = Math.PI + angle;
			}
			
			else if(dy < 0) {
				if(dx > 0 ) rotate = angle;
				else if(dx < 0) rotate = -(angle);
			}
		
	}

	public void update(){
		move();		
		
	}

	public void move(){
		xPos += vx;
		yPos += vy;
	}
	
	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(rotate, enemy.width/2, enemy.health/2);
		g2d.drawImage(image,at,null);

	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, width, height);
	}


}

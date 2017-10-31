package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import GameState.LevelState;
import main.Panel;

public class Enemy {

	BufferedImage image;

	public int type, width ,height, speed;

	public double 
	xPos, yPos,
	vx, vy, 
	dx, dy,
	health,
	scale,
	rotate;

	public boolean dead;
	Player player;

	ArrayList<EnemyBullet> bullets;

	public Enemy(int t, int x, int y, Player p){
		player = p;
		xPos = x;
		yPos = y;
		type = t;

		switch (type){
		case 1:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 106;
			height =  80;
			health = 10;
			scale = 0.4;
			speed = randInt(10,14);
			break;

		case 2:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 101;
			height =  74;
			health = 30;
			scale = 0.5;
			speed = randInt(8,12);
			break;

		case 3:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 100;
			height =  94;
			health = 20;
			scale = 0.6;
			speed = randInt(8,10);
			break;

		case 4:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 126;
			height =  108;
			health = 100;
			scale = 1.0;
			speed = randInt(6,8);
			break;

		case 5:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 136;
			height =  84;
			health = 80;
			scale = 1.2;
			speed = randInt(4,6);
			break;

		case 6:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 94;
			height =  148;
			health = 90;
			scale = 1.4;
			speed = randInt(2,3);
			break;

		case 7:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 172;
			height =  151;
			health = 150;
			scale = 2.0;
			speed = 1;
			break;

		}

	}

	public void update(){
		if(health <= 0) {
			dead = true;
		}
		dx = player.xPos - xPos;
		dy = player.yPos - yPos;
		moveToPlayer();
		rotateToPlayer();
		
	}

	
	private void moveToPlayer() {
		//Denna funktion kontrollerar vart spelaren är och förflyttar fienden till spelaren
		double theta = Math.atan(dx/dy);
		
		double maxDistanceX = randInt(50 * type, 100 * type); 
		double maxDistanceY = randInt(50 * type, 100 * type); 		
		if(Math.abs(dx) > maxDistanceX  && Math.abs(dy) > maxDistanceY) {
			vx = Math.sin(theta) * speed;
			vy = Math.cos(theta) * speed;
		}
		
		else if(Math.abs(dx) < maxDistanceX && Math.abs(dy) < maxDistanceY) {
			vx = randInt(-5,5);
			vy = randInt(-5, 5);
		}
		
		else {
			vx = vy = 0;
		}
		
		if(dy < 0) {
			xPos -= vx;
			yPos -= vy;
		}
		
		else {
			xPos += vx;
			yPos += vy;
		}

	}
	
	private void rotateToPlayer() {
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

	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(rotate, width * scale /2, height * scale / 2);
		at.scale(scale, scale);
		g2d.drawImage(image, at, null);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, (int)(width* scale) , (int) (scale * height));
	}

	public void getDamage(int i) {
		//denna metod anropas när fienderna blir träffade av skott. 
		health -= i;

	}

}

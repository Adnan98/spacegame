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

	public int type, width ,height;
	public double xPos, yPos;
	public double playerX, playerY;
	public double vx, vy;
	Player player;
	int dX;
	int dY;
	public double health;
	public int damage;
	public double scale;

	public double rotate;
	public boolean dead;

	ArrayList<EnemyBullet> bullets;

	public Enemy(int t, int x, int y, Player p){
		xPos = x;
		yPos = y;

		switch (t){
		case 1:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 106;
			height =  80;
			health = 10;
			scale = 0.5;

			vx = randInt(-15,15);
			vy = randInt(-15,15);

			break;

		case 2:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Enemies/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 101;
			height =  74;
			health = 30;
			scale = 1.0;
			vx = randInt(-10,10);
			vy = randInt(-10,10);

			break;

		case 3:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Enemies/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 100;
			height =  94;
			health = 20;
			scale = 0.8;
			vx = randInt(-5,5);
			vy = randInt(-5,5);

			break;

		case 4:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Enemies/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 126;
			height =  108;
			health = 100;
			scale = 1.5;
			vx = randInt(-5,5);
			vy = randInt(-5,5);

			break;

		case 5:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Enemies/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 136;
			height =  84;
			health = 80;
			scale = 1.2;
			vx = randInt(-5,5);
			vy = randInt(-5,5);

			break;

		case 6:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Enemies/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 94;
			height =  148;
			health = 90;
			scale = 1.4;
			vx = randInt(-5,5);
			vy = randInt(-5,5);

			break;

		case 7:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Enemies/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 172;
			height =  151;
			health = 150;
			scale = 2.0;
			vx = randInt(-5,5);
			vy = randInt(-5,5);

			break;

		}

	}

	public void update(){
		moveToPlayer();
	}

	private void moveToPlayer() {
		//Denna funktion kontrollerar vart spelaren är och förflyttar fienden till spelaren
		
	}

	public void setDead(){
		dead = true;
	}

	public boolean isDead(){return dead;}

	public void draw(Graphics2D g2d){
		g2d.drawImage(image,(int) xPos, (int)yPos, null);
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

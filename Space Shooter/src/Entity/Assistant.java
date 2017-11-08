package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import main.Panel;
import GameState.LevelState;

public class Assistant{

	BufferedImage shadow;
	BufferedImage filling;
	BufferedImage image;

	public double 
	xPos, yPos,
	health,
	maxHealth,
	targetx,
	targety,
	speed,
	rotate,
	targetRotate;
	LevelState levelstate;
	int healthBarMaxWidth = 100;

	public boolean dead;

	ArrayList<Bullet> bullets;
	private int dt;
	private long shootTime;
	double scale = 0.75;
	private Enemy targetEnemy;


	public Assistant(int x, int y, LevelState l){
		bullets = new ArrayList<>();
		xPos = x;
		yPos = y;
		this.levelstate = l;

		try {
			image = ImageIO.read(getClass().getResource("/PNG/ufoGreen.png"));
			shadow = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_shadow_mid.png"));
			filling = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_green_mid.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shootTime = 0;

		speed = 6;
		targetx = randInt(50, Panel.WIDTH-50);
		targety = randInt(50, Panel.HEIGHT-50);
		rotate = 0;
	
		health = maxHealth = 6000;
		
		targetEnemy = levelstate.enemies.get(0);
	}

	public void update() {
		if((int)(targetx / 10) == (int)xPos / 10) {
			targetx = randInt(50, Panel.WIDTH-50);
			targety = randInt(50, Panel.HEIGHT-50);
		}
		
		if(targetEnemy.health <= 0 || !targetEnemy.getRectangle().intersects(new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT))) {
			targetEnemy = levelstate.enemies.get(randInt(0,  levelstate.enemies.size()-1));
		}

		move();
		shoot();
		rotate();
		//The time difference since last time shooted
		dt = (int) ((System.nanoTime() / 1000000000) - (shootTime / 1000000000));

		for(Bullet b : bullets)
			b.update();

		checkBulletCollision();
	}

	public void move() {
		if(!targetEnemy.getRectangle().intersects(new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT))) {
			double dx = targetx - xPos;
			double dy = targety - yPos;
			double theta = Math.atan(dx/dy);

			if(dy < 0) {
				xPos -= Math.sin(theta) * speed;
				yPos -=  Math.cos(theta) * speed;
			}

			else {
				xPos += Math.sin(theta) * speed;
				yPos +=  Math.cos(theta) * speed;
			}
		}
	}

	public void rotate() {
		double dx = targetx - xPos;
		double dy = targety - yPos;

		if(targetEnemy.getRectangle().intersects(new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT))) {
			dx = levelstate.enemies.get(0).xPos - xPos;
			dy = levelstate.enemies.get(0).yPos - yPos;
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

	public void shoot() {
		if(targetEnemy.getRectangle().intersects(new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT))) {
			double theta = Math.atan((targetEnemy.xPos - xPos) / (targetEnemy.yPos - yPos));
			
			if((targetEnemy.yPos - yPos) < 0)
				theta *= -1;

			if(dt >= 1) {
				bullets.add(new Bullet(
						1, //THe type of bullet
						image.getWidth(), //THe width of the sattelite
						image.getHeight(),// the height of the sattelite
						xPos + image.getWidth() * scale /2, //the center X coordiinate for this object
						yPos + image.getHeight() * scale /2,//the center Y coordiinate for this object
						Math.toDegrees(theta)//The angle to the target
						));
				shootTime = System.nanoTime();
			}
		}
	}

	public void checkBulletCollision(){
		for(int i = 0; i < bullets.size(); i++){
			for(int x = 0; x < levelstate.enemies.size(); x++){
				Enemy e = levelstate.enemies.get(x);//Get the current enemy 
				Rectangle rb = bullets.get(i).getRectangle();//Get the rectangle of the bullet
				Rectangle re = e.getRectangle();//Get the rectangle of the enemy
				Rectangle rp = new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT);//Get a rectangle representing the window

				if(rp.contains(rb)){
					//Only check for bullet collision if bullet is inside screen
					if(rb.intersects(re)){
						e.getDamage(bullets.get(i).damage);//Cause damage to the enemy

						bullets.get(i).damage -= 100 * e.type;
						if(bullets.get(i).damage <= 0) {		
							bullets.remove(i);
							i--;

						}
					}
				}
			}
		}
	}

	public void draw(Graphics2D g2d) {

		for(Bullet b : bullets)
			b.draw(g2d);

		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(rotate, image.getWidth()/2, image.getHeight()/2);
		at.scale(scale, scale);
		g2d.drawImage(image, at, null);

		//Följande ritar ut health-bar för satelit
		g2d.drawImage(shadow, (int)xPos, (int)yPos - 10, healthBarMaxWidth , shadow.getHeight() / 4, null);
		g2d.drawImage(filling, (int)xPos, (int)yPos - 10, (int)(healthBarMaxWidth * (health/maxHealth)), (filling.getHeight() / 4), null);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, (int)(image.getWidth()) , (int) (image.getHeight()));
	}

}


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

public class Satellite {

	BufferedImage shadow;
	BufferedImage filling;
	BufferedImage image;

	public int type;

	public double 
	xPos, yPos,
	health,
	maxHealth;
	LevelState levelstate;
	int healthBarMaxWidth = 100;

	public boolean dead;

	ArrayList<Bullet> bullets;
	private int dt;
	private long shootTime;

	public Satellite(int t, int x, int y, LevelState l){
		bullets = new ArrayList<>();
		xPos = x;
		yPos = y;
		type = t;
		this.levelstate = l;

		switch(type) {

		case 1:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/sattelite"+type+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			health = maxHealth = 3000;

			break;

		case 2:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/sattelite"+type+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			health = maxHealth = 5000;

			break;
		}
		
		try {
			shadow = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_shadow_mid.png"));
			filling = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_green_mid.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		shootTime = 0;
	}

	public void update() {
		shoot();

		//The time difference since last time shooted
		dt = (int) ((System.nanoTime() / 1000000000) - (shootTime / 1000000000));

		for(Bullet b : bullets)
			b.update();
		
		checkBulletCollision();
	}

	public void shoot() {
		Enemy e = levelstate.enemies.get(0);
		double theta = Math.atan((e.xPos - xPos) / (e.yPos - yPos));
		if((e.yPos - yPos) < 0)
			theta *= -1;
		
		if(dt >= (4+type)) {
			bullets.add(new Bullet(
					2 + type, //THe type of bullet
					image.getWidth(), //THe width of the sattelite
					image.getHeight(),// the height of the sattelite
					xPos + image.getWidth()/2, //the center X coordiinate for this object
					yPos + image.getHeight()/2,//the center Y coordiinate for this object
					Math.toDegrees(theta)//The angle to the target
					));
			shootTime = System.nanoTime();
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
		at.rotate(Math.toRadians(20), image.getWidth()/2, image.getHeight()/2);
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


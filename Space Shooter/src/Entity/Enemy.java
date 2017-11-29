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
	BufferedImage shadow;
	BufferedImage filling;

	public int type, width ,height, speed;

	public double 
	xPos, yPos,
	vx, vy, 
	dx, dy,
	health,
	maxHealth,
	rotate;

	int healthBarMaxWidth = 100;

	public boolean dead;
	Player player;

	ArrayList<EnemyBullet> bullets;
	private int target;
	Rectangle rpanel;
	
	public Enemy(int t, int x, int y, Player p){
		bullets = new ArrayList<>();
		player = p;
		xPos = x;
		yPos = y;
		type = t;

		//Depending on the type of enemy the attributes are changed
		switch (type){
		case 1:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 106;
			height =  80;
			maxHealth = health  = 100;
			speed = randInt(20,28);
			break;

		case 2:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}

			width = 101;
			height =  74;
			maxHealth = health  = 300;
			speed = randInt(16,24);
			break;

		case 3:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 100;
			height =  94;
			maxHealth = health  = 200;
			speed = randInt(16,20);
			break;

		case 4:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 126;
			height =  108;
			maxHealth = health  = 1000;
			speed = randInt(12,16);
			break;

		case 5:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 136;
			height =  84;
			maxHealth = health  = 800;
			speed = randInt(8,12);
			break;

		case 6:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 94;
			height =  148;
			maxHealth = health  = 900;
			speed = randInt(4,6);
			break;

		case 7:
			try {
				image = ImageIO.read(getClass().getResource("/PNG/Ships/spaceShips_00"+type+".png"));
			} catch (IOException e) {e.printStackTrace();}	

			width = 172;
			height =  151;
			maxHealth = health  = 1500;
			speed = 1;
			break;

		}


		try {
			//Load the health-bar images 
			shadow = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_shadow_mid.png"));
			filling = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_yellow_mid.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//What to moveto/shoot at
		target = 0;

		if(player.satellites.size() > 0){
			//The enemy randomly selects if it wants to shoot at the player or at a sattelite
			target = randInt(0,1);
		}

		rpanel = new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT);//rectangle panel

	}

	public void update(){
		if(health <= 0) {
			dead = true;
		}

		if(target == 1 && player.satellites.get(0) == null){
			target = 0;
		}
		
		//get the difference of x and y coordinates from the player to the enemy
		dx = (target == 0 ? player.xPos : player.satellites.get(0).xPos) - xPos;
		dy = (target == 0 ? player.yPos : player.satellites.get(0).yPos) - yPos;
		moveToPlayer();
		rotateToPlayer();
		shoot();
		playerBulletCollision();
	}


	private void moveToPlayer() {
		//Denna funktion kontrollerar vart spelaren �r och f�rflyttar fienden till spelaren
		//The anlge between the player and the enemy
		double theta = Math.atan(dx/dy);

		//The maximum distance the enemy can move towards the player
		double maxDistanceX = randInt(75 * type, 100 * type); 
		double maxDistanceY = randInt(75 * type, 100 * type); 		
		if(Math.abs(dx) > maxDistanceX  && Math.abs(dy) > maxDistanceY || !rpanel.contains(getRectangle())){ //) {
			//If the enemy is id not within the screen or within the maximum distance from the player:
			vx = Math.sin(theta) * speed;
			vy = Math.cos(theta) * speed;
		}

		else if(Math.abs(dx) < maxDistanceX && Math.abs(dy) < maxDistanceY) {
			//If the enemy is in the zone, it randomly moves around
			vx = randInt(-5,5);
			vy = randInt(-5, 5);
		}

		else {
			vx = vy = 0;
		}

		//Depending on the position of the player relative to the enemy, we either subrtract or add the vectors to the enemy's position
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
		//This function determines the angle of rotation that makes the enemy face the player
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
		for(int i = 0; i < bullets.size(); i++){
			//remove/update bullets
			EnemyBullet b  = bullets.get(i);
			if(b.xPos < -100 || b.xPos > Panel.WIDTH+100 || b.yPos < -100 || b.yPos > Panel.HEIGHT + 100) {
				//If the bullet foes outside the screen.
				bullets.remove(i);
				i--;
			}
			else {
				bullets.get(i).update();
			}

		}	

		if(bullets.size() < 1) {
			
			//calculate the center coordinates, where the bullet will be created
			double centerx = xPos;
			double centery = yPos;
			double rotateDegrees = Math.toDegrees(rotate);

			if(rotateDegrees < 90){
				centerx = xPos + (width ) / 2;
				centery = yPos +(height )/ 2;
			}
			if(rotateDegrees > 90 && rotateDegrees < 180){
				centerx = xPos - (width ) / 2;
				centery = yPos +(height )/ 2;
			}
			if(rotateDegrees > 180 && rotateDegrees < 270){
				centerx = xPos - (width ) / 2;
				centery = yPos -(height )/ 2;
			}
			if(rotateDegrees > 270 && rotateDegrees < 360){
				centerx = xPos + (width ) / 2;
				centery = yPos -(height )/ 2;
			}

			//get the targets coordinates
			double targetX = (target == 0 ? player.xPos : player.satellites.get(0).xPos);
			double targetY = (target == 0 ? player.yPos : player.satellites.get(0).yPos);

			EnemyBullet b = new EnemyBullet(this, type, centerx, centery, targetX, targetY);
			bullets.add(b);
		}
	}

	public void draw(Graphics2D g2d){
		//Följande ritar ut alla bullets för enemy
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw((Graphics2D) g2d);
		}

		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(rotate, width  /2, height  / 2);
		g2d.drawImage(image, at, null);

		//The following draws a health bar above the enemies
		g2d.drawImage(shadow, (int)xPos, (int)yPos, healthBarMaxWidth , shadow.getHeight() / 4, null);
		g2d.drawImage(filling, (int)xPos, (int)yPos, (int)(healthBarMaxWidth * (health/maxHealth)), (filling.getHeight() / 4), null);

	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, (int)(width) , (int) (height));
	}

	public void getDamage(int i) {
		//denna metod anropas n�r fienderna blir tr�ffade av skott. 
		health -= i;

	}

	public void playerBulletCollision() {
		//Denna metod kontrollerar om fiendens skott träffar spelaren
		for(int i = 0; i < bullets.size(); i++){
			Rectangle rb = bullets.get(i).getRectangle();//rectangle bullet

			if(rpanel.contains(rb)){
				//Only check for bullet collision if bullet is inside screen
				if(target == 0){
					Rectangle rp = player.getRectangle();//rectangle player

					if(player.shield != null) {
						//this means the player has shield activated and cannot take damage, the shield will take damage instead
						Rectangle rs = player.shield.getRectangle();
						if(rb.intersects(rs))	{
							player.shield.health -= bullets.get(i).damage;
							bullets.remove(i);
							i--;
						}
					}
					else {//no shield -> the player will take damage
						if(rb.intersects(rp)) {
							player.health -= bullets.get(i).damage;
							bullets.remove(i);
							i--;
						}
					}

				}

				else{
					//if the enemy is shooting at a sattelite
					Rectangle rs = player.satellites.get(0).getRectangle();
					if(rb.intersects(rs)){
						player.satellites.get(0).health -= bullets.get(i).damage;
						
						if(player.satellites.get(0).health <= 0) {
							player.satellites.remove(0);
							target = 0;
						}
						
						bullets.remove(i);
						i--;
					}
				}
			}

		}
	}

}

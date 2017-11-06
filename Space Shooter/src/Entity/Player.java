package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import util.UI;

import GameState.LevelState;
import main.Panel;

public class Player {

	double xPos = 500;
	double yPos = 300;
	int width = 99;
	int height = 75;
	double scale = 0.8; 
	double centerX;
	double centerY;

	public boolean alive;
	public double health;
	public double maxHealth;

	public double fire = 2000;
	public double maxFire = fire;

	public double boost = 1000;
	public double maxBoost = boost;
	int boostCost = 10;

	double moveSpeed = 2;
	double maxSpeed = 5;
	double boostSpeed = 10;
	double stopSpeed = 0.1;

	double rotateDegrees;
	double rotateSpeed = 1;
	double maxRotateSpeed = 3;
	double stopRotateSpeed = 0.1;

	double vectorX;
	double vectorY;
	double vectorHypo = 0;
	double rotateVector;

	public boolean forward;
	public boolean backward;
	public boolean right;
	public boolean left;
	public boolean boosting;
	public boolean firing;
	BufferedImage image;
	BufferedImage damage_image;
	BufferedImage damage_image_1;
	BufferedImage damage_image_2;
	BufferedImage damage_image_3;

	long shootTime;
	public ArrayList<Bullet> bullets;
	UI ui;
	public int bulletType;
	public double dt;
	public Shield shield;

	public Player(){

		alive = true;
		width = 99;
		height = 75;

		bullets = new ArrayList<>();

		maxHealth = health = 10000;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/PNG/playerShip1_green.png"));
			damage_image_1 = ImageIO.read(getClass().getResourceAsStream("/PNG/Damage/playerShip1_damage1.png"));
			damage_image_2 = ImageIO.read(getClass().getResourceAsStream("/PNG/Damage/playerShip1_damage2.png"));
			damage_image_3 = ImageIO.read(getClass().getResourceAsStream("/PNG/Damage/playerShip1_damage3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ui = new UI(this);
		bulletType = 1;
		shield = null;

	}

	public void update(){	
		if(health < 0){
			alive = false;
			LevelState.gameOver = true;

		}

		if(alive){
			getNextPosition();
			getRotation();
			move();
			keepInsideBounds();
			fire += 2;
			if(fire > maxFire) fire = maxFire;

			health++;
			if(health>maxHealth)health = maxHealth;

			if(!boosting){//If not boosting, restore speed to original
				maxSpeed = 5;
			}

			//The time difference since last time shooted
			dt = ((System.nanoTime() / 1000000) - (shootTime / 1000000));
			shoot();

			if(health <= maxHealth * 3/4 )
				damage_image = damage_image_1;
			if(health <= maxHealth * 2/4)
				damage_image = damage_image_2;
			if(health <= maxHealth * 1/4)
				damage_image = damage_image_3;

			if(shield != null) {
				shield.health -= 0.5;
				if(shield.health <= 0)
					shield = null;
			}
		}

		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
		}	


	}

	private void getNextPosition(){
		//Determine where the next position of the player is, by reading in the keyboard input:
		if(forward || backward){
			if(boosting){
				if(boost > boostCost){
					maxSpeed = boostSpeed; 
					boost -= boostCost;	
				}
				else boosting = false;
			}//Move faster if the user is pressing SHIFT

			//If the player is moving forw or backw we need to increse the hypothenusa vector and cap it out at max speed
			vectorHypo += moveSpeed;	

			if(vectorHypo > maxSpeed) {
				vectorHypo = maxSpeed;
			}

			if(forward) {
				//The real vectors, vectorX and vectorY are components of the vectorHypo and we'll calculate them
				//with the angle of rotation and some Trigonometry
				vectorX = vectorHypo * Math.sin(Math.toRadians(rotateDegrees));
				vectorY = -(vectorHypo * Math.cos(Math.toRadians(rotateDegrees)));
			}

			else if(backward) {
				vectorX = -(vectorHypo/2) * Math.sin(Math.toRadians(rotateDegrees));
				vectorY = (vectorHypo/2) * Math.cos(Math.toRadians(rotateDegrees));
			}

		}
		//If you stop moving, smoothly decreas the speed and stop
		else{
			vectorX -= stopSpeed;
			if(vectorX < 0)vectorX = 0;
			vectorY -= stopSpeed;
			if(vectorY < 0)vectorY = 0;
		}
	}


	public void getRotation(){

		if(left){
			rotateVector -= rotateSpeed;
			if(rotateVector < -maxRotateSpeed){
				rotateVector = -maxRotateSpeed;
			}
		}

		else if(right){
			rotateVector += rotateSpeed;
			if(rotateVector >  maxRotateSpeed){
				rotateVector = maxRotateSpeed;
			}
		}

		else{
			if(rotateVector > 0){
				rotateVector -= stopRotateSpeed;
				if(rotateVector < 0){
					rotateVector = 0;
				}
			}

			else if(rotateVector < 0){
				rotateVector += stopRotateSpeed;
				if(rotateVector > 0){
					rotateVector = 0;
				}
			}
		}
	}


	public void move(){
		yPos += vectorY;
		xPos += vectorX;
		rotateDegrees += rotateVector;
		if(rotateDegrees > 360)rotateDegrees = 0;//We dont want the angle of rotation to exceed 360 deg so reset it to 0
		if(rotateDegrees < 0)rotateDegrees = 360;
	}

	public void keepInsideBounds(){
		if(xPos<0)xPos = 0; 
		if(xPos > Panel.WIDTH - height) xPos = Panel.WIDTH - height;
		if(yPos < 0 )yPos = 0;
		if(yPos > Panel.HEIGHT - height) yPos = Panel.HEIGHT - height;
		//remove the bullets if they go outside the screen
		for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			if(b.xPos < 0 || b.xPos > Panel.WIDTH || b.yPos < 0 || b.yPos > Panel.HEIGHT) {
				bullets.remove(i);
				i--;
			}
		}
	}

	public void shoot(){
		if(firing){
			double centerx = xPos;
			double centery = yPos;

			if(rotateDegrees < 90){
				centerx = xPos + width / 2;
				centery = yPos + height / 2;
			}
			if(rotateDegrees > 90 && rotateDegrees < 180){
				centerx = xPos - width / 2;
				centery = yPos + height / 2;
			}
			if(rotateDegrees > 180 && rotateDegrees < 270){
				centerx = xPos - width / 2;
				centery = yPos - height / 2;
			}
			if(rotateDegrees > 270 && rotateDegrees < 360){
				centerx = xPos + width / 2;
				centery = yPos - height / 2;
			}

			Bullet b = new Bullet(bulletType, width, height, centerx, centery, rotateDegrees);

			if(fire > b.cost){
				if(b.type > 2) {
					//If the player has selected a missile, it can only shoot one missile every 2 seconds and not as a stream
					if((dt/1000) > b.type) {
						fire -= b.cost;
						bullets.add(b); 
						shootTime = System.nanoTime();
					}

				}

				else {
					//If the player has selected lasers it can shoot them as a stream
					fire -= b.cost;
					bullets.add(b); 
					shootTime = System.nanoTime();
				}
			}

			else {
				b = null;//Deletes the bullet since it could not be created due to low ammo
			}
		}
	}


	public void draw(Graphics2D g2d){	
		try {
			ui.draw(g2d);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Printa alla skott
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw((Graphics2D) g2d);
		}

		//printa ut spelaren 
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.rotate(Math.toRadians(rotateDegrees), width/2, height/2);
		g2d.drawImage(image,at,null);
		g2d.drawImage(damage_image,at,null);

		if(shield != null) shield.draw(g2d);

	}


	public void enemyBulletCollision(Enemy e) {
		//Denna metod kontrollerar om spelarens skott tr�ffar n�gon fiende 
		for(int i = 0; i < bullets.size(); i++){
			Rectangle rb = bullets.get(i).getRectangle();
			Rectangle re = e.getRectangle();
			Rectangle rp = new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT);

			if(rp.contains(rb)){
				//Only check for bullet collision if bullet is inside screen
				if(rb.intersects(re)){
					e.getDamage(bullets.get(i).damage);

					Bullet b = bullets.get(i);
					b.damage -= 10 * e.type;
					if(b.damage <= 0) {
						b = null;
						bullets.remove(i);
						i--;
					}
				}
			}

		}
	}



	public Rectangle getRectangle() {
		return new Rectangle((int) xPos, (int) yPos, width, height);
	}






}

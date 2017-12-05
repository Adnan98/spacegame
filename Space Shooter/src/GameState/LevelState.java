package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import Entity.Assistant;
import Entity.Bullet;
import Entity.Enemy;
import Entity.Meteor;
import Entity.Player;
import Entity.PowerUp;
import Entity.Satellite;
import main.Panel;
import util.PauseMenu;
import util.Scores;

public class LevelState extends GameState {
	/*
	 * THIS IS THE BIGGEST AND MOST IMPORTANT CLASS TO THE GAME IN THE SENSE THAT THE GAME ITSELF IS RUN IN THIS CLASS
	 * similarily to othe gamestates it has the updat and draw functions to run
	 * Moreover there a alot of objects i.e. the player that are managed in the class
	 */

	public boolean paused;//To check if the player has paused the game (public because need to be accessed from PauseMenu)
	boolean started = false;//If the player presses enter the game starts
	public boolean gameOver;

	PauseMenu menu;
	Scores scores;
	Object[] highscores;//An array of objects for the highscores (which is like a dictionary)
	BufferedImage bg;
	
	public Player player;
	Meteor meteor;
	Enemy enemy;
	ArrayList<PowerUp> powerups;
	ArrayList<Meteor> meteors;//Array of all meteor objects in the game
	public ArrayList<Enemy> enemies;
	//The following are the maximum coordinates where objects can go. (the player can only move in the screen)
	int worldLimitX = Panel.WIDTH *2;
	int worldLimitY = Panel.HEIGHT *2;

	//THe maximum amount of meteors and enemies possible on the field
	int maxMeteor = 100;
	public int maxEnemy = 1;//The number will be increased later
	double points;
	int divident = 10;//THe real score is points/divident
	int time = 0;//current Time
	long startTime;//Start time of the game
	int elapsed;//How many seconds have elapsed since started
	public int coins;//The amont of coins the player has

	public LevelState(GameStateManager GSM){
		this.GSM = GSM;
		menu = new PauseMenu(GSM);//create new pausemenu object
		scores = new Scores();//Create and object for the Score class

		try {
			//Ladda bakgrundsbilden 
			bg = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/blue.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	//public static  int getCoins(){return coins;}


	public void init() {
		//This function is invoked everytime the player starts a game. Variables are resetted
		highscores = scores.loadScores();//Load the highscores 
		paused = false;
		gameOver = false;
		started = true;
		
		player = new Player(this);				
		powerups = new ArrayList<>();
		meteors = new ArrayList<>();
		enemies = new ArrayList<>();
		
		coins  = 0;
		points = 0;
		startTime = System.nanoTime();
		elapsed = 0;
	}


	public void update() {

		if(player.alive && !paused){
			//As long as the player is alive and the user has not paused the game:
			elapsed = (int) ((System.nanoTime() - startTime)/1000000000);

			if(points/divident > (maxEnemy * 10 * divident)) {
				/*
				 * The number of enemies (maxEnemy) will increase depending on the score
				 * if maxenemy = 1: increase score atfter 100 points
				 * if maxenemy = 2: increase score atfter 200 points
				 * if maxenemy = 3: increase score atfter 300 points
				 * ...
				 */
				maxEnemy++;
			}

			spawnMeteor();
			removeMeteor();
			spawnEnemy();
			removeEnemy();
			checkBulletMeteorCollision();
			checkPlayerCollision();
			checkPowerupCollision();
			player.update();

			for(Enemy e : enemies)player.enemyBulletCollision(e);
			for(Meteor m : meteors) m.update();

			for(int i = 0; i < enemies.size(); i++){
				Enemy e = enemies.get(i);
				e.update();
				if(e.dead){
					enemies.remove(i);//If enemy died, remove it
					i--;
					points += e.type * 100;//Get points for killing enemy
					//Add new powerup to the field
					powerups.add(new PowerUp(this, randInt(0, (player.shield == null ? 6 : 3)), (int)e.xPos , (int)e.yPos));
					e = null;
				}
			}			

		}

		else if(!player.alive){
			gameOver = true;
		}

	}

	public void spawnMeteor(){
		if(meteors.size() < maxMeteor){
			//This method created a new meteor if there are not enough on the field

			//The following code determines the starting position of a new meteor.
			//If the startingposition is in the visible zone of the player, a new postion is generated
			//This makes sure the Mets are always spawned outside thevisible screen
			int meteorX = 100;
			while(meteorX > 0 && meteorX < Panel.WIDTH){meteorX = randInt(-worldLimitX, worldLimitX);}

			int meteorY = 100;
			while(meteorY > 0 && meteorY < Panel.HEIGHT){meteorY = randInt(-worldLimitY, worldLimitY);}

			meteor = new Meteor(randInt(0,1), randInt(1,4), meteorX, meteorY);
			meteors.add(meteor);
		}		
	}

	private void removeMeteor() {
		//If the metor goes outside the visible zone: remove it
		for(int i = 0; i < meteors.size(); i++){
			if(meteors.get(i).xPos< -worldLimitX || meteors.get(i).xPos>worldLimitX|| meteors.get(i).yPos<-worldLimitY|| meteors.get(i).yPos> worldLimitY){
				meteors.remove(i);
				i--;
			}
		}		
	}

	public void spawnEnemy(){
		if(enemies.size()<maxEnemy){

			//Determine which enemy to spawn:
			int  maxType = 1;
			for(int i = 0; i < 7; i++) {
				//depeniding on the score, different type of enemies will spawn
				//As the score increases more difficult enemies will appear.
				if(points/divident > i*5*divident) {
					maxType = 1 + i;
				}
			}

			int enemyType = randInt(1, maxType);
			
			//Get spawning position outside the visible field
			int enemyX = 100;
			while(enemyX > 0 && enemyX < Panel.WIDTH){enemyX = randInt(-worldLimitX, worldLimitX);}
			int enemY = 100;
			while(enemY > 0 && enemY < Panel.HEIGHT){enemY = randInt(-worldLimitY, worldLimitY);}

			enemy = new Enemy(enemyType, enemyX, enemY, player);
			//add the enemy to the array
			enemies.add(enemy);
		}		
	}

	private void removeEnemy() {
		//If the enemy goes outside the visible zone: remove it (save memeory)
		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).xPos< -worldLimitX || enemies.get(i).xPos>worldLimitX|| enemies.get(i).yPos<-worldLimitY|| enemies.get(i).yPos> worldLimitY){
				enemies.remove(i);
				i--;
			}
		}		
	}


	private void checkBulletMeteorCollision() {
		//This function checks enemy's bullets collision withe meterors
		for(int i = 0; i < player.bullets.size(); i++){
			Rectangle rb = player.bullets.get(i).getRectangle();
			Rectangle rp = new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT);

			if(rp.contains(rb)){
				//If the bullet is inside the Panel:
				for(int x = 0; x < meteors.size(); x++){
					//loop theough all meteors.
					Meteor m = meteors.get(x);
					Rectangle rm = m.getRectangle();

					if(rb.intersects(rm)){
						//If the bullet intersects the meteor
						maxMeteor += 2;

						if(m.type > 1){
							//If it's not the smallest type, it explodes in to smaller metoerites
							int cx = (int) m.xPos;
							int cy = (int) m.yPos;
							int type = m.type;
							points += 10 * type;
							meteors.remove(x);
							for(int j = 0; j < type; j++){
								//Adde new, smaller meteorites where the previous was destroyed
								meteor = new Meteor(m.color, type-1, cx, cy);
								meteors.add(meteor);
							}

							if(m.type > 2){
								//Only the big stones drop new powerups
								powerups.add(new PowerUp(this, randInt(0, (player.boost < player.maxBoost / 4 ? 1 : 0) ), cx, cy));
							}
						}		

						else{//if it's a small metoer just remove it
							meteors.remove(x);
							x--;
							points += 10;
						}

						//Add damage to the bullets and remove it if the damage is lost
						Bullet b = player.bullets.get(i);
						b.damage -= 100 * m.type;
						if(b.damage <= 0) {
							if(b.type == 6) {
								//Only the type 6 missile explodes into smaller bullets
								for(int k = 0; k < 10; k++){
									player.bullets.add(new Bullet(1, player.WIDTH, player.HEIGHT, b.xPos, b.yPos, randInt(0,360)));
								}
							}
							b = null;
							player.bullets.remove(i);
							i--;
						}
					}
				}

			}
		}
	}

	private void checkPlayerCollision() {
		//Check if either any enemy or meteor collides with the player and get damage
		Rectangle rp = player.getRectangle();

		for(int i = 0; i < meteors.size(); i++){
			Meteor m = meteors.get(i);
			Rectangle rm = m.getRectangle();
			int type = m.type;

			if(rp.intersects(rm)){
				player.health -= 10 * type;
			}
		}

		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			Rectangle re = e.getRectangle();
			int type = e.type;
			if(rp.intersects(re)){
				player.health -= 50 * type;
			}
		}

	}

	public void checkPowerupCollision(){
		//This function checks if the player is picking up any poweups.
		//If the player collides, the powerup is removed and collision() function in PowerUp is called
		for(int i = 0; i < powerups.size(); i++){
			PowerUp p = powerups.get(i);
			Rectangle rect_powerup = p.getRectangle();
			Rectangle rect_player = player.getRectangle();
			if(rect_player.intersects(rect_powerup)){
				p.collision();
				powerups.remove(i);
				i--;
			}
		}
	}


	@SuppressWarnings("unchecked")
	public void draw(Graphics2D g2d) {

		g2d.setColor(Color.white);
		int bgSize = 256;
		//Loop through to automatically draw the BG as tiles: First Columns, then Rows
		for(int i = 0; i < (Panel.WIDTH / bgSize) + 1; i++){
			for(int x = 0; x < (Panel.HEIGHT/ bgSize) + 1; x++ ){
				g2d.drawImage(bg, bgSize * i, bgSize * x, null);
			}
		}

		if(!started) {
			g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 4F));
			drawCenteredText(g2d,"PRESS ENTER TO START", Panel.WIDTH/2, Panel.HEIGHT/2);

			g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1F));
			drawCenteredText(g2d,"press ESC anytime to pause the game", Panel.WIDTH/2, Panel.HEIGHT/2 + 100);
		}

		//Print out the score: 
		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1F));
		
		g2d.drawString("Score: "+ (int)points/ divident, Panel.WIDTH-200, 50);

		for(PowerUp p : powerups){
			p.draw(g2d);
		}


		for(int i = 0; i < enemies.size(); i++){
			//Kontrollera ifalll objektet �r utanf�r den synliga delen av planen. Om den �r det hoppar den 
			//�ver iterationen. Om den �r inuti den synliga delen s� ritas den ut. 
			Enemy e = enemies.get(i);
			if(e.xPos < -100 || e.xPos > Panel.WIDTH + 100|| e.yPos < -100 || e.yPos > Panel.HEIGHT + 100) {
				continue;
			}
			else {
				e.draw(g2d);
			}
		}

		player.draw(g2d);

		for(int i = 0; i < meteors.size(); i++){
			//Kontrollera ifalll objektet �r utanf�r den synliga delen av planen. Om den �r det hoppar den 
			//�ver iterationen. Om den �r inuti den synliga delen s� ritas den ut. 
			Meteor m = meteors.get(i);
			if(m.xPos < -10 || m.xPos > Panel.WIDTH + 10|| m.yPos < -10 || m.yPos > Panel.HEIGHT + 10) {
				continue;
			}
			else {
				m.draw(g2d);
			}
		}

		if(paused) menu.draw(g2d);//draw the pause menu if necessary


		//Print GAME OVER and score if gameOver
		if(gameOver && !paused){
			g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1F));
			drawCenteredText(g2d,"press F5 to restart", Panel.WIDTH/2, 30);

			int startX = Panel.HEIGHT/2 - 200;
			g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 4F));
			drawCenteredText(g2d,"GAME OVER", Panel.WIDTH/2, startX);
			g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 2F));
			drawCenteredText(g2d,"Score: "+ (int) points/divident, Panel.WIDTH/2, startX + 100);
			drawCenteredText(g2d,"Time: "+elapsed +" seconds", Panel.WIDTH/2, startX + 150);
			drawCenteredText(g2d,"Enter your name in the console to save your score", Panel.WIDTH/2, Panel.HEIGHT-50);

			if(elapsed == 0) {
				String name = "";
				if((int)points/divident > ((Map.Entry<String, Integer>) highscores[0]).getValue()){
					//This means the the player beat the highcscore
					//Display the message that the player beat the highscore:
					drawCenteredText(g2d,"CONGRATS "+name+"! NEW HIGHSCORE", Panel.WIDTH/2, startX + 250);

				}

				else {
					//If the player didn't beat high score: print the top 3
					drawCenteredText(g2d,"HIGHSCORES", Panel.WIDTH/2, startX + 250);
					for (int i = 0; i < 3; i++) {
						drawCenteredText(g2d,((Map.Entry<String, Integer>) highscores[i]).getKey() + ": " + Integer.toString(((Map.Entry<String, Integer>) highscores[i]).getValue()), Panel.WIDTH/2, startX + 300 + i * 50);
					}
			
				//The players name is taken in from the console input and then we save the score 
				//with the help of the Scores object
				System.out.println("ENTER YOUR NAME: ");
				name = TextIO.getlnString();
				scores.saveScore(name, (int)points/divident);
				System.out.println("Score saved! you can now return to the game window");
				
				}

			}		


		}



	}

	@Override
	public void keyPressed(int key) {
		if(key == KeyEvent.VK_ENTER) init();	

		if(key == KeyEvent.VK_ESCAPE) {
			if(paused) paused = false;
			else paused = true;
		}

		if(key == KeyEvent.VK_LEFT) player.left = true;
		if(key == KeyEvent.VK_RIGHT) player.right = true;
		if(key == KeyEvent.VK_UP) player.forward = true;	
		if(key == KeyEvent.VK_DOWN) player.backward = true;	
		if(key == KeyEvent.VK_SHIFT)player.boosting = true;
		if(key == KeyEvent.VK_SPACE)player.firing = true;
		//if(key == KeyEvent.VK_DOWN) player.backward = true;

		if(key == KeyEvent.VK_1)player.bulletType = 1;
		if(key == KeyEvent.VK_2)player.bulletType = 2;
		if(key == KeyEvent.VK_3)player.bulletType = 3;
		if(key == KeyEvent.VK_4)player.bulletType = 4;
		if(key == KeyEvent.VK_5)player.bulletType = 5;
		if(key == KeyEvent.VK_6)player.bulletType = 6;

		if(key == KeyEvent.VK_7 && coins >=  15){
			player.satellites.add(new Satellite(1, randInt(0 , Panel.WIDTH - 150), randInt(100, Panel.HEIGHT - 100), this));
			coins -= 5;
		}

		if(key == KeyEvent.VK_8 && coins >= 20){
			player.satellites.add(new Satellite(2, randInt(0 , Panel.WIDTH - 150), randInt(100, Panel.HEIGHT - 100), this));
			coins -= 10;
		}


		if(key == KeyEvent.VK_9 && coins >= 25){
			player.assistants.add(new Assistant(randInt(0 , Panel.WIDTH - 150), randInt(100, Panel.HEIGHT - 100), this));
			coins -= 15;
		}

		//The followinf are cheatcodes created for "testing" and demostration
		if(key == KeyEvent.VK_F11) {
			coins = 1000;
			player.health = 1000000;
		}

		if(key == KeyEvent.VK_F4) {
			player.health = 0;
			points += 100000;
		}


		if(gameOver){
			if(key == KeyEvent.VK_F5){
				//restarts the game
				init();
				gameOver = false;
			}
		}
	}

	@Override
	public void keyReleased(int key) {
		if(key == KeyEvent.VK_LEFT) player.left = false;
		if(key == KeyEvent.VK_RIGHT) player.right = false;
		if(key == KeyEvent.VK_UP) player.forward = false;	
		if(key == KeyEvent.VK_DOWN) player.backward = false;	
		if(key == KeyEvent.VK_SHIFT)player.boosting = false;
		if(key == KeyEvent.VK_SPACE)player.firing = false;
		//if(key == KeyEvent.VK_DOWN) player.backward = false;	
	}

	public void mouseClick(int x, int y) {
		menu.mouseClick(this, x, y);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	protected void drawCenteredText(Graphics2D g2, String s,
			float centerX, float baselineY) {
		FontRenderContext frc = g2.getFontRenderContext();
		Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
		float width = (float) bounds.getWidth();
		g2.drawString(s, centerX - width / 2, baselineY);
	}
}

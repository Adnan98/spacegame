package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
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
import Entity.Shield;
import Entity.Satellite;
import main.Panel;
import util.PauseMenu;
import util.Scores;

public class LevelState extends GameState {

	public boolean paused = false;
	boolean started = false;
	public static boolean gameOver;

	PauseMenu menu;
	Scores scores;
	private Object[] highscores;
	BufferedImage bg;
	int bgSize = 256;

	public Player player;
	Meteor meteor;
	Enemy enemy;
	ArrayList<PowerUp> powerups;
	ArrayList<Meteor> meteors;
	public ArrayList<Enemy> enemies;

	int worldLimitX = Panel.WIDTH *2;
	int worldLimitY = Panel.HEIGHT *2;

	double healthBarWidth;
	double healthBarMaxWidth = 500;

	int maxMeteor = 100;
	public static int maxEnemy = 1;
	public static double points;
	public int divident = 10;
	public static int time = 0;
	long startTime;
	int elapsed;
	private String name = "";
	boolean printed;
	public static int coins;
	
	public LevelState(GameStateManager GSM){
		this.GSM = GSM;
		menu = new PauseMenu(GSM);
		
		try {
			//Ladda bakgrundsbilden 
			bg = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/blue.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		scores = new Scores();
	
	}

	public static  int getCoins(){
		return coins;
	}


	public void init() {
		highscores = scores.loadScores();
		
		gameOver = false;
		started = true;
		coins  = 0;
		player = new Player(this);				
		powerups = new ArrayList<>();
		meteors = new ArrayList<>();
		enemies = new ArrayList<>();
		points = 0;
		startTime = System.nanoTime();
		elapsed = 0;
	}


	public void update() {

		if(player.alive && !paused){
			elapsed = (int) ((System.nanoTime() - startTime)/1000000000);
			
			if(points/divident > (maxEnemy * 10 * divident)) {
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
					points += e.type * 100;
					powerups.add(new PowerUp(this, randInt(0, (player.shield == null ? 6 : 3)), (int)e.xPos , (int)e.yPos));
					e = null;
				}
			}			

		}

		else if(!player.alive){
			gameOver = true;
			if(elapsed != 0) {
				elapsed -= 1;
				points += 1;
			}
		}

	}




	public void spawnMeteor(){
		if(meteors.size() < maxMeteor){

			int meteorX = 100;

			while(meteorX > 0 && meteorX < Panel.WIDTH){
				meteorX = randInt(-worldLimitX, worldLimitX);
			}

			int meteorY = 100;

			while(meteorY > 0 && meteorY < Panel.HEIGHT){
				meteorY = randInt(-worldLimitY, worldLimitY);
			}

			meteor = new Meteor(randInt(0,1), randInt(1,4), meteorX, meteorY);
			meteors.add(meteor);
		}		
	}

	private void removeMeteor() {
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
		    	if(points/divident > i*5 * divident) {
		    		maxType = 1 + i;
		    	}
		    }

			int enemyType = randInt(1, maxType);
			int enemyX = 100;

			while(enemyX > 0 && enemyX < Panel.WIDTH){enemyX = randInt(-worldLimitX, worldLimitX);}

			int enemY = 100;

			while(enemY > 0 && enemY < Panel.HEIGHT){enemY = randInt(-worldLimitY, worldLimitY);}

			enemy = new Enemy(enemyType, enemyX, enemY, player);
			enemies.add(enemy);
		}		
	}

	private void removeEnemy() {
		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).xPos< -worldLimitX || enemies.get(i).xPos>worldLimitX|| enemies.get(i).yPos<-worldLimitY|| enemies.get(i).yPos> worldLimitY){
				enemies.remove(i);
				i--;
			}
		}		
	}


	private void checkBulletMeteorCollision() {

		for(int i = 0; i < player.bullets.size(); i++){
			Rectangle rb = player.bullets.get(i).getRectangle();
			Rectangle rp = new Rectangle(0,0,Panel.WIDTH,Panel.HEIGHT);

			if(rp.contains(rb)){
				for(int x = 0; x < meteors.size(); x++){
					Meteor m = meteors.get(x);
					Rectangle rm = m.getRectangle();

					if(rb.intersects(rm)){
						maxMeteor += 2;

						if(m.type > 1){
							int cx = (int) m.xPos;
							int cy = (int) m.yPos;
							int type = m.type;
							points += 10 * type;
							meteors.remove(x);
							for(int j = 0; j < type; j++){
								meteor = new Meteor(m.color, type-1, cx, cy);
								meteors.add(meteor);
							}

							if(m.type > 2){
								powerups.add(new PowerUp(this, randInt(0, (player.boost < player.maxBoost / 4 ? 1 : 0) ), cx, cy));
							}
							//The big stones drop new powerups

						}		

						else{
							meteors.remove(x);
							x--;
							points += 10;
						}

						Bullet b = player.bullets.get(i);
						b.damage -= 100 * m.type;
						if(b.damage <= 0) {
							if(b.type == 6) {
								for(int k = 0; k < 10; k++){
									player.bullets.add(new Bullet(1, player.width, player.height, b.xPos, b.yPos, randInt(0,360)));
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

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
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

		g2d.setColor(Color.white);
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

		if(paused) menu.draw(g2d);
		

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
				if((int)points/divident > ((Map.Entry<String, Integer>) highscores[0]).getValue()){
					//This means the the player beat the highcscore. We want to prompt for name and then save the highscore
					
					//First display the message that the player beat the highscore:
					drawCenteredText(g2d,"CONGRATS "+name+"! NEW HIGHSCORE", Panel.WIDTH/2, startX + 250);
					
				}
				
				else {
					drawCenteredText(g2d,"HIGHSCORES", Panel.WIDTH/2, startX + 250);
					
					for (int i = 0; i < 3; i++) {
						drawCenteredText(g2d,
								((Map.Entry<String, Integer>) highscores[i]).getKey() + ": " + Integer.toString(((Map.Entry<String, Integer>) highscores[i]).getValue()), 
								
								Panel.WIDTH/2, 
								startX + 300 + i * 50);
					}
				}
				
				if(!printed){
					printed = true;
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

		if(key == KeyEvent.VK_F11) {
			coins = 1000;
			player.health = 1000000;
		}
		
		if(key == KeyEvent.VK_F4) {
			player.health = 0;
		}


		if(gameOver){
			if(key == KeyEvent.VK_F5){
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

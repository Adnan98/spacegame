package GameState;

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
import java.util.Random;

import javax.imageio.ImageIO;

import Entity.Bullet;
import Entity.Enemy;
import Entity.Meteor;
import Entity.Player;
import main.Panel;

public class LevelState extends GameState {

	boolean playing;
	boolean paused;
	public static boolean gameOver;

	BufferedImage bg;
	int bgSize = 256;

	Player player;
	Meteor meteor;
	Enemy enemy;
	ArrayList<Meteor> meteors;
	ArrayList<Enemy> enemies;

	int worldLimitX = Panel.WIDTH *2;
	int worldLimitY = Panel.HEIGHT *2;

	double healthBarWidth;
	double healthBarMaxWidth = 500;

	int maxMeteor = 100;
	public static int maxEnemy = 0;
	public int score = 0;
	public static int points = 0;
	public static int time = 0;
	long startTime;
	int elapsed;

	public LevelState(GameStateManager GSM){
		this.GSM = GSM;
		
		try {
			//Ladda bakgrundsbilden 
			bg = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/blue.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		init();		
	}


	public void init() {
		player = new Player();
		meteors = new ArrayList<>();
		enemies = new ArrayList<>();
		score = points = 0;
		startTime = System.nanoTime();
		elapsed = 0;

	}


	public void update() {
		
		if(player.alive){
			elapsed = (int) ((System.nanoTime() - startTime)/1000000000);

			while(score < points){
				score += 1;
			}

			spawnMeteor();
			removeMeteor();
			spawnEnemy();
			removeEnemy();
			checkBulletMeteorCollision();
			checkPlayerCollision();
			player.update();

			for(int i = 0; i < enemies.size(); i++){
				player.enemyBulletCollision(enemies.get(i));
			}

			for(int i = 0; i < meteors.size(); i++){
				meteors.get(i).update();
			}

			for(int i = 0; i < enemies.size(); i++){
				Enemy e = enemies.get(i);
				e.update();
				if(e.dead){
					enemies.remove(i);//If enemy died, remove it
					i--;
					points += e.type * 100; 
				}
			}			

		}

		else{
			gameOver = true;
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

			int enemyX = 100;

			while(enemyX > 0 && enemyX < Panel.WIDTH){enemyX = randInt(-worldLimitX, worldLimitX);}

			int enemY = 100;

			while(enemY > 0 && enemY < Panel.HEIGHT){enemY = randInt(-worldLimitY, worldLimitY);}

			enemy = new Enemy(randInt(1,7), enemyX, enemY, player);
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

						}
						else{
							meteors.remove(x);
							x--;
							points += 10;
						}
						
						Bullet b = player.bullets.get(i);
						b.damage -= 10 * m.type;
						if(b.damage <= 0) {
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


	public void draw(Graphics2D g2d) {

		//Loop through to automatically draw the BG as tiles: First Columns, then Rows
		for(int i = 0; i < (Panel.WIDTH / bgSize) + 1; i++){
			for(int x = 0; x < (Panel.HEIGHT/ bgSize) + 1; x++ ){
				g2d.drawImage(bg, bgSize * i, bgSize * x, null);
			}
		}

		//Print out the score:
		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1F));

		g2d.setColor(Color.white);
		g2d.drawString("Score: "+score / 100, Panel.WIDTH-200, 50);
		
		for(int i = 0; i < enemies.size(); i++){
			//Kontrollera ifalll objektet är utanför den synliga delen av planen. Om den är det hoppar den 
			//över iterationen. Om den är inuti den synliga delen så ritas den ut. 
			Enemy e = enemies.get(i);
			if(e.xPos < -10 || e.xPos > Panel.WIDTH + 10|| e.yPos < -10 || e.yPos > Panel.HEIGHT + 10) {
				continue;
			}
			else {
				e.draw(g2d);
			}
		}
		
		for(int i = 0; i < meteors.size(); i++){
			//Kontrollera ifalll objektet är utanför den synliga delen av planen. Om den är det hoppar den 
			//över iterationen. Om den är inuti den synliga delen så ritas den ut. 
			Meteor m = meteors.get(i);
			if(m.xPos < -10 || m.xPos > Panel.WIDTH + 10|| m.yPos < -10 || m.yPos > Panel.HEIGHT + 10) {
				continue;
			}
			else {
				m.draw(g2d);
			}
		}
		player.draw(g2d);
		
		//Print GAME OVER and score if gameOver
		if(gameOver){
			g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 4F));
			drawCenteredText(g2d,"GAME OVER", Panel.WIDTH/2, Panel.HEIGHT/2);
			g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 2F));
			drawCenteredText(g2d,"Score: "+score / 100, Panel.WIDTH/2, Panel.HEIGHT/2 + 100);
			g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 2F));
			drawCenteredText(g2d,"Time: "+elapsed +" seconds", Panel.WIDTH/2, Panel.HEIGHT/2 + 150);

			g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1F));
			drawCenteredText(g2d,"press F5 to restart", Panel.WIDTH/2, Panel.HEIGHT - 30);

		}



	}

	@Override
	public void keyPressed(int key) {
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
		// TODO Auto-generated method stub
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

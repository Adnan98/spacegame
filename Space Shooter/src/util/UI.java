package util;

import java.awt.AlphaComposite;
import java.awt.geom.Arc2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import main.Panel;

import Entity.Player;
import GameState.LevelState;

public class UI {
	
	Player player;
	BufferedImage barHorizontal_shadow_mid;
	BufferedImage barHorizontal_blue_mid;
	BufferedImage barHorizontal_green_mid;
	BufferedImage barHorizontal_red_mid;
	BufferedImage glassPanel;
	BufferedImage coin;
	public ArrayList <BufferedImage> utils;
	int barMaxWidth = 500;
	
	public UI(Player p){
		this.player = p;
		utils = new ArrayList<>();
		
		try {
			barHorizontal_shadow_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_shadow_mid.png"));
			barHorizontal_blue_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_blue_mid.png"));
			barHorizontal_green_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_green_mid.png"));
			barHorizontal_red_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_red_mid.png"));
			glassPanel = ImageIO.read(getClass().getResource("/uipack-space/PNG/glassPanel.png"));
			
			//Ladda bild f�r samtliga kanoner och spara de i listan
			for(int i = 1; i <= 6; i++) {
				utils.add(ImageIO.read(getClass().getResource("/PNG/bullets/bullet"+i+".png")));
			}
			
			//Ladda bild f�r samtliga utils och spara de i listan
			for(int i = 1; i <= 3; i++) {
				utils.add(ImageIO.read(getClass().getResource("/PNG/utils/util"+i+".png")));
			}
			
			coin = ImageIO.read(getClass().getResource("/PNG/powerups/powerup0.png"));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
	}
	
	public void draw(Graphics2D g2d) throws IOException{
		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize()));//Originella storleken för fonten: 20px
		g2d.setColor(Color.white);
		
		int healthBarWidth = (int)(barMaxWidth * (player.health/player.maxHealth) > 0 ? (int)barMaxWidth * (player.health/player.maxHealth) : 0);
		int ammoBarWidth = (int)(barMaxWidth * (player.fire/player.maxFire) > 0 ? (int)barMaxWidth * (player.fire/player.maxFire) : 0);
		
		g2d.drawString("HEALTH", 50, 50);
		g2d.drawImage(barHorizontal_shadow_mid, 150, 40, barMaxWidth , barHorizontal_shadow_mid.getHeight() / 2, null);
		g2d.drawImage(barHorizontal_red_mid, 150, 40, healthBarWidth, (barHorizontal_red_mid.getHeight() / 2), null);
		
		g2d.drawString("AMMO", 50, 75);
		g2d.drawImage(barHorizontal_shadow_mid, 150, 65, barMaxWidth , barHorizontal_shadow_mid.getHeight() / 2, null);
		g2d.drawImage(barHorizontal_blue_mid, 150, 65, ammoBarWidth, (barHorizontal_blue_mid.getHeight() / 2), null);
		
		g2d.drawString("BOOST", 50, 100);
		g2d.drawImage(barHorizontal_shadow_mid, 150, 90, barMaxWidth , barHorizontal_shadow_mid.getHeight() / 2, null);
		g2d.drawImage(barHorizontal_green_mid, 150, 90, (int)(barMaxWidth * (player.boost/player.maxBoost)), (barHorizontal_green_mid.getHeight() / 2), null);
		
		//Draw how many coins the player has
		g2d.drawImage(coin, Panel.WIDTH - 200, 80, null);
		g2d.drawString(Integer.toString(LevelState.coins), Panel.WIDTH - 150, 100);
		
		
		for(int i = 0; i < 9; i ++) {
			int k = i+1;
			//This if statement checks that the current bullet is not selected so all non-selected bullets can be drawn in low opacity
			if(player.bulletType != k) g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g2d.drawImage(glassPanel, 10 + (i*100), Panel.HEIGHT-100,  null);
			g2d.drawString(Integer.toString(k), 20 + (i*100), Panel.HEIGHT-75);
			
			if(i == 5) {
				g2d.drawString("FLARES", 20 + (i*100), Panel.HEIGHT-10);
			}
			
			if(i <= 5) g2d.drawImage(utils.get(i), 50 + (i*100), Panel.HEIGHT-80,null);
			
			
			if(i >= 6) {
				BufferedImage image = utils.get(i);
				AffineTransform at = AffineTransform.getTranslateInstance(20 + (i*100), Panel.HEIGHT-90);
				at.rotate(Math.toRadians((i != 8 ? -45 : 0)), image.getWidth()/2, image.getHeight()/2);
				double scale = i * 0.1 - 0.1;
				at.scale(scale, scale);
				g2d.drawImage(image, at, null);
				
				g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 0.75f));
				g2d.drawString(Integer.toString(50 + 25*(i-6)), 80 + (i*100), Panel.HEIGHT-10);
				
				AffineTransform at2 = AffineTransform.getTranslateInstance(60 + (i*100), Panel.HEIGHT-20);
				double scale_coin = 0.5;
				at2.scale(scale_coin , scale_coin);
				g2d.drawImage(coin, at2, null);

			}
			
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));			
			double extent = 1 - (player.dt/(player.bulletType * 1000));
			if(player.dt > player.bulletType * 1000 || player.bulletType < 3)
				extent  = 0;
	        g2d.fill(new Arc2D.Double(700, 50, 50, 50, 90, 360 * extent, Arc2D.PIE));//arguments: x , y, width, height, starting angle, extent
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));//This resets the drawing opacity so everything that follows is drawn normally

		}	
		
	}
	
}

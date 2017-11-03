package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Panel;

import Entity.Player;

public class UI {
	
	Player player;
	BufferedImage glassPanel_cornerTL;
	BufferedImage barHorizontal_shadow_mid;
	BufferedImage barHorizontal_blue_mid;
	BufferedImage barHorizontal_green_mid;
	BufferedImage barHorizontal_red_mid;
	int barMaxWidth = 500;
	
	public UI(Player p){
		this.player = p;
		
		try {
			glassPanel_cornerTL = ImageIO.read(getClass().getResource("/uipack-space/PNG/glassPanel_cornerTL.png"));
			barHorizontal_shadow_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_shadow_mid.png"));
			barHorizontal_blue_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_blue_mid.png"));
			barHorizontal_green_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_green_mid.png"));
			barHorizontal_red_mid = ImageIO.read(getClass().getResource("/uipack-space/PNG/barHorizontal_red_mid.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
	}
	
	public void draw(Graphics2D g2d){
		g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize()));//Originella storleken för fonten: 20px
		g2d.setColor(Color.white);
		
		int healthBarWidth = (int)(barMaxWidth * (player.health/player.maxHealth) > 0 ? (int)barMaxWidth * (player.health/player.maxHealth) : 0);
		
		g2d.drawString("HEALTH", 50, 50);
		g2d.drawImage(barHorizontal_shadow_mid, 150, 40, barMaxWidth , barHorizontal_shadow_mid.getHeight() / 2, null);
		g2d.drawImage(barHorizontal_red_mid, 150, 40, healthBarWidth, (barHorizontal_red_mid.getHeight() / 2), null);
		
		g2d.drawString("AMMO", 50, 75);
		g2d.drawImage(barHorizontal_shadow_mid, 150, 65, barMaxWidth , barHorizontal_shadow_mid.getHeight() / 2, null);
		g2d.drawImage(barHorizontal_blue_mid, 150, 65, (int)(barMaxWidth * (player.fire/player.maxFire)), (barHorizontal_blue_mid.getHeight() / 2), null);
		
		g2d.drawString("BOOST", 50, 100);
		g2d.drawImage(barHorizontal_shadow_mid, 150, 90, barMaxWidth , barHorizontal_shadow_mid.getHeight() / 2, null);
		g2d.drawImage(barHorizontal_green_mid, 150, 90, (int)(barMaxWidth * (player.boost/player.maxBoost)), (barHorizontal_green_mid.getHeight() / 2), null);
		
		
		
		
		
	}
	
}

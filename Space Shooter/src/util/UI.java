package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Panel;

import Entity.Player;

public class UI {
	
	Player player;
	BufferedImage glassPanel_cornerTL;
	 
	public UI(Player p){
		this.player = p;
		
		try {
			glassPanel_cornerTL = ImageIO.read(getClass().getResource("/uipack-space/PNG/glassPanel_cornerTL.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
	}
	
	public void draw(Graphics2D g2d){
		g2d.drawImage(glassPanel_cornerTL, 100, 100, Panel.WIDTH/2, glassPanel_cornerTL.getHeight(), null);
		
	}
	
}

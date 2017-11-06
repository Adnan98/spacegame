package Entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Shield {

	int xPos, yPos, width, height, rotate, health;
	Player player;
	ArrayList<BufferedImage> images;	

	public Shield(Player p, int h) {
		player = p;
		health = h;
		images  = new ArrayList<>();
		
		try {
			for(int i = 0; i < 3; i++){
				int k = i+1;
				images.add(ImageIO.read(getClass().getResource("/PNG/Effects/shield"+(k)+".png")));
			}
				
		} catch (IOException e) {e.printStackTrace();}

	}
	
	public void draw(Graphics2D g2d){
		AffineTransform at = AffineTransform.getTranslateInstance(player.xPos, player.yPos);
		at.rotate(Math.toRadians(player.rotateDegrees), images.get(2).getWidth()/2, images.get(2).getHeight()/2);
		g2d.drawImage(images.get(2),at,null);
	}
	

}

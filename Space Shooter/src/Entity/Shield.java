package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Shield {

	int xPos, yPos, width, height, rotate;
	public double health;
	Player player;
	ArrayList<BufferedImage> images;	
	BufferedImage image;
	
	public Shield(Player p, int h) {
		player = p;
		health = h * 600;
		images  = new ArrayList<>();
		
		try {
			for(int i = 0; i < 3; i++){
				int k = i+1;
				images.add(ImageIO.read(getClass().getResource("/PNG/Effects/shield"+(k)+".png")));
			}
				
		} catch (IOException e) {e.printStackTrace();}

	}
	
	public void draw(Graphics2D g2d){		
		if(health > 0) image = images.get(0);
		if(health > 1200) image = images.get(1);
		if(health > 1800) image = images.get(2);
		AffineTransform at = AffineTransform.getTranslateInstance(player.xPos - 25, player.yPos - 25);
		at.rotate(Math.toRadians(player.rotateDegrees), image.getWidth()/2, image.getHeight()/2);
		g2d.drawImage(image,at,null);
			
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int) player.xPos - 25, (int) player.yPos - 25, image.getWidth() , image.getHeight());
	}


}

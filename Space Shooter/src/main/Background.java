package main;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class Background {
	private BufferedImage image;

	private double x;
	private double y;
	private double dx;
	private double dy;

	private double moveScale;

	public Background(String s, double ms){

		try{
			image = ImageIO.read(getClass().getResourceAsStream(s));

			moveScale = ms;

		}

		catch(Exception e){
			e.printStackTrace();

		}
	}
	public void setPosition(double x, double y){
		//Move the image starting position
		this.x = (x * moveScale) % Panel.WIDTH;
		this.y = (y * moveScale) % Panel.HEIGHT;

	}

	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;

	}

	public void update(){
		x += dx;
		y += dy;
	}

	public void draw(Graphics2D g2d){
		g2d.drawImage(image, (int)x, (int)y, Panel.WIDTH, Panel.HEIGHT, null);
		if(x<0){g2d.drawImage(image, (int)x + Panel.WIDTH, (int)y,Panel.WIDTH, Panel.HEIGHT, null);}
		if(x>0){g2d.drawImage(image, (int)x - Panel.WIDTH, (int)y,Panel.WIDTH, Panel.HEIGHT, null);}
		
	}
}

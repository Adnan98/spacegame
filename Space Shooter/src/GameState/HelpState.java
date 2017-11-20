package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entity.Player;

import main.Panel;

public class HelpState extends GameState {

    GameStateManager GSM;
    BufferedImage bg;
    Player player;

    public HelpState(GameStateManager gsm) {
        this.GSM = gsm;


        try {
            //Ladda bakgrundsbilden 
            bg = ImageIO.read(getClass().getResourceAsStream("/grid.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        player = new Player(null);

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void draw(Graphics2D g2d) {
        //Loop through to automatically draw the BG as tiles: First Columns, then Rows
        for(int i = 0; i < (Panel.WIDTH / bg.getWidth()) + 1; i++){
            for(int x = 0; x < (Panel.HEIGHT/ bg.getHeight()) + 1; x++ ){

                g2d.drawImage(bg, bg.getWidth() * i, bg.getHeight() * x, null);

            }
        }
        
    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        player.draw(g2d);
        g2d.setColor(Color.WHITE);



    }
    
    protected void drawCenteredText(Graphics2D g2, String s,
            float centerX, float baselineY) {
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
        float width = (float) bounds.getWidth();
        g2.drawString(s, centerX - width / 2, baselineY);
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

    }

    @Override
    public void keyReleased(int key) {
        if(key == KeyEvent.VK_LEFT) player.left = false;
        if(key == KeyEvent.VK_RIGHT) player.right = false;
        if(key == KeyEvent.VK_UP) player.forward = false;    
        if(key == KeyEvent.VK_SHIFT)player.boosting = false;
        if(key == KeyEvent.VK_SPACE)player.firing = false;

    }

    @Override
    public void mouseClick(int x, int y) {
        // TODO Auto-generated method stub

    }

}

package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
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
    String[] instructions = {
    						"Press key ← to rotate left",
    						"Press key →  to rotate right",
    						"Press key ↑ to move forward",
    						"Press key ↓ to move backwards",
    						"Press the SPACEBAR to shoot",
                            "Hold the SHIFT key to shoot",
                            "Press keys 1 - 6 to select weapon",
                            "Press keys 7 - 9 to launch utilities",
                            " ",
                            " ",
                            "You have to stay on the field",
                            "as long as possible"
                             
    						};

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
        
        g2d.setColor(new Color(0, 0, 0, 137));        
        g2d.fillRect(0, 0, Panel.WIDTH, Panel.HEIGHT);
        
        player.draw(g2d);
       
        g2d.setColor(Color.WHITE);
		g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 2.5F));
		g2d.drawString("TRAINING MODE", 700, 60);
		g2d.setFont(Panel.titleFont.deriveFont(Panel.titleFont.getSize() * 1F));
		g2d.drawString("press [ESC] to go back.", 700, 100);
		g2d.drawString("press [F5] to start game", 700, 120);
		
		
		g2d.setFont(new Font("calibri", Font.PLAIN, 20));
        for(int i = 0; i < instructions.length; i++) {
        	g2d.drawString(instructions[i], Panel.WIDTH - 500, 300 + (i * 25));
        }
        

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

        if(key == KeyEvent.VK_ESCAPE) GSM.setState(GSM.MENUSTATE);
        if(key == KeyEvent.VK_F5) GSM.setState(GSM.LEVELSTATE);
    }

    @Override
    public void keyReleased(int key) {
        if(key == KeyEvent.VK_LEFT) player.left = false;
        if(key == KeyEvent.VK_RIGHT) player.right = false;
        if(key == KeyEvent.VK_UP) player.forward = false;    
        if(key == KeyEvent.VK_SHIFT)player.boosting = false;
        if(key == KeyEvent.VK_SPACE)player.firing = false;
        if(key == KeyEvent.VK_DOWN) player.backward = false;    
    }

    @Override
    public void mouseClick(int x, int y) {
        // TODO Auto-generated method stub

    }

}

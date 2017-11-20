package util; 
 
import java.awt.Color; 
import java.awt.Graphics2D; 
import java.awt.font.FontRenderContext; 
import java.awt.geom.AffineTransform; 
import java.awt.geom.Rectangle2D; 
import java.awt.image.BufferedImage; 
 
import javax.imageio.ImageIO; 
 
import GameState.GameStateManager; 
import GameState.LevelState; 
import GameState.MenuState; 
import main.Background; 
import main.Panel; 
 
public class PauseMenu { 
 
    GameStateManager GSM; 
    BufferedImage button, panel; 
    int buttonWidth = 300; 
    int buttonHeight = 39; 
    public String[] options = {"Return to game", "Restart game", "Main menu", "QUIT GAME"}; 
 
    public PauseMenu(GameStateManager gsm) { 
        this.GSM = gsm; 
 
        try{ 
            button = ImageIO.read(getClass().getResourceAsStream("/UI/buttonGreen.png")); 
            panel = ImageIO.read(getClass().getResource("/uipack-space/PNG/glassPanel.png"));             
        } 
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
    } 
 
    public void draw(Graphics2D g2d) { 
        g2d.setFont(Panel.regularFont.deriveFont(Panel.regularFont.getSize() * 1.5F)); 
        g2d.setColor(Color.BLACK); 
 
        AffineTransform at = AffineTransform.getTranslateInstance( Panel.WIDTH/2 - buttonWidth + 50,Panel.HEIGHT/2 - 200); 
        at.scale(5.0, 5.0); 
        g2d.drawImage(panel, at, null); 
        int marginTop = -100; 
 
        for(int i = 0; i < options.length; i++){ 
            //This draws all the buttons 
            //X position                        Y position                    Width        Height 
            g2d.drawImage(button, (Panel.WIDTH/2 - buttonWidth/2), (Panel.HEIGHT/2) + marginTop, buttonWidth, buttonHeight, null); 
            //Draws the text over the buttons 
            drawCenteredText(g2d, options[i], Panel.WIDTH/2, Panel.HEIGHT/2 + marginTop + 30); 
            marginTop += buttonHeight * 2; 
        } 
 
    } 
 
    public void mouseClick(LevelState levelState, int x, int y) { 
        int mouseX = x; 
        int mouseY = y; 
 
        //Check if first button is pressed: "Return to game" 
        if( mouseX > Panel.WIDTH/2 - MenuState.buttonWidth/2 && mouseX < (Panel.WIDTH/2 - MenuState.buttonWidth/2) + MenuState.buttonWidth){ 
 
            int startY = Panel.HEIGHT/2 -100; 
             
            if(mouseY >  startY && mouseY < startY + MenuState.buttonHeight){ 
                levelState.paused = false; 
            } 
 
            //Check if second button is pressed: "Restart game" 
            if(mouseY > startY + MenuState.buttonHeight*2 && mouseY < (startY + MenuState.buttonHeight + MenuState.buttonHeight*2)){ 
                levelState.paused = false; 
                levelState.init(); 
            } 
 
            //Check if third button is pressed: "Main menu" 
            if(mouseY > startY + MenuState.buttonHeight*4 && mouseY < (startY + MenuState.buttonHeight + MenuState.buttonHeight*4)){ 
                GSM.setState(0); 
            } 
             
            //Check if third button is pressed: "QUIT" 
            if(mouseY > startY + MenuState.buttonHeight*6 && mouseY < (startY + MenuState.buttonHeight + MenuState.buttonHeight*6)){ 
                System.exit(0); 
            } 
 
        } 
 
    } 
 
 
 
    protected void drawCenteredText(Graphics2D g2, String s, 
            float centerX, float baselineY) { 
        FontRenderContext frc = g2.getFontRenderContext(); 
        Rectangle2D bounds = g2.getFont().getStringBounds(s, frc); 
        float width = (float) bounds.getWidth(); 
        g2.drawString(s, centerX - width / 2, baselineY); 
    } 
 
} 

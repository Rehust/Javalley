package Entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import javax.security.auth.kerberos.KerberosTicket;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        screenX = gp.screenWidth / 2 - (gp.tileSize) / 2;
        screenY = gp.screenHeight / 2 - (gp.tileSize) / 2;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            // read image files
            up1 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_04.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_05.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_01.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_02.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_06.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_08.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_09.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Resource/Character/main (1)/sprite_10.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(){
        if(keyH.upPressed) {
            direction = "up";
            worldY -= speed;
        }
        else if(keyH.downPressed) {
            direction = "down";
            worldY += speed;
        }
        else if(keyH.leftPressed) {
            direction = "left";
            worldX -= speed;
        }
        else if(keyH.rightPressed) {
            direction = "right";
            worldX += speed;
        }

        if(!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed){
            spriteCounter--;
        }
        spriteCounter++;
        if(spriteCounter > 10){
            if(spriteNum == 1) spriteNum = 2;
            else if(spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2){
//        g2.setColor(Color.CYAN);
//        // Draw a rectangle with the specified color
//        g2.fillRect(worldX, worldY, gp.tileSize, gp.tileSize); // start with (worldX, worldY) and its width and height
        BufferedImage image = null;
        switch (direction){
            case "up":
                if(spriteNum == 1) image = up1;
                if(spriteNum == 2) image = up2;
                break;
            case "down":
                if(spriteNum == 1) image = down1;
                if(spriteNum == 2) image = down2;
                break;
            case "left":
                if(spriteNum == 1) image = left1;
                if(spriteNum == 2) image = left2;
                break;
            case "right":
                if(spriteNum == 1) image = right1;
                if(spriteNum == 2) image = right2;
                break;
        }

        // Draw an image
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}

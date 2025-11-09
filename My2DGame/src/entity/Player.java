package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends entity {
    GamePanel gp;
    KeyHandler keyH;
    int spriteCounter = 0;
    int spriteNum = 1;

    public BufferedImage down1, down2, down3, up1, up2, up3, left1, left2, left3, right1, right2, right3;


    // --- SHOVEL SKILL FIELDS ---
    private BufferedImage shovelImage;
    private boolean shovelActive = false;
    private int shovelDuration = 20; // frames
    private int shovelTimer = 0;
    private int shovelCooldown = 60; // frames
    private int shovelCooldownTimer = 0;

    public Player(GamePanel gp,KeyHandler keyH){
        this.gp=gp;
        this.keyH=keyH;
        setDefaultValues();
        getPlayerImage();
        loadShovelImage(); // load shovel image at construction
    }
    public void setDefaultValues(){
        x=100;
        y=100;
        speed=4;
        direction= "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player2/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player2/up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/res/player2/up3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player2/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player2/down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/res/player2/down3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player2/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player2/left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/res/player2/left3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player2/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player2/right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/res/player2/right3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadShovelImage() {
        try {
            // Place shovel.png under src/res/tools/shovel.png
            shovelImage = ImageIO.read(getClass().getResourceAsStream("/res/tool/shovel.png"));
        } catch (Exception e) {
            System.out.println("Could not load shovel.png: " + e.getMessage());
            shovelImage = null;
        }
    }

    // Call this to attempt using the shovel (edge-triggered)
    public void tryUseShovel() {
        if (!shovelActive && shovelCooldownTimer <= 0) {
            shovelActive = true;
            shovelTimer = shovelDuration;
            shovelCooldownTimer = shovelCooldown;
            // Optional: apply effects here (damage, dig tile, etc.)
            // e.g., check tile in front of player: gp.tileManager.modifyTile(...)
        }
    }

    public void update(){
        boolean moving = false;
        if(keyH.upPressed == true){
            direction = "up";
            if(y - speed >= 0) { // Kiểm tra biên trên
                y -= speed;
                moving = true;
            }
        } else if (keyH.downPressed == true){
            direction = "down"; // Đã sửa: bỏ khoảng trắng thừa
            if(y + speed <= gp.screenHeight - gp.tileSize) { // Kiểm tra biên dưới
                y += speed;
                moving = true;
            }
        } else if (keyH.leftPressed == true){
            direction = "left";
            if(x - speed >= 0) { // Kiểm tra biên trái
                x -= speed;
                moving = true;
            }
        } else if (keyH.rightPressed == true){
            direction = "right";
            if(x + speed <= gp.screenWidth - gp.tileSize) { // Kiểm tra biên phải
                x += speed;
                moving = true;
            }
        }

        if (moving) {
            spriteCounter++;
            if (spriteCounter > 10) { // tốc độ đổi ảnh (càng nhỏ càng nhanh)
                spriteNum++;
                if (spriteNum > 3) spriteNum = 1; // 3 ảnh thì quay lại 1
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // đứng yên → về ảnh đầu tiên
        }

        // --- shovel input: try to use when K is pressed ---
        if (keyH.kPressed) {
            tryUseShovel();
        }

        // --- shovel timers ---
        if (shovelActive) {
            shovelTimer--;
            if (shovelTimer <= 0) shovelActive = false;
        }
        if (shovelCooldownTimer > 0) shovelCooldownTimer--;
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) image = up1;
                else if (spriteNum == 2) image = up2;
                else if (spriteNum == 3) image = up3;
                break;
            case "down":
                if (spriteNum == 1) image = down1;
                else if (spriteNum == 2) image = down2;
                else if (spriteNum == 3) image = down3;
                break;
            case "left":
                if (spriteNum == 1) image = left1;
                else if (spriteNum == 2) image = left2;
                else if (spriteNum == 3) image = left3;
                break;
            case "right":
                if (spriteNum == 1) image = right1;
                else if (spriteNum == 2) image = right2;
                else if (spriteNum == 3) image = right3;
                break;
        }

        g2.drawImage(image, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // draw shovel on top if active
        if (shovelActive && shovelImage != null) {
            int drawX = x;
            int drawY = y;

            // place shovel one tile in front of player depending on direction
            switch (direction) {
                case "up":
                    drawY = y - gp.tileSize;
                    break;
                case "down":
                    drawY = y + gp.tileSize;
                    break;
                case "left":
                    drawX = x - gp.tileSize;
                    break;
                case "right":
                    drawX = x + gp.tileSize;
                    break;
            }

            // draw shovel sized to one tile (adjust if you want smaller/larger)
            g2.drawImage(shovelImage, drawX, drawY, gp.tileSize, gp.tileSize, null);
        }

        // Optional: draw cooldown UI (small icon with overlay)
        if (shovelCooldownTimer > 0) {
            int uiX = 10;
            int uiY = 10;
            int size = gp.tileSize;
            if (shovelImage != null) g2.drawImage(shovelImage, uiX, uiY, size, size, null);
            // overlay cooldown (simple rectangle)
            float fraction = (float) shovelCooldownTimer / (float) shovelCooldown;
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(uiX, uiY, (int) (size * fraction), size / 4);
        }
    }


}
package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.IOException;
import java.nio.Buffer;
import java.security.Key;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public int equippedTool = 0;
    private Cursor shovelCursor;
    private Cursor defaultCursor;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValue();
        getPlayerImage();
        loadCursor();
    }

    public void setDefaultValue() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "right";
    }

    public void getPlayerImage() {
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void loadCursor() {
        try {
            BufferedImage shovelImage = ImageIO.read(getClass().getResourceAsStream("/tool/shovel.gif"));
            Toolkit toolkit = Toolkit.getDefaultToolkit();

            int imageWidth = shovelImage.getWidth();
            int imageHeight = shovelImage.getHeight();
            Dimension bestCursorSize = toolkit.getBestCursorSize(shovelImage.getWidth(), shovelImage.getHeight());

            System.out.println("Original image size: " + shovelImage.getWidth() + "x" + shovelImage.getHeight());
            System.out.println("Best cursor size: " + bestCursorSize.width + "x" + bestCursorSize.height);

            shovelCursor = toolkit.createCustomCursor(shovelImage, new Point(16 / 2, 32 - 1), "ShovelCursor");

            defaultCursor = Cursor.getDefaultCursor();
        } catch (Exception e) {
            e.printStackTrace();
            shovelCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        }
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        }
        if (keyH.downPressed) {
            direction = "down";
            y += speed;
        }
        if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }
        int previousTool = equippedTool;
        equippedTool = keyH.selectedToolSlot;

        if(previousTool != equippedTool){
            if (equippedTool == 1){
                gp.setCursor(shovelCursor);
            }
            else {
                gp.setCursor(defaultCursor);
            }
        }
    }

    public void useTool() {
        int tileX = x / gp.tileSize;
        int tileY = y / gp.tileSize;
        Tile tile = gp.getTileManager().getTile(tileX, tileY);
        if (equippedTool == 1) { // Shovel
            System.out.println("Using shovel at tile: " + tileX + ", " + tileY);
            // add
            if (tile.diggable) {
                gp.getTileManager().mapTileNum[tileX][tileY] = 0;
            }
        }
    }

    public void draw(Graphics2D g2d) {

        if (equippedTool == 1) {
            int tileX = x / gp.tileSize;
            int tileY = y / gp.tileSize;
            Tile tile = gp.getTileManager().getTile(tileX, tileY);

            if (tile.diggable) {
                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke(3)); // 3 pixel thick border
                g2d.drawRect(tileX * gp.tileSize, tileY * gp.tileSize, gp.tileSize, gp.tileSize);
            }
        }
        g2d.setColor(Color.white);
        g2d.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = up;
                break;
            case "down":
                break;
            case "left":
                break;
            case "right":
                break;
        }
        g2d.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}

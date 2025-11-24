package Tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        loadMap("/Resource/map/world01.txt");
        getTileImage();
    }

    public void getTileImage(){
        try{
            for(int i = 0; i < 6; i++)
                tile[i] = new Tile();

            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Resource/Structure+Tile (1)/Structure+Tile/Tile/grass.png"));

            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Resource/Structure+Tile (1)/Structure+Tile/Tile/wall.png"));
            tile[1].collision = true;

            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Resource/Structure+Tile (1)/Structure+Tile/Tile/water.png"));
            tile[2].collision = true;

            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Resource/Structure+Tile (1)/Structure+Tile/Tile/earth.png"));

            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Resource/Structure+Tile (1)/Structure+Tile/Tile/tree.png"));
            tile[4].collision  = true;

            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/Resource/Structure+Tile (1)/Structure+Tile/Tile/sand.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMap(String filePath){
        try{
            // Import the text file
            InputStream is = getClass().getResourceAsStream(filePath);
            // read the content of the text file
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0, row = 0;
            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine(); // read a line of text
                String numbers[] = line.split(" "); // split the string around matches of the given regular expression
                while(col < gp.maxWorldCol){
                    int num = Integer.parseInt(numbers[col]); // change from String to int
                    mapTileNum[row][col] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
            int tileNum = mapTileNum[worldRow][worldCol];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = gp.player.screenX - gp.player.worldX + worldX;
            int screenY = gp.player.screenY - gp.player.worldY + worldY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}

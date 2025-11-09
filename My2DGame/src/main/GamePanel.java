package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int   originalTitleSize=16; //16x16 title
    final int scale =3;

    public final int tileSize=originalTitleSize*scale; //48x48 title
    public final int maxScreenCol=16;
    public final int maxScreenRow=12;
    public final int screenWidth= tileSize * maxScreenCol; //768px
    public final int screenHeight=tileSize * maxScreenRow; //576px

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this,keyH);

    // edge-detect state for K (shovel)
    private boolean prevK = false;

    //Set player's default position
    int playerX=100;
    int playerY=100;
    int playerSpeed=4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //FPS
    int FPS=60;
    TileManager tileM = new TileManager(this);

    public void run(){
        double drawInterval=1000000000/FPS;
        double delta=0;
        long lastTime=System.nanoTime();
        long currentTime;
        long timer=0;
        int drawCount=0;

        while (gameThread!=null){
            currentTime=System.nanoTime();
            delta += (currentTime-lastTime)/drawInterval;
            timer+=(currentTime - lastTime);
            lastTime=currentTime;
            if (delta >=1){
                update();
                repaint();
                delta--;
                drawCount++;

            }
            if (timer>=1000000000){
                System.out.println("FPS:" + drawCount);
                drawCount=0;
                timer=0;
            }

        }
    }

    public void update(){
        // update player (movement, animations, timers inside player.update())
        player.update();

        // edge-detect: chỉ kích hoạt shovel khi vừa nhấn K (false -> true)
        if (keyH.kPressed && !prevK) {
            player.tryUseShovel();
        }

        // lưu trạng thái K cho vòng tiếp theo
        prevK = keyH.kPressed;

        // các cập nhật khác (nếu có)...
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose();
    }


}

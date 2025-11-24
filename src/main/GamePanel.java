package main;

import Entity.Player;
import Tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{ // inherit JPanel, interface Runnable
    // SCREEN SETTINGS

    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; // scale the pixel

    public final int tileSize = originalTileSize * scale;// 48x48 tile

    // Screen: (12x16) x tileSize
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixel
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixel

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    // FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler(); // create a keyhandler object
    Thread gameThread; // can start and stop; keep running until stop it
    public Player player = new Player(this, keyH);
    public CollisionChecker cChecker = new CollisionChecker(this);
    TileManager tileM = new TileManager(this);

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // set the size of JPanel
        this.setBackground(Color.black); // black background
        this.setDoubleBuffered(true); // offscreen painting buffer: improve rendering performance
        this.addKeyListener(keyH); // recognize keys
        this.setFocusable(true); // be focused to receive key input
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // run on new thread, also automatically call run method
        // if we don't call the start func. we will run on EDT (Event Dispatch Thread)
    }

        // Sleep method
//    @Override
//    public void run() {
//        double drawInterval = 1000000000/FPS; // draw screen every drawInterval second
//        double nextDrawTime = System.nanoTime() + drawInterval; // next draw time
//
//
//        while(gameThread != null){
//            //long currentTime = System.nanoTime(); // returns running Java Virtual Machine's high-resolution time source
//
//            // 1 UPDATE: update information (movement)
//            update();
//            // 2 DRAW: draw updated information
//            repaint(); // to call the paintComponent function
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime / 1000000;
//                if(remainingTime < 0) remainingTime = 0;
//
//                Thread.sleep((long) remainingTime);  // pause the game loop until this sleep time is over
//
//                nextDrawTime += drawInterval; // plus for another frame
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    // Delta method
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0; // set the timer
        int drawCount = 0; // the number of frames in each second

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000){ // display FPS
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // UPDATE
    public void update(){
        player.update();
    }

    //DRAW
    public void paintComponent(Graphics g){
        super.paintComponent(g); // force to do this
        Graphics2D g2 = (Graphics2D)g; // change Graphics g to Graphics2D to get more func
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose(); // dispose the resources of painting
    }
}

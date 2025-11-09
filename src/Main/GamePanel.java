package Main;

import Entity.Player;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.Key;

public class GamePanel extends JPanel implements Runnable{
    //screen setting
    final int originalTileSize = 16;
    final int scale = 4;

    public final int tileSize = originalTileSize * scale; // 48x48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);
    MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) { // Left click
                player.useTool();
            }
        }
    };

    public TileManager getTileManager() {
        return tileM;
    }
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        this.addMouseListener(mouseAdapter);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS; // 0.01666 seconds
        double nextDrawTime =  System.nanoTime() + drawInterval;

        while (gameThread != null){

            update();

            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000.0;

                if (remainingTime < 0)
                    remainingTime = 0;

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        tileM.draw(g2d);
        player.draw(g2d);

        g2d.dispose();
    }
}

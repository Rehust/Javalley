package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame window = new JFrame(); // create a window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // click 'x' to stop the program
        window.setResizable(false); // cannot resize the window
        window.setTitle("Javalley"); // the title

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel); // a func of JPanel: add gamePanel to window

        window.pack(); // be sized to be fit the preferred size and layouts of its subcomponents

        window.setLocationRelativeTo(null); // align center
        window.setVisible(true); // can see the window

        gamePanel.startGameThread(); // start the game thread
    }
}

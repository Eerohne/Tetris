package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tetris {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        frame.setSize(200, 400);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game game = new Game();
        frame.add(game);
        
        frame.setVisible(true);
    }
    
}

class Game extends JPanel{
    ArrayList<Tetromino> tetrominos = new ArrayList<>();
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        
        
    }
    
}

class Tetromino{
    Point[] tetromino = new Point[4];
    
    public Tetromino(){
        
    }
}

class Point{
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }
}

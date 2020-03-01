package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tetris {

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Tetris");
        frame.setSize(200, 400);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game game = new Game();
        frame.add(game);
        
        frame.setVisible(true);
        
        while(true){
            Thread.sleep(500);
            game.updateTetromino();
            game.repaint();
        }
    }
    
}

class Game extends JPanel implements KeyListener{
    //Square = 20; x = 10 ; y = 20
    int score, highScore;
    Tetromino bloc;
    ArrayList<Tetromino> tetrominos = new ArrayList<>();

    public Game() {
        addKeyListener(this);
        setFocusable(true);
        
        bloc = new Tetromino();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        
        for (Tetromino tetromino : tetrominos) {
            drawTetromino(tetromino, g);
        }
        
        //updateTetromino();
        drawTetromino(bloc, g);
    }
    
    private void drawTetromino(Tetromino tetromino, Graphics g){
        g.setColor(tetromino.color);
        for (Point point : tetromino.tetromino) {
                g.fillRect(point.x, point.y, 20, 20);
        }
    }
    
    public void updateTetromino(){
        for (Point square : bloc.tetromino) {
            square.y += 20;
        }
        if(collision()){
            while(!containsEarthCollision()){
                for (Point square : bloc.tetromino) {
                    square.y -= 20;
                }
            }
            
            Tetromino temp = new Tetromino(bloc);
            
            tetrominos.add(temp);
            this.repaint();
            bloc.generateTetromino();
        } 
        else{
            
        }
    }

    public boolean fullRow(){
        for(int i = 0; i < 20; i++){
            
        }
        
        return false;
    }
    
    public boolean collision(){
        boolean isCollision = false;
        
        for (Point point : bloc.tetromino) {
            if(point.y >= 400)
                isCollision = true;
            for (Tetromino tetromino : tetrominos) {
                for (Point staticPoint : tetromino.tetromino) {
                    if(point.equals(staticPoint))
                        return true;
                }
            }
        }
        
        if(isCollision){}
        
        return isCollision;
    }
    
    public boolean containsEarthCollision(){
        int i = 0;
        for (Point point : bloc.tetromino) {
            if((point.y <= 380) && !collision())
                ++i;
        }
        if(i == 4)
            return true;
        return false;
    }
    
    private boolean sideCollision(boolean left){
        for (Point point : bloc.tetromino) {
            if(left){
                if(point.x == 0)
                    return true;
                for (Tetromino tetromino : tetrominos) {
                    for (Point staticPoint : tetromino.tetromino) {
                        Point tempP = new Point(staticPoint);
                        //tempP.x -= 20;
                        if(tempP.equals(point))
                            return true;
                    }
                }
            }
            if(!left){
                if(point.x == 180){
                    return true;
                }
                for (Tetromino tetromino : tetrominos) {
                    for (Point staticPoint : tetromino.tetromino) {
                        if(staticPoint.equals(point))
                            return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch(keyCode){
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_DOWN:
                if(!collision()){
                    for (Point point : bloc.tetromino)
                        point.y += 20;
                }
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_LEFT:
                if(!sideCollision(true))
                    for (Point point : bloc.tetromino){
                        point.x -= 20;
                        repaint();
                    }
                break;
            case KeyEvent.VK_RIGHT:
                if(!sideCollision(false))
                    for (Point point : bloc.tetromino){
                        point.x += 20;  
                        repaint();
                    }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}


class Tetromino{
    Point[] tetromino = new Point[4];
    Color color;
    
    public Tetromino(){
        this.generateTetromino();
    }
    
    public Tetromino(Tetromino t){
        color = t.color;
        for (int i = 0; i < 4; i++) {
            this.tetromino[i] = t.tetromino[i];
        }
    }
    
    public void generateTetromino(){
        Random random = new Random();
        switch(random.nextInt(6)){
            case 0://I MIDDLE FLAT 21
                tetromino[0] = new Point(3, -1);
                tetromino[1] = new Point(4, -1);
                tetromino[2] = new Point(5, -1);
                tetromino[3] = new Point(6, -1);
                color = Color.cyan;
                break;
            case 1://O MIDDLE 21/22
                tetromino[0] = new Point(4, -1);
                tetromino[1] = new Point(5, -1);
                tetromino[2] = new Point(4, -2);
                tetromino[3] = new Point(5, -2);
                color = Color.yellow;
                break;
            case 2://T LEFT-MIDDLE FLAT-SIDE 21/22
                tetromino[0] = new Point(3, -1);
                tetromino[1] = new Point(4, -1);
                tetromino[2] = new Point(5, -1);
                tetromino[3] = new Point(4, -2);
                color = Color.magenta;
                break; 
            case 3://S LEFT-MIDDLE 21/22
                tetromino[0] = new Point(3, -1);
                tetromino[1] = new Point(4, -1);
                tetromino[2] = new Point(4, -2);
                tetromino[3] = new Point(5, -2);
                color = Color.green;
                break;
            case 4://Z LEFT-MIDDLE 21/22
                tetromino[0] = new Point(3, -2);
                tetromino[1] = new Point(4, -2);
                tetromino[2] = new Point(4, -1);
                tetromino[3] = new Point(5, -1);
                color = Color.red;
                break;
            case 5://J LEFT-MIDDLE FLAT-SIDE 21/22
                tetromino[0] = new Point(3, -1);
                tetromino[1] = new Point(4, -1);
                tetromino[2] = new Point(5, -1);
                tetromino[3] = new Point(3, -2);
                color = Color.blue;
                break;
            case 6://L LEFT-MIDDLE FLAT-SIDE 21/22
                tetromino[0] = new Point(3, -1);
                tetromino[1] = new Point(4, -1);
                tetromino[2] = new Point(5, -1);
                tetromino[3] = new Point(5, -2);
                color = Color.orange;
                break;
        }
        
        this.adaptToGrid();
    }
    
    private void adaptToGrid(){
        for (Point point : tetromino) {
            point.x *= 20;
            point.y *= 20;
        }
    }
}


class Point {
    int x, y;

    public Point(Point p){
        this(p.x, p.y);
    }
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }
    
    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Point point = (Point) o;
        // field comparison
        return Objects.equals(x, point.x)
                && Objects.equals(y, point.y);
    }
}
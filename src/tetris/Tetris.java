package tetris;

import java.awt.Color;
import java.awt.Font;
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
        this.setBackground(Color.black);
        
        if(gameOver()){
            gameOverScreen(g);
        }
        else{
            for (Tetromino tetromino : tetrominos) {
                drawTetromino(tetromino, g);
            }

            //updateTetromino();
            drawTetromino(bloc, g);
            gravity(g);
        }
    }
    
    private void drawTetromino(Tetromino tetromino, Graphics g){
        g.setColor(tetromino.color);
        for (Minos point : tetromino.tetromino) {
                g.fillRect(point.x, point.y, 20, 20);
        }
    }
    
    public void updateTetromino(){
        for (Minos square : bloc.tetromino) {
            square.y += 20;
        }
        if(collision()){
            while(!containsEarthCollision()){
                for (Minos square : bloc.tetromino) {
                    square.y -= 20;
                }
            }
            
            Tetromino temp = new Tetromino(bloc);
            
            tetrominos.add(temp);
            this.repaint();
            bloc.generateTetromino();
        }
    }

    private ArrayList<Integer> fullRows(){
        ArrayList<Integer> rows = new ArrayList<>();
        int row = 0;
        
        for(int i = 0; i < 20; i++){
            for (Tetromino tetromino : tetrominos) {
                for (Minos point : tetromino.tetromino) {
                    if(point.y == (i*20))
                        ++row;
                }
            }
            if(row==10){
                for (Tetromino tetromino : tetrominos) {
                    for (int j = 0; j < 4; j++) {
                        if(tetromino.tetromino[j].y == (i*20))
                            tetromino.tetromino[j].destroy();
                    }
                }
                rows.add(i);
            }              
            else row = 0;
        }
        
        switch(rows.size()){
            case 1:
                score += 400;
                break;
            case 2:
                score += 1000;
                break;
            case 3:
                score += 3000;
                break;
            case 4:
                score += 12000;
                break;
            default:
                break;
        }
        
        return rows;
    }
    
    public void gravity(Graphics g){
        ArrayList<Integer> fullRows = fullRows();
        if(!fullRows.isEmpty()){
            for (Integer i : fullRows) {
                g.setColor(Color.black);
                g.fillRect(0, i*20, 200, 20);
            }
            
            for (Integer fullRow : fullRows) {
                for(int i = fullRow; i >= 0; i--){
                    for (Tetromino tetromino : tetrominos) {
                        for (Minos point : tetromino.tetromino) {
                            if(point.y < i*20){
                                point.y += 20;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public boolean collision(){
        
        for (Minos point : bloc.tetromino) {
            if(point.y >= 400)
                return true;
            for (Tetromino tetromino : tetrominos) {
                for (Minos staticMinos : tetromino.tetromino) {
                    if(point.equals(staticMinos))
                        return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean containsEarthCollision(){
        int i = 0;
        for (Minos point : bloc.tetromino) {
            if((point.y <= 380) && !collision())
                ++i;
        }
        if(i == 4)
            return true;
        return false;
    }
    
    private boolean sideCollision(boolean left){
        for (Minos point : bloc.tetromino) {
            if(left){
                if(point.x < 0)
                    return true;
            }
            if(!left){
                if(point.x >= 200)
                    return true;
            }
            
            for (Tetromino tetromino : tetrominos) {
                for (Minos staticMinos : tetromino.tetromino) {
                    if(staticMinos.equals(point)){
                        if(staticMinos.equals(point)){
                            System.out.println("INDEED");
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean gameOver(){
        for (Minos point : bloc.tetromino) {
            if(collision() && point.y <= 0)
                return true;
        }
        
        return false;
    }
    
    public void gameOverScreen(Graphics g){
        if(score > highScore){
            highScore = score;
        }
        
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        g.setColor(Color.RED);
        g.drawString("GAME OVER", 10, 20*2);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Score : " + this.score, 10, 20*3+5);
        g.drawString("High Score : " + this.highScore, 10, 20*4+10);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        g.drawString("Press any key to continue...", 10, 20*5+15);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if(gameOver()){
            switch(keyCode){
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                default:
                    score = 0;
                    tetrominos.clear();
                    bloc.generateTetromino();
            }
        }else{
            switch(keyCode){
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_SPACE:
                    bloc.generateTetromino();
                    break;
                case KeyEvent.VK_DOWN:
                    if(!collision()){
                        for (Minos point : bloc.tetromino)
                            point.y += 20;
                    }
                    break;
                case KeyEvent.VK_UP:
                    bloc.rotation();
                    break;
                case KeyEvent.VK_LEFT:
                    for (Minos point : bloc.tetromino){
                        point.x -= 20;
                    }
                    if(sideCollision(true))
                        for (Minos point : bloc.tetromino){
                            point.x += 20;
                        }
                    repaint();
                    break;
                case KeyEvent.VK_RIGHT:
                    for (Minos point : bloc.tetromino){
                        point.x += 20;
                    }
                    if(sideCollision(false))
                        for (Minos point : bloc.tetromino){
                            point.x -= 20;
                        }
                    repaint();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}


class Tetromino{
    Minos[] tetromino = new Minos[4];
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
        switch(random.nextInt(7)){
            case 0://I MIDDLE FLAT 21
                tetromino[0] = new Minos(3, -1);
                tetromino[1] = new Minos(4, -1);
                tetromino[2] = new Minos(5, -1);
                tetromino[3] = new Minos(6, -1);
                color = Color.cyan;
                break;
            case 1://O MIDDLE 21/22
                tetromino[0] = new Minos(4, -1);
                tetromino[1] = new Minos(5, -1);
                tetromino[2] = new Minos(4, -2);
                tetromino[3] = new Minos(5, -2);
                color = Color.yellow;
                break;
            case 2://T LEFT-MIDDLE FLAT-SIDE 21/22
                tetromino[0] = new Minos(3, -1);
                tetromino[1] = new Minos(4, -1);
                tetromino[2] = new Minos(5, -1);
                tetromino[3] = new Minos(4, -2);
                color = Color.magenta;
                break; 
            case 3://S LEFT-MIDDLE 21/22
                tetromino[0] = new Minos(3, -1);
                tetromino[1] = new Minos(4, -1);
                tetromino[2] = new Minos(4, -2);
                tetromino[3] = new Minos(5, -2);
                color = Color.green;
                break;
            case 4://Z LEFT-MIDDLE 21/22
                tetromino[0] = new Minos(3, -2);
                tetromino[1] = new Minos(4, -2);
                tetromino[2] = new Minos(4, -1);
                tetromino[3] = new Minos(5, -1);
                color = Color.red;
                break;
            case 5://J LEFT-MIDDLE FLAT-SIDE 21/22
                tetromino[0] = new Minos(3, -1);
                tetromino[1] = new Minos(4, -1);
                tetromino[2] = new Minos(5, -1);
                tetromino[3] = new Minos(3, -2);
                color = Color.blue;
                break;
            case 6://L LEFT-MIDDLE FLAT-SIDE 21/22
                tetromino[0] = new Minos(3, -1);
                tetromino[1] = new Minos(4, -1);
                tetromino[2] = new Minos(5, -1);
                tetromino[3] = new Minos(5, -2);
                color = Color.orange;
                break;
        }
        
        this.adaptToGrid();
    }
    
    private void adaptToGrid(){
        for (Minos point : tetromino) {
            point.x *= 20;
            point.y *= 20;
        }
    }
    
    private void unadaptToGrid(){
        for (Minos point : tetromino) {
            point.x /= 20;
            point.y /= 20;
        }
    }
    
    public void rotation(){
        //I
        /*if(color == Color.cyan){
            Minos second = new Minos(tetromino[1]);
            boolean isHorizontal = true;
            
            for (int i = 1; i < 4; i++) {
                if(!(tetromino[i].y == tetromino[i-1].y))
                    isHorizontal = false;
            }
            
            if(isHorizontal){
                for (Minos minos : tetromino) {
                    minos.x = second.x;
                }
                
                tetromino[0].y -= 20;
                tetromino[2].y += 20;
                tetromino[3].y += 40;
            }
            else{
                for (Minos minos : tetromino) {
                    minos.y = second.y;
                }
                
                tetromino[0].x -= 20;
                tetromino[2].x += 20;
                tetromino[3].x += 40;
            }
        }*/
        //O
        if(color == Color.yellow)
            return;
        /*//T
        else if(color == Color.magenta){
        }
        //S
        else if(color == Color.green){
            Minos second = new Minos(tetromino[1]);
            boolean isHorizontal = false;
            
            if(tetromino[0].y == tetromino[1].y){
                isHorizontal = true;
            }
            
            if(isHorizontal){
                tetromino[0].x = second.x;
                tetromino[2].x = second.x + 20;
                tetromino[3].x = second.x + 20;
                
                tetromino[0].y -= 20;
                tetromino[2].y += 20;
                tetromino[3].y += 40;
            }
            else{
                tetromino[0].y = second.y;
                tetromino[2].y = second.y - 20;
                tetromino[3].y = second.y - 20;
                
                tetromino[0].x -= 20;
                tetromino[2].x -= 20;
            }
        }
        //Z
        else if(color == Color.red){
            Minos second = new Minos(tetromino[2]);
            boolean isHorizontal = false;
            
            if(tetromino[0].y == tetromino[1].y){
                isHorizontal = true;
            }
            
            if(isHorizontal){
                tetromino[0].x = second.x - 20;
                tetromino[1].x = second.x - 20;
                tetromino[3].x = second.x;
                
                tetromino[0].y += 20;
                tetromino[2].y -= 20;
                tetromino[3].y -= 40;
            }
            else{
                tetromino[0].y = second.y - 20;
                tetromino[1].y = second.y - 20;
                tetromino[3].y = second.y;
                
                tetromino[1].x += 20;
                tetromino[3].x += 20;
            }
        }
        //J
        else if(color == Color.blue){
        }
        //L
        else if(color == Color.orange){
        }*/
        
        else{
            //unadaptToGrid();
            Minos pivot = new Minos(tetromino[2]);
            //pivot.x /= 20;
            //pivot.y /= 20;
            
            for (Minos minos : tetromino) {
                //if(!minos.equals(pivot)){
                    int yTranslation = (minos.x - pivot.x );
                    int xTranslation = (pivot.y - minos.y);

                    minos.x = (pivot.y  + xTranslation);
                    minos.y = (pivot.x  + yTranslation);
                    
                    //minos.x = (pivot.x + yTranslation);
                
            }
            
            //adaptToGrid();
        }
    }
}


class Minos {
    int x, y;

    public Minos(Minos p){
        this(p.x, p.y);
    }
    
    public Minos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Minos() {
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
        Minos point = (Minos) o;
        // field comparison
        return Objects.equals(x, point.x)
                && Objects.equals(y, point.y);
    }
    
    public void destroy(){
        this.x = 1000;
        this.y = -1000;
    }
}
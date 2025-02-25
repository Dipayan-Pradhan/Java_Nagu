import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class snakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tilesize =25;

    Tile snakeHead;
    Tile Food;
    ArrayList<Tile> snakeBody;
    Random random;
    Timer gameLoop;

    int velocityX;
    int velocityY;
    boolean gameOver = false;


    snakeGame (int boardWidth,int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        addKeyListener(this);
        setFocusable(true);

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();
        Food = new Tile(10,7);
        
        random = new Random();
        placeFood();

        velocityX=0;
        velocityY=0;

        gameLoop =new Timer(100,this);
        gameLoop.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw (Graphics g){
    /*for(int i=0;i<boardWidth/tilesize;i++){
            g.drawLine(i*tilesize,0,i*tilesize,boardHeight);
            g.drawLine(0,i*tilesize,boardWidth,i*tilesize);
        } */
        //snakehead 
        g.setColor(Color.blue);
        //g.fillRect(snakeHead.x*tilesize,snakeHead.y*tilesize,tilesize,tilesize);
        g.fill3DRect(snakeHead.x*tilesize,snakeHead.y*tilesize,tilesize,tilesize,true);
        //Food
        g.setColor(Color.red);
        //g.fillRect(Food.x*tilesize,Food.y*tilesize,tilesize,tilesize);
        g.fill3DRect(Food.x*tilesize,Food.y*tilesize,tilesize,tilesize,true);

        g.setColor(Color.blue);
        for(int i=0; i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x*tilesize, snakePart.y*tilesize, tilesize, tilesize);
            g.fill3DRect(snakePart.x*tilesize, snakePart.y*tilesize, tilesize, tilesize,true);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver){
            g.setColor(Color.green);
            g.drawString("GameOver"+ String.valueOf(snakeBody.size()), tilesize-16, tilesize);
        }else{
            g.setColor(Color.green);
            g.drawString("Score= "+String.valueOf(snakeBody.size()), tilesize-16, tilesize);
        }

    }
    public void placeFood(){
        Food.x =random.nextInt(boardWidth/tilesize);//600/25=24
        Food.y=random.nextInt(boardHeight/tilesize);

    /*// Increase speed by reducing timer delay
    int newDelay = gameLoop.getDelay() - 1;  // Reduce delay by 1ms
    if (newDelay >= 50) {  // Ensure it doesn't go below 50ms
        gameLoop.setDelay(newDelay);
    } else {
        gameLoop.setDelay(50);  // Set a minimum speed limit
    }*/

    }
    public boolean collision(Tile tile1 , Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        if(collision(snakeHead, Food)){
            snakeBody.add(new Tile(Food.x, Food.y));
            placeFood();
        }
        for(int i=snakeBody.size()-1; i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for(int i= 0; i< snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if(snakeHead.x*tilesize < 0 || snakeHead.x*tilesize > boardHeight ||
            snakeHead.y*tilesize<0 || snakeHead.y*tilesize > boardHeight){
                gameOver= true;
            }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && velocityY != 1) {
            velocityX=0;
            velocityY=-1;
        }else if((e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && velocityY != -1){
            velocityX=0;
            velocityY=1;
        }else if((e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) && velocityX != 1){
            velocityX=-1;
            velocityY=0;
        }else if((e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) && velocityX != -1){
            velocityX=1;
            velocityY=0;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
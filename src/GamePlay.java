import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class GamePlay extends JPanel implements KeyListener,ActionListener {
    private boolean play=false;
    private int score=0;
    private int totalBricks=21;
    private final Timer time;
    private int playerX=310;
    private int ballposX=120;
    private int ballposY=350;
    private int ballXdir=-1;
    private int ballYdir=-2;
    private MapGenerator map;

    public GamePlay(){
        map=new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 0;
        time=new Timer(delay,this);
        time.start();
    }
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(1,1,692,592);
        map.draw((Graphics2D) g);
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,682,3);
        g.fillRect(691,0,3,592);

        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,590,30);

        g.setColor(Color.yellow);
        g.fillRect(playerX,550,100,8);
        //ball color
        g.setColor(Color.green);
        g.fillOval(ballposX,ballposY,20,30);

        if(ballposY>570){
            play =false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over : "+score,190,300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press Enter to Restart",190,350);
        }
        if(totalBricks==0){
            play=false;
            ballYdir=-2;
            ballXdir=-1;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over : "+score,190,300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press Enter to Restart",190,350);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        time.start();
        if(play) {
            if (new Rectangle(ballposX , ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 0))){
                ballYdir = -ballYdir;
            }
        }
        ABORT:
        for(int i=0;i<map.map.length;i++){
            for(int j=0;j<map.map[0].length;j++){
                if(map.map[i][j]>0){
                    int brickX=j*map.brickwidth+80;
                    int brickY=i*map.brickheight+50;
                    int brickWidth=map.brickwidth;
                    int brickHeight=map.brickheight;


                    Rectangle rect=new Rectangle(brickX,brickY,brickWidth,brickHeight);
                    Rectangle ballrect=new Rectangle(ballposX,ballposY,20,20);
                    if(ballrect.intersects(rect)){
                        map.setBrickValue(0,i,j);
                        totalBricks--;
                        score+=5;
                        if(ballposX+19<= rect.x || ballposX+1 >= rect.x+brickWidth){
                            ballXdir=-ballXdir;
                        }
                        else{
                            ballYdir=-ballYdir;
                        }
                        break ABORT;
                    }
                }
            }
        }
        ballposX+=ballXdir;
        ballposY+=ballYdir;
        if(ballposX<0)
            ballXdir=-ballXdir;
        if(ballposY<0){
            ballYdir=-ballYdir;
        }
        if(ballposX>670)
            ballXdir=-ballXdir;

        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT){
            if (playerX >= 600) {
                playerX = 600;
            } else {
                MoveRight();
            }
        }
        //  boolean b = e.getKeyCode() == KeyEvent.VK_LEFT;
        if ( e.getKeyCode() == KeyEvent.VK_LEFT){
            if (playerX < 10) {
                playerX = 10;
            } else {
                MoveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                ballposX=120;
                ballposY=350;
                ballXdir=-1;
                ballYdir=-2;
                score=0;
                playerX=310;
                totalBricks=21;
                map=new MapGenerator(3,7);
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void MoveRight(){
        play=true;
        playerX+=20;
    }
    public void MoveLeft(){
        play=true;
        playerX-=20;
    }
}
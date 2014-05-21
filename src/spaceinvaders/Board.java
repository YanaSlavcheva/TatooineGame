package spaceinvaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Board extends JPanel implements Runnable, Commons { 

    private Dimension d;
    private ArrayList aliens; // TODO change aliens spacing in array
    private Player player;
    private Shot shot;
    
    
    private int alienX = 650; //original 150 // TODO: to set the position right after correcting the movement
    private int alienY = 5; // original 5
    private int direction = -1; //original -1
    public  int deaths = 0;

    private int lives = 30; // This must be 30 if we want to have 3 lives. Coz shot is 10px long; Nick
    
    private boolean ingame = true;
    private final String expl = "../spacepix/explosion.png";
    private final String alienpix = "../spacepix/alien.png";
    private String message = "Game Over";

    private Thread animator;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.white); //changed from black

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {

        aliens = new ArrayList();

        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienpix));

        for (int i=0; i < 5; i++) { //changed from 4 - !!!linked with NUMBER_OF_ALIENS_TO_DESTROY in Commons.java
            for (int j=0; j < 3; j++) { //changed from 6
                Alien alien = new Alien(alienX + 50*j, alienY + 50*i);// changed from 18
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) 
    {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            ingame = false;
        
        }
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible())
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
    }

    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this); 
            }
        }
    }
    
    // This method show score and lives; Nick 
    public void drawScore(Graphics g){
    	
    	int score = deaths;
    	
    	Font small = new Font("Helvetica", Font.BOLD, 14);
        
        g.setColor(Color.black);
        g.setFont(small);
        g.drawString("Score: "+Integer.toString(score), 50, 50);
        g.drawString("HP: "+Integer.toString(lives/10), 50, 70);

    }
    
    // Method to draw the background; Mitko
    public void drawBackground(Graphics g){
    	BackgroundImage bground = new BackgroundImage();
    	
    	g.drawImage(bground.getImage(), -8, -8, this);
    }
    
    public void paint(Graphics g)
    {
      super.paint(g);
      
      //TODO we don't need the ground - remove it later?!?
//      g.setColor(Color.white); //changed from black
//      g.fillRect(0, 0, d.width, d.height); //changed (0, 0, d.width, d.height)
//      g.setColor(Color.green);   

      if (ingame) {
    	  
    	drawBackground(g);
//        g.drawLine(GROUND, 0, GROUND, BOARD_HEIGTH); //changed from (0, GROUND, BOARD_WIDTH, GROUND)
        drawAliens(g);
        drawPlayer(g);
        drawShot(g);
        drawBombing(g);
        drawScore(g);
      }

      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }

    public void gameOver()
    {

        Graphics g = this.getGraphics();

        g.setColor(Color.black); 
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message))/2, 
            BOARD_WIDTH/2);
    }

    public void animationCycle()  {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Game won!";
        }

        // player

        player.act();

        // shot
        if (shot.isVisible()) {
            Iterator it = aliens.iterator();
            int shotX = shot.getX();
            int shotY = shot.getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX) && 
                        shotX <= (alienX + ALIEN_WIDTH) &&
                        shotY >= (alienY) &&
                        shotY <= (alienY+ALIEN_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(getClass().getResource(expl));
                            alien.setImage(ii.getImage());
                            alien.setDying(true);
                            deaths++;
                            shot.die();
                        }
                }
            }
            
            //here we manage the player shots behaviour
            //TODO to make the player shoot more than one shot per screen
            int x = shot.getX(); //changed from int y = shot.getY();
            x += 4; //changed from y-= 4;
            if (x > BOARD_WIDTH) //changed from y < 0
                shot.die();
            else shot.setX(x); //changed from shot.setY(y);
        }

        // aliens
        // this makes the aliens bounce up and down
         Iterator it1 = aliens.iterator();

         while (it1.hasNext()) {
             Alien a1 = (Alien) it1.next();
             int y = a1.getY(); //changed from x-es

             if (y  >= BOARD_HEIGTH - BORDER_DOWN && direction != -1) { //changed from x, width
                 direction = -1;
                 Iterator i1 = aliens.iterator();
                 while (i1.hasNext()) {
                     Alien a2 = (Alien) i1.next();
                     a2.setX(a2.getX() - GO_LEFT); //changed from a2.setY(a2.getY() + GO_DOWN);
                 }
             }

            if (y <= BORDER_UP && direction != 1) { //changed from x
                direction = 1;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                    a.setX(a.getX() - GO_LEFT); //changed from a.setY(a.getY() + GO_DOWN);
                }
            }
        }

        //guess this makes aliens win the game - invasion 
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int x = alien.getX(); //changed from x-es

                if (x < GROUND + ALIEN_WIDTH) { //changed from (y > GROUND - ALIEN_HEIGHT)
                    ingame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());   
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !b.isDestroyed()) {
                if ( bombY >= (playerY) && //changed from  bombX >= (playerX)
                    bombY <= (playerY+PLAYER_HEIGHT) && //changed from bombX <= (playerX+PLAYER_WIDTH)
                    bombX >= (playerX) && //changed from bombY >= (playerY)
                    bombX <= (playerX+PLAYER_WIDTH) ) { //changed from bombY <= (playerY+PLAYER_HEIGHT)
                	
                	if (lives==1) { // Here we implement lives idea; Nick
						
					ImageIcon ii = 
                            new ImageIcon(this.getClass().getResource(expl));
                        player.setImage(ii.getImage());
                        player.setDying(true);
                        b.setDestroyed(true);;
                    }else {
						lives--;
					}
                }
            }
        	
            //this moves the aliens bombs left
            if (!b.isDestroyed()) { // speed of bombs is here; Nick
                b.setX(b.getX() - 1);  //changed from b.setY(b.getY() + 1);
                if (b.getX() <= GROUND + BOMB_WIDTH) { //changed from b.getY() >= GROUND - BOMB_HEIGHT
                    b.setDestroyed(true);
                }
            }
        }
    }

    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {
            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) 
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

          player.keyPressed(e);

          int x = player.getX();
          int y = player.getY();

          if (ingame)
          {
            if (e.isControlDown()) { //changed from (e.isAltDown()) - we will shoot with ctrl
                if (!shot.isVisible()) 
                    shot = new Shot(x, y);
                    
            }
          }
        }
    }
}

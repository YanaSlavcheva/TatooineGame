package tatooine;


import org.apache.commons.io.FileUtils;






import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

import java.io.File;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Commons {
	
	
	
	
	private Dimension d;
	private ArrayList fighters; // TODO change fighters spacing in array
	private Player player;
	private Shot shot;

	private int fighterX = 650; // original 150 // TODO: to set the position
								// right
								// after correcting the movement
	private int fighterY = 5 + DIVIDING_LINE; // original 5
	private int direction = -1; // original -1
	public int deaths = 0;
	public int score;


	private int lives = 100; // This must be 30 if we want to have 3 lives. Coz
								// shot is 10px long; Nick

	private boolean ingame = true;
	private final String expl = "../tatooinepix/explosion.png";
	private final String fighterpix = "../tatooinepix/fighter.png";
	private String message = "Game Over";
	private String messageExtended = "Nice try. I knew you were going to fail anyway...";
	private String messageScores = "SCORE: ";

	private Thread animator;

	public Board() {

		addKeyListener(new TAdapter());
		setFocusable(true);
		d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
		setBackground(Color.white);

		gameInit();
		setDoubleBuffered(true);
	}

	public void addNotify() {
		super.addNotify();
		gameInit();
	}

	public void gameInit() {

		fighters = new ArrayList();

		ImageIcon ii = new ImageIcon(this.getClass().getResource(fighterpix));

		for (int i = 0; i < 5; i++) { //!!!linked with NUMBER_OF_FIGHTERS_TO_DESTROY in Commons.java
			for (int j = 0; j < 3; j++) {
				Fighter fighter = new Fighter(fighterX + 50 * j, fighterY + 50
						* i);
				fighter.setImage(ii.getImage());
				fighters.add(fighter);
			}
		}

		player = new Player();
		shot = new Shot();

		if (animator == null || !ingame) {
			animator = new Thread(this);
			animator.start();
		}
	}

	public void drawFighters(Graphics g) {
		Iterator it = fighters.iterator();

		while (it.hasNext()) {
			Fighter fighter = (Fighter) it.next();

			if (fighter.isVisible()) {
				g.drawImage(fighter.getImage(), fighter.getX(), fighter.getY(),
						this);
			}

			if (fighter.isDying()) {
				fighter.die();
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

		Iterator i3 = fighters.iterator();

		while (i3.hasNext()) {
			Fighter a = (Fighter) i3.next();

			Fighter.Bomb b = a.getBomb();

			if (!b.isDestroyed()) {
				g.drawImage(b.getImage(), b.getX(), b.getY(), this);
			}
		}
	}

	// This method show score and lives
	public void drawScore(Graphics g) {

//		int score = deaths * 100;

		Font small = new Font("Helvetica", Font.BOLD, 16);

		g.setColor(Color.gray);
		g.setFont(small);
		g.drawString("SCORE: " + Integer.toString(score), 40, 35);
		g.drawString("HP: " + Integer.toString(lives), 40, 60);

	}

	// Method to draw the background
	public void drawBackground(Graphics g) {
		BackgroundImage bground = new BackgroundImage();

		g.drawImage(bground.getImage(), -8, -8, this);

		// the name of the game on the top
		Font small = new Font("Helvetica", Font.BOLD, 16);
		Font big = new Font("Helvetica", Font.BOLD, 30);

		g.setColor(Color.gray);
		g.setFont(big);
		g.drawString("T  A  T  O  O  I  N  E", 380, 40);
		g.setColor(Color.gray);
		g.setFont(small);
		g.drawString("THE    EMPIRE    STRIKES    BACK", 382, 60);
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (ingame) {

			drawBackground(g);

			// the line to keep the playing field from the captions
			g.setColor(Color.gray);
			g.fillRect(0, DIVIDING_LINE, BOARD_WIDTH, 5);
			g.setColor(Color.gray);

			drawFighters(g);
			drawPlayer(g);
			drawShot(g);
			drawBombing(g);
			drawScore(g);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void gameOver() {

		Graphics g = this.getGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

		//GAME OVER
		// this sets the rectangle
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 90, BOARD_WIDTH - 100, 105);
		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 90, BOARD_WIDTH - 100, 105);

		// this writes the main message
		Font big = new Font("Helvetica", Font.BOLD, 30);
		FontMetrics finalTexts = this.getFontMetrics(big);
		g.setColor(Color.white);
		g.setFont(big);
		g.drawString(message, (BOARD_WIDTH - finalTexts.stringWidth(message)) / 2,
				BOARD_WIDTH / 2 - 50);
		
		// this writes the additional message
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics finalTextsSmall = this.getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(messageExtended, (BOARD_WIDTH - finalTextsSmall.stringWidth(messageExtended)) / 2, BOARD_WIDTH / 2 - 25);
		
		//this draws scores
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(messageScores + Integer.toString(score), (BOARD_WIDTH - finalTextsSmall.stringWidth(messageScores)) / 2, BOARD_WIDTH / 2);
		
		try {
			scoreBoard(g);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
	}
	//We will call scoreBoard() from gameOver() Nick;
		
	private void scoreBoard(Graphics g) throws Exception {
		
		String userName ="";
		File file = new File("scoreBoard.mge"); 
		if (!file.exists()) {
			
			file.createNewFile();
			String defaulfName = "Gencho";
			int defaultScore = 0;
			FileUtils.write(file, Integer.toString(defaultScore)+" "+ defaulfName);
		
		}
		String string = FileUtils.readFileToString(file); 
		int highScore = Integer.parseInt(string.trim().split(" ")[0]);
		

		if (highScore<score) {
			
			userName = JOptionPane.showInputDialog("Enter your name");
			FileUtils.write(file, Integer.toString(score)+" "+ userName);
			g.drawString(Integer.toString(score), 400,200);
		}else{
			userName = string.trim().split(" ")[1];
			g.drawString(Integer.toString(highScore), 400,200);
		}
		
		g.setColor(Color.white);
		g.drawString("HIGH SCORE", 300,150);
		g.drawString(userName, 250,200);
				
	}
	
	public void animationCycle() {

		if (deaths == NUMBER_OF_FIGHTERS_TO_DESTROY) {
			ingame = false;
			message = "Game won!";
			messageExtended = "Wasn't that hard, was it? Enjoy your life on your free planet. By yourself.";
		}

		// player
		player.act();

		// shot
		if (shot.isVisible()) {
			Iterator it = fighters.iterator();
			int shotX = shot.getX();
			int shotY = shot.getY();

			while (it.hasNext()) {
				Fighter fighter = (Fighter) it.next();
				int fighterX = fighter.getX();
				int fighterY = fighter.getY();

				if (fighter.isVisible() && shot.isVisible()) {
					if (shotX >= (fighterX)
							&& shotX <= (fighterX + FIGHTER_WIDTH)
							&& shotY >= (fighterY)
							&& shotY <= (fighterY + FIGHTER_HEIGHT)) {
						ImageIcon ii = new ImageIcon(getClass().getResource(
								expl));
						fighter.setImage(ii.getImage());
						fighter.setDying(true);
						deaths++;
						shot.die();
					}
				}
			}
			score = deaths * 100;
			//the player shots behavior
			int x = shot.getX();
			x += 4;
			if (x > BOARD_WIDTH)
				shot.die();
			else
				shot.setX(x);
		}


		// fighters
		// this makes the fighters bounce up and down
		Iterator it1 = fighters.iterator();

		while (it1.hasNext()) {
			Fighter a1 = (Fighter) it1.next();
			int y = a1.getY();

			if (y >= BOARD_HEIGTH - BORDER_DOWN && direction != -1) {
				direction = -1;
				Iterator i1 = fighters.iterator();
				while (i1.hasNext()) {
					Fighter a2 = (Fighter) i1.next();
					a2.setX(a2.getX() - GO_LEFT);
				}
			}

			if (y <= BORDER_UP + DIVIDING_LINE && direction != 1) {
				direction = 1;

				Iterator i2 = fighters.iterator();
				while (i2.hasNext()) {
					Fighter a = (Fighter) i2.next();
					a.setX(a.getX() - GO_LEFT);
				}
			}
		}

		// guess this makes fighters win the game - invasion
		Iterator it = fighters.iterator();

		while (it.hasNext()) {
			Fighter fighter = (Fighter) it.next();
			if (fighter.isVisible()) {

				int x = fighter.getX();

				if (x < GROUND + FIGHTER_WIDTH) {
					ingame = false;
					message = "Invasion!";
					messageExtended = "Well. You're fucked.";
				}

				fighter.act(direction);
			}
		}

		// bombs

		Iterator i3 = fighters.iterator();
		Random generator = new Random();

		while (i3.hasNext()) {
			int shot = generator.nextInt(15);
			Fighter a = (Fighter) i3.next();
			Fighter.Bomb b = a.getBomb();
			if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

				b.setDestroyed(false);
				b.setX(a.getX());
				b.setY(a.getY());
			}

			int bombX = b.getX();
			int bombY = b.getY();
			int playerX = player.getX();
			int playerY = player.getY();

			// HIT DETECTION
			if (player.isVisible() && !b.isDestroyed()) {
				if (bombY >= (playerY) && 
						bombY <= (playerY + PLAYER_HEIGHT) && 
						bombX >= (playerX) &&
						bombX <= (playerX + PLAYER_WIDTH + HIT_BUFFER)) {

					// count of lives
					if (lives < 1) {

						ImageIcon ii = new ImageIcon(this.getClass()
								.getResource(expl));
						player.setImage(ii.getImage());
						player.setDying(true); // Player dies
						b.setDestroyed(true);
					} else {
						lives--;
					}
				}
			}

			// this moves the fighters bombs left
			if (!b.isDestroyed()) { // speed of bombs is here
				b.setX(b.getX() - 5);
				if (b.getX() <= GROUND + BOMB_WIDTH) {
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

			if (ingame) {
				if (e.isControlDown()) {
					if (!shot.isVisible())
						shot = new Shot(x, y);

				}
				if (e.isShiftDown()) { // Add a kill button;  Nick
					lives =0;		   //
					ingame = false;    // must delete it letter;
				}
			}
		}
	}
}

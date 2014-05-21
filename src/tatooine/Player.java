package tatooine;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class Player extends Sprite implements Commons{

    private final int START_Y = 310 + DIVIDING_LINE/2; //changed from 280
    private final int START_X = 10; //changed from 270

    private final String player = "../tatooinepix/player.png";
    private int heigth; //changed from width

    public Player() {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(player));

        heigth = ii.getImage().getHeight(null); // changed from width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        y += dy; //changed from x += dx;
        if (y <= BORDER_UP + DIVIDING_LINE) // changed from x
            y = BORDER_UP + DIVIDING_LINE; // changed from x
        if (y >= BOARD_HEIGTH - 2*heigth) //changed from (x >= BOARD_WIDTH - 2*width)
            y = BOARD_HEIGTH - 2*heigth; // x = BOARD_WIDTH - 2*width;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) //changed from VK_LEFT
        {
            dy = -2; //changed from dx
        }

        if (key == KeyEvent.VK_DOWN) //changed from VK_RIGHT
        {
            dy = 2; //changed from dx
        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) //changed from VK_LEFT
        {
            dy = 0; //changed from dx
        }

        if (key == KeyEvent.VK_DOWN) //changed from VK_RIGHT
        {
            dy = 0; //changed from dx
        }
    }
}

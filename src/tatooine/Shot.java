package tatooine;

import javax.swing.ImageIcon;


public class Shot extends Sprite {

    private String shot = "../tatooinepix/shot.png";
    private final int H_SPACE = 20; //changed from 1
    private final int V_SPACE = 23; //changed from 6

    public Shot() {
    }

    public Shot(int x, int y) {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y + V_SPACE);
    }
}

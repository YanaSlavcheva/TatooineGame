package tatooine;

import javax.swing.ImageIcon;


public class Fighter extends Sprite {

    private Bomb bomb;
    private final String shot = "../tatooinepix/fighter.png";

    public Fighter(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());

    }
    //this codes makes fighters bounce up-down
    public void act(int direction) {
        this.y += direction; //changed from x += direction
        					
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private final String bomb = "../tatooinepix/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(this.getClass().getResource(bomb));
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}

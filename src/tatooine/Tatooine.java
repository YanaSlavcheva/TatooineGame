package tatooine;

import javax.swing.JFrame;

public class Tatooine extends JFrame implements Commons {

    public Tatooine()
    {
        add(new Board());
        setTitle("Tatooine - The Empire Strikes Back");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new Tatooine();
    }
}
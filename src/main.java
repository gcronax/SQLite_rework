import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class main {
    static JFrame frameMenu = null;

    public static void main(String[] args) {
        Scanner scan= new Scanner(System.in);
        frameMenu = new JFrame("menu");
        frameMenu.setSize(600, 100);

        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameMenu.setLocation(dimension.width/4, dimension.height/4);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        JButton btnpisos = new JButton("skateboards");
        btnpisos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("skateboard","skateboards");
            frameMenu.setVisible(false);
        });
        btnpisos.setFont(new Font("Arial", Font.BOLD, 18));
        btnpisos.setBackground(Color.lightGray);
        panel.add(btnpisos);

        JButton btnpropietarios = new JButton("ruedas");
        btnpropietarios.addActionListener(e -> {
            tablasrefactorizado.menuTablas("rueda","ruedas");
            frameMenu.setVisible(false);
        });
        btnpropietarios.setFont(new Font("Arial", Font.BOLD, 18));
        btnpropietarios.setBackground(Color.lightGray);
        panel.add(btnpropietarios);

        JButton btninquilinos = new JButton("tablas");
        btninquilinos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("tabla","tablas");
            frameMenu.setVisible(false);
        });
        btninquilinos.setFont(new Font("Arial", Font.BOLD, 18));
        btninquilinos.setBackground(Color.lightGray);
        panel.add(btninquilinos);


        frameMenu.add(panel);
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMenu.setVisible(true);



    }
}

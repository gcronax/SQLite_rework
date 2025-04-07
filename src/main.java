import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class main {
    public static JPanel panel = new JPanel();

    public static void main(String[] args) {
        tablasrefactorizado.frameSubMenu.setTitle("Menu");
        tablasrefactorizado.frameSubMenu.setSize(800, 500);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        tablasrefactorizado.frameSubMenu.setLocation(dimension.width/4, dimension.height/4);

        panel.setLayout(new GridLayout(1, 1));
        JButton btnpisos = new JButton("skateboards");
        btnpisos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("skateboard","skateboards");
            tablasrefactorizado.frameSubMenu.setVisible(false);
        });
        btnpisos.setFont(new Font("Arial", Font.BOLD, 18));
        btnpisos.setBackground(Color.lightGray);
        panel.add(btnpisos);

        JButton btnpropietarios = new JButton("ruedas");
        btnpropietarios.addActionListener(e -> {
            tablasrefactorizado.menuTablas("rueda","ruedas");
            tablasrefactorizado.frameSubMenu.setVisible(false);
        });
        btnpropietarios.setFont(new Font("Arial", Font.BOLD, 18));
        btnpropietarios.setBackground(Color.lightGray);
        panel.add(btnpropietarios);

        JButton btninquilinos = new JButton("tablas");
        btninquilinos.addActionListener(e -> {
            tablasrefactorizado.menuTablas("tabla","tablas");
            panel.setVisible(false);
            tablasrefactorizado.frameSubMenu.revalidate();
            tablasrefactorizado.frameSubMenu.repaint();
        });
        btninquilinos.setFont(new Font("Arial", Font.BOLD, 18));
        btninquilinos.setBackground(Color.lightGray);
        panel.add(btninquilinos);


        tablasrefactorizado.frameSubMenu.setLayout(new BorderLayout());

        tablasrefactorizado.frameSubMenu.add(panel, BorderLayout.NORTH);
        tablasrefactorizado.frameSubMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tablasrefactorizado.frameSubMenu.setVisible(true);



    }
}

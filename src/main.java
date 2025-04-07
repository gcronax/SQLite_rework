import javax.swing.*;
import java.awt.*;

public class main {
    public static JPanel panelMenu = new JPanel();

    public static void main(String[] args) {
        tablasrefactorizado.frameSubMenu.setTitle("Menu");
        tablasrefactorizado.frameSubMenu.setSize(800, 500);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        tablasrefactorizado.frameSubMenu.setLocation(dimension.width/4, dimension.height/4);

        panelMenu.setLayout(new GridLayout(1, 1));
        JButton btnskate = new JButton("skateboards");
        btnskate.addActionListener(e -> {
            tablasrefactorizado.menuTablas("skateboard","skateboards");
            panelMenu.setVisible(false);
            tablasrefactorizado.frameSubMenu.revalidate();
            tablasrefactorizado.frameSubMenu.repaint();        });
        btnskate.setFont(new Font("Arial", Font.BOLD, 18));
        btnskate.setBackground(Color.lightGray);
        panelMenu.add(btnskate);

        JButton btnruedas = new JButton("ruedas");
        btnruedas.addActionListener(e -> {
            tablasrefactorizado.menuTablas("rueda","ruedas");
            panelMenu.setVisible(false);
            tablasrefactorizado.frameSubMenu.revalidate();
            tablasrefactorizado.frameSubMenu.repaint();        });
        btnruedas.setFont(new Font("Arial", Font.BOLD, 18));
        btnruedas.setBackground(Color.lightGray);
        panelMenu.add(btnruedas);

        JButton btntablas = new JButton("tablas");
        btntablas.addActionListener(e -> {
            tablasrefactorizado.menuTablas("tabla","tablas");
            panelMenu.setVisible(false);
            tablasrefactorizado.frameSubMenu.revalidate();
            tablasrefactorizado.frameSubMenu.repaint();
        });
        btntablas.setFont(new Font("Arial", Font.BOLD, 18));
        btntablas.setBackground(Color.lightGray);
        panelMenu.add(btntablas);

        JButton btnsalir = new JButton("salir");
        btnsalir.addActionListener(e -> {
            tablasrefactorizado.frameSubMenu.dispose();

        });
        btnsalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnsalir.setBackground(Color.lightGray);
        panelMenu.add(btnsalir);


        tablasrefactorizado.frameSubMenu.setLayout(new BorderLayout());

        tablasrefactorizado.frameSubMenu.add(panelMenu, BorderLayout.NORTH);
        tablasrefactorizado.frameSubMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        tablasrefactorizado.frameSubMenu.setVisible(true);



    }
}

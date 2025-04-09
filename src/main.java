import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class main {
    public static JPanel panelMenu = new JPanel();

    public static void main(String[] args) {
        tablasrefactorizado.BDS="DaviTeca";
        tablasrefactorizado.frameSubMenu.setTitle("Menu");
        tablasrefactorizado.frameSubMenu.setSize(800, 500);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        tablasrefactorizado.frameSubMenu.setLocation(dimension.width/4, dimension.height/4);



        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        ArrayList<String> nombres = new ArrayList<>();
        panelMenu.setLayout(new GridLayout(1, 1));

        try {
            conn = tablasrefactorizado.connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
            while (rs.next()) {
                String tableName = rs.getString("name");
                if (!Objects.equals(tableName, "sqlite_sequence")){
                    //System.out.println(tableName);
                    //nombres.add(tableName);
                    JButton btnruedas = new JButton(tableName);
                    btnruedas.addActionListener(e -> {
                        tablasrefactorizado.menuTablas(tableName,tableName);
                        panelMenu.setVisible(false);
                        tablasrefactorizado.frameSubMenu.revalidate();
                        tablasrefactorizado.frameSubMenu.repaint();        });
                    btnruedas.setFont(new Font("Arial", Font.BOLD, 18));
                    btnruedas.setBackground(Color.lightGray);
                    panelMenu.add(btnruedas);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) tablasrefactorizado.disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }






//        JButton btnskate = new JButton("skateboards");
//        btnskate.addActionListener(e -> {
//            tablasrefactorizado.menuTablas("skateboard","skateboards");
//            panelMenu.setVisible(false);
//            tablasrefactorizado.frameSubMenu.revalidate();
//            tablasrefactorizado.frameSubMenu.repaint();        });
//        btnskate.setFont(new Font("Arial", Font.BOLD, 18));
//        btnskate.setBackground(Color.lightGray);
//        panelMenu.add(btnskate);



//        JButton btntablas = new JButton("tablas");
//        btntablas.addActionListener(e -> {
//            tablasrefactorizado.menuTablas("tabla","tablas");
//            panelMenu.setVisible(false);
//            tablasrefactorizado.frameSubMenu.revalidate();
//            tablasrefactorizado.frameSubMenu.repaint();
//        });
//        btntablas.setFont(new Font("Arial", Font.BOLD, 18));
//        btntablas.setBackground(Color.lightGray);
//        panelMenu.add(btntablas);
//
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

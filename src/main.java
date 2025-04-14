import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class main {
    public static JPanel panelMenu = new JPanel();
    public static JPanel panelBox = new JPanel();


    public static void main(String[] args) {
        tablasrefactorizado.frameSubMenu.setTitle("Menu");
        tablasrefactorizado.frameSubMenu.setSize(800, 500);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        tablasrefactorizado.frameSubMenu.setLocation(dimension.width/4, dimension.height/4);

        tablasrefactorizado.BDS="DaviTeca";

        panelMenu=new JPanel();
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
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
                        panelBox.setVisible(false);
                        tablasrefactorizado.frameSubMenu.revalidate();
                        tablasrefactorizado.frameSubMenu.repaint();
                    });
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

        JButton btnsalir = new JButton("salir");
        btnsalir.addActionListener(e -> {
            tablasrefactorizado.frameSubMenu.dispose();

        });
        btnsalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnsalir.setBackground(Color.lightGray);
        panelMenu.add(btnsalir);


        JLabel textBox= new JLabel();
        textBox.setText("Selecciona que base de datos quieres administrar");
        String[] opciones = {"DaviTeca", "gestion", "materiales_swing", "mimahair", "ordenadores", "skateshop","AgenciaViajesBaseDatos"};
        JComboBox<String> cmbTipo = new JComboBox<>(opciones);
        cmbTipo.addActionListener(e -> {
            tablasrefactorizado.BDS= (String) cmbTipo.getSelectedItem();
            generarCabecera();
            tablasrefactorizado.frameSubMenu.revalidate();
            tablasrefactorizado.frameSubMenu.repaint();
        });


        cmbTipo.setMaximumSize(new Dimension(300,30));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        panelBox.add(textBox);

        centerPanel.add(cmbTipo);
        centerPanel.setSize(new Dimension(40,40));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        panelBox.add(centerPanel, BorderLayout.CENTER);

        tablasrefactorizado.frameSubMenu.setLayout(new BorderLayout());
        tablasrefactorizado.frameSubMenu.add(panelMenu, BorderLayout.NORTH);
        tablasrefactorizado.frameSubMenu.add(panelBox, BorderLayout.CENTER);

        tablasrefactorizado.frameSubMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tablasrefactorizado.frameSubMenu.setResizable(false);
        tablasrefactorizado.frameSubMenu.setVisible(true);



    }

    private static void generarCabecera() {
        tablasrefactorizado.frameSubMenu.remove(panelMenu);
        panelMenu=new JPanel();
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
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
                        panelBox.setVisible(false);
                        tablasrefactorizado.frameSubMenu.revalidate();
                        tablasrefactorizado.frameSubMenu.repaint();
                    });
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

        JButton btnsalir = new JButton("salir");
        btnsalir.addActionListener(e -> {
            tablasrefactorizado.frameSubMenu.dispose();

        });
        btnsalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnsalir.setBackground(Color.lightGray);
        panelMenu.add(btnsalir);
        tablasrefactorizado.frameSubMenu.add(panelMenu, BorderLayout.NORTH);
        tablasrefactorizado.frameSubMenu.revalidate();
        tablasrefactorizado.frameSubMenu.repaint();

    }
}

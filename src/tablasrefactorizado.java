import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class tablasrefactorizado {
    public static String entityName;
    public static String tableName;
    public static String[] headers;
    public static int[] columnTypes;
    private static JFrame frameSubMenu = null;
    private static JFrame frameConsulta = null;
    private static JFrame frameInsertar = null;
    private static JFrame frameEliminar = null;
    private static JFrame frameActualizar = null;
    public static Object[] cambiante;
    private static final String URL = "jdbc:sqlite:skateshop.db";


    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
        return conn;
    }

    public static void disconnect(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static void menuTablas(String entity, String table) {
        entityName = entity;
        tableName = table;
        try {
            headers = getHeaders();
            columnTypes = getColumnTypes();

            frameSubMenu = new JFrame("Gestion de "+tableName);
            frameSubMenu.setSize(600, 100);
            Toolkit mipantalla= Toolkit.getDefaultToolkit();
            Dimension dimension = mipantalla.getScreenSize();
            frameSubMenu.setLocation(dimension.width/4, dimension.height/4);
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 1));
            JButton btnconsultar = new JButton("consultar");
            btnconsultar.addActionListener(e -> {
                queryData(0,true);
            });
            btnconsultar.setFont(new Font("Arial", Font.BOLD, 18));
            btnconsultar.setBackground(Color.lightGray);
            panel.add(btnconsultar);

            JButton btninsertar = new JButton("insertar");
            btninsertar.addActionListener(e -> {
                insertData();
                frameSubMenu.setVisible(false);
            });
            btninsertar.setFont(new Font("Arial", Font.BOLD, 18));
            btninsertar.setBackground(Color.lightGray);
            panel.add(btninsertar);

            JButton btneliminar = new JButton("eliminar");
            btneliminar.addActionListener(e -> {
                deleteData();
                frameSubMenu.setVisible(false);
            });
            btneliminar.setFont(new Font("Arial", Font.BOLD, 18));
            btneliminar.setBackground(Color.lightGray);
            panel.add(btneliminar);

            JButton btnactualizar = new JButton("actualizar");
            btnactualizar.addActionListener(e -> {
                updateData();
                frameSubMenu.setVisible(false);
            });
            btnactualizar.setFont(new Font("Arial", Font.BOLD, 18));
            btnactualizar.setBackground(Color.lightGray);
            panel.add(btnactualizar);

            JButton btnsalir = new JButton("salir");
            btnsalir.addActionListener(e -> {
                if (frameConsulta!=null){
                    frameConsulta.dispose();
                }
                cambiante=null;
                frameSubMenu.dispose();
                main.frameMenu.setVisible(true);
            });
            btnsalir.setFont(new Font("Arial", Font.BOLD, 18));
            btnsalir.setBackground(Color.lightGray);
            panel.add(btnsalir);


            frameSubMenu.add(panel);
            frameSubMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameSubMenu.setVisible(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    static final boolean[] click = {true};

    public static void queryData(int x,boolean bool) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName +" order by "+headers[x]+(bool?" asc":" desc"));
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            if (frameConsulta == null) {
                frameConsulta = new JFrame("Listado de " + tableName);
                frameConsulta.setSize(800, 400);
                JScrollPane scrollPane = new JScrollPane(table);
                frameConsulta.add(scrollPane);
                frameConsulta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                frameConsulta.getContentPane().removeAll();
                frameConsulta.setTitle("Listado de " + tableName);
                JScrollPane scrollPane = new JScrollPane(table);
                frameConsulta.add(scrollPane);
                frameConsulta.revalidate();
                frameConsulta.repaint();
            }
            Toolkit mipantalla= Toolkit.getDefaultToolkit();
            Dimension dimension = mipantalla.getScreenSize();
            frameConsulta.setLocation(dimension.width/4, dimension.height/2);
            frameConsulta.setVisible(true);


            JTableHeader tableHeader = table.getTableHeader();
            tableHeader.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    click[0] = !click[0];
                    queryData(tableHeader.columnAtPoint(e.getPoint()), click[0]);
                }
            });
            if (cambiante==null){
                cambiante=new Object[columnCount];
                for (int i=0; i<columnCount;i++){
                    cambiante[i]=table.getValueAt(0, i);
                }
            }
            table.getSelectionModel().addListSelectionListener(e ->{
                cambiante=new Object[columnCount];
                for (int i=0; i<columnCount;i++){
                    cambiante[i]=table.getValueAt(table.getSelectedRow(), i);
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void insertData() {
        Statement stmt = null;
        ResultSet rs = null;
        String[] columns = new String[0];
        int[] types = new int[0];
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];
            types = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
                types[i - 1] = metaData.getColumnType(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        Scanner scanner = new Scanner(System.in);
        String[] fieldValues = new String[columns.length];


        frameInsertar = new JFrame("Añadir "+tableName);
        frameInsertar.setSize(300, 600);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameInsertar.setLocation(dimension.width/20, dimension.height/3);
        JPanel panel = new JPanel();

        ArrayList<JTextField> textFields=new ArrayList<>();

        for (int i = 1; i < columns.length; i++) {
            JTextField textField = new JTextField(20);
            textFields.add(textField);
            JLabel label = new JLabel("Ingrese " + columns[i]);
            panel.add(label);
            panel.add(textField);
        }

        String[] finalColumns = columns;
        Connection finalConn = conn;
        int[] finalTypes = types;

        JButton btninsertar = new JButton("insertar");
        btninsertar.addActionListener(e -> {

            int i=1;
            for (JTextField text:textFields){
                fieldValues[i++] =text.getText();
            }

            if (mostrarDialogo(frameInsertar)){
                try{
                    insertar(finalColumns, finalConn, fieldValues, finalTypes);
                }catch (Exception es) {
                    System.out.println("Error al insertar " + entityName + ": " + es.getMessage());
                }
                frameInsertar.dispose();
                frameSubMenu.setVisible(true);
                queryData(0,true);
            }

        });
        panel.add(btninsertar);
        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.setVisible(true);
            frameInsertar.dispose();
        });
        panel.add(btncancelar);

        frameInsertar.add(panel);
        frameInsertar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameInsertar.setVisible(true);


    }

    private static void insertar(String[] columns, Connection conn, String[] fieldValues, int[] types) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(" VALUES (");

        for (int i = 1; i < columns.length; i++) {
            sql.append(columns[i]);
            values.append("?");
            if (i < columns.length - 1) {
                sql.append(", ");
                values.append(", ");
            }
        }
        sql.append(") ");
        values.append(") ");
        sql.append(values);

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql.toString());

            //System.out.println(Arrays.toString(types));
            for (int i = 1; i < fieldValues.length; i++) {
                if (types[i] == 12) {
                    pstmt.setString(i, fieldValues[i]);
                }
                if (types[i] == 7) {
                    pstmt.setFloat(i, Float.parseFloat(fieldValues[i]));
                }
                if (types[i] == 4) {
                    pstmt.setInt(i, Integer.parseInt(fieldValues[i]));
                }
                if (types[i] == 2) {
                    pstmt.setDouble(i, Double.parseDouble(fieldValues[i]));
                }
                if (types[i] == 91) {
                    pstmt.setDate(i, Date.valueOf(fieldValues[i]));
                }
            }

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(entityName + " insertado exitosamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al insertar " + entityName + ": " + e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void deleteData() {

        frameEliminar = new JFrame("Ingrese el ID de " + entityName + " que desea eliminar");
        frameEliminar.setSize(400, 100);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameEliminar.setLocation(dimension.width/4, dimension.height/3);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JTextField textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(400,30));// Campo de texto
        if (cambiante!=null){
            textField.setText(String.valueOf(cambiante[0]));

        }
        Timer timer = new Timer(500, new ActionListener() {
            private Object ultimoValor = cambiante;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cambiante!=null){
                    if (cambiante != ultimoValor) { // Solo actualiza si cambió
                        textField.setText(String.valueOf(cambiante[0]));
                        ultimoValor = cambiante;
                    }
                }

            }
        });
        timer.start();

        JButton button = new JButton("eliminar id");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mostrarDialogo(frameEliminar)){
                    try{
                        eliminar(Integer.parseInt(textField.getText()));
                    }catch (Exception es) {
                        System.out.println("Error al eliminar el " + entityName + ": " + es.getMessage());
                    }
                    frameEliminar.dispose();
                    frameSubMenu.setVisible(true);
                    queryData(0,true);
                }
            }
        });
        panel.add(textField);
        panel.add(button);

        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.setVisible(true);
            frameEliminar.dispose();
        });
        panel.add(btncancelar);

        frameEliminar.add(panel);
        frameEliminar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameEliminar.setVisible(true);


    }

    private static void eliminar(int id) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = connect();
            String sql = "DELETE FROM " + tableName + " WHERE " + headers[0] + " = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(entityName + " eliminado exitosamente.");
            } else {
                System.out.println("No se encontró una " + entityName + " con el ID proporcionado.");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el " + entityName + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void updateData() {
        //dejar solo id y generar dinamicamente botones por nombre campo a actualizar que contengan la llamada en si a insertar donde su text field sea local y id global
        frameActualizar = new JFrame("Actualizar "+tableName);
        frameActualizar.setSize(300, 500);
        Toolkit mipantalla= Toolkit.getDefaultToolkit();
        Dimension dimension = mipantalla.getScreenSize();
        frameActualizar.setLocation(dimension.width/20, dimension.height/3);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField textFieldid = new JTextField(20);
        textFieldid.setMaximumSize(new Dimension(400,20));
        ArrayList<JTextField> textFields=new ArrayList<>();
        JLabel labelid = new JLabel("Ingrese el ID del " + entityName);
        panel.add(labelid);
        panel.add(textFieldid);
        for (int i = 1; i < headers.length; i++) {
            JTextField textField = new JTextField(20);
            textField.setMaximumSize(new Dimension(400,20));
            textFields.add(textField);
            JLabel label = new JLabel("Ingrese " + headers[i]);
            panel.add(label);
            panel.add(textField);
        }

        if (cambiante!=null){
            textFieldid.setText(String.valueOf(cambiante[0]));
            for (int i = 0; i < textFields.size(); i++) {
                textFields.get(i).setText(String.valueOf(cambiante[i+1]));
            }
        }
        Timer timer = new Timer(500, new ActionListener() {
            private Object ultimoValor = cambiante;
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (cambiante != ultimoValor) {
                        textFieldid.setText(String.valueOf(cambiante[0]));
                        for (int i = 0; i < textFields.size(); i++) {
                            textFields.get(i).setText(String.valueOf(cambiante[i+1]));
                        }
                        ultimoValor = cambiante;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        timer.start();


        panel.add(Box.createVerticalStrut(10)); // Espacio entre componentes
        JButton btnactualizar = new JButton("actualizar");
        btnactualizar.addActionListener(e -> {
            if (mostrarDialogo(frameActualizar)){
                try{
                    int i=1;
                    for (JTextField text:textFields){
                        if (!Objects.equals(text.getText(), "")){
                            //System.out.println(text.getText());
                            actualizar(i, text.getText(), Integer.parseInt(textFieldid.getText()));
                        }
                        i++;
                    }
                    timer.stop();
                }catch (Exception es) {
                    System.out.println("Error al actualizar el " + entityName + ": " + es.getMessage());
                }
                frameActualizar.dispose();
                frameSubMenu.setVisible(true);
                queryData(0,true);
            }


        });
        panel.add(btnactualizar);

        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.setVisible(true);
            frameActualizar.dispose();
            timer.stop();
        });
        panel.add(btncancelar);
        //panel.setLayout( new BoxLayout(panel,BoxLayout.Y_AXIS));
        frameActualizar.add(panel);
        JScrollPane scrollPane = new JScrollPane(panel);
        frameActualizar.add(scrollPane);
        frameActualizar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameActualizar.setVisible(true);
    }

    private static void actualizar(int selection, String newValue, int id) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = connect();
            String sql = "UPDATE " + tableName + " SET " + headers[selection] + " = ? WHERE " + headers[0] + " = ?";
            pstmt = conn.prepareStatement(sql);

            if (columnTypes[selection] == 12) {
                pstmt.setString(1, newValue);
            }
            if (columnTypes[selection] == 4) {
                pstmt.setInt(1, Integer.parseInt(newValue));
            }
            if (columnTypes[selection] == 2) {
                pstmt.setDouble(1, Double.parseDouble(newValue));
            }
            if (columnTypes[selection] == 91) {
                pstmt.setDate(1, Date.valueOf(newValue));
            }
            if (columnTypes[selection] == 7) {
                pstmt.setFloat(1, Float.parseFloat(newValue));
            }

            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(entityName + " actualizado exitosamente.");
            } else {
                System.out.println("No se encontró el " + entityName + " con el ID proporcionado.");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el " + entityName + ": " + e.getMessage());
        } finally {
            disconnect(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static boolean mostrarDialogo(JFrame framselect) {
        final boolean[] select = {false};
        JDialog dialogo = new JDialog(framselect, "Warning", true);
        dialogo.setSize(180, 100); // Tamaño del diálogo.
        dialogo.setLayout(new FlowLayout());
        JLabel etiqueta = new JLabel("Esta usted seguro?");
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose(); // cierra el JDialog.

            }
        });
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select[0] =true;
                dialogo.dispose(); // cierra el JDialog.

            }
        });
        dialogo.add(etiqueta);
        dialogo.add(btnAceptar);
        dialogo.add(btnCerrar);
        dialogo.setLocationRelativeTo(framselect);
        dialogo.setVisible(true);
        return select[0];
    }

    public static String[] getHeaders() {
        Statement stmt = null;
        ResultSet rs = null;
        String[] columns = new String[0];
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return columns;
    }

    public static int[] getColumnTypes() {
        Statement stmt = null;
        ResultSet rs = null;
        int[] types = new int[0];
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            types = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                types[i - 1] = metaData.getColumnType(i);
                //System.out.print(metaData.getColumnTypeName(i)+" ");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return types;
    }
}
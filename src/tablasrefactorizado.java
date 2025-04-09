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

public class tablasrefactorizado {
    public static String entityName;
    public static String tableName;
    public static String[] headers;
    public static int[] columnTypes;
    public static JFrame frameSubMenu = new JFrame();
    public static Object[] cambiante;
    public static String BDS;
    private static JPanel panelconsulta;
    public static JPanel panelaDerecho;
    public static JPanel panelSubmenu;//new BorderLayout()

    public static Connection connect() {
        String URL = "jdbc:sqlite:"+BDS+".db";
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

            frameSubMenu.setTitle("Gestion de "+tableName);
            frameSubMenu.setSize(800, 500);
            Toolkit mipantalla= Toolkit.getDefaultToolkit();
            Dimension dimension = mipantalla.getScreenSize();
            frameSubMenu.setLocation(dimension.width/4, dimension.height/4);
            panelSubmenu = new JPanel();
            panelSubmenu.setLayout(new GridLayout(1, 1));
            JButton btninsertar = new JButton("insertar");
            btninsertar.addActionListener(e -> {
                insertData();
                panelSubmenu.setVisible(false);
                frameSubMenu.revalidate();
                frameSubMenu.repaint();
            });
            btninsertar.setFont(new Font("Arial", Font.BOLD, 18));
            btninsertar.setBackground(Color.lightGray);
            panelSubmenu.add(btninsertar);

            JButton btneliminar = new JButton("eliminar");
            btneliminar.addActionListener(e -> {
                deleteData();
                panelSubmenu.setVisible(false);
                frameSubMenu.revalidate();
                frameSubMenu.repaint();
            });
            btneliminar.setFont(new Font("Arial", Font.BOLD, 18));
            btneliminar.setBackground(Color.lightGray);
            panelSubmenu.add(btneliminar);

            JButton btnactualizar = new JButton("actualizar");
            btnactualizar.addActionListener(e -> {
                updateData();
                panelSubmenu.setVisible(false);
                frameSubMenu.revalidate();
                frameSubMenu.repaint();
            });
            btnactualizar.setFont(new Font("Arial", Font.BOLD, 18));
            btnactualizar.setBackground(Color.lightGray);
            panelSubmenu.add(btnactualizar);

            JButton btnsalir = new JButton("salir");
            btnsalir.addActionListener(e -> {

                cambiante=null;
                //frameSubMenu.dispose();
                if (panelaDerecho!=null){
                    frameSubMenu.remove(panelaDerecho);
                    frameSubMenu.revalidate();
                    frameSubMenu.repaint();
                }
                frameSubMenu.remove(panelconsulta);
                frameSubMenu.remove(panelSubmenu);
                frameSubMenu.setTitle("Menu");
                main.panelMenu.setVisible(true);
                frameSubMenu.revalidate();
                frameSubMenu.repaint();
            });
            btnsalir.setFont(new Font("Arial", Font.BOLD, 18));
            btnsalir.setBackground(Color.lightGray);
            panelSubmenu.add(btnsalir);

            frameSubMenu.add(panelSubmenu, BorderLayout.NORTH);
            frameSubMenu.setVisible(true);
            queryData(0,true);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    static final boolean[] click = {true};

    public static void queryData(int x,boolean bool) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        if (panelconsulta != null) {
            frameSubMenu.remove(panelconsulta);
        }
        panelconsulta = new JPanel(new BorderLayout());

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName +" order by "+headers[x]+(bool?" asc":" desc"));
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String recaulcula=metaData.getColumnName(i);
                if (i-1==x){
                    if (bool){
                        recaulcula=recaulcula+" ↓";
                    }else{
                        recaulcula=recaulcula+" ↑";
                    }
                }
                columns[i - 1] = recaulcula;
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
            table.getTableHeader().setReorderingAllowed(false);

            JScrollPane scrollPane = new JScrollPane(table);
            panelconsulta.add(scrollPane, BorderLayout.CENTER);
            frameSubMenu.add(panelconsulta, BorderLayout.CENTER);
            frameSubMenu.revalidate();
            frameSubMenu.repaint();

            JTableHeader tableHeader = table.getTableHeader();
            tableHeader.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    click[0] = !click[0];
                    //System.out.println(tableHeader.columnAtPoint(e.getPoint()) +" " +click[0]);
                    queryData(tableHeader.columnAtPoint(e.getPoint()), click[0]);
                }
            });
//            if (cambiante==null){
//                cambiante=new Object[columnCount];
//                for (int i=0; i<columnCount;i++){
//                    cambiante[i]=table.getValueAt(0, i);
//                }
//            }
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
        String[] fieldValues = new String[columns.length];
        panelaDerecho =new JPanel();
        panelaDerecho.setLayout(new BoxLayout(panelaDerecho, BoxLayout.Y_AXIS));
        ArrayList<JTextField> textFields=new ArrayList<>();
        JTextField textFielid = new JTextField(20);
        if (notAutiIncrement()){
            textFielid.setMaximumSize(new Dimension(300,20));
            JLabel label = new JLabel("Ingrese " + columns[0]);
            panelaDerecho.add(label);
            panelaDerecho.add(textFielid);
        }

        for (int i = 1; i < columns.length; i++) {
            JTextField textField = new JTextField(20);
            textField.setMaximumSize(new Dimension(300,20));
            textFields.add(textField);
            JLabel label = new JLabel("Ingrese " + columns[i]);
            panelaDerecho.add(label);
            panelaDerecho.add(textField);
        }

        String[] finalColumns = columns;
        Connection finalConn = conn;
        int[] finalTypes = types;

        JButton btninsertar = new JButton("insertar");
        btninsertar.addActionListener(e -> {
            if(notAutiIncrement()){
                fieldValues[0]=textFielid.getText();
            }

            int i=1;
            for (JTextField text:textFields){
                fieldValues[i++] =text.getText();
            }

            if (mostrarDialogo()){
                try{
                    insertar(finalColumns, finalConn, fieldValues, finalTypes);
                }catch (Exception es) {
                    System.out.println("Error al insertar " + entityName + ": " + es.getMessage());
                }
                frameSubMenu.remove(panelaDerecho);
                panelSubmenu.setVisible(true);
                frameSubMenu.revalidate();
                frameSubMenu.repaint();
                queryData(0,true);
            }

        });
        panelaDerecho.add(Box.createVerticalStrut(10));

        panelaDerecho.add(btninsertar);
        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.remove(panelaDerecho);
            panelSubmenu.setVisible(true);
            frameSubMenu.revalidate();
            frameSubMenu.repaint();
        });
        panelaDerecho.add(btncancelar);
        panelaDerecho.setPreferredSize(new Dimension(180, frameSubMenu.getHeight()));
        frameSubMenu.add(panelaDerecho, BorderLayout.EAST);
        frameSubMenu.revalidate();
        frameSubMenu.repaint();

    }

    private static void insertar(String[] columns, Connection conn, String[] fieldValues, int[] types) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(" VALUES (");
        if (notAutiIncrement()){
            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i]);
                values.append("?");
                if (i < columns.length - 1) {
                    sql.append(", ");
                    values.append(", ");
                }
            }
        }else {
            for (int i = 1; i < columns.length; i++) {
                sql.append(columns[i]);
                values.append("?");
                if (i < columns.length - 1) {
                    sql.append(", ");
                    values.append(", ");
                }
            }
        }
        sql.append(") ");
        values.append(") ");
        sql.append(values);

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql.toString());

            //System.out.println(Arrays.toString(types));
            int paramIndex = 1;
            if (notAutiIncrement()) {
                pstmt.setObject(paramIndex++, fieldValues[0]);
            }
            for (int i = 1; i < fieldValues.length; i++) {
                pstmt.setObject(paramIndex++, fieldValues[i]);
//                if (types[i] == 12) {
//                    pstmt.setString(i, fieldValues[i]);
//                }
//                if (types[i] == 7) {
//                    pstmt.setFloat(i, Float.parseFloat(fieldValues[i]));
//                }
//                if (types[i] == 4) {
//                    pstmt.setInt(i, Integer.parseInt(fieldValues[i]));
//                }
//                if (types[i] == 2) {
//                    pstmt.setDouble(i, Double.parseDouble(fieldValues[i]));
//                }
//                if (types[i] == 91) {
//                    pstmt.setDate(i, Date.valueOf(fieldValues[i]));
//                }
            }

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(entityName + " insertado exitosamente.");
            }
        } catch (Exception e) {
            errorMessage("Error al insertar");
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


        panelaDerecho =new JPanel();
        panelaDerecho.setLayout(new BoxLayout(panelaDerecho, BoxLayout.Y_AXIS));
        JLabel labelid = new JLabel("Ingrese ID a eliminar ");
        panelaDerecho.add(labelid);
        JTextField textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(400,20));
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
                if (mostrarDialogo()){
                    try{
                        eliminar(textField.getText());
                    }catch (Exception es) {
                        System.out.println("Error al eliminar el " + entityName + ": " + es.getMessage());
                    }
                    frameSubMenu.remove(panelaDerecho);
                    panelSubmenu.setVisible(true);
                    frameSubMenu.revalidate();
                    frameSubMenu.repaint();
                    queryData(0,true);
                }
            }
        });
        panelaDerecho.add(textField);
        panelaDerecho.add(Box.createVerticalStrut(10));

        panelaDerecho.add(button);

        JButton btncancelar = new JButton("cancelar");
        btncancelar.addActionListener(e -> {
            frameSubMenu.remove(panelaDerecho);
            panelSubmenu.setVisible(true);
            frameSubMenu.revalidate();
            frameSubMenu.repaint();
        });
        panelaDerecho.add(btncancelar);
        panelaDerecho.setPreferredSize(new Dimension(180, frameSubMenu.getHeight()));
        frameSubMenu.add(panelaDerecho, BorderLayout.EAST);
        frameSubMenu.revalidate();
        frameSubMenu.repaint();


    }

    private static void eliminar(Object id) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = connect();
            String sql = "DELETE FROM " + tableName + " WHERE " + headers[0] + " = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(entityName + " eliminado exitosamente.");
            } else {
                errorMessage("No se encontró el ID proporcionado.");
                System.out.println("No se encontró una " + entityName + " con el ID proporcionado.");
            }
        } catch (Exception e) {
            errorMessage("Error al eliminar el id");
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
        panelaDerecho =new JPanel();
        panelaDerecho.setLayout(new BoxLayout(panelaDerecho, BoxLayout.Y_AXIS));
        JTextField textFieldid = new JTextField(20);
        textFieldid.setMaximumSize(new Dimension(300,20));
        ArrayList<JTextField> textFields=new ArrayList<>();
        JLabel labelid = new JLabel("Ingrese ID");
        panelaDerecho.add(labelid);
        panelaDerecho.add(textFieldid);
        for (int i = 1; i < headers.length; i++) {
            JTextField textField = new JTextField(20);
            textField.setMaximumSize(new Dimension(300,20));
            textFields.add(textField);
            JLabel label = new JLabel("Ingrese " + headers[i]);
            panelaDerecho.add(label);
            panelaDerecho.add(textField);
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


        panelaDerecho.add(Box.createVerticalStrut(10)); // Espacio entre componentes
        JButton btnactualizar = new JButton("actualizar");
        panelaDerecho.add(btnactualizar);

        JButton btncancelar = new JButton("cancelar");

        panelaDerecho.add(btncancelar);
        JScrollPane scrollPane = new JScrollPane(panelaDerecho);
        scrollPane.setPreferredSize(new Dimension(180, frameSubMenu.getHeight()));
        frameSubMenu.add(scrollPane, BorderLayout.EAST);
        frameSubMenu.revalidate();
        frameSubMenu.repaint();
        btncancelar.addActionListener(e -> {
            frameSubMenu.remove(scrollPane);
            panelSubmenu.setVisible(true);
            frameSubMenu.revalidate();
            frameSubMenu.repaint();
            timer.stop();
        });
        btnactualizar.addActionListener(e -> {
            if (mostrarDialogo()){
                try{
                    int i=1;
                    for (JTextField text:textFields){
                        if (!Objects.equals(text.getText(), "")){
                            //System.out.println(text.getText());
                            //actualizar(i, text.getText(), Integer.parseInt(textFieldid.getText()));
                            actualizar(i, text.getText(), textFieldid.getText());

                        }
                        i++;
                    }
                    timer.stop();
                }catch (Exception es) {
                    System.out.println("Error al actualizar el " + entityName + ": " + es.getMessage());
                }
                frameSubMenu.remove(scrollPane);
                panelSubmenu.setVisible(true);
                frameSubMenu.revalidate();
                frameSubMenu.repaint();
                queryData(0,true);
            }
        });


    }

    private static void actualizar(int selection, String newValue, Object id) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = connect();
            String sql = "UPDATE " + tableName + " SET " + headers[selection] + " = ? WHERE " + headers[0] + " = ?";
            pstmt = conn.prepareStatement(sql);
//            System.out.println(Arrays.toString(columnTypes));
//            System.out.println(Arrays.toString(headers));
            pstmt.setObject(1, newValue);

//            if (columnTypes[selection] == 12) {
//                pstmt.setString(1, newValue);
//            }
//            if (columnTypes[selection] == 4) {
//                pstmt.setInt(1, Integer.parseInt(newValue));
//            }
//            if (columnTypes[selection] == 2) {
//                pstmt.setDouble(1, Double.parseDouble(newValue));
//            }
//            if (columnTypes[selection] == 91) {
//                pstmt.setDate(1, Date.valueOf(newValue));
//            }
//            if (columnTypes[selection] == 7) {
//                pstmt.setFloat(1, Float.parseFloat(newValue));
//            }

            pstmt.setObject(2, id);
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

    private static boolean mostrarDialogo() {
        final boolean[] select = {false};
        JDialog dialogo = new JDialog(frameSubMenu, "Warning", true);
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
        dialogo.setLocationRelativeTo(frameSubMenu);
        dialogo.setVisible(true);
        return select[0];
    }
    private static void errorMessage(String message) {
        JDialog dialogo = new JDialog(frameSubMenu, "Error", true);
        dialogo.setSize(180, 100); // Tamaño del diálogo.
        dialogo.setLayout(new FlowLayout());
        JLabel etiqueta = new JLabel(message);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose(); // cierra el JDialog.
            }
        });
        dialogo.add(etiqueta);
        dialogo.add(btnAceptar);

        dialogo.setLocationRelativeTo(frameSubMenu);
        dialogo.setVisible(true);
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
    public static boolean notAutiIncrement(){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = tablasrefactorizado.connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM sqlite_sequence");
            while (rs.next()) {
                String Name = rs.getString("name");
                if(Objects.equals(tableName, Name)){
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        } finally {
            try {
                if (conn != null) tablasrefactorizado.disconnect(conn);
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return true;
    }
}
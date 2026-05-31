package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import model.Predio;

/**
 * Vista del sistema catastral.
 * Interfaz grafica construida con Java Swing (JFrame, JTable, JScrollPane,
 * JTextField, JComboBox, JButton, JLabel, JPanel, JOptionPane, JFileChooser).
 */
public class PredioView extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtBuscar;
    private JComboBox<String> cbColumna;
    private JComboBox<String> cbAlgoritmo;
    private JLabel lblTiempo;
    private JLabel lblCantidad;
    private JLabel lblEstado;
    private JButton btnBuscar;
    private JButton btnOrdenar;
    private JButton btnLimpiar;
    private JButton btnCargar;

    private final String[] columnas = {"NPN", "Municipio", "Direccion", "NumeroFicha"};

    /**
     * Constructor principal. Configura ventana y construye componentes.
     */
    public PredioView() {
        setTitle("Sistema Catastral - Antioquia");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    /**
     * Inicializa paneles, tabla y controles.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel superior con controles
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(lblBuscar);
        txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(300, 40));
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(txtBuscar);

        panelSuperior.add(new JLabel("Columna:"));
        JLabel lblColumna = new JLabel("Columna:");
        lblColumna.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(lblColumna);
        cbColumna = new JComboBox<>(columnas);
        cbColumna.setPreferredSize(new Dimension(200, 40));
        cbColumna.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(cbColumna);

        panelSuperior.add(new JLabel("Algoritmo:"));
        JLabel lblAlgoritmo = new JLabel("Algoritmo:");
        lblAlgoritmo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(lblAlgoritmo);
        cbAlgoritmo = new JComboBox<>(new String[]{"QuickSort", "MergeSort"});
        cbAlgoritmo.setPreferredSize(new Dimension(200, 40));
        cbAlgoritmo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(cbAlgoritmo);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(140, 45));
        btnBuscar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(btnBuscar);

        btnOrdenar = new JButton("Ordenar");
        btnOrdenar.setPreferredSize(new Dimension(140, 45));
        btnOrdenar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(btnOrdenar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(140, 45));
        btnLimpiar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSuperior.add(btnLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla central con scroll
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // NPN
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Municipio
        table.getColumnModel().getColumn(2).setPreferredWidth(400); // Direccion
        table.getColumnModel().getColumn(3).setPreferredWidth(200); // NumeroFicha

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        lblTiempo = new JLabel("Tiempo: N/A");
        lblTiempo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblCantidad = new JLabel("Registros: 0");
        lblCantidad.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblEstado = new JLabel("Estado: Listo");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelInferior.add(lblTiempo);
        panelInferior.add(lblCantidad);
        panelInferior.add(lblEstado);

        btnCargar = new JButton("Cargar CSV");
        btnCargar.setPreferredSize(new Dimension(140, 45));
        btnCargar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelInferior.add(btnCargar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Getters para el controlador
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JComboBox<String> getCbColumna() { return cbColumna; }
    public JComboBox<String> getCbAlgoritmo() { return cbAlgoritmo; }
    public JLabel getLblTiempo() { return lblTiempo; }
    public JLabel getLblCantidad() { return lblCantidad; }
    public JLabel getLblEstado() { return lblEstado; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnOrdenar() { return btnOrdenar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnCargar() { return btnCargar; }
    public DefaultTableModel getTableModel() { return tableModel; }

    /**
     * Actualiza la tabla con la lista de predios.
     *
     * @param predios lista a mostrar.
     */
    public void actualizarTabla(java.util.List<Predio> predios) {
        tableModel.setRowCount(0);
        if (predios != null) {
            for (Predio p : predios) {
                tableModel.addRow(new Object[]{
                        p.getNpn(),
                        p.getMunicipio(),
                        p.getDireccion(),
                        p.getNumeroFicha()
                });
            }
        }
    }

    /**
     * Limpia la tabla.
     */
    public void limpiarTabla() {
        tableModel.setRowCount(0);
    }

    /**
     * Muestra mensaje al usuario.
     *
     * @param mensaje texto del mensaje.
     * @param esError true para mostrar como error.
     */
    public void mostrarMensaje(String mensaje, boolean esError) {
        if (esError) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Abre JFileChooser para seleccionar un archivo CSV.
     *
     * @return archivo seleccionado o null si se cancela.
     */
    public File seleccionarArchivoCsv() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de predios");
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Selecciona una fila y se asegura de que sea visible.
     * También destaca la columna especificada.
     *
     * @param rowIndex índice de fila a seleccionar
     * @param colIndex índice de columna a destacar (puede ser -1 para no destacar columna)
     */
    public void seleccionarFilaConColumna(int rowIndex, int colIndex) {
        if (rowIndex < 0 || rowIndex >= table.getRowCount()) {
            return;
        }
        table.setRowSelectionInterval(rowIndex, rowIndex);
        if (colIndex >= 0 && colIndex < table.getColumnCount()) {
            table.setColumnSelectionInterval(colIndex, colIndex);
        } else {
            // solo selección de fila
            table.setColumnSelectionAllowed(false);
        }
        // Asegurar que la celda sea visible
        Rectangle rect = table.getCellRect(rowIndex, colIndex >= 0 ? colIndex : 0, true);
        table.scrollRectToVisible(rect);
    }
}
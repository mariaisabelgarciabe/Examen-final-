package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import model.Predio;

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

    private final String[] columnas = {
            "NPN",
            "Municipio",
            "Dirección",
            "Número Ficha"
    };

    private boolean resaltarResultado = false;

    public PredioView() {

        setTitle("Sistema Catastral de Antioquia");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(252, 252, 252));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        panelSuperior.setBackground(new Color(245, 248, 255));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelSuperior.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(300, 40));
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 210, 210),
                        1
                )
        );

        panelSuperior.add(txtBuscar);

        JLabel lblColumna = new JLabel("Columna:");
        lblColumna.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelSuperior.add(lblColumna);

        cbColumna = new JComboBox<>(columnas);
        cbColumna.setPreferredSize(new Dimension(180, 40));
        cbColumna.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        panelSuperior.add(cbColumna);

        JLabel lblAlgoritmo = new JLabel("Algoritmo:");
        lblAlgoritmo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelSuperior.add(lblAlgoritmo);

        cbAlgoritmo = new JComboBox<>(
                new String[]{"QuickSort", "MergeSort"}
        );

        cbAlgoritmo.setPreferredSize(new Dimension(180, 40));

        cbAlgoritmo.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        panelSuperior.add(cbAlgoritmo);

        btnBuscar = new JButton("Buscar");

        btnBuscar.setPreferredSize(
                new Dimension(140, 45)
        );

        btnBuscar.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnBuscar.setBackground(
                new Color(173, 216, 230)
        );

        btnBuscar.setForeground(
                new Color(50, 50, 50)
        );

        btnBuscar.setFocusPainted(false);

        btnBuscar.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelSuperior.add(btnBuscar);

        btnOrdenar = new JButton("Ordenar");

        btnOrdenar.setPreferredSize(
                new Dimension(140, 45)
        );

        btnOrdenar.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnOrdenar.setBackground(
                new Color(174, 235, 200)
        );

        btnOrdenar.setForeground(
                new Color(50, 50, 50)
        );

        btnOrdenar.setFocusPainted(false);

        btnOrdenar.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelSuperior.add(btnOrdenar);

        btnLimpiar = new JButton("Limpiar");

        btnLimpiar.setPreferredSize(
                new Dimension(140, 45)
        );

        btnLimpiar.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnLimpiar.setBackground(
                new Color(245, 183, 177)
        );

        btnLimpiar.setForeground(
                new Color(50, 50, 50)
        );

        btnLimpiar.setFocusPainted(false);

        btnLimpiar.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelSuperior.add(btnLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        table.setFillsViewportHeight(true);

        table.getTableHeader().setReorderingAllowed(false);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setRowHeight(34);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        table.setShowGrid(true);

        table.setGridColor(
                new Color(230, 230, 230)
        );

        table.getTableHeader().setBackground(
                new Color(137, 207, 240)
        );

        table.getTableHeader().setForeground(
                new Color(50, 50, 50)
        );

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        table.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column) {

                        Component c =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        if (resaltarResultado) {

                            c.setBackground(
                                    new Color(220, 235, 250)
                            );

                        } else {

                            if (row % 2 == 0) {

                                c.setBackground(
                                        new Color(250, 250, 250)
                                );

                            } else {

                                c.setBackground(Color.WHITE);
                            }

                            if (isSelected) {

                                c.setBackground(
                                        new Color(220, 230, 240)
                                );
                            }
                        }

                        setBorder(
                                BorderFactory.createEmptyBorder(
                                        0,
                                        10,
                                        0,
                                        10
                                )
                        );

                        return c;
                    }
                }
        );

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 220, 220),
                        1
                )
        );

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 15, 10)
        );

        panelInferior.setBackground(
                new Color(245, 248, 255)
        );

        lblTiempo = new JLabel("Tiempo: N/A");

        lblTiempo.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        lblTiempo.setForeground(
                new Color(90, 90, 90)
        );

        lblCantidad = new JLabel("Registros: 0");

        lblCantidad.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        lblCantidad.setForeground(
                new Color(90, 90, 90)
        );

        lblEstado = new JLabel("Estado: Listo");

        lblEstado.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        lblEstado.setForeground(
                new Color(90, 90, 90)
        );

        panelInferior.add(lblTiempo);
        panelInferior.add(lblCantidad);
        panelInferior.add(lblEstado);

        btnCargar = new JButton("Cargar CSV");

        btnCargar.setPreferredSize(
                new Dimension(150, 45)
        );

        btnCargar.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnCargar.setBackground(
                new Color(215, 189, 226)
        );

        btnCargar.setForeground(
                new Color(50, 50, 50)
        );

        btnCargar.setFocusPainted(false);

        btnCargar.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelInferior.add(btnCargar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JComboBox<String> getCbColumna() {
        return cbColumna;
    }

    public JComboBox<String> getCbAlgoritmo() {
        return cbAlgoritmo;
    }

    public JLabel getLblTiempo() {
        return lblTiempo;
    }

    public JLabel getLblCantidad() {
        return lblCantidad;
    }

    public JLabel getLblEstado() {
        return lblEstado;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnOrdenar() {
        return btnOrdenar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnCargar() {
        return btnCargar;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void actualizarTabla(java.util.List<Predio> predios) {

        tableModel.setRowCount(0);

        if (predios != null) {

            for (Predio p : predios) {

                tableModel.addRow(
                        new Object[]{
                                p.getNpn(),
                                p.getMunicipio(),
                                p.getDireccion(),
                                p.getNumeroFicha()
                        }
                );
            }
        }
    }

    public void limpiarTabla() {
        tableModel.setRowCount(0);
    }

    public void mostrarMensaje(
            String mensaje,
            boolean esError
    ) {

        if (esError) {

            JOptionPane.showMessageDialog(
                    this,
                    mensaje,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    mensaje,
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public File seleccionarArchivoCsv() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle(
                "Seleccionar archivo CSV de predios"
        );

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            return fileChooser.getSelectedFile();
        }

        return null;
    }

    public void seleccionarFilaConColumna(
            int rowIndex,
            int colIndex
    ) {

        if (rowIndex < 0 ||
                rowIndex >= table.getRowCount()) {

            return;
        }

        table.setRowSelectionInterval(
                rowIndex,
                rowIndex
        );

        if (colIndex >= 0 &&
                colIndex < table.getColumnCount()) {

            table.setColumnSelectionInterval(
                    colIndex,
                    colIndex
            );

        } else {

            table.setColumnSelectionAllowed(false);
        }

        Rectangle rect = table.getCellRect(
                rowIndex,
                colIndex >= 0 ? colIndex : 0,
                true
        );

        table.scrollRectToVisible(rect);
    }

    public void setResaltarResultado(
            boolean activar
    ) {

        this.resaltarResultado = activar;

        if (table != null) {

            table.repaint();
        }
    }
}
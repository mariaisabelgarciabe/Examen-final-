package controller;

import model.*;
import view.PredioView;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PredioController {
    private final PredioView vista;
    private List<Predio> predios;
    private final List<Predio> prediosOriginales;
    private Predio ultimaBusqueda;

    private Timer tiempoTimer;
    private long tiempoStart;

    private static final String RUTA_AUTO_CARGA = "C:/Users/Isabel/Downloads/predios.csv";

    public PredioController(PredioView vista) {
        this.vista = vista;
        this.predios = new ArrayList<>();
        this.prediosOriginales = new ArrayList<>();
        this.ultimaBusqueda = null;

        asignarEventos();

        if (cargarCSV(RUTA_AUTO_CARGA)) {
            vista.getLblEstado().setText("Estado: Cargado desde predios.csv inicial");
            actualizarTabla(predios);
            vista.getLblCantidad().setText("Registros: " + predios.size());
            vista.setResaltarResultado(false);
        }
    }

    private void asignarEventos() {
        vista.getBtnCargar().addActionListener(e -> cargarDesdeDialogo());
        vista.getBtnOrdenar().addActionListener(e -> ordenar());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());

        vista.getTxtBuscar().addActionListener(e -> buscar());
    }

    private void cargarDesdeDialogo() {
        File archivo = vista.seleccionarArchivoCsv();
        if (archivo != null) {
            if (cargarCSV(archivo.getAbsolutePath())) {
                vista.mostrarMensaje("CSV cargado correctamente: " + predios.size() + " registros", false);
                vista.getLblEstado().setText("Estado: CSV cargado");
                actualizarTabla(predios);
                vista.getLblCantidad().setText("Registros: " + predios.size());
                vista.setResaltarResultado(false);
            }
        }
    }

    
    private void iniciarTemporizador() {
        tiempoStart = System.nanoTime();
        if (tiempoTimer != null && tiempoTimer.isRunning()) {
            tiempoTimer.stop();
        }
        tiempoTimer = new Timer(50, e -> {
            long ahora = System.nanoTime();
            double elapsedMs = (ahora - tiempoStart) / 1_000_000.0;
            vista.getLblTiempo().setText(String.format("Tiempo: %.3f ms", elapsedMs));
        });
        tiempoTimer.setRepeats(true);
        tiempoTimer.start();
    }

    
    private void detenerTemporizador(double tiempoFinalMs) {
        if (tiempoTimer != null) {
            tiempoTimer.stop();
        }
        vista.getLblTiempo().setText(String.format("Tiempo: %.3f ms", tiempoFinalMs));
    }

    private boolean cargarCSV(String ruta) {
        SwingWorker<List<Predio>, Void> worker = new SwingWorker<>() {
            private double tiempoMs;

            
            protected List<Predio> doInBackground() throws Exception {
                long startTime = System.nanoTime();
                List<Predio> result = CsvReader.leerCsv(ruta);
                long endTime = System.nanoTime();
                tiempoMs = (endTime - startTime) / 1_000_000.0;
                return result;
            }

            
            protected void done() {
                try {
                    predios = new ArrayList<>(get());
                    prediosOriginales.clear();
                    prediosOriginales.addAll(predios);
                    actualizarTabla(predios);
                    vista.getLblCantidad().setText("Registros: " + predios.size());
                    vista.getLblEstado().setText("Estado: CSV cargado");
                    detenerTemporizador(tiempoMs);
                    vista.setResaltarResultado(false);
                } catch (Exception ex) {
                    vista.mostrarMensaje("Error cargando CSV: " + ex.getMessage(), true);
                    detenerTemporizador(0);
                    vista.setResaltarResultado(false);
                }
            }
        };
        vista.getLblTiempo().setText("Tiempo: Calculando...");
        iniciarTemporizador();
        try {
            worker.execute();
            return true;
        } catch (Exception ex) {
            vista.mostrarMensaje("Error iniciando carga: " + ex.getMessage(), true);
            detenerTemporizador(0);
            vista.setResaltarResultado(false);
            return false;
        }
    }

    private void ordenar() {
        if (predios.isEmpty()) {
            vista.mostrarMensaje("No hay datos para ordenar. Cargue un CSV primero.", true);
            return;
        }

        try {
            predios.clear();
            predios.addAll(prediosOriginales);
        } catch (Exception ex) {
            vista.mostrarMensaje("No se pudieron restablecer datos originales. Recargue el CSV.", true);
            return;
        }

        String columna = (String) vista.getCbColumna().getSelectedItem();
        String algoritmo = (String) vista.getCbAlgoritmo().getSelectedItem();

        if (columna == null || algoritmo == null) {
            vista.mostrarMensaje("Seleccione columna y algoritmo.", true);
            return;
        }

        final String columnaFinal = columna;
        final String algoritmoFinal = algoritmo;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private double tiempoMs;

            
            protected Void doInBackground() throws Exception {
                long inicio = System.nanoTime();

                if ("QuickSort".equalsIgnoreCase(algoritmoFinal)) {
                    AlgoritmosOrdenamiento.quickSort((ArrayList<Predio>) predios, columnaFinal);
                } else {
                    AlgoritmosOrdenamiento.mergeSort((ArrayList<Predio>) predios, columnaFinal);
                }

                long fin = System.nanoTime();
                tiempoMs = (fin - inicio) / 1_000_000.0;

                return null;
            }

            
            protected void done() {
                actualizarTabla(predios);
                vista.getLblCantidad().setText("Registros: " + predios.size());
                vista.getLblEstado().setText("Estado: Ordenado por " + columnaFinal);
                detenerTemporizador(tiempoMs);
                vista.setResaltarResultado(false);
            }
        };
        vista.getLblTiempo().setText("Tiempo: Calculando...");
        iniciarTemporizador();
        worker.execute();
    }

    private void buscar() {
        if (predios.isEmpty()) {
            vista.mostrarMensaje("No hay datos. Cargue un CSV primero.", true);
            return;
        }

        String termino = vista.getTxtBuscar().getText();
        if (termino == null || termino.trim().isEmpty()) {
            vista.mostrarMensaje("Ingrese un termino de busqueda.", true);
            return;
        }

        String columna = (String) vista.getCbColumna().getSelectedItem();
        if (columna == null) {
            columna = "NPN";
        }

        final String terminoFinal = termino.trim();
        final String columnaFinal = columna;

        SwingWorker<List<Predio>, Void> worker = new SwingWorker<>() {
            private double tiempoSortMs;
            private double tiempoBusquedaMs;
            private double totalMs;

            protected List<Predio> doInBackground() throws Exception {
                List<Predio> datos = new ArrayList<>();
                datos.addAll(prediosOriginales);

                long inicioSort = System.nanoTime();

                String algoritmoFinal = (String) vista.getCbAlgoritmo().getSelectedItem();

                if ("QuickSort".equalsIgnoreCase(algoritmoFinal)) {
                    AlgoritmosOrdenamiento.quickSort((ArrayList<Predio>) datos, columnaFinal);
                } else {
                    AlgoritmosOrdenamiento.mergeSort((ArrayList<Predio>) datos, columnaFinal);
                }

                long finSort = System.nanoTime();

                List<Predio> resultados = BusquedaBinaria.buscarParcial(
                        (ArrayList<Predio>) datos, columnaFinal, terminoFinal);
                long finBusqueda = System.nanoTime();

                tiempoSortMs = (finSort - inicioSort) / 1_000_000.0;
                tiempoBusquedaMs = (finBusqueda - finSort) / 1_000_000.0;
                totalMs = tiempoSortMs + tiempoBusquedaMs;

                return resultados;
            }
            
            protected void done() {
                try {
                    List<Predio> resultados = get();
                    ultimaBusqueda = resultados.isEmpty() ? null : resultados.get(0);
                    if (resultados.isEmpty()) {
                        vista.mostrarMensaje("No se encontraron coincidencias para: " + terminoFinal, true);
                        vista.getLblEstado().setText("Estado: Sin resultados");
                        vista.setResaltarResultado(false);
                    } else {
                        actualizarTabla(resultados);
                        vista.getLblCantidad().setText("Registros encontrados: " + resultados.size());
                        vista.getLblEstado()
                                .setText("Estado: Busqueda completada - " + resultados.size() + " coincidencias");
                        int colIndex = vista.getCbColumna().getSelectedIndex();
                        if (colIndex >= 0) {
                            vista.seleccionarFilaConColumna(0, colIndex);
                        }
                        vista.setResaltarResultado(true);
                    }
                    detenerTemporizador(totalMs);
                } catch (Exception ex) {
                    vista.mostrarMensaje("Error en la busqueda: " + ex.getMessage(), true);
                    vista.getLblEstado().setText("Estado: Error");
                    detenerTemporizador(0);
                    vista.setResaltarResultado(false);
                }
            }
        };
        vista.getLblEstado().setText("Estado: Ordenando y buscando...");
        vista.getLblTiempo().setText("Tiempo: Calculando...");
        iniciarTemporizador();
        worker.execute();
    }

    private void limpiar() {
        predios.clear();
        predios.addAll(prediosOriginales);
        actualizarTabla(predios);
        ultimaBusqueda = null;
        vista.getLblTiempo().setText("Tiempo: N/A");
        vista.getLblCantidad().setText("Registros: " + predios.size());
        vista.getLblEstado().setText("Estado: Limpio");
        vista.getTxtBuscar().setText("");
        vista.setResaltarResultado(false);
    }

    private void actualizarTabla(final List<Predio> lista) {
        SwingUtilities.invokeLater(() -> {
            vista.actualizarTabla(lista);
        });
    }
}
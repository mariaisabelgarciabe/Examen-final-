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
            }
        }
    }

    private boolean cargarCSV(String ruta) {
        SwingWorker<List<Predio>, Void> worker = new SwingWorker<>() {
            private long startTime;

            @Override
            protected List<Predio> doInBackground() throws Exception {
                startTime = System.nanoTime();
                List<Predio> result = CsvReader.leerCsv(ruta);
                long endTime = System.nanoTime();
                double tiempoMs = (endTime - startTime) / 1_000_000.0;
                // Store time in a property accessible via getStateValue? We'll use a field.
                this.tiempoMs = tiempoMs;
                return result;
            }

            private double tiempoMs;

            @Override
            protected void done() {
                try {
                    predios = new ArrayList<>(get());
                    prediosOriginales.clear();
                    prediosOriginales.addAll(predios);
                    actualizarTabla(predios);
                    vista.getLblCantidad().setText("Registros: " + predios.size());
                    vista.getLblEstado().setText("Estado: CSV cargado");
                    vista.getLblTiempo().setText(String.format("Tiempo: %.3f ms", tiempoMs));
                } catch (Exception ex) {
                    vista.mostrarMensaje("Error cargando CSV: " + ex.getMessage(), true);
                    vista.getLblTiempo().setText("Tiempo: Error");
                }
            }
        };
        vista.getLblTiempo().setText("Tiempo: Calculando...");
        try {
            worker.execute();
            return true;
        } catch (Exception ex) {
            vista.mostrarMensaje("Error iniciando carga: " + ex.getMessage(), true);
            vista.getLblTiempo().setText("Tiempo: Error");
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

            @Override
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

            @Override
            protected void done() {
                actualizarTabla(predios);
                vista.getLblCantidad().setText("Registros: " + predios.size());
                vista.getLblEstado().setText("Estado: Ordenado por " + columnaFinal);
                vista.getLblTiempo()
                        .setText(String.format("Tiempo: %.3f ms (%s - %s)", tiempoMs, algoritmoFinal, columnaFinal));
            }
        };
        vista.getLblTiempo().setText("Tiempo: Calculando...");
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

            @Override
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

            @Override
            protected void done() {
                try {
                    List<Predio> resultados = get();
                    ultimaBusqueda = resultados.isEmpty() ? null : resultados.get(0);
                    if (resultados.isEmpty()) {
                        vista.mostrarMensaje("No se encontraron coincidencias para: " + terminoFinal, true);
                        vista.getLblEstado().setText("Estado: Sin resultados");
                    } else {
                        actualizarTabla(resultados);
                        vista.getLblCantidad().setText("Registros encontrados: " + resultados.size());
                        vista.getLblEstado()
                                .setText("Estado: Busqueda completada - " + resultados.size() + " coincidencias");
                        // Seleccionar primera fila y destacar columna buscada
                        int colIndex = vista.getCbColumna().getSelectedIndex();
                        if (colIndex >= 0) {
                            vista.seleccionarFilaConColumna(0, colIndex);
                        }
                    }
                    vista.getLblTiempo().setText(String.format(
                            "Tiempo: %.3f ms (Sort: %.3f ms + Busqueda: %.3f ms)",
                            totalMs, tiempoSortMs, tiempoBusquedaMs));
                } catch (Exception ex) {
                    vista.mostrarMensaje("Error en la busqueda: " + ex.getMessage(), true);
                    vista.getLblEstado().setText("Estado: Error");
                    vista.getLblTiempo().setText("Tiempo: Error");
                }
            }
        };
        vista.getLblEstado().setText("Estado: Ordenando y buscando...");
        vista.getLblTiempo().setText("Tiempo: Calculando...");
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
    }

    private void actualizarTabla(final List<Predio> lista) {
        SwingUtilities.invokeLater(() -> {
            vista.actualizarTabla(lista);
        });
    }
}

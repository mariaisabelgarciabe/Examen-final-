package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public static List<Predio> leerCsv(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || !archivo.isFile()) {
            throw new IOException("El archivo no existe o no es valido: " + rutaArchivo);
        }

        List<Predio> predios = new ArrayList<>();
        int lineasInvalidas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    if (!esEncabezadoValido(linea)) {
                        throw new IllegalStateException("Encabezado invalido. Columnas esperadas: NPN,Municipio,Direccion,NumeroFicha");
                    }
                    continue;
                }

                String[] datos = dividirLinea(linea);
                if (datos.length < 4) {
                    lineasInvalidas++;
                    continue;
                }

                String npn = limpiar(datos[0]);
                String municipio = limpiar(datos[1]);
                String direccion = limpiar(datos[2]);
                String numeroFicha = limpiar(datos[3]);

                predios.add(new Predio(npn, municipio, direccion, numeroFicha));
            }
        }

        if (predios.isEmpty() && lineasInvalidas == 0) {
            throw new IllegalStateException("El archivo CSV esta vacio.");
        }

        return predios;
    }

    private static boolean esEncabezadoValido(String linea) {
        String l = linea.toLowerCase();
        return l.contains("npn") && l.contains("municipio") && l.contains("direccion") && l.contains("ficha");
    }

    private static String[] dividirLinea(String linea) {
        java.util.List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;
        char comilla = 0;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (entreComillas) {
                if (c == comilla) {
                    entreComillas = false;
                } else {
                    actual.append(c);
                }
            } else {
                if (c == '"' || c == '\'') {
                    entreComillas = true;
                    comilla = c;
                } else if (c == ',') {
                    campos.add(actual.toString());
                    actual.setLength(0);
                } else {
                    actual.append(c);
                }
            }
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }

    private static String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}

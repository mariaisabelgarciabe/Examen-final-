package model;

import java.util.ArrayList;
import java.util.List;


public class BusquedaBinaria {

    
    public static List<Predio> buscar(ArrayList<Predio> predios, String columna, String termino) {
        List<Predio> resultados = new ArrayList<>();
        if (predios == null || predios.isEmpty() || termino == null || termino.trim().isEmpty()) {
            return resultados;
        }

        String normTerm = termino.trim().toUpperCase();
        int indice = busquedaBinariaExacta(predios, columna, normTerm);
        if (indice == -1) {
            return resultados;
        }

        int i = indice;

        while (i >= 0 && predios.get(i).getValorColumna(columna).trim().toUpperCase().equals(normTerm)) {
            i--;
        }
        i++;

        while (i < predios.size() && predios.get(i).getValorColumna(columna).trim().toUpperCase().equals(normTerm)) {
            resultados.add(predios.get(i));
            i++;
        }

        return resultados;
    }


    public static List<Predio> buscarParcial(ArrayList<Predio> predios, String columna, String termino) {
        List<Predio> resultados = new ArrayList<>();
        if (predios == null || predios.isEmpty() || termino == null || termino.trim().isEmpty()) {
            return resultados;
        }
        String normTerm = termino.trim().toUpperCase();
        for (Predio p : predios) {
            String valor = p.getValorColumna(columna);
            if (valor == null) continue;
            String normValor = valor.trim().toUpperCase();
            if (normValor.contains(normTerm)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    
    private static int busquedaBinariaExacta(List<Predio> predios, String columna, String termino) {
        int izquierda = 0;
        int derecha = predios.size() - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            String valorMedio = predios.get(medio).getValorColumna(columna);
            String normValor = valorMedio == null ? "" : valorMedio.trim().toUpperCase();
            int cmp = normValor.compareTo(termino);

            if (cmp == 0) {
                return medio;
            } else if (cmp < 0) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }
        return -1;
    }


    private static int busquedaBinariaLowerBound(List<Predio> predios, String columna, String termino) {
        int izquierda = 0;
        int derecha = predios.size() - 1;
        int resultado = predios.size();

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            String valorMedio = predios.get(medio).getValorColumna(columna);
            String normValor = valorMedio == null ? "" : valorMedio.trim().toUpperCase();

            if (normValor.compareTo(termino) >= 0) {
                resultado = medio;
                derecha = medio - 1;
            } else {
                izquierda = medio + 1;
            }
        }
        return resultado;
    }
}

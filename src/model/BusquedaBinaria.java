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

    /**
     * Busca todos los predios cuya columna contenga el texto como substring.
     * Primero busca la primera posicion >= termino y expande.
     *
     * @param predios lista ordenada de predios.
     * @param columna columna en la cual buscar.
     * @param termino termino parcial.
     * @return lista de coincidencias parciales.
     */
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

    /**
     * Busqueda binaria estandar para exacto.
     *
     * @param predios lista ordenada.
     * @param columna columna a buscar.
     * @param termino termino exacto (ya normalizado).
     * @return indice del termino o -1 si no existe.
     */
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

    /**
     * Retorna el primer indice con valor >= termino.
     */
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

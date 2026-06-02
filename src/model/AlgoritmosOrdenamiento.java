package model;

import java.util.ArrayList;
import model.TextUtil;

public class AlgoritmosOrdenamiento {

    public static void quickSort(ArrayList<Predio> predios, String columna) {
        if (predios == null || predios.size() <= 1) {
            return;
        }
        quickSort(predios, 0, predios.size() - 1, columna);
    }

    private static void quickSort(ArrayList<Predio> predios, int izquierda, int derecha, String columna) {
        if (izquierda < derecha) {
            int indicePivote = particionarHoare(predios, izquierda, derecha, columna);
            quickSort(predios, izquierda, indicePivote, columna);
            quickSort(predios, indicePivote + 1, derecha, columna);
        }
    }

    private static int particionarHoare(ArrayList<Predio> predios, int izquierda, int derecha, String columna) {
        String pivote = predios.get(izquierda).getValorColumna(columna);
        int i = izquierda - 1;
        int j = derecha + 1;

        while (true) {
            do {
                i++;
            } while (comparar(predios.get(i).getValorColumna(columna), pivote) < 0);

            do {
                j--;
            } while (comparar(predios.get(j).getValorColumna(columna), pivote) > 0);

            if (i >= j) {
                return j;
            }

            Predio temp = predios.get(i);
            predios.set(i, predios.get(j));
            predios.set(j, temp);
        }
    }

    public static void mergeSort(ArrayList<Predio> predios, String columna) {
        if (predios == null || predios.size() <= 1) return;

        Predio[] arr = predios.toArray(new Predio[0]);
        Predio[] temp = new Predio[arr.length];

        mergeSort(arr, temp, 0, arr.length - 1, columna);

        for (int i = 0; i < arr.length; i++) {
            predios.set(i, arr[i]);
        }
    }

    private static void mergeSort(Predio[] arr, Predio[] temp, int left, int right, String columna) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, temp, left, mid, columna);
            mergeSort(arr, temp, mid + 1, right, columna);

            merge(arr, temp, left, mid, right, columna);
        }
    }

    private static void merge(Predio[] arr, Predio[] temp, int left, int mid, int right, String columna) {
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (comparar(temp[i].getValorColumna(columna), temp[j].getValorColumna(columna)) <= 0) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }

        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
    }

    private static int comparar(String a, String b) {
        String na = TextUtil.normalizarTexto(a);
        String nb = TextUtil.normalizarTexto(b);
        return na.compareTo(nb);
    }
}

package main;

import javax.swing.SwingUtilities;
import controller.PredioController;
import view.PredioView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PredioView vista = new PredioView();
            new PredioController(vista);
            vista.setVisible(true);
        });
    }
}

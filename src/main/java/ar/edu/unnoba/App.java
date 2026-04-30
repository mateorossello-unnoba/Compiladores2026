package ar.edu.unnoba;

import ar.edu.unnoba.ui.VentanaCompilador;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada principal del compilador.
 * Permite ejecutar el compilador en modo gráfico.
 */

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaCompilador ventana = new VentanaCompilador();
            ventana.setVisible(true);
        });
    }
}

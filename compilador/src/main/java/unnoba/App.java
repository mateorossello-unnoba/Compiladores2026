package unnoba;

import javax.swing.SwingUtilities;
import unnoba.ui.VentanaCompilador;

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

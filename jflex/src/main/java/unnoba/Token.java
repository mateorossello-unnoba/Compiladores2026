package unnoba;

/**
 * Clase que representa un token generado por el analizador léxico.
 * Contiene información sobre el tipo de token, su posición en el código fuente y su valor
 */

public class Token {
    public final String nombre;
    public final int linea;
    public final int columna;
    public final Object valor;

    public Token(String nombre, int linea, int columna) {
        this(nombre, linea, columna, null);
    }

    public Token(String nombre, int linea, int columna, Object valor) {
        this.nombre = nombre;
        this.linea = linea;
        this.columna = columna;
        this.valor = valor;
    }

    // Método para representar el token como una cadena legible

    @Override
    public String toString() {
        String pos = "@ (L: " + linea + ", C: " + columna + ")";
        
        if (valor == null) {
            return "[" + nombre + "]" + pos;
        } else {
            return "[" + nombre + "] -> " + valor + " " + pos;
        }
    }
}

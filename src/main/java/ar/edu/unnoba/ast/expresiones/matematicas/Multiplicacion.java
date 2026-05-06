package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;

public class Multiplicacion extends OperacionMatematicaBinaria {
    public Multiplicacion(Expresion izquierda, Expresion derecha) {
        super("*", izquierda, derecha);
    }
}

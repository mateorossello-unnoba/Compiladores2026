package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;

public class Suma extends OperacionMatematicaBinaria {
    public Suma(Expresion izquierda, Expresion derecha) {
        super("+", izquierda, derecha);
    }
}

package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;

public class Resta extends OperacionMatematicaBinaria {
    public Resta(Expresion izquierda, Expresion derecha) {
        super("-", izquierda, derecha);
    }
}

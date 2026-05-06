package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;

public class Division extends OperacionMatematicaBinaria {
    public Division(Expresion izquierda, Expresion derecha) {
        super("/", izquierda, derecha);
    }
}

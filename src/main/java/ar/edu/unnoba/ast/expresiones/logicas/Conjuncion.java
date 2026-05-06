package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class Conjuncion extends OperacionLogicaBinaria {
    public Conjuncion(Expresion izquierda, Expresion derecha) {
        super("&&", izquierda, derecha);
    }
}

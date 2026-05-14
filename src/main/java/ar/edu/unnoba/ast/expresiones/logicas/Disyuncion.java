package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class Disyuncion extends OperacionLogicaBinaria {
    public Disyuncion(Expresion izquierda, Expresion derecha) {
        super("||", izquierda, derecha);
    }
}

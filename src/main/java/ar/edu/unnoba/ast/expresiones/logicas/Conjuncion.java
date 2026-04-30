package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Conjuncion extends OperacionBinaria {
    public Conjuncion(Expresion izquierda, Expresion derecha) {
        super("&&", izquierda, derecha);
    }
}

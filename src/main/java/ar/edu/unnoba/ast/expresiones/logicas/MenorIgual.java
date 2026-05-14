package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class MenorIgual extends OperacionRelacionalBinaria {
    public MenorIgual(Expresion izquierda, Expresion derecha) {
        super("<=", izquierda, derecha);
    }
}

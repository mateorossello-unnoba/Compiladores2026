package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class MayorIgual extends OperacionRelacionalBinaria {
    public MayorIgual(Expresion izquierda, Expresion derecha) {
        super(">=", izquierda, derecha);
    }
}

package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class MayorIgual extends OperacionBinaria {
    public MayorIgual(Expresion izquierda, Expresion derecha) {
        super(">=", izquierda, derecha);
    }
}

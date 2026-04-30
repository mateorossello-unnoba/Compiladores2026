package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Igual extends OperacionBinaria {
    public Igual(Expresion izquierda, Expresion derecha) {
        super("==", izquierda, derecha);
    }
}

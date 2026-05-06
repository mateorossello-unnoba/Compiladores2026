package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class Igual extends OperacionRelacionalBinaria {
    public Igual(Expresion izquierda, Expresion derecha) {
        super("==", izquierda, derecha);
    }
}

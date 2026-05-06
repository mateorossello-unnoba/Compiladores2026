package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class Mayor extends OperacionRelacionalBinaria {
    public Mayor(Expresion izquierda, Expresion derecha) {
        super(">", izquierda, derecha);
    }
}

package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class Menor extends OperacionRelacionalBinaria {
    public Menor(Expresion izquierda, Expresion derecha) {
        super("<", izquierda, derecha);
    }
}

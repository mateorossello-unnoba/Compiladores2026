package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;

public class Desigual extends OperacionRelacionalBinaria {
    public Desigual(Expresion izquierda, Expresion derecha) {
        super("!=", izquierda, derecha);
    }
}

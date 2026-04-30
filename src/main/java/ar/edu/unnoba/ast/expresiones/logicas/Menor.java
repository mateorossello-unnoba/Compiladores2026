package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Menor extends OperacionBinaria {
    public Menor(Expresion izquierda, Expresion derecha) {
        super("<", izquierda, derecha);
    }
}

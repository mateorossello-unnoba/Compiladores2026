package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Resta extends OperacionBinaria {
    public Resta(Expresion izquierda, Expresion derecha) {
        super("-", izquierda, derecha);
    }
}

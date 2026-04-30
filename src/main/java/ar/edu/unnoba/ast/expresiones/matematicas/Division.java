package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Division extends OperacionBinaria {
    public Division(Expresion izquierda, Expresion derecha) {
        super("/", izquierda, derecha);
    }
}

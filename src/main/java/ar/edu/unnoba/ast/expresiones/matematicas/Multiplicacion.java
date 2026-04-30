package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Multiplicacion extends OperacionBinaria {
    public Multiplicacion(Expresion izquierda, Expresion derecha) {
        super("*", izquierda, derecha);
    }
}

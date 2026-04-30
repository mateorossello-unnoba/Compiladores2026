package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;

public class Disyuncion extends OperacionBinaria {
    public Disyuncion(Expresion izquierda, Expresion derecha) {
        super("||", izquierda, derecha);
    }
}

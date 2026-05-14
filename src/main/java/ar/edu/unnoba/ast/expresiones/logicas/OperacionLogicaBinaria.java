package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;
import ar.edu.unnoba.ast.TipoDato;

public abstract class OperacionLogicaBinaria extends OperacionBinaria {
    public OperacionLogicaBinaria(String etiqueta, Expresion izquierda, Expresion derecha) {
        super(etiqueta, TipoDato.BOOLEAN, izquierda, derecha);
    }
}

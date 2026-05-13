package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;
import ar.edu.unnoba.ast.TipoDato;

public abstract class OperacionMatematicaBinaria extends OperacionBinaria {
    public OperacionMatematicaBinaria(String etiqueta, TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super(etiqueta, tipoDato, izquierda, derecha);
    }
}

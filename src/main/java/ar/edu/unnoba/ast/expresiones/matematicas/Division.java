package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class Division extends OperacionMatematicaBinaria {
    public Division(TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super("/", tipoDato, izquierda, derecha);
    }
}

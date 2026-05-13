package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class Suma extends OperacionMatematicaBinaria {
    public Suma(TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super("+", tipoDato, izquierda, derecha);
    }
}

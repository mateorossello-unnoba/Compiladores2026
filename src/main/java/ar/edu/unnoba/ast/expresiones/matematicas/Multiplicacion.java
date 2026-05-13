package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class Multiplicacion extends OperacionMatematicaBinaria {
    public Multiplicacion(TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super("*", tipoDato, izquierda, derecha);
    }
}

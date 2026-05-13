package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class Constante extends Expresion {
    private String valor;

    public Constante(String valorString, TipoDato tipoDato, int dimensionArreglo) {
        super(valorString.replace("\"", "\\\""), tipoDato);
        this.valor = valorString;
        this.dimensionArreglo = dimensionArreglo;
    }

    public String getValor() {
        return valor;
    }
}

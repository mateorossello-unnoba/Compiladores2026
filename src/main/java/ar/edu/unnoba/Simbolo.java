package ar.edu.unnoba;

import ar.edu.unnoba.ast.TipoDato;

public class Simbolo {
    private TipoDato tipoDato;
    private int dimensionArreglo;

    public Simbolo(TipoDato tipo, int dimension) {
        this.tipoDato = tipo;
        this.dimensionArreglo = dimension;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public int getDimensionArreglo() {
        return dimensionArreglo;
    }
}

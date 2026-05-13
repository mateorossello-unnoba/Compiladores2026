package ar.edu.unnoba.ast;

public abstract class Expresion extends Nodo {
    protected TipoDato tipoDato;
    protected int dimensionArreglo = 0;

    public Expresion(String etiqueta, TipoDato tipoDato) {
        super(etiqueta);
        this.tipoDato = tipoDato;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public int getDimensionArreglo() {
        return dimensionArreglo;
    }

    public void setDimensionArreglo(int dimensionArreglo) {
        this.dimensionArreglo = dimensionArreglo;
    }
}

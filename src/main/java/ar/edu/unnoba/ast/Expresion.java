package ar.edu.unnoba.ast;

import ar.edu.unnoba.TablaSimbolos;

public abstract class Expresion extends Nodo {
    protected TipoDato tipoDato;
    protected int dimensionArreglo = 0;

    public Expresion(String etiqueta) {
        super(etiqueta);
    }

    public abstract void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception;

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public int getDimensionArreglo() {
        return dimensionArreglo;
    }
}

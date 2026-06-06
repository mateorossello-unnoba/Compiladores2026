package ar.edu.unnoba.ast;

public abstract class Expresion extends Nodo {
    protected TipoDato tipoDato;
    protected int dimensionArreglo = 0;
    private String irReferencia;

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

    public String getIrReferencia() {
        return irReferencia;
    }

    public void setIrReferencia(String irReferencia) {
        this.irReferencia = irReferencia;
    }

    @Override
    protected String getEtiqueta() {
        return super.getEtiqueta() + " (" + this.tipoDato + ")";
    }
}

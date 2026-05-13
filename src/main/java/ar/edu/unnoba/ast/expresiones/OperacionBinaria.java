package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public abstract class OperacionBinaria extends Expresion {
    protected final Expresion izquierda;
    protected final Expresion derecha;

    public OperacionBinaria(String etiqueta, TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super(etiqueta, tipoDato);
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + izquierda.graficar(miId) + derecha.graficar(miId);
    }
}

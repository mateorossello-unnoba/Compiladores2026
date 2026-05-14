package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public abstract class OperacionUnaria extends Expresion {
    protected final Expresion operando;

    public OperacionUnaria(String etiqueta, TipoDato tipoDato, Expresion operando) {
        super(etiqueta, tipoDato);
        this.operando = operando;
    }
    
    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + operando.graficar(miId);
    }
}

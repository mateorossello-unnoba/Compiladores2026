package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;

public abstract class OperacionUnaria extends Expresion {
    protected final Expresion operando;

    public OperacionUnaria(String etiqueta, Expresion operando) {
        super(etiqueta);
        this.operando = operando;
    }
    
    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + operando.graficar(miId);
    }
}

package ar.edu.unnoba.ast;

import ar.edu.unnoba.TablaSimbolos;

public abstract class Sentencia extends Nodo {
    public Sentencia(String etiqueta) {
        super(etiqueta);
    }

    public abstract void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception;
}

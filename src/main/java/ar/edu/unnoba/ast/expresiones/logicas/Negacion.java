package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;
import ar.edu.unnoba.ast.TipoDato;

public class Negacion extends OperacionUnaria {
    public Negacion(Expresion operando) {
        super("!", TipoDato.BOOLEAN, operando);
    }
}

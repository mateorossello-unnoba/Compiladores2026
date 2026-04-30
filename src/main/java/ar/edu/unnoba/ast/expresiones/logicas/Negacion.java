package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;

public class Negacion extends OperacionUnaria {
    public Negacion(Expresion operando) {
        super("!", operando);
    }
}

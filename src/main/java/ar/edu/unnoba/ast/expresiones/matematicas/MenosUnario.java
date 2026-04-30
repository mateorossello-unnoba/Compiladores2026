package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;

public class MenosUnario extends OperacionUnaria {
    public MenosUnario(Expresion operando) {
        super("-", operando);
    }
}

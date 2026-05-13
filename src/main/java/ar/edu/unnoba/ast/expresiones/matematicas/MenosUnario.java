package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;
import ar.edu.unnoba.ast.TipoDato;

public class MenosUnario extends OperacionUnaria {
    public MenosUnario(TipoDato tipoDato, Expresion operando) {
        super("-", tipoDato, operando);
    }
}

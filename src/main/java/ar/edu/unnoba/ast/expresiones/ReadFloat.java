package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class ReadFloat extends Expresion {
    public ReadFloat() {
        super("READ_FLOAT()", TipoDato.FLOAT);
    }
}

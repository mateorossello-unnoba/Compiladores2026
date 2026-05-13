package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class ReadBool extends Expresion {
    public ReadBool() {
        super("READ_BOOL()", TipoDato.BOOLEAN);
    }
}

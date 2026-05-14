package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class ReadInt extends Expresion {
    public ReadInt() {
        super("READ_INT()", TipoDato.INT);
    }
}

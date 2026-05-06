package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class ReadFloat extends Expresion {
    public ReadFloat() {
        super("READ_FLOAT()");
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        this.tipoDato = TipoDato.FLOAT;
    }
}

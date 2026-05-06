package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class ReadBool extends Expresion {
    public ReadBool() {
        super("READ_BOOL()");
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        this.tipoDato = TipoDato.BOOLEAN;
    }
}

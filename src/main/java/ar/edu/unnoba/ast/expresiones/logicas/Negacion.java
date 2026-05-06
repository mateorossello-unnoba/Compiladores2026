package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class Negacion extends OperacionUnaria {
    public Negacion(Expresion operando) {
        super("!", operando);
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        operando.obtenerTipo(tablaSimbolos);

        if (operando.getTipoDato() != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: El operador de negación '!' solo se puede aplicar a expresiones de tipo BOOLEAN.");
        }

        this.tipoDato = TipoDato.BOOLEAN;
    }
}

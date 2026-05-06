package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.TablaSimbolos;

public class Continue extends Sentencia {
    public Continue() {
        super("CONTINUE");
    }

    @Override
    public void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception {
        if (!dentroDeCiclo) {
            throw new Exception("Error Semántico: La instrucción CONTINUE no puede usarse fuera de un ciclo.");
        }
    }
}

package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.TablaSimbolos;

public class Break extends Sentencia {
    public Break() {
        super("BREAK");
    } 

    @Override
    public void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception {
        if (!dentroDeCiclo) {
            throw new Exception("Error Semántico: La instrucción BREAK no puede usarse fuera de un ciclo.");
        }
    }
}

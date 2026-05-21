package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Break extends Sentencia {
    public Break() {
        super("BREAK");
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        if (generadorCodigo.getPilaBreak().isEmpty()) {
            throw new IllegalStateException("Sentencia BREAK fuera de un ciclo.");
        }

        return "  br label %" + generadorCodigo.getPilaBreak().peek() + "\n";
    }
}

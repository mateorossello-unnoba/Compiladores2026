package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Continue extends Sentencia {
    public Continue() {
        super("CONTINUE");
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        if (generadorCodigo.getPilaContinue().isEmpty()) {
            throw new IllegalStateException("Sentencia CONTINUE fuera de un ciclo.");
        }

        return "  br label %" + generadorCodigo.getPilaContinue().peek() + "\n";
    }
}

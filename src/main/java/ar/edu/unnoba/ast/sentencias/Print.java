package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.Sentencia;

public class Print extends Sentencia {
    private final Expresion expresion;

    public Print(Expresion expresion) {
        super("PRINT");
        this.expresion = expresion;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + expresion.graficar(miId);
    }
}

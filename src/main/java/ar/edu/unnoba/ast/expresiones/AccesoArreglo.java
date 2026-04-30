package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;

public class AccesoArreglo extends Expresion {
    private final Identificador identificador;
    private final Expresion indice;

    public AccesoArreglo(Identificador identificador, Expresion indice) {
        super("ACCESO []");
        this.identificador = identificador;
        this.indice = indice;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + identificador.graficar(miId) + indice.graficar(miId);
    }
}

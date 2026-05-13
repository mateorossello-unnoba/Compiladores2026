package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Sentencia;

public class AsignacionArreglo extends Sentencia {
    private final Identificador identificador;
    private final Expresion indice;
    private final Expresion valor;

    public AsignacionArreglo(Identificador identificador, Expresion indice, Expresion valor) {
        super("ASIGNACIÓN []");
        this.identificador = identificador;
        this.indice = indice;
        this.valor = valor;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + identificador.graficar(miId) + indice.graficar(miId) + valor.graficar(miId);
    }
}

package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.TablaSimbolos;
import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

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

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        identificador.obtenerTipo(tablaSimbolos);
        indice.obtenerTipo(tablaSimbolos);

        if (identificador.getTipoDato() != TipoDato.ARRAY) {
            throw new Exception("Error Semántico: La variable '" + identificador.getNombre() + "' no es de tipo ARRAY.");
        }

        if (indice.getTipoDato() != TipoDato.INT) {
            throw new Exception("Error Semántico: El índice del arreglo debe ser una expresión de tipo INT.");
        }

        this.tipoDato = TipoDato.FLOAT;
    }
}

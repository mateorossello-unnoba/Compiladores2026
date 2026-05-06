package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

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

    @Override
    public void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception {
        identificador.obtenerTipo(tablaSimbolos);
        indice.obtenerTipo(tablaSimbolos);
        valor.obtenerTipo(tablaSimbolos);

        if (identificador.getTipoDato() != TipoDato.ARRAY) {
            throw new Exception("Error Semántico: La variable '" + identificador.getNombre() + "' no es un arreglo.");
        }

        if (indice.getTipoDato() != TipoDato.INT) {
            throw new Exception("Error Semántico: El índice del arreglo debe ser de tipo INT.");
        }

        TipoDato tipoValor = valor.getTipoDato();
        if (tipoValor == TipoDato.BOOLEAN || tipoValor == TipoDato.ARRAY) {
            throw new Exception("Error Semántico: A una posición de un arreglo solo se le pueden asignar valores de tipo INT o FLOAT.");
        }
    }
}

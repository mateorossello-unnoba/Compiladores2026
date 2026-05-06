package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public abstract class OperacionMatematicaBinaria extends OperacionBinaria {
    public OperacionMatematicaBinaria(String etiqueta, Expresion izquierda, Expresion derecha) {
        super(etiqueta, izquierda, derecha);
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        izquierda.obtenerTipo(tablaSimbolos);
        derecha.obtenerTipo(tablaSimbolos);

        TipoDato tipoIzquierda = izquierda.getTipoDato();
        TipoDato tipoDerecha = derecha.getTipoDato();

        if (tipoIzquierda == TipoDato.BOOLEAN || tipoDerecha == TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: No se pueden realizar operaciones aritméticas con valores de tipo BOOLEAN.");
        }

        if (tipoIzquierda == TipoDato.ARRAY || tipoDerecha == TipoDato.ARRAY) {
            this.tipoDato = TipoDato.ARRAY;
            this.dimensionArreglo = (tipoIzquierda == TipoDato.ARRAY) ? izquierda.getDimensionArreglo() : derecha.getDimensionArreglo();
            return;
        }

        if (tipoIzquierda == TipoDato.FLOAT || tipoDerecha == TipoDato.FLOAT) {
            this.tipoDato = TipoDato.FLOAT;
        } else {
            this.tipoDato = TipoDato.INT;
        }
    }
}

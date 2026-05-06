package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public abstract class OperacionLogicaBinaria extends OperacionBinaria {
    public OperacionLogicaBinaria(String etiqueta, Expresion izquierda, Expresion derecha) {
        super(etiqueta, izquierda, derecha);
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        izquierda.obtenerTipo(tablaSimbolos);
        derecha.obtenerTipo(tablaSimbolos);

        TipoDato tipoIzquierda = izquierda.getTipoDato();
        TipoDato tipoDerecha = derecha.getTipoDato();

        if (tipoIzquierda != TipoDato.BOOLEAN || tipoDerecha != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: Los operadores lógicos (" + this.getEtiqueta() + ") requieren operandos de tipo BOOLEAN.");
        }
        
        this.tipoDato = TipoDato.BOOLEAN;
    }
}

package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionBinaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public abstract class OperacionRelacionalBinaria extends OperacionBinaria {
    public OperacionRelacionalBinaria(String etiqueta, Expresion izquierda, Expresion derecha) {
        super(etiqueta, izquierda, derecha);
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        izquierda.obtenerTipo(tablaSimbolos);
        derecha.obtenerTipo(tablaSimbolos);

        TipoDato tipoIzquierdo = izquierda.getTipoDato();
        TipoDato tipoDerecho = derecha.getTipoDato();

        if (tipoIzquierdo == TipoDato.BOOLEAN || tipoDerecho == TipoDato.BOOLEAN) {
            boolean ambosBooleanos = (tipoIzquierdo == TipoDato.BOOLEAN && tipoDerecho == TipoDato.BOOLEAN);
            String operador = this.getEtiqueta();
            boolean admiteBooleanos = operador.equals("==") || operador.equals("!=");

            if (!ambosBooleanos) {
                throw new Exception("Error Semántico: No se puede comparar (" + operador + ") un BOOLEAN con otro tipo.");
            }
            
            if (!admiteBooleanos) {
                throw new Exception("Error Semántico: No se pueden realizar comparaciones (" + operador + ") entre tipos BOOLEAN.");
            }
        }
        
        this.tipoDato = TipoDato.BOOLEAN;
    }
}

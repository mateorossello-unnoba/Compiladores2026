package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class Asignacion extends Sentencia {
    private final Identificador variable;
    private final Expresion valor;

    public Asignacion(Identificador variable, Expresion valor) {
        super("=");
        this.variable = variable;
        this.valor = valor;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + variable.graficar(miId) + valor.graficar(miId);
    }

    @Override
    public void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception {
        variable.obtenerTipo(tablaSimbolos);
        valor.obtenerTipo(tablaSimbolos);

        TipoDato tipoVariable = variable.getTipoDato();
        TipoDato tipoValor = valor.getTipoDato();

        if (tipoVariable == TipoDato.BOOLEAN && tipoValor != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: A una variable BOOLEAN solo se le puede asignar un BOOLEAN.");
        }
        
        if (tipoVariable == TipoDato.INT && tipoValor != TipoDato.INT) {
            throw new Exception("Error Semántico: A una variable INT solo se le puede asignar un valor INT.");
        }
        
        if (tipoVariable == TipoDato.FLOAT && (tipoValor == TipoDato.BOOLEAN || tipoValor == TipoDato.ARRAY)) {
            throw new Exception("Error Semántico: A una variable FLOAT solo se le pueden asignar valores INT o FLOAT.");
        }

        if (tipoVariable == TipoDato.ARRAY) {
            if (tipoValor == TipoDato.BOOLEAN) {
                throw new Exception("Error Semántico: No se puede asignar un BOOLEAN a un ARRAY.");
            }

            if (tipoValor == TipoDato.ARRAY && variable.getDimensionArreglo() != valor.getDimensionArreglo()) {
                throw new Exception("Error Semántico: No se puede asignar un arreglo de tamaño " + valor.getDimensionArreglo() + " a uno de tamaño " + variable.getDimensionArreglo() + ".");
            }
        }
    }
}

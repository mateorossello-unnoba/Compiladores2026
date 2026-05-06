package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class MenosUnario extends OperacionUnaria {
    public MenosUnario(Expresion operando) {
        super("-", operando);
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        operando.obtenerTipo(tablaSimbolos);

        if (operando.getTipoDato() == TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: El operador unario '-' no se puede aplicar a un valor de tipo BOOLEAN.");
        }

        this.tipoDato = operando.getTipoDato();
        this.dimensionArreglo = operando.getDimensionArreglo();
    }
}

package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Negacion extends OperacionUnaria {
    public Negacion(Expresion operando) {
        super("!", TipoDato.BOOLEAN, operando);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generador) {
        // Generar código para el operando, asegurando que el operando sea del tipo booleano
        String codigoOperando = this.operando.generarCodigo(generador);
        
        // Obtener un nuevo puntero para almacenar el resultado de la operación unaria
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        // Código ejemplo:
        // %puntero.1 = xor i1 0, %operando
        return codigoOperando + "  " + puntero + " = xor i1 true, " + this.getOperando().getIrReferencia() + "\n";
    }
}

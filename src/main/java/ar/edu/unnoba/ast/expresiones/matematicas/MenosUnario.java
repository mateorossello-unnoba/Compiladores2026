package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.OperacionUnaria;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class MenosUnario extends OperacionUnaria {
    public MenosUnario(TipoDato tipoDato, Expresion operando) {
        super("-", tipoDato, operando);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generador) {
        // Generar código para el operando, asegurando que el operando sea del tipo final esperado
        String codigoOperando = this.operando.generarCodigo(generador);
        
        // Obtener un nuevo puntero para almacenar el resultado de la operación unaria
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        String instruccion = this.getTipoDato() == TipoDato.FLOAT ? "fsub double 0.0, " + this.getOperando().getIrReferencia() : "sub i32 0, " + this.getOperando().getIrReferencia();
        
        // Código ejemplo:
        // %puntero.1 = sub i32 0, 42
        // %puntero.2 = fsub double 0.0, 3.14
        return codigoOperando + "  " + puntero + " = " + instruccion + "\n";
    }
}

package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Resta extends OperacionMatematicaBinaria {
    public Resta(TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super("-", tipoDato, izquierda, derecha);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        if(this.tipoDato == TipoDato.ARRAY) {
            String codigoArreglo = generadorCodigo.generarOperacionArreglos(this.etiqueta, this.izquierda, this.derecha);
            this.setIrReferencia(generadorCodigo.getUltimoPunteroArreglo());
            return codigoArreglo;
        }

        // Generar código para los operandos izquierdo y derecho, asegurando que ambos operandos sean del tipo final esperado
        String codigoIzquierda = generadorCodigo.generarConConversion(this.izquierda, this.getTipoDato());
        String codigoDerecha = generadorCodigo.generarConConversion(this.derecha, this.getTipoDato());

        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        String instruccion = this.getTipoDato() == TipoDato.FLOAT ? "fsub double" : "sub i32";

        // Código ejemplo:
        // %puntero.1 = fsub double 0.0, 3.14
        // %puntero.2 = fsub double 0.0, 2.71
        // %puntero.3 = fsub double %puntero.1, %puntero.2
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " " + this.izquierda.getIrReferencia() + ", " + this.derecha.getIrReferencia() + "\n";
    }
}

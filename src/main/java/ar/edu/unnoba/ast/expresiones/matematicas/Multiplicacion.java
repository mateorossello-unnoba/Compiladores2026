package ar.edu.unnoba.ast.expresiones.matematicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Multiplicacion extends OperacionMatematicaBinaria {
    public Multiplicacion(TipoDato tipoDato, Expresion izquierda, Expresion derecha) {
        super("*", tipoDato, izquierda, derecha);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        if(this.tipoDato == TipoDato.ARRAY) {
            String codigoArreglo = generadorCodigo.generarOperacionArreglos(this.etiqueta, this.izquierda, this.derecha);
            this.setIrReferencia(generadorCodigo.getUltimoPunteroArreglo());
            return codigoArreglo;
        }

        // Generar código para los operandos izquierdo y derecho, asegurando que ambos operandos sean del tipo final esperado
        String codigoIzquierda = this.izquierda.generarCodigo(generadorCodigo);
        String codigoDerecha = this.derecha.generarCodigo(generadorCodigo);

        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        String instruccion = this.getTipoDato() == TipoDato.FLOAT ? "fmul double" : "mul i32";

        // Código ejemplo:
        // %puntero.1 = fmul double 0.0, 3.14
        // %puntero.2 = fmul double 0.0, 2.71
        // %puntero.3 = fmul double %puntero.1, %puntero.2
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " " + this.izquierda.getIrReferencia() + ", " + this.derecha.getIrReferencia() + "\n";
    }
}

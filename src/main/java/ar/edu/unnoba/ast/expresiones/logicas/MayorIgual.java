package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class MayorIgual extends OperacionRelacionalBinaria {
    public MayorIgual(Expresion izquierda, Expresion derecha) {
        super(">=", izquierda, derecha);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        if (this.izquierda.getTipoDato() == TipoDato.ARRAY || this.derecha.getTipoDato() == TipoDato.ARRAY) {
            String codigoArreglo = generadorCodigo.generarComparacionArreglos(this.etiqueta, this.izquierda, this.derecha);
            this.setIrReferencia(generadorCodigo.getUltimoPunteroArreglo());
            return codigoArreglo;
        }

        // Generar código para los operandos izquierdo y derecho
        String codigoIzquierda = this.izquierda.generarCodigo(generadorCodigo);
        String codigoDerecha = derecha.generarCodigo(generadorCodigo);
        
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        TipoDato tipoOperando = this.getIzquierda().getTipoDato();
        String tipoOperador = generadorCodigo.obtenerTipo(tipoOperando);
        
        String instruccion = (tipoOperando == TipoDato.FLOAT) ? "fcmp oge" : "icmp sge";

        // Código ejemplo:
        //  %puntero.1 = icmp sge i32 %izquierda, %derecha
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " " + tipoOperador + " " + this.izquierda.getIrReferencia() + ", " + this.derecha.getIrReferencia() + "\n";
    }
}

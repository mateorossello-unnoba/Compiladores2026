package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Conjuncion extends OperacionLogicaBinaria {
    public Conjuncion(Expresion izquierda, Expresion derecha) {
        super("&&", izquierda, derecha);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // Generar código para los operandos izquierdo y derecho
        String codigoIzquierda = this.izquierda.generarCodigo(generadorCodigo);
        String codigoDerecha = this.derecha.generarCodigo(generadorCodigo);

        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        String instruccion = "and";

        // Código ejemplo:
        // %puntero.1 = and i1 %izquierda, %derecha
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " i1 " + this.izquierda.getIrReferencia() + ", " + this.derecha.getIrReferencia() + "\n";
    }
}

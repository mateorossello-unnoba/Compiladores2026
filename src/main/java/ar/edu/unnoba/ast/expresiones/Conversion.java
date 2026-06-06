package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Conversion extends Expresion {
    private final Expresion expresion;
    private final TipoDato tipoDestino;

    public Conversion(Expresion expresion, TipoDato tipoDestino) {
        super("CAST_" + tipoDestino.toString().toUpperCase(), tipoDestino);
        this.expresion = expresion;
        this.tipoDestino = tipoDestino;
        this.setDimensionArreglo(expresion.getDimensionArreglo());
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + expresion.graficar(miId);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generador) {
        StringBuilder codigo = new StringBuilder();
        
        codigo.append(expresion.generarCodigo(generador));
        
        String punteroConvertido = AyudanteGeneradorCodigo.getNuevoPuntero();
        TipoDato tipoExpresion = expresion.getTipoDato();
        
        codigo.append("  ; --- CAST (").append(tipoExpresion).append(" -> ").append(tipoDestino).append(") ---\n");

        if (tipoExpresion == TipoDato.INT && tipoDestino == TipoDato.FLOAT) {
             codigo.append("  ").append(punteroConvertido).append(" = sitofp i32 ").append(expresion.getIrReferencia()).append(" to double\n");
        } else {
            throw new IllegalStateException("Conversión no soportada de " + expresion.getTipoDato() + " a " + tipoDestino + ".");
        }
        
        this.setIrReferencia(punteroConvertido);
        return codigo.toString();
    }
}

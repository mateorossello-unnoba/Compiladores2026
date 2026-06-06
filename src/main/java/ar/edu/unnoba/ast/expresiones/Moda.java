package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.llvm.GeneradorCodigo;
import java.util.List;

public class Moda extends Expresion {
    private final List<Sentencia> sentencias;
    private final Expresion resultado;

    public Moda(List<Sentencia> sentencias, Expresion resultado, int dimensionArreglo) {
        super("MODA", resultado.getTipoDato());
        this.sentencias = sentencias;
        this.resultado = resultado;
        this.dimensionArreglo = dimensionArreglo;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder dot = new StringBuilder(super.graficar(idPadre));

        for (Sentencia sentencia : sentencias) {
            dot.append(sentencia.graficar(miId));
        }
        
        dot.append(resultado.graficar(miId));
        return dot.toString();
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();
        
        codigo.append("  ; --- VARIABLES LOCALES DE MODA ---\n");
        codigo.append("  %moda_array = alloca [").append(this.dimensionArreglo).append(" x double]\n");
        codigo.append("  %moda_indice_externo = alloca i32\n");
        codigo.append("  %moda_indice_interno = alloca i32\n");
        codigo.append("  %moda_frecuencia_actual = alloca i32\n");
        codigo.append("  %moda_frecuencia_maxima = alloca i32\n");
        codigo.append("  %moda_valor_resultado = alloca double\n");

        for (Sentencia sentencia : sentencias) {
            codigo.append(sentencia.generarCodigo(generadorCodigo));
        }
        
        codigo.append(resultado.generarCodigo(generadorCodigo));
        this.setIrReferencia(resultado.getIrReferencia());
        return codigo.toString();
    }
}

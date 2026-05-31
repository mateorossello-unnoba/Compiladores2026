package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.ast.expresiones.Constante;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Print extends Sentencia {
    private final Expresion expresion;

    public Print(Expresion expresion) {
        super("PRINT");
        this.expresion = expresion;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + expresion.graficar(miId);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        String codigoExpresion = this.expresion.generarCodigo(generadorCodigo);
        TipoDato tipoDato = this.expresion.getTipoDato();

        if (tipoDato == TipoDato.ARRAY) {
            StringBuilder codigoArray = new StringBuilder(codigoExpresion);
            int dimension = this.expresion.getDimensionArreglo();
            String referenciaArreglo = this.expresion.getIrReferencia();
            
            String referenciaInicio = AyudanteGeneradorCodigo.registrarString("["); 
            String referenciaComa = AyudanteGeneradorCodigo.registrarString("%f, ");
            String referenciaFinal = AyudanteGeneradorCodigo.registrarString("%f]\\0A");

            codigoArray.append("  call i32 (i8*, ...) @printf(i8* getelementptr ([2 x i8], [2 x i8]* ").append(referenciaInicio).append(", i32 0, i32 0))\n");
            
            for (int i = 0; i < dimension; i++) {
                String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
                String valorCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
                
                codigoArray.append("  ").append(punteroCelda).append(" = getelementptr [").append(dimension).append(" x double], [").append(dimension).append(" x double]* ").append(referenciaArreglo).append(", i32 0, i32 ").append(i).append("\n");
                codigoArray.append("  ").append(valorCelda).append(" = load double, double* ").append(punteroCelda).append("\n");
                
                if (i < dimension - 1) {
                    codigoArray.append("  call i32 (i8*, ...) @printf(i8* getelementptr ([5 x i8], [5 x i8]* ").append(referenciaComa).append(", i32 0, i32 0), double ").append(valorCelda).append(")\n");
                } else {
                    codigoArray.append("  call i32 (i8*, ...) @printf(i8* getelementptr ([5 x i8], [5 x i8]* ").append(referenciaFinal).append(", i32 0, i32 0), double ").append(valorCelda).append(")\n");
                }
            }

            return codigoArray.toString();
        }

        String formato;
        int longitudFormato;
        switch (tipoDato) {
            case BOOLEAN -> {
                formato = "%d\\0A";
                longitudFormato = 4;
            }
            case FLOAT -> {
                formato = "%f\\0A";
                longitudFormato = 4;
            }
            case INT -> {
                formato = "%d\\0A";
                longitudFormato = 4;
            }
            case STRING -> {
                formato = "%s\\0A";
                longitudFormato = 4;
            }
            default -> throw new IllegalStateException("Tipo de dato no soportado.");
        };

        String referenciaFormato = AyudanteGeneradorCodigo.registrarString(formato);
        String tipo = generadorCodigo.obtenerTipo(tipoDato);
        String referenciaValor = this.expresion.getIrReferencia();

        String valorFinal;
        if (tipoDato == TipoDato.STRING) {
            int longitud = ((Constante)this.expresion).getValor().toString().length() + 1;
            valorFinal = "i8* getelementptr inbounds ([" + longitud + " x i8], [" + longitud + " x i8]* " + referenciaValor + ", i32 0, i32 0)";
        } else {
            valorFinal = tipo + " " + referenciaValor;
        }
        
        String instruccion = "  call i32 (i8*, ...) @printf(i8* getelementptr ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + referenciaFormato + ", i32 0, i32 0), " + valorFinal + ")\n";

        return codigoExpresion + instruccion;
    }

    public Expresion getExpresion() {
        return expresion;
    }
}

package ar.edu.unnoba.ast;

import ar.edu.unnoba.Simbolo;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;
import java.util.List;

public class Programa extends Nodo {
    private final List<Declaracion> declaraciones;
    private final List<Sentencia> sentencias;

    public Programa(List<Declaracion> declaraciones, List<Sentencia> sentencias) {
        super("PROGRAM");
        this.declaraciones = declaraciones;
        this.sentencias = sentencias;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder();

        resultado.append(super.graficar(idPadre));
        
        for (Declaracion declaracion : declaraciones) {
            resultado.append(declaracion.graficar(miId));
        }
        
        for (Sentencia sentencia : sentencias) {
            resultado.append(sentencia.graficar(miId));
        }
        
        return resultado.toString();
    }

    @Override
    public String generarCodigo(GeneradorCodigo generador) {
        StringBuilder cuerpoCodigo = new StringBuilder();

        for (Sentencia sentencia : sentencias) {
            cuerpoCodigo.append(sentencia.generarCodigo(generador));
        }

        StringBuilder codigoFinal = new StringBuilder();

        // Incluir la cabecera estándar con la configuración del compilador
        codigoFinal.append("; --- Compilador UNNOBA - 2026 ---\n");
        codigoFinal.append("target datalayout = \"e-m:w-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128\"\n");
        codigoFinal.append("target triple = \"x86_64-pc-linux-gnu\"\n\n");

        // Incluir las definiciones de los strings registrados durante la generación de código
        codigoFinal.append("; --- Cadenas Globales ---\n");
        codigoFinal.append(AyudanteGeneradorCodigo.obtenerStrings());
        codigoFinal.append("\n");

        // Declaración de funciones externas
        codigoFinal.append("declare i32 @printf(i8*, ...)\n");
        codigoFinal.append("declare i32 @scanf(i8*, ...)\n\n");

        // Definición de la función main
        codigoFinal.append("define i32 @main() {\n");
        codigoFinal.append("entrada:\n");

        // Reservar memoria para las variables declaradas en el programa
        codigoFinal.append("  ; --- Reserva de Memoria ---\n");

        if (declaraciones != null) {
            for (Declaracion declaracion : declaraciones) {
                Simbolo simbolo = declaracion.getSimbolo();
                String tipo = generador.obtenerTipo(simbolo.getTipoDato());

                for (Identificador variable : declaracion.getVariables()) {
                    if (simbolo.getTipoDato() == TipoDato.ARRAY) {
                        int dimensionArreglo = simbolo.getDimensionArreglo();
                        codigoFinal.append("  %").append(variable.getNombre()).append(" = alloca [").append(dimensionArreglo).append(" x double]\n");
                    } else {
                        codigoFinal.append("  %").append(variable.getNombre()).append(" = alloca ").append(tipo).append("\n");
                    }
                }
            }
        }

        codigoFinal.append("\n");

        // Generar el código para las sentencias del programa
        codigoFinal.append("  ; --- Ejecucion del Programa ---\n");
        codigoFinal.append(cuerpoCodigo);

        // Instrucción de retorno al final de la función main
        codigoFinal.append("  ret i32 0\n");
        codigoFinal.append("}\n");

        return codigoFinal.toString();
    }

    public List<Declaracion> getDeclaraciones() {
        return declaraciones;
    }

    public List<Sentencia> getSentencias() {
        return sentencias;
    }
}

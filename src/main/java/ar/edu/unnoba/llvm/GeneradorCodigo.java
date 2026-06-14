package ar.edu.unnoba.llvm;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import java.util.List;
import java.util.Stack;

public class GeneradorCodigo {
    private Stack<String> pilaBreak = new Stack<>();
    private Stack<String> pilaContinue = new Stack<>();
    private String ultimoPunteroArreglo = "";

    public static String generarControlLimites(String nombreArreglo, int dimension, String referenciaIndice) {
        StringBuilder codigo = new StringBuilder();
        
        String compararMayorIgualCero = AyudanteGeneradorCodigo.getNuevoPuntero();
        String compararMenorDimension = AyudanteGeneradorCodigo.getNuevoPuntero();
        String compararValido = AyudanteGeneradorCodigo.getNuevoPuntero();
        
        String etiquetaValida = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaError = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        codigo.append("  ; --- Control de Limites para Acceder a Arreglo ---\n");
        codigo.append("  ").append(compararMayorIgualCero).append(" = icmp sge i32 ").append(referenciaIndice).append(", 0\n");
        codigo.append("  ").append(compararMenorDimension).append(" = icmp slt i32 ").append(referenciaIndice).append(", ").append(dimension).append("\n");
        codigo.append("  ").append(compararValido).append(" = and i1 ").append(compararMayorIgualCero).append(", ").append(compararMenorDimension).append("\n");
        codigo.append("  br i1 ").append(compararValido).append(", label %").append(etiquetaValida).append(", label %").append(etiquetaError).append("\n\n");

        codigo.append(etiquetaError).append(":\n");
        String mensajeError = "Acceso fuera de rango para '" + nombreArreglo + "' que es un arreglo de longitud " + dimension + ".\\0A";

        String referenciaStringError = AyudanteGeneradorCodigo.registrarString(mensajeError);
        int longitudReal = mensajeError.replace("\\0A", " ").length() + 1;
        
        codigo.append("  call i32 (i8*, ...) @printf(i8* getelementptr ([").append(longitudReal).append(" x i8], [").append(longitudReal).append(" x i8]* ").append(referenciaStringError).append(", i32 0, i32 0))\n");
        codigo.append("  ret i32 1\n\n");

        codigo.append(etiquetaValida).append(":\n");
        
        return codigo.toString();
    }

    public String generarOperacionArreglos(String operador, Expresion izquierda, Expresion derecha) {
        StringBuilder codigo = new StringBuilder();
        
        boolean izquierdaEsArreglo = izquierda.getTipoDato() == TipoDato.ARRAY;
        int n = izquierdaEsArreglo ? izquierda.getDimensionArreglo() : derecha.getDimensionArreglo();

        codigo.append(izquierda.generarCodigo(this));
        codigo.append(derecha.generarCodigo(this));
        
        String punteroArregloResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        String punteroI = AyudanteGeneradorCodigo.getNuevoPuntero();

        codigo.append("  ; --- Inicio de Operacion de Arreglos ---\n");
        codigo.append("  ").append(punteroArregloResultado).append(" = alloca [").append(n).append(" x double]\n");
        codigo.append("  ").append(punteroI).append(" = alloca i32\n");
        codigo.append("  store i32 0, i32* ").append(punteroI).append("\n");

        String condicionBucle = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String cuerpoBucle = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String avanzarI = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String finAlgoritmo = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        // Condición del bucle
        codigo.append("  br label %").append(condicionBucle).append("\n\n");
        codigo.append(condicionBucle).append(":\n");
        String valorIndiceI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorIndiceI).append(" = load i32, i32* ").append(punteroI).append("\n");
        String comparador = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(comparador).append(" = icmp slt i32 ").append(valorIndiceI).append(", ").append(n).append("\n");
        codigo.append("  br i1 ").append(comparador).append(", label %").append(cuerpoBucle).append(", label %").append(finAlgoritmo).append("\n\n");

        // Cuerpo del bucle
        codigo.append(cuerpoBucle).append(":\n");
        
        String valorIzquierda;
        if (izquierdaEsArreglo) {
            String punteroCeldaIzquierda = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(punteroCeldaIzquierda).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(izquierda.getIrReferencia()).append(", i32 0, i32 ").append(valorIndiceI).append("\n");
            valorIzquierda = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(valorIzquierda).append(" = load double, double* ").append(punteroCeldaIzquierda).append("\n");
        } else {
            valorIzquierda = izquierda.getIrReferencia();
        }

        String valorDerecha;
        if (derecha.getTipoDato() == TipoDato.ARRAY) {
            String punteroCeldaDerecha = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(punteroCeldaDerecha).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(derecha.getIrReferencia()).append(", i32 0, i32 ").append(valorIndiceI).append("\n");
            valorDerecha = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(valorDerecha).append(" = load double, double* ").append(punteroCeldaDerecha).append("\n");
        } else {
            valorDerecha = derecha.getIrReferencia();
        }

        String instruccion = switch (operador) {
            case "+" -> "fadd double";
            case "-" -> "fsub double";
            case "*" -> "fmul double";
            case "/" -> "fdiv double";
            default -> throw new IllegalStateException("Operador no soportado.");
        };

        String valorRespuesta = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorRespuesta).append(" = ").append(instruccion).append(" ").append(valorIzquierda).append(", ").append(valorDerecha).append("\n");
        
        String punteroCeldaNueva = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroCeldaNueva).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(punteroArregloResultado).append(", i32 0, i32 ").append(valorIndiceI).append("\n");
        codigo.append("  store double ").append(valorRespuesta).append(", double* ").append(punteroCeldaNueva).append("\n");

        codigo.append("  br label %").append(avanzarI).append("\n\n");

        // Avanzar el índice
        codigo.append(avanzarI).append(":\n");
        String proximoValorI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(proximoValorI).append(" = add i32 ").append(valorIndiceI).append(", 1\n");
        codigo.append("  store i32 ").append(proximoValorI).append(", i32* ").append(punteroI).append("\n");
        codigo.append("  br label %").append(condicionBucle).append("\n\n");

        // Fin del algoritmo
        codigo.append(finAlgoritmo).append(":\n");

        // Guardar el puntero para que el nodo se lo asigne a su propia referencia
        ultimoPunteroArreglo = punteroArregloResultado;
        return codigo.toString();
    }

    public String generarComparacionArreglos(String operador, Expresion izquierda, Expresion derecha) {
        StringBuilder codigo = new StringBuilder();
        
        boolean izquierdaEsArreglo = izquierda.getTipoDato() == TipoDato.ARRAY;
        int n = izquierdaEsArreglo ? izquierda.getDimensionArreglo() : derecha.getDimensionArreglo();

        codigo.append(izquierda.generarCodigo(this));
        codigo.append(derecha.generarCodigo(this));

        String punteroResultadoBooleano = AyudanteGeneradorCodigo.getNuevoPuntero();
        String punteroI = AyudanteGeneradorCodigo.getNuevoPuntero();

        codigo.append("  ; --- Inicio de Comparacion de Arreglos ---\n");

        codigo.append("  ").append(punteroResultadoBooleano).append(" = alloca i1\n");
        codigo.append("  store i1 true, i1* ").append(punteroResultadoBooleano).append("\n");
        
        codigo.append("  ").append(punteroI).append(" = alloca i32\n");
        codigo.append("  store i32 0, i32* ").append(punteroI).append("\n");

        String condicionBucle = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String cuerpoBucle = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String falloComparacion = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String avanzarI = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String fin = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        // Condición del bucle
        codigo.append("  br label %").append(condicionBucle).append("\n\n");
        codigo.append(condicionBucle).append(":\n");
        String valorIndiceI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorIndiceI).append(" = load i32, i32* ").append(punteroI).append("\n");
        String compararLimite = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(compararLimite).append(" = icmp slt i32 ").append(valorIndiceI).append(", ").append(n).append("\n");
        codigo.append("  br i1 ").append(compararLimite).append(", label %").append(cuerpoBucle).append(", label %").append(fin).append("\n\n");

        // Cuerpo del bucle
        codigo.append(cuerpoBucle).append(":\n");
        
        String valorIzquierda;
        if (izquierdaEsArreglo) {
            String punteroCeldaIzquierda = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(punteroCeldaIzquierda).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(izquierda.getIrReferencia()).append(", i32 0, i32 ").append(valorIndiceI).append("\n");
            valorIzquierda = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(valorIzquierda).append(" = load double, double* ").append(punteroCeldaIzquierda).append("\n");
        } else {
            valorIzquierda = izquierda.getIrReferencia();
        }

        String valorDerecha;
        if (derecha.getTipoDato() == TipoDato.ARRAY) {
            String punteroCeldaDerecha = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(punteroCeldaDerecha).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(derecha.getIrReferencia()).append(", i32 0, i32 ").append(valorIndiceI).append("\n");
            valorDerecha = AyudanteGeneradorCodigo.getNuevoPuntero();
            codigo.append("  ").append(valorDerecha).append(" = load double, double* ").append(punteroCeldaDerecha).append("\n");
        } else {
            valorDerecha = derecha.getIrReferencia();
        }

        String instruccion = switch (operador) {
            case "!=" -> "fcmp one double";
            case "==" -> "fcmp oeq double";
            case ">"  -> "fcmp ogt double";
            case ">=" -> "fcmp oge double";
            case "<"  -> "fcmp olt double";
            case "<=" -> "fcmp ole double";
            default -> throw new IllegalStateException("Operador no soportado.");
        };

        String compararElementos = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(compararElementos).append(" = ").append(instruccion).append(" ").append(valorIzquierda).append(", ").append(valorDerecha).append("\n");
        
        codigo.append("  br i1 ").append(compararElementos).append(", label %").append(avanzarI).append(", label %").append(falloComparacion).append("\n\n");

        // Fallo en la comparación
        codigo.append(falloComparacion).append(":\n");
        codigo.append("  store i1 false, i1* ").append(punteroResultadoBooleano).append("\n");
        codigo.append("  br label %").append(fin).append("\n\n");

        // Avanzar el índice
        codigo.append(avanzarI).append(":\n");
        String proximoValorI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(proximoValorI).append(" = add i32 ").append(valorIndiceI).append(", 1\n");
        codigo.append("  store i32 ").append(proximoValorI).append(", i32* ").append(punteroI).append("\n");
        codigo.append("  br label %").append(condicionBucle).append("\n\n");

        // Fin del algoritmo
        codigo.append(fin).append(":\n");
        String resultadoFinalBooleano = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(resultadoFinalBooleano).append(" = load i1, i1* ").append(punteroResultadoBooleano).append("\n");

        // Guardar el puntero para que el nodo se lo asigne a su propia referencia
        ultimoPunteroArreglo = resultadoFinalBooleano;
        return codigo.toString();
    }

    public Stack<String> getPilaBreak() {
        return pilaBreak;
    }

    public Stack<String> getPilaContinue() {
        return pilaContinue;
    }

    public String getUltimoPunteroArreglo() {
        return ultimoPunteroArreglo;
    }

    // --- MÉTODOS AUXILIARES DE GENERACIÓN ---

    public String obtenerTipo(TipoDato tipoDato) {
        return switch (tipoDato) {
            case BOOLEAN -> "i1";
            case FLOAT -> "double";
            case INT -> "i32";
            case ARRAY -> "double*";
            case STRING -> "i8*";
            default -> throw new IllegalStateException("Tipo de dato desconocido.");
        };
    }

    public String generarBloqueCodigo(List<Sentencia> sentencias) {
        StringBuilder codigo = new StringBuilder();

        for (Sentencia sentencia : sentencias) {
            codigo.append(sentencia.generarCodigo(this));
        }

        return codigo.toString();
    }
}

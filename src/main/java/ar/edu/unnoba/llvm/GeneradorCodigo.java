package ar.edu.unnoba.llvm;

import ar.edu.unnoba.ast.*;
import ar.edu.unnoba.ast.expresiones.*;
import ar.edu.unnoba.ast.expresiones.matematicas.*;
import ar.edu.unnoba.ast.expresiones.logicas.*;
import ar.edu.unnoba.ast.sentencias.*;
import ar.edu.unnoba.Simbolo;
import java.util.List;
import java.util.Stack;

public class GeneradorCodigo {
    private Stack<String> pilaBreak = new Stack<>();
    private Stack<String> pilaContinue = new Stack<>();

    // Método principal para generar el código LLVM a partir del programa AST
    public String generarPrograma(Programa programa) {
        String cuerpoCodigo = generarBloqueCodigo(programa.getSentencias());
        StringBuilder codigoFinal = new StringBuilder();

        // Incluir la cabecera estándar con la configuración del compilador
        codigoFinal.append("; --- Compilador UNNOBA - 2026 ---\n");
        codigoFinal.append("target datalayout = \"e-m:w-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128\"\n");
        codigoFinal.append("target triple = \"x86_64-pc-windows-msvc19.33.0\"\n\n");

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

        if (programa.getDeclaraciones() != null) {
            for (Declaracion declaracion : programa.getDeclaraciones()) {
                Simbolo simbolo = declaracion.getSimbolo();
                String tipo = obtenerTipo(simbolo.getTipoDato());

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

    // Bifurcación principal para generar código LLVM según el tipo de nodo AST
    private String generar(Nodo nodo) {
        if (nodo == null) return "";
        
        return switch (nodo) {
            case AccesoArreglo accesoArreglo -> generarAccesoArreglo(accesoArreglo);
            case Constante constante -> generarConstante(constante);
            case Identificador identificador -> generarIdentificador(identificador);
            case Moda moda -> generarModa(moda);
            case OperacionUnaria operacionUnaria -> generarOperacionUnaria(operacionUnaria);
            case ReadBool readBool -> generarReadBool(readBool);
            case ReadFloat readFloat -> generarReadFloat(readFloat);
            case ReadInt readInt -> generarReadInt(readInt);
            case OperacionMatematicaBinaria operacionMatematicaBinaria -> generarOperacionMatematicaBinaria(operacionMatematicaBinaria);
            case OperacionLogicaBinaria operacionLogicaBinaria -> generarOperacionLogicaBinaria(operacionLogicaBinaria);
            case OperacionRelacionalBinaria operacionRelacionalBinaria -> generarOperacionRelacionalBinaria(operacionRelacionalBinaria);
            case Asignacion asignacion -> generarAsignacion(asignacion);
            case AsignacionArreglo asignacionArreglo -> generarAsignacionArreglo(asignacionArreglo);
            case Break breakSentencia -> generarBreak(breakSentencia);
            case Continue continueSentencia -> generarContinue(continueSentencia);
            case If ifSentencia -> generarIf(ifSentencia);
            case Print print -> generarPrint(print);
            case While whileSentencia -> generarWhile(whileSentencia);
            default -> ""; 
        };
    }

    // --- GENERAR EXPRESIONES ---

    private String generarAccesoArreglo(AccesoArreglo acceso) {
        String codigoIndice = generar(acceso.getIndice());
        String referenciaIndice = acceso.getIndice().getIrReferencia();
        String nombreArreglo = acceso.getIdentificador().getNombre();
        
        int dimensionArreglo = acceso.getIdentificador().getDimensionArreglo(); 
        
        String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
        String valorCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
        acceso.setIrReferencia(valorCelda);
        
        // Obtener puntero a la celda del arreglo
        String instruccionPuntero = "  " + punteroCelda + " = getelementptr [" + dimensionArreglo + " x double], [" + dimensionArreglo + " x double]* %" + nombreArreglo + ", i32 0, i32 " + referenciaIndice + "\n";
        
        // Cargar el valor de la celda del arreglo
        String instruccionCarga = "  " + valorCelda + " = load double, double* " + punteroCelda + "\n";

        return codigoIndice + instruccionPuntero + instruccionCarga;
    }

    private String generarConstante(Constante constante) {
        // Obtener un nuevo puntero para almacenar el valor de la constante
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        constante.setIrReferencia(puntero);
        
        // Código ejemplo:
        // %puntero.1 = fadd double 0.0, 3.14
        return switch (constante.getTipoDato()) {
            case INT -> "  " + puntero + " = add i32 0, " + constante.getValor() + "\n";
            case FLOAT -> "  " + puntero + " = fadd double 0.0, " + constante.getValor() + "\n";
            case BOOLEAN -> "  " + puntero + " = xor i1 " + constante.getValor() + ", false\n";
            case ARRAY -> {
                String valores = constante.getValor().toString().replace("[", "").replace("]", "").trim();
                StringBuilder valor = new StringBuilder("[");
                
                if (!valores.isEmpty()) {
                    String[] elementos = valores.split(",");

                    for (int i = 0; i < elementos.length; i++) {
                        valor.append("double ").append(elementos[i].trim());
                        if (i < elementos.length - 1) valor.append(", ");
                    }
                }

                valor.append("]");

                constante.setIrReferencia(valor.toString());
                yield "";
            }
            case STRING -> {
                String nombreString = AyudanteGeneradorCodigo.registrarString(constante.getValor().toString());
                constante.setIrReferencia(nombreString);
                yield "";
            }
            default -> throw new IllegalStateException("Tipo de constante desconocida.");
        };
    }

    private String generarIdentificador(Identificador identificador) {
        // Obtener un nuevo puntero para cargar el valor de la variable
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        identificador.setIrReferencia(puntero);

        String tipo = obtenerTipo(identificador.getTipoDato());

        // Código ejemplo:
        // %puntero.1 = load i32, i32* @variable
        return "  " + puntero + " = load " + tipo + ", " + tipo + "* %" + identificador.getNombre() + "\n";
    }

    private String generarModa(Moda moda) {
        // TODO: Implementar generación de código para la operación de moda
        return "";
    }

    private String generarOperacionUnaria(OperacionUnaria operacionUnaria) {
        // Generar código para el operando de la operación unaria
        String codigoOperando = generar(operacionUnaria.getOperando());
        
        // Obtener un nuevo puntero para almacenar el resultado de la operación unaria
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        operacionUnaria.setIrReferencia(puntero);

        String instruccion = switch (operacionUnaria) {
            case MenosUnario menosUnario -> operacionUnaria.getTipoDato() == TipoDato.FLOAT ? "fsub double 0.0, " + menosUnario.getOperando().getIrReferencia() : "sub i32 0, " + menosUnario.getOperando().getIrReferencia();
            case Negacion negacion -> "xor i1 " + operacionUnaria.getOperando().getIrReferencia() + ", true";
            default -> throw new IllegalStateException("Operación unaria desconocida.");
        };
        
        // Código ejemplo:
        // %puntero.1 = fsub double 0.0, %puntero
        // %puntero.2 = xor i1 %puntero, true
        return codigoOperando + "  " + puntero + " = " + instruccion + "\n";
    }

    private String generarReadBool(ReadBool readBool) {
        // Registrar el formato para leer un booleano
        String formato = AyudanteGeneradorCodigo.registrarString("%d");

        // Reservar espacio en memoria para el booleano leído
        String punteroMemoria = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionAsignacion = "  " + punteroMemoria + " = alloca i32\n";

        // Instrucción para llamar a scanf y leer el booleano desde la entrada estándar
        int longitudFormato = formato.length() + 1;
        String instruccionEscanear = "  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + formato + ", i64 0, i64 0), i32* " + punteroMemoria + ")\n";

        // Leer el valor almacenado en memoria después de la llamada a scanf
        String punteroTemporal = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionLeerValor = "  " + punteroTemporal + " = load i32, i32* " + punteroMemoria + "\n";

        // Convertir el entero leído (0 o 1) a un valor booleano (i1) usando XOR con false
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionConvertir = "  " + punteroResultado + " = xor i1 " + punteroTemporal + ", false\n";

        readBool.setIrReferencia(punteroResultado);

        // Código ejemplo:
        // @.str.0 = private unnamed_addr constant [3 x i8] c"%d\00"
        // %puntero.1 = alloca i32
        // call i32 (i8*, ...) @scanf(i8* @.str.0, i32* %puntero.1)
        // %puntero.2 = load i32, i32* %puntero.1
        // %puntero.3 = xor i1 %puntero.2, false
        return instruccionAsignacion + instruccionEscanear + instruccionLeerValor + instruccionConvertir;
    }

    private String generarReadFloat(ReadFloat readFloat) {
        // Registrar el formato para leer un float
        String formato = AyudanteGeneradorCodigo.registrarString("%lf");

        // Reservar espacio en memoria para el float leído
        String punteroMemoria = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionAsignacion = "  " + punteroMemoria + " = alloca double\n";

        // Instrucción para llamar a scanf y leer el float desde la entrada estándar
        int longitudFormato = formato.length() + 1;
        String instruccionEscanear = "  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + formato + ", i64 0, i64 0), double* " + punteroMemoria + ")\n";

        // Leer el valor almacenado en memoria después de la llamada a scanf
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionLeerValor = "  " + punteroResultado + " = load double, double* " + punteroMemoria + "\n";

        readFloat.setIrReferencia(punteroResultado);
        
        // Código ejemplo:
        // @.str.0 = private unnamed_addr constant [3 x i8] c"%f\00"
        // %puntero.1 = alloca double
        // call i32 (i8*, ...) @scanf(i8* @.str.0, double* %puntero.1)
        // %puntero.2 = load double, double* %puntero.1
        return instruccionAsignacion + instruccionEscanear + instruccionLeerValor;
    }

    private String generarReadInt(ReadInt readInt) {
        // Registrar el formato para leer un entero
        String formato = AyudanteGeneradorCodigo.registrarString("%d");
        
        // Reservar espacio en memoria para el entero leído
        String punteroMemoria = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionAsignacion = "  " + punteroMemoria + " = alloca i32\n";
        
        // Instrucción para llamar a scanf y leer el entero desde la entrada estándar
        int longitudFormato = formato.length() + 1;
        String instruccionEscanear = "  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + formato + ", i64 0, i64 0), i32* " + punteroMemoria + ")\n";

        // Leer el valor almacenado en memoria después de la llamada a scanf
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionLeerValor = "  " + punteroResultado + " = load i32, i32* " + punteroMemoria + "\n";

        readInt.setIrReferencia(punteroResultado);

        // Código ejemplo:
        // @.str.0 = private unnamed_addr constant [3 x i8] c"%d\00"
        // %puntero.1 = alloca i32
        // call i32 (i8*, ...) @scanf(i8* @.str.0, i32* %puntero.1)
        // %puntero.2 = load i32, i32* %puntero.1
        return instruccionAsignacion + instruccionEscanear + instruccionLeerValor;
    }

    private String generarOperacionMatematicaBinaria(OperacionMatematicaBinaria operacionMatematicaBinaria) {
        // Obtener el tipo de dato final para la operación matemática binaria
        TipoDato tipoFinal = operacionMatematicaBinaria.getTipoDato();

        // Generar código para los operandos izquierdo y derecho, asegurando que ambos operandos sean del tipo final esperado
        String codigoIzquierda = generarConConversion(operacionMatematicaBinaria.getIzquierda(), tipoFinal);
        String codigoDerecha = generarConConversion(operacionMatematicaBinaria.getDerecha(), tipoFinal);

        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        operacionMatematicaBinaria.setIrReferencia(puntero);

        String instruccion = obtenerOperacionMatematicaBinaria(operacionMatematicaBinaria);

        // Código ejemplo:
        // %puntero.1 = fadd double 0.0, 3.14
        // %puntero.2 = fadd double 0.0, 2.71
        // %puntero.3 = fadd double %puntero.1, %puntero.2
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " " + operacionMatematicaBinaria.getIzquierda().getIrReferencia() + ", " + operacionMatematicaBinaria.getDerecha().getIrReferencia() + "\n";
    }

    // Método auxiliar para obtener la instrucción LLVM correspondiente a una operación matemática binaria
    private String obtenerOperacionMatematicaBinaria(OperacionMatematicaBinaria operacionMatematicaBinaria) {
        return switch (operacionMatematicaBinaria) {
            // Suma (+)
            case Suma suma -> operacionMatematicaBinaria.getTipoDato() == TipoDato.FLOAT ? "fadd double" : "add i32";
            // Resta (-)
            case Resta resta -> operacionMatematicaBinaria.getTipoDato() == TipoDato.FLOAT ? "fsub double" : "sub i32";
            // Multiplicación (*)
            case Multiplicacion multiplicacion -> operacionMatematicaBinaria.getTipoDato() == TipoDato.FLOAT ? "fmul double" : "mul i32";
            // División (/)
            case Division division -> operacionMatematicaBinaria.getTipoDato() == TipoDato.FLOAT ? "fdiv double" : "sdiv i32";
            default -> throw new IllegalStateException("Operación matemática binaria desconocida.");
        };
    }

    private String generarOperacionLogicaBinaria(OperacionLogicaBinaria operacionLogicaBinaria) {
        // Generar código para los operandos izquierdo y derecho de la operación lógica binaria
        String codigoIzquierda = generar(operacionLogicaBinaria.getIzquierda());
        String codigoDerecha = generar(operacionLogicaBinaria.getDerecha());
        
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        operacionLogicaBinaria.setIrReferencia(puntero);

        String instruccion = obtenerOperacionLogicaBinaria(operacionLogicaBinaria);

        // Código ejemplo:
        // %puntero.1 = icmp eq i32 5, 5
        // %puntero.2 = icmp eq i32 3, 4
        // %puntero.3 = and i1 %puntero.1, %puntero.2
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " i1 " + operacionLogicaBinaria.getIzquierda().getIrReferencia() + ", " + operacionLogicaBinaria.getDerecha().getIrReferencia() + "\n";
    }

    // Método auxiliar para obtener la instrucción LLVM correspondiente a una operación lógica binaria
    private String obtenerOperacionLogicaBinaria(OperacionLogicaBinaria operacionLogicaBinaria) {
        return switch (operacionLogicaBinaria) {
            // Conjunción (&&)
            case Conjuncion conjuncion -> "and";
            // Disyunción (||)
            case Disyuncion disyuncion -> "or";
            default -> throw new IllegalStateException("Operación lógica binaria desconocida.");
        };
    }

    private String generarOperacionRelacionalBinaria(OperacionRelacionalBinaria operacionRelacionalbinaria) {
        // Generar código para los operandos izquierdo y derecho de la operación relacional binaria
        String codigoIzquierda = generar(operacionRelacionalbinaria.getIzquierda());
        String codigoDerecha = generar(operacionRelacionalbinaria.getDerecha());
        
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        operacionRelacionalbinaria.setIrReferencia(puntero);

        TipoDato tipoOperando = operacionRelacionalbinaria.getIzquierda().getTipoDato();
        String tipoOperador = obtenerTipo(tipoOperando);
        
        String instruccion = obtenerOperacionRelacionalBinaria(operacionRelacionalbinaria, tipoOperando);

        // Código ejemplo:
        // %puntero.1 = fadd double 0.0, 3.14
        // %puntero.2 = fadd double 0.0, 2.71
        // %puntero.3 = fcmp ogt double %puntero.1, %puntero.2
        return codigoIzquierda + codigoDerecha + "  " + puntero + " = " + instruccion + " " + tipoOperador + " " + operacionRelacionalbinaria.getIzquierda().getIrReferencia() + ", " + operacionRelacionalbinaria.getDerecha().getIrReferencia() + "\n";
    }

    // Método auxiliar para obtener la instrucción LLVM correspondiente a una operación relacional binaria
    private String obtenerOperacionRelacionalBinaria(OperacionRelacionalBinaria opRelacional, TipoDato tipoOperando) {
        boolean esFloat = (tipoOperando == TipoDato.FLOAT);
        
        return switch (opRelacional) {
            // Desigual (!=)
            case Desigual desigual -> esFloat ? "fcmp one" : "icmp ne";
            // Igual (==)
            case Igual igual -> esFloat ? "fcmp oeq" : "icmp eq";
            // Mayor (>)
            case Mayor mayor -> esFloat ? "fcmp ogt" : "icmp sgt";
            // Mayor o igual (>=)
            case MayorIgual mayorIgual -> esFloat ? "fcmp oge" : "icmp sge";
            // Menor (<)
            case Menor menor -> esFloat ? "fcmp olt" : "icmp slt";
            // Menor o igual (<=)
            case MenorIgual menorIgual -> esFloat ? "fcmp ole" : "icmp sle";
            default -> throw new IllegalStateException("Operación relacional binaria desconocida.");
        };
    }

    // --- GENERAR SENTENCIAS ---

    private String generarAsignacion(Asignacion asignacion) {
        TipoDato tipoFinal = asignacion.getVariable().getTipoDato();
        String codigoValor = generarConConversion(asignacion.getValor(), tipoFinal);

        if (tipoFinal == TipoDato.ARRAY) {
            StringBuilder stringBuilder = new StringBuilder();
            int dimensionArreglo = asignacion.getVariable().getDimensionArreglo();

            String literal = ((Constante)asignacion.getValor()).getValor().toString();
            String contenido = literal.substring(1, literal.length() - 1);
            String[] elementos = contenido.split(",");

            for (int i = 0; i < dimensionArreglo; i++) {
                String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
                String valorElemento = (i < elementos.length) ? elementos[i].trim() : "0.0";

                stringBuilder.append("  ").append(punteroCelda).append(" = getelementptr [").append(dimensionArreglo).append(" x double], [").append(dimensionArreglo).append(" x double]* %").append(asignacion.getVariable().getNombre()).append(", i32 0, i32 ").append(i).append("\n");
                stringBuilder.append("  store double ").append(valorElemento).append(", double* ").append(punteroCelda).append("\n");
            }

            return stringBuilder.toString();
        }
        
        String tipo = obtenerTipo(asignacion.getVariable().getTipoDato());
        String instruccionAsignacion = "  store " + tipo + " " + asignacion.getValor().getIrReferencia() + ", " + tipo + "* %" + asignacion.getVariable().getNombre() + "\n";

        // Código ejemplo:
        // %puntero.1 = fadd double 0.0, 3.14
        // store double %puntero.1, double* @variable
        return codigoValor + instruccionAsignacion;
    }

    private String generarAsignacionArreglo(AsignacionArreglo asignacion) {
        String codigoIndice = generar(asignacion.getIndice());
        String codigoValor = generarConConversion(asignacion.getValor(), TipoDato.FLOAT);
        
        String referenciaIndice = asignacion.getIndice().getIrReferencia();
        String referenciaValor = asignacion.getValor().getIrReferencia();
        String nombreArreglo = asignacion.getIdentificador().getNombre();
        int dimension = asignacion.getIdentificador().getDimensionArreglo();

        String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();

        // Obtener puntero a la celda del arreglo
        String instruccionPuntero = "  " + punteroCelda + " = getelementptr [" + dimension + " x double], [" + dimension + " x double]* %" + nombreArreglo + ", i32 0, i32 " + referenciaIndice + "\n";
        
        // Almacenar el valor en la celda del arreglo
        String instruccionAlmacenamiento = "  store double " + referenciaValor + ", double* " + punteroCelda + "\n";

        return codigoIndice + codigoValor + instruccionPuntero + instruccionAlmacenamiento;
    }

    private String generarBreak(Break breakSentencia) {
        if (pilaBreak.isEmpty()) {
            throw new IllegalStateException("Sentencia BREAK fuera de un ciclo.");
        }
        
        return "  br label %" + pilaBreak.peek() + "\n";
    }

    private String generarContinue(Continue continueSentencia) {
        if (pilaContinue.isEmpty()) {
            throw new IllegalStateException("Sentencia CONTINUE fuera de un ciclo.");
        }
        
        return "  br label %" + pilaContinue.peek() + "\n";
    }

    private String generarIf(If ifSentencia) {
        StringBuilder codigo = new StringBuilder();

        codigo.append(generar(ifSentencia.getCondicion()));
        String referenciaCondicion = ifSentencia.getCondicion().getIrReferencia();

        String etiquetaThen = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFin = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        boolean tieneElse = ifSentencia.getBloqueElse() != null && !ifSentencia.getBloqueElse().isEmpty();
        String etiquetaElse = tieneElse ? AyudanteGeneradorCodigo.getNuevaEtiqueta() : etiquetaFin;

        codigo.append("  br i1 ").append(referenciaCondicion).append(", label %").append(etiquetaThen).append(", label %").append(etiquetaElse).append("\n\n");
        codigo.append(etiquetaThen).append(":\n");
        codigo.append(generarBloqueCodigo(ifSentencia.getBloqueThen()));
        codigo.append("  br label %").append(etiquetaFin).append("\n\n");

        if (tieneElse) {
            codigo.append(etiquetaElse).append(":\n");
            codigo.append(generarBloqueCodigo(ifSentencia.getBloqueElse()));
            codigo.append("  br label %").append(etiquetaFin).append("\n\n");
        }

        codigo.append(etiquetaFin).append(":\n");

        return codigo.toString();
    }

    private String generarPrint(Print print) {
        String codigoExpresion = generar(print.getExpresion());
        TipoDato tipoDato = print.getExpresion().getTipoDato();

        if (tipoDato == TipoDato.ARRAY) {
            StringBuilder codigoArray = new StringBuilder(codigoExpresion);
            int dimension = print.getExpresion().getDimensionArreglo();
            String referenciaArreglo = print.getExpresion().getIrReferencia();
            
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
        switch (tipoDato) {
            case BOOLEAN -> formato = "%d\\0A";
            case FLOAT -> formato = "%f\\0A";
            case INT -> formato = "%d\\0A";
            case STRING -> formato = "%s\\0A";
            default -> throw new IllegalStateException("Tipo de dato no soportado para PRINT.");
        };

        String referenciaFormato = AyudanteGeneradorCodigo.registrarString(formato);
        String tipo = obtenerTipo(tipoDato);
        String referenciaValor = print.getExpresion().getIrReferencia();

        String valorFinal;
        if (tipoDato == TipoDato.STRING) {
            int longitud = ((Constante)print.getExpresion()).getValor().toString().length() + 1;
            valorFinal = "i8* getelementptr inbounds ([" + longitud + " x i8], [" + longitud + " x i8]* " + referenciaValor + ", i32 0, i32 0)";
        } else {
            valorFinal = tipo + " " + referenciaValor;
        }

        int longitudFormato = formato.length() + 1;
        String instruccion = "  call i32 (i8*, ...) @printf(i8* getelementptr ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + referenciaFormato + ", i32 0, i32 0), " + valorFinal + ")\n";

        return codigoExpresion + instruccion;
    }

    private String generarWhile(While whileSentencia) {
        StringBuilder codigo = new StringBuilder();

        String etiquetaInicio = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFin = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        
        pilaBreak.push(etiquetaFin);
        pilaContinue.push(etiquetaInicio);

        codigo.append("  br label %").append(etiquetaInicio).append("\n\n");
        codigo.append(etiquetaInicio).append(":\n");
        Clausula principal = whileSentencia.getBloquePrincipal();
        codigo.append(generar(principal.getCondicion()));

        String etiquetaCuerpo = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        boolean tieneAltWhile = whileSentencia.getBloquesAltWhile() != null && !whileSentencia.getBloquesAltWhile().isEmpty();
        String etiquetaAltWhile = tieneAltWhile ? AyudanteGeneradorCodigo.getNuevaEtiqueta() : etiquetaFin;

        codigo.append("  br i1 ").append(principal.getCondicion().getIrReferencia()).append(", label %").append(etiquetaCuerpo).append(", label %").append(etiquetaAltWhile).append("\n\n");
        codigo.append(etiquetaCuerpo).append(":\n");
        codigo.append(generarBloqueCodigo(principal.getCuerpo()));
        codigo.append("  br label %").append(etiquetaInicio).append("\n\n");

        if (tieneAltWhile) {
            List<Clausula> bloquesAltWhile = whileSentencia.getBloquesAltWhile();

            for (int i = 0; i < bloquesAltWhile.size(); i++) {
                Clausula alternativa = bloquesAltWhile.get(i);
                
                codigo.append(etiquetaAltWhile).append(":\n");
                codigo.append(generar(alternativa.getCondicion()));

                String etiquetaCuerpoAlternativa = AyudanteGeneradorCodigo.getNuevaEtiqueta();
                boolean esUltimaAlternativa = (i == bloquesAltWhile.size() - 1);
                String etiquetaSiguiente = esUltimaAlternativa ? etiquetaFin : AyudanteGeneradorCodigo.getNuevaEtiqueta();

                codigo.append("  br i1 ").append(alternativa.getCondicion().getIrReferencia()).append(", label %").append(etiquetaCuerpoAlternativa).append(", label %").append(etiquetaSiguiente).append("\n\n");
                codigo.append(etiquetaCuerpoAlternativa).append(":\n");
                codigo.append(generarBloqueCodigo(alternativa.getCuerpo()));
                codigo.append("  br label %").append(etiquetaInicio).append("\n\n");
            }
        }

        codigo.append(etiquetaFin).append(":\n");

        pilaBreak.pop();
        pilaContinue.pop();

        return codigo.toString();
    }

    // --- MÉTODOS AUXILIARES ---

    private String obtenerTipo(TipoDato tipoDato) {
        return switch (tipoDato) {
            case BOOLEAN -> "i1";
            case FLOAT -> "double";
            case INT -> "i32";
            case ARRAY -> "double*";
            case STRING -> "i8*";
            default -> throw new IllegalStateException("Tipo de dato desconocido.");
        };
    }

    private String generarConConversion(Expresion expresion, TipoDato tipoEsperado) {
        String codigoBase = generar(expresion);

        if (expresion.getTipoDato() == tipoEsperado) {
            return codigoBase;
        }

        if (tipoEsperado == TipoDato.FLOAT && expresion.getTipoDato() == TipoDato.INT) {
            String nuevoPuntero = AyudanteGeneradorCodigo.getNuevoPuntero();
            String instruccionConversion = "  " + nuevoPuntero + " = sitofp i32 " + expresion.getIrReferencia() + " to double\n";
            expresion.setIrReferencia(nuevoPuntero);
            return codigoBase + instruccionConversion;
        }

        throw new IllegalStateException("Conversión no soportada de " + expresion.getTipoDato() + " a " + tipoEsperado + ".");
    }

    private String generarBloqueCodigo(List<Sentencia> sentencias) {
        StringBuilder codigo = new StringBuilder();

        for (Sentencia sentencia : sentencias) {
            codigo.append(generar(sentencia));
        }

        return codigo.toString();
    }
}

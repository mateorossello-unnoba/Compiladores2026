package ar.edu.unnoba.ast;

import ar.edu.unnoba.Simbolo;
import ar.edu.unnoba.TablaSimbolos;

public class ValidadorSemantico {
    // --- VARIABLES Y ACCESO --- //

    // Verifica que una variable haya sido declarada antes de su uso y devuelve su tipo de dato
    public static TipoDato verificarExistenciaVariable(String id, TablaSimbolos tabla) throws Exception {
        Simbolo simbolo = tabla.obtenerSimbolo(id);

        if (simbolo == null) {
            throw new Exception("Error Semántico: La variable '" + id + "' no ha sido declarada antes de su uso.");
        }

        return simbolo.getTipoDato();
    }

    // Verifica que el acceso a un arreglo sea válido y devuelve el tipo de dato resultante
    public static TipoDato verificarAccesoArreglo(String nombreVariable, TipoDato tipoVariable, TipoDato tipoIndice) throws Exception {
        if (tipoVariable != TipoDato.ARRAY) {
            throw new Exception("Error Semántico: La variable '" + nombreVariable + "' no es de tipo ARRAY.");
        }

        if (tipoIndice != TipoDato.INT) {
            throw new Exception("Error Semántico: El índice del arreglo debe ser una expresión de tipo INT.");
        }

        return TipoDato.FLOAT;
    }

    // Verifica que el valor de un arreglo constante sea válido y devuelve su dimensión
    public static int calcularDimensionArregloConstante(String valorArreglo) {
        if (valorArreglo.equals("[]")) {
            return 0;
        }

        int comas = valorArreglo.length() - valorArreglo.replace(",", "").length();
        return comas + 1;
    }

    // --- ASIGNACIONES --- //

    // Verifica que una asignación sea válida considerando el tipo de dato de la variable, el tipo de dato del valor que se le quiere asignar y las dimensiones de ambos en caso de ser arreglos
    public static void verificarAsignacion(TipoDato tipoVariable, TipoDato tipoValor, int dimensionVariable, int dimensionValor) throws Exception {
        if (tipoVariable == TipoDato.BOOLEAN && tipoValor != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: A una variable BOOLEAN solo se le puede asignar un BOOLEAN.");
        }
        
        if (tipoVariable == TipoDato.INT && tipoValor != TipoDato.INT) {
            throw new Exception("Error Semántico: A una variable INT solo se le puede asignar un valor INT.");
        }
        
        if (tipoVariable == TipoDato.FLOAT && (tipoValor == TipoDato.BOOLEAN || tipoValor == TipoDato.ARRAY)) {
            throw new Exception("Error Semántico: A una variable FLOAT solo se le pueden asignar valores INT o FLOAT.");
        }

        if (tipoVariable == TipoDato.ARRAY) {
            if (tipoValor == TipoDato.BOOLEAN) {
                throw new Exception("Error Semántico: No se puede asignar un BOOLEAN a un ARRAY.");
            }

            if (tipoValor == TipoDato.ARRAY && dimensionVariable != dimensionValor) {
                throw new Exception("Error Semántico: No se puede asignar un arreglo de tamaño " + dimensionValor + " a uno de tamaño " + dimensionVariable + ".");
            }
        }
    }

    // Verifica que una asignación a una posición de un arreglo sea válida considerando el tipo de dato de la variable, el tipo de dato del índice y el tipo de dato del valor que se le quiere asignar
    public static void verificarAsignacionArreglo(String nombreVariable, TipoDato tipoVariable, TipoDato tipoIndice, TipoDato tipoValor) throws Exception {
        if (tipoVariable != TipoDato.ARRAY) {
            throw new Exception("Error Semántico: La variable '" + nombreVariable + "' no es un arreglo.");
        }

        if (tipoIndice != TipoDato.INT) {
            throw new Exception("Error Semántico: El índice del arreglo debe ser una expresión de tipo INT.");
        }

        if (tipoValor == TipoDato.BOOLEAN || tipoValor == TipoDato.ARRAY) {
            throw new Exception("Error Semántico: A una posición de un arreglo solo se le pueden asignar valores de tipo INT o FLOAT.");
        }
    }

    // --- OPERACIONES MATEMÁTICAS --- //

    // Verifica que una operación matemática binaria sea válida para los tipos de dato de sus operandos y devuelve el tipo de dato resultante
    public static TipoDato verificarOperacionMatematicaBinaria(TipoDato tipoIzquierda, TipoDato tipoDerecha, int tamañoIzquierda, int tamañoDerecha) throws Exception {
        if (tipoIzquierda == TipoDato.BOOLEAN || tipoDerecha == TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: No se pueden realizar operaciones aritméticas con valores de tipo BOOLEAN.");
        }

        if (tipoIzquierda == TipoDato.ARRAY && tipoDerecha == TipoDato.ARRAY) {
            if (tamañoIzquierda != tamañoDerecha) {
                throw new Exception("Error Semántico: No se pueden realizar operaciones aritméticas entre arreglos de tamaños diferentes (" + tamañoIzquierda + " y " + tamañoDerecha + ").");
            }

            return TipoDato.ARRAY;
        }

        if (tipoIzquierda == TipoDato.ARRAY || tipoDerecha == TipoDato.ARRAY) {
            return TipoDato.ARRAY;
        }

        if (tipoIzquierda == TipoDato.FLOAT || tipoDerecha == TipoDato.FLOAT) {
            return TipoDato.FLOAT;
        } else {
            return TipoDato.INT;
        }
    }
    
    // Verifica que una operación de menos unario sea válida para el tipo de dato de su operando y devuelve el tipo de dato resultante
    public static TipoDato verificarMenosUnario(TipoDato tipoOperando) throws Exception {
        if (tipoOperando == TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: El operador unario '-' no se puede aplicar a un valor de tipo BOOLEAN.");
        }

        return tipoOperando;
    }

    // --- OPERACIONES LÓGICAS Y RELACIONALES --- //

    // Verifica que una operación lógica binaria sea válida para los tipos de dato de sus operandos y devuelve el tipo de dato resultante
    public static TipoDato verificarOperacionLogicaBinaria(String operador, TipoDato tipoIzquierdo, TipoDato tipoDerecho) throws Exception {
        if (tipoIzquierdo != TipoDato.BOOLEAN || tipoDerecho != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: Los operadores lógicos (" + operador + ") requieren operandos de tipo BOOLEAN.");
        }
        
        return TipoDato.BOOLEAN;
    }

    // Verifica que una operación relacional binaria sea válida para los tipos de dato de sus operandos y devuelve el tipo de dato resultante
    public static TipoDato verificarOperacionRelacionalBinaria(String operador, TipoDato tipoIzquierdo, TipoDato tipoDerecho) throws Exception {
        if (tipoIzquierdo == TipoDato.BOOLEAN || tipoDerecho == TipoDato.BOOLEAN) {
            boolean ambosBooleanos = (tipoIzquierdo == TipoDato.BOOLEAN && tipoDerecho == TipoDato.BOOLEAN);
            boolean admiteBooleanos = operador.equals("==") || operador.equals("!=");

            if (!ambosBooleanos) {
                throw new Exception("Error Semántico: No se puede comparar (" + operador + ") un BOOLEAN con otro tipo.");
            }
            
            if (!admiteBooleanos) {
                throw new Exception("Error Semántico: No se pueden realizar comparaciones (" + operador + ") entre tipos BOOLEAN.");
            }
        }
        
        return TipoDato.BOOLEAN;
    }

    // Verifica que una operación de negación lógica sea válida para el tipo de dato de su operando y devuelve el tipo de dato resultante
    public static TipoDato verificarNegacionLogica(TipoDato tipoOperando) throws Exception {
        if (tipoOperando != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: El operador de negación '!' solo se puede aplicar a expresiones de tipo BOOLEAN.");
        }
        
        return TipoDato.BOOLEAN;
    }

    // --- FUNCIONES --- //

    // Verifica que una llamada a la función moda sea válida considerando el tipo de dato de su argumento y devuelve el tipo de dato resultante
    public static TipoDato verificarModa(TipoDato tipoArgumento) throws Exception {
        if (tipoArgumento != TipoDato.ARRAY) {
            throw new Exception("Error Semántico: La función moda solo admite como argumento un ARRAY.");
        }
        
        return TipoDato.FLOAT;
    }

    // --- ESTRUCTURAS DE CONTROL --- //

    // Verifica que la condición de una estructura de control sea de tipo BOOLEAN
    public static void verificarCondicionEstructuraControl(TipoDato tipoCondicion, String nombreEstructura) throws Exception {
        if (tipoCondicion != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: La condición de la sentencia " + nombreEstructura + " debe ser de tipo BOOLEAN.");
        }
    }

    // Verifica que una instrucción BREAK o CONTINUE esté dentro de un ciclo
    public static void verificarBreakContinue(int ciclosActivos, String instruccion) throws Exception {
        if (ciclosActivos <= 0) {
            throw new Exception("Error Semántico: La instrucción " + instruccion + " no puede usarse fuera de un ciclo.");
        }
    }
}

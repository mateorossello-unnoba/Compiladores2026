package unnoba;
import java_cup.runtime.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

%%

%public
%class Lexer
%unicode
%implements java_cup.runtime.Scanner
%type java_cup.runtime.Symbol
%line
%column

%{
    // ------------------------------------------------------------------
    //  Zona de código Java incrustado: variables auxiliares y métodos
    // ------------------------------------------------------------------

    // Buffer para construir cadenas literales
    StringBuffer cadena = new StringBuffer();
    int cadena_linea = 0;
    int cadena_columna = 0;

    // Estructuras para manejar indentación
    private Deque<Integer> indentStack = new ArrayDeque<>();
    private Queue<Symbol> pendingTokens = new java.util.LinkedList<>();
    private int pendingIndent = 0;

    // Inicialización de la pila de indentación
    {
        indentStack.push(0);
    }

    private ComplexSymbolFactory complexSymbolFactory = new ComplexSymbolFactory();

    // Métodos auxiliares para crear tokens usando ComplexSymbolFactory.Location
    private Symbol token(int cupId, String nombreConsola) {
        System.out.println("Léxico -> Reconocido: [" + nombreConsola + "]");
        return complexSymbolFactory.newSymbol(
            nombreConsola, 
            cupId, 
            new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + 1),
            new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + yylength())
        );
    }

    private Symbol token(int cupId, String nombreConsola, Object valor) {
        System.out.println("Léxico -> Reconocido: [" + nombreConsola + "] | Lexema: " + valor);
        return complexSymbolFactory.newSymbol(
            nombreConsola, 
            cupId, 
            new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + 1), 
            new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + yylength()), 
            valor
        );
    }

    private Symbol token(int cupId, String nombreConsola, int linea, int columna, Object valor) {
        System.out.println("Léxico -> Reconocido: [" + nombreConsola + "] | Lexema: " + valor);
        return complexSymbolFactory.newSymbol(
            nombreConsola, 
            cupId, 
            new ComplexSymbolFactory.Location(linea + 1, columna + 1), 
            new ComplexSymbolFactory.Location(linea + 1, columna + (valor != null ? valor.toString().length() : 1)), 
            valor
        );
    }

    // Método para procesar la indentación
    private void processIndent(int indent, int line, int col) {
        int top = indentStack.peek();

        if (indent > top) {
            indentStack.push(indent);
            pendingTokens.add(token(sym.INDENT, "INDENT"));
        } else if (indent < top) {
            while (indentStack.peek() > indent) {
                indentStack.pop();
                pendingTokens.add(token(sym.DEDENT, "DEDENT"));
            }
            if (indentStack.peek() != indent) {
                throw new RuntimeException("Indentación inconsistente en la línea " + (line + 1));
            }
        }
    }

    // Método para manejar la cola y el EOF
    public Symbol next_token() throws java.io.IOException {
        if (!pendingTokens.isEmpty()) {
            return pendingTokens.poll();
        }
        Symbol token = yylex();
        if (token == null) {
            processIndent(0, yyline, yycolumn); // Vacía todos los DEDENT pendientes
            token = pendingTokens.isEmpty() ? null : pendingTokens.poll();
        }
        return token;
    }
%}

// Inicialización en estado MEDICION
%init{
    yybegin(MEDICION);
%init}

// ------------------------------------------------------------------
//  Definición de macros (expresiones regulares reutilizables)
// ------------------------------------------------------------------
Terminacion = \r\n | \n | \r
Espacio = [ ] | \t | \f
Blanco = {Terminacion} | {Espacio}
LineaBlanco = {Espacio}*{Terminacion}

// Comentarios de una línea con %
ComentarioLinea = "%"[^\r\n]*{Terminacion}?

// Identificadores: comienzan con letra (incluye Unicode) y pueden contener letras, dígitos o _
Identificador = \p{L}(\p{L} | [0-9] | "_")*

// Definiciones numéricas
Digito = [0-9]
Entero = [1-9]{Digito}* | 0                     // Enteros sin ceros a la izquierda
Flotante = {Digito}+"."{Digito}* | "."{Digito}+ // Ej: 12.34, .123, 123.

FlotanteSigno = "-"? {Flotante}

Arreglo = \[{Blanco}*({FlotanteSigno}({Blanco}*,{Blanco}*{FlotanteSigno})*)?{Blanco}*\]

// Constantes booleanas
Booleano = "true" | "false"

// ------------------------------------------------------------------
//  Declaración de estados léxicos
// ------------------------------------------------------------------
%state MEDICION
%state NORMAL
%state CADENA
%state COMENTARIO_LINEA
%state COMENTARIO_MULTILINEA

%%

// ==================================================================
//  ESTADO MEDICION: al comienzo de cada línea
// ==================================================================

<MEDICION> {
    {LineaBlanco}   { /* Ignorar líneas completamente en blanco */ }
    
    " "             { pendingIndent++; }
    "\t"            { pendingIndent += 4; }

    // Comentario de línea: pasa al estado COMENTARIO_LINEA
    "%"             { pendingIndent = 0; yybegin(COMENTARIO_LINEA); }

    // Comentario multilínea: pasa al estado COMENTARIO_MULTILINEA
    "{*"            { pendingIndent = 0; yybegin(COMENTARIO_MULTILINEA); }

    [^ \t\r\n]      {
                        yypushback(1); 
                        yybegin(NORMAL);
                        processIndent(pendingIndent, yyline, yycolumn);
                        pendingIndent = 0;
                        if (!pendingTokens.isEmpty()) {
                            return pendingTokens.poll();
                        }
                    }
}

// ==================================================================
//  ESTADO COMENTARIO_LINEA: todo se ignora hasta el fin de línea
// ==================================================================

<COMENTARIO_LINEA> {
    \n      { yybegin(MEDICION); }

    [^\n]*  { /* Ignorar todo hasta el final de línea */ }
}

// ==================================================================
//  ESTADO NORMAL: análisis de tokens en el cuerpo del código
// ==================================================================

<NORMAL> {
    // ------------------------------------------------------------------
    //  Espacios y comentarios se ignoran
    // ------------------------------------------------------------------
    {Espacio}           { /* Ignorar */ }
    {ComentarioLinea}   { /* Ignorar */ }
    "{*"                { yybegin(COMENTARIO_MULTILINEA); }

    // ------------------------------------------------------------------
    //  Palabras reservadas
    // ------------------------------------------------------------------
    "PROGRAM"       { return token(sym.PROGRAM, "PROGRAM", yytext()); }
    "WHILE"         { return token(sym.WHILE, "WHILE", yytext()); }
    "ALT_WHILE"     { return token(sym.ALT_WHILE, "ALT_WHILE", yytext()); }
    "BREAK"         { return token(sym.BREAK, "BREAK", yytext()); }
    "CONTINUE"      { return token(sym.CONTINUE, "CONTINUE", yytext()); }
    "IF"            { return token(sym.IF, "IF", yytext()); }
    "ELIF"          { return token(sym.ELIF, "ELIF", yytext()); }
    "ELSE"          { return token(sym.ELSE, "ELSE", yytext()); }
    "PRINT"         { return token(sym.PRINT, "PRINT", yytext()); }
    "READ_INT"      { return token(sym.READ_INT, "READ_INT", yytext()); }
    "READ_FLOAT"    { return token(sym.READ_FLOAT, "READ_FLOAT", yytext()); }
    "READ_BOOL"     { return token(sym.READ_BOOL, "READ_BOOL", yytext()); }
    "INT"           { return token(sym.INT, "INT", yytext()); }
    "FLOAT"         { return token(sym.FLOAT, "FLOAT", yytext()); }
    "BOOLEAN"       { return token(sym.BOOLEAN, "BOOLEAN", yytext()); }
    "ARRAY"         { return token(sym.ARRAY, "ARRAY", yytext()); }
    "moda"          { return token(sym.MODA, "MODA", yytext()); }

    // ------------------------------------------------------------------
    //  Literales
    // ------------------------------------------------------------------
    {Entero}        { return token(sym.ENTERO, "ENTERO", yytext()); }
    {Flotante}      { return token(sym.FLOTANTE, "FLOTANTE", yytext()); }
    {Booleano}      { return token(sym.BOOLEANO, "BOOLEANO", yytext()); }
    {Arreglo}       { return token(sym.ARREGLO, "ARREGLO", yytext()); }
    {Identificador} { return token(sym.IDENTIFICADOR, "IDENTIFICADOR", yytext()); }

    // ------------------------------------------------------------------
    //  Operadores aritméticos
    // ------------------------------------------------------------------
    "+" { return token(sym.SUMA, "SUMA", yytext()); }
    "-" { return token(sym.RESTA, "RESTA", yytext()); }
    "*" { return token(sym.MULTIPLICACION, "MULTIPLICACION", yytext()); }
    "/" { return token(sym.DIVISION, "DIVISION", yytext()); }

    // ------------------------------------------------------------------
    //  Operadores relacionales y lógicos
    // ------------------------------------------------------------------
    "=="    { return token(sym.IGUAL, "IGUAL", yytext()); }
    "!="    { return token(sym.DESIGUAL, "DESIGUAL", yytext()); }
    ">="    { return token(sym.MAYOR_IGUAL, "MAYOR_IGUAL", yytext()); }
    "<="    { return token(sym.MENOR_IGUAL, "MENOR_IGUAL", yytext()); }
    "&&"    { return token(sym.CONJUNCION, "CONJUNCION", yytext()); }
    "||"    { return token(sym.DISYUNCION, "DISYUNCION", yytext()); }
    "="     { return token(sym.ASIGNACION, "ASIGNACION", yytext()); }
    ">"     { return token(sym.MAYOR, "MAYOR", yytext()); }
    "<"     { return token(sym.MENOR, "MENOR", yytext()); }
    "!"     { return token(sym.NEGACION, "NEGACION", yytext()); }

    // ------------------------------------------------------------------
    //  Signos de puntuación
    // ------------------------------------------------------------------
    "(" { return token(sym.PARENTESIS_IZQ, "PARENTESIS_IZQ", yytext()); }
    ")" { return token(sym.PARENTESIS_DER, "PARENTESIS_DER", yytext()); }
    "[" { return token(sym.CORCHETE_IZQ, "CORCHETE_IZQ", yytext()); }
    "]" { return token(sym.CORCHETE_DER, "CORCHETE_DER", yytext()); }
    "," { return token(sym.COMA, "COMA", yytext()); }
    ":" { return token(sym.DOS_PUNTOS, "DOS_PUNTOS", yytext()); } 

    // ------------------------------------------------------------------
    //  Cadenas de caracteres (entrada al estado CADENA)
    // ------------------------------------------------------------------
    \"  { cadena.setLength(0);
            cadena_linea = this.yyline;
            cadena_columna = this.yycolumn;
            yybegin(CADENA);
        }

    // ------------------------------------------------------------------
    //  Fin de línea: vuelve al estado MEDICION para medir la siguiente línea
    // ------------------------------------------------------------------
    {Terminacion}   { yybegin(MEDICION); }

    // ------------------------------------------------------------------
    //  Cualquier carácter no reconocido es un error léxico
    // ------------------------------------------------------------------
    [^] { throw new Error("Carácter inválido <" + yytext() + ">"); }
}

// ==================================================================
//  ESTADO CADENA: manejo de cadenas literales
// ==================================================================

<CADENA> {
    \"      { yybegin(NORMAL);
              return token(sym.CADENA, "CADENA", cadena_linea, cadena_columna, cadena.toString());
            }

    "\\n"   { cadena.append('\n'); }
    "\\t"   { cadena.append('\t'); }
    "\\\""  { cadena.append('\"'); }
    "\\\\"  { cadena.append('\\'); }

    <<EOF>> { throw new Error("Fin del archivo dentro de la cadena: \n" + cadena.toString()); }
    [^]     { cadena.append(yytext()); }
}

// ==================================================================
//  ESTADO COMENTARIO_MULTILINEA: todo se ignora hasta encontrar "*}"
// ==================================================================

<COMENTARIO_MULTILINEA> {
    "*}"    { yybegin(NORMAL); }

    <<EOF>> { throw new Error("Fin del archivo dentro del comentario multilínea."); }
    [^]     { /* Ignorar cualquier carácter */ }
}

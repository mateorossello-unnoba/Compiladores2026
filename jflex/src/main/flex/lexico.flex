package unnoba;

%%

%public
%class Lexer
%unicode
%type Token
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
    private java.util.Stack<Integer> pilaIndentacion = new java.util.Stack<>();
    private java.util.Queue<Token> pendingTokens = new java.util.LinkedList<>();
    private boolean primeraLinea = true;     // true antes de la primera línea con código
    private boolean eofProcesado = false;    // evita procesar EOF más de una vez

    // Métodos auxiliares para crear tokens con línea/columna actual
    private Token token(String nombre) {
        return new Token(nombre, this.yyline + 1, this.yycolumn + 1);
    }

    private Token token(String nombre, Object valor) {
        return new Token(nombre, this.yyline + 1, this.yycolumn + 1, valor);
    }

    private Token token(String nombre, int linea, int columna, Object valor) {
        return new Token(nombre, linea + 1, columna + 1, valor);
    }

    // Método público que el parser llama para obtener el siguiente token.
    // Implementa la cola de tokens pendientes y cierra bloques al final del archivo.
    public Token next_token() throws java.io.IOException {
        // Si hay tokens encolados, entregamos el primero
        if (!pendingTokens.isEmpty()) {
            return pendingTokens.poll();
        }
        // Pedimos el siguiente token al escáner (yylex)
        Token token = yylex();
        // Si ya no hay más tokens (EOF) y aún no procesamos el cierre de bloques...
        if (token == null && !eofProcesado) {
            eofProcesado = true;
            // Primero emitimos un NEWLINE para cerrar la última sentencia
            if (!primeraLinea) {
                pendingTokens.add(token("NEWLINE"));
            }
            // Emitimos un DEDENT por cada nivel de indentación aún abierto (excepto el nivel 0)
            while (pilaIndentacion.size() > 1) {
                pilaIndentacion.pop();
                pendingTokens.add(token("DEDENT"));
            }
            // Si después de encolar hay algo, lo entregamos
            if (!pendingTokens.isEmpty()) {
                return pendingTokens.poll();
            }
        }
        return token;
    }
%}

// Inicialización: pila con nivel 0 y comenzamos en estado MEDICION
%init{
    pilaIndentacion.push(0);
    yybegin(MEDICION);
%init}

// ------------------------------------------------------------------
//  Definición de macros (expresiones regulares reutilizables)
// ------------------------------------------------------------------

Terminacion = \r\n | \n | \r
Espacio = [ ] | \t | \f
Blanco = {Terminacion} | {Espacio}

// Comentarios de una línea: con % o con //
ComentarioLinea = "%"[^\r\n]*{Terminacion}?
ComentarioLineaBarra = "//"[^\r\n]*{Terminacion}?

// Identificadores: comienzan con letra (incluye Unicode) y pueden contener letras, dígitos o _
Identificador = \p{L}(\p{L} | [0-9] | "_")*

// Definiciones numéricas
Digito = [0-9]
EnteroSinCero = [1-9]{Digito}* | 0          // Enteros sin ceros a la izquierda
Entero = {Digito}+                           // Entero genérico (para constantes de arreglo)
Flotante = {Entero}"."{Digito}* | "."{Digito}+ | {Entero}"."   // Ej: 12.34, .123, 123.
Numero = -?({Flotante}|{Entero})             // Números con signo opcional (para arreglos)

// Constantes booleanas
Booleano = "true" | "false"

// Constante de arreglo: [ num, num, ... ]
ConstanteArreglo = \[{Blanco}*{Numero}({Blanco}*,{Blanco}*{Numero})*{Blanco}*\]

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
// ESTADO MEDICION: al comienzo de cada línea (después de un \n)
// ==================================================================

<MEDICION> {
    [ \t]   { /* Ignoramos espacios y tabulaciones; la columna se incrementa automáticamente */ }

    \n      { /* Línea en blanco (solo espacios y luego salto). No afecta indentación */ }

    // Comentario de línea: pasamos al estado COMENTARIO_LINEA
    "%"     { yybegin(COMENTARIO_LINEA); }
    "//"    { yybegin(COMENTARIO_LINEA); }

    // Comentario multilínea: pasamos al estado correspondiente
    "{*"    { yybegin(COMENTARIO_MULTILINEA); }

    // Cualquier otro carácter: primer carácter significativo de la línea
    [^]     {
                // La columna actual (yycolumn) es el nivel de indentación
                int nivel = this.yycolumn;
                int tope = pilaIndentacion.peek();   // nivel del bloque actual

                // Lista para almacenar los tokens que se generarán por este cambio de indentación
                java.util.List<Token> tokens = new java.util.ArrayList<>();

                // Caso 1: nivel mayor -> abre un nuevo bloque
                if (nivel > tope) {
                    tokens.add(token("INDENT"));
                    pilaIndentacion.push(nivel);
                }
                // Caso 2: nivel igual -> misma profundidad, solo se emite NEWLINE (si no es primera línea)
                else if (nivel == tope) {
                    if (!primeraLinea) {
                        tokens.add(token("NEWLINE"));
                    }
                }
                // Caso 3: nivel menor -> cierra uno o varios bloques
                else {
                    if (!primeraLinea) {
                        tokens.add(token("NEWLINE"));
                    }
                    // Desapilar mientras el tope sea mayor que el nivel actual
                    while (pilaIndentacion.peek() > nivel) {
                        pilaIndentacion.pop();
                        tokens.add(token("DEDENT"));
                    }
                    // Verificar consistencia: el nuevo tope debe ser exactamente el nivel medido
                    if (pilaIndentacion.peek() != nivel) {
                        throw new Error("Indentación inconsistente: nivel " + nivel +
                                        " no coincide con ningún bloque abierto");
                    }
                }
                primeraLinea = false;

                // Si se generaron tokens, encolamos todos menos el primero
                if (!tokens.isEmpty()) {
                    Token first = tokens.remove(0);
                    pendingTokens.addAll(tokens);
                    // Devolvemos el primer token ahora; el resto se entregarán en llamadas posteriores
                    yypushback(1);   // devolvemos el carácter leído para que se procese en NORMAL
                    yybegin(NORMAL);
                    return first;
                } else {
                    // No hay tokens de indentación (caso raro), solo pasamos a NORMAL
                    yypushback(1);
                    yybegin(NORMAL);
                }
              }
}

// ==================================================================
// ESTADO COMENTARIO_LINEA: ignoramos hasta el fin de línea
// ==================================================================

<COMENTARIO_LINEA> {
    \n      { yybegin(MEDICION); }
    [^\n]*  { /* Ignorar todo hasta el final de línea */ }
}

// ==================================================================
// ESTADO NORMAL: análisis de tokens en el cuerpo del código
// ==================================================================

<NORMAL> {
    // ------------------------------------------------------------------
    //  Blancos y comentarios (se ignoran)
    // ------------------------------------------------------------------
    {Blanco}                     { /* Ignorar */ }
    {ComentarioLinea}            { /* Ignorar */ }
    {ComentarioLineaBarra}       { /* Ignorar */ }

    // ------------------------------------------------------------------
    //  Palabras reservadas
    // ------------------------------------------------------------------
    "PROGRAM"    { return token("PROGRAM", yytext()); }
    "WHILE"      { return token("WHILE", yytext()); }
    "ALT_WHILE"  { return token("ALT_WHILE", yytext()); }
    "IF"         { return token("IF", yytext()); }
    "ELIF"       { return token("ELIF", yytext()); }
    "ELSE"       { return token("ELSE", yytext()); }
    "BREAK"      { return token("BREAK", yytext()); }
    "CONTINUE"   { return token("CONTINUE", yytext()); }
    "PRINT"      { return token("PRINT", yytext()); }
    "READ_INT"   { return token("READ_INT", yytext()); }
    "READ_FLOAT" { return token("READ_FLOAT", yytext()); }
    "READ_BOOL"  { return token("READ_BOOL", yytext()); }
    "INT"        { return token("INT", yytext()); }          // Tipo entero
    "FLOAT"      { return token("FLOAT", yytext()); }
    "BOOLEAN"    { return token("BOOLEAN", yytext()); }
    "ARRAY"      { return token("ARRAY", yytext()); }

    // ------------------------------------------------------------------
    //  Literales
    // ------------------------------------------------------------------
    {Booleano}         { return token("BOOLEANO", yytext()); }
    {ConstanteArreglo} { return token("CONSTANTE_ARREGLO", yytext()); }
    {Flotante}         { return token("FLOTANTE", yytext()); }
    {EnteroSinCero}    { return token("ENTERO", yytext()); }
    {Identificador}    { return token("IDENTIFICADOR", yytext()); }

    // ------------------------------------------------------------------
    //  Operadores aritméticos
    // ------------------------------------------------------------------
    "+"   { return token("SUMA", yytext()); }
    "-"   { return token("RESTA", yytext()); }
    "*"   { return token("MULTIPLICACION", yytext()); }
    "/"   { return token("DIVISION", yytext()); }

    // ------------------------------------------------------------------
    //  Operadores relacionales y lógicos
    // ------------------------------------------------------------------
    "=="  { return token("IGUAL", yytext()); }
    "!="  { return token("DESIGUAL", yytext()); }
    ">="  { return token("MAYOR_IGUAL", yytext()); }
    "<="  { return token("MENOR_IGUAL", yytext()); }
    "&&"  { return token("CONJUNCION", yytext()); }
    "||"  { return token("DISYUNCION", yytext()); }
    "="   { return token("ASIGNACION", yytext()); }
    ">"   { return token("MAYOR", yytext()); }
    "<"   { return token("MENOR", yytext()); }
    "!"   { return token("NEGACION", yytext()); }

    // ------------------------------------------------------------------
    //  Signos de puntuación
    // ------------------------------------------------------------------
    "("   { return token("PARENTESIS_IZQ", yytext()); }
    ")"   { return token("PARENTESIS_DER", yytext()); }
    "["   { return token("CORCHETE_IZQ", yytext()); }
    "]"   { return token("CORCHETE_DER", yytext()); }
    "{"   { return token("LLAVE_IZQ", yytext()); }
    "}"   { return token("LLAVE_DER", yytext()); }
    ","   { return token("COMA", yytext()); }
    "."   { return token("PUNTO", yytext()); }
    ":"   { return token("DOS_PUNTOS", yytext()); }    // Para declaraciones

    // ------------------------------------------------------------------
    //  Cadenas de caracteres (entrada al estado CADENA)
    // ------------------------------------------------------------------
    \"    { cadena.setLength(0);
            cadena_linea   = this.yyline;
            cadena_columna = this.yycolumn;
            yybegin(CADENA);
          }

    // ------------------------------------------------------------------
    //  Fin de línea: volvemos al estado MEDICION para medir la siguiente línea
    // ------------------------------------------------------------------
    \n    { yybegin(MEDICION); }

    // ------------------------------------------------------------------
    //  Cualquier carácter no reconocido -> error léxico
    // ------------------------------------------------------------------
    [^]   { throw new Error("Carácter inválido <" + yytext() + ">"); }
}

// ==================================================================
// ESTADO CADENA: manejo de cadenas literales
// ==================================================================

<CADENA> {
    \"      { yybegin(NORMAL);
              return token("CADENA", cadena_linea, cadena_columna, cadena.toString());
            }

    "\\n"   { cadena.append('\n'); }
    "\\t"   { cadena.append('\t'); }
    "\\\""  { cadena.append('\"'); }
    "\\\\"  { cadena.append('\\'); }

    <<EOF>> { throw new Error("Fin del archivo dentro de la cadena: \n" + cadena.toString()); }

    [^]     { cadena.append(yytext()); }
}

// ==================================================================
// ESTADO COMENTARIO_MULTILINEA: todo se ignora hasta encontrar "*}"
// ==================================================================

<COMENTARIO_MULTILINEA> {
    "*}"    { yybegin(MEDICION); }
    <<EOF>> { throw new Error("Fin del archivo dentro del comentario multilínea"); }
    [^]     { /* Ignorar cualquier carácter */ }
}
package unnoba;

%%

%public
%class Lexer
%unicode
%type Token
%line
%column

%{
    StringBuffer cadena = new StringBuffer();
    int cadena_linea = 0;
    int cadena_columna = 0;

    private Token token(String nombre) {
        return new Token(nombre, this.yyline + 1, this.yycolumn + 1);
    }

    private Token token(String nombre, Object valor) {
        return new Token(nombre, this.yyline + 1, this.yycolumn + 1, valor);
    }

    private Token token(String nombre, int linea, int columna, Object valor) {
        return new Token(nombre, linea + 1, columna + 1, valor);
    }

    public Token next_token() throws java.io.IOException {
        return yylex();
    }
%}

Terminacion = \r\n | \n | \r
Espacio = [ ] | \t | \f
Blanco = {Terminacion} | {Espacio}

ComentarioLinea = "%"[^\r\n]*{Terminacion}?
ComentarioLineaBarra = "//"[^\r\n]*{Terminacion}?

Identificador = \p{L}(\p{L} | [0-9] | "_")*

Digitos = [0-9]+
Booleano = "true" | "false"
Entero = 0 | [1-9][0-9]*
Flotante = {Entero}"."{Digitos}? | "."{Digitos}
Numero = -?({Flotante}|{Entero})
ConstanteArreglo = \[{Blanco}*{Numero}({Blanco}*,{Blanco}*{Numero})*{Blanco}*\]

%state CADENA
%state COMENTARIO_MULTILINEA

%%

<YYINITIAL> {
  /* Blancos y Comentarios */
  {Blanco}                  { /* Ignorar */ }
  {ComentarioLinea}         { /* Ignorar */ }
  {ComentarioLineaBarra}    { /* Ignorar */ }

  /* Palabras reservadas */
  "PROGRAM"     { return token("PROGRAM", yytext()); }
  "WHILE"       { return token("WHILE", yytext()); }
  "ALT_WHILE"   { return token("ALT_WHILE", yytext()); }
  "CONTINUE"    { return token("CONTINUE", yytext()); }
  "BREAK"       { return token("BREAK", yytext()); }
  "IF"          { return token("IF", yytext()); }
  "ELIF"        { return token("ELIF", yytext()); }
  "ELSE"        { return token("ELSE", yytext()); }
  "BOOLEAN"     { return token("BOOLEAN", yytext()); }
  "INTEGER"     { return token("INTEGER", yytext()); }
  "FLOAT"       { return token("FLOAT", yytext()); }
  "ARRAY"       { return token("ARRAY", yytext()); }
  "PRINT"       { return token("PRINT", yytext()); }
  "READ_INT"    { return token("READ_INT", yytext()); }
  "READ_FLOAT"  { return token("READ_FLOAT", yytext()); }
  "READ_BOOL"   { return token("READ_BOOL", yytext()); }
  "moda"        { return token("MODA", yytext()); }


  /* Literales */
  {Booleano}         { return token("BOOLEANO", yytext()); }
  {ConstanteArreglo} { return token("CONSTANTE_ARREGLO", yytext()); }
  {Flotante}         { return token("FLOTANTE", yytext()); }
  {Entero}           { return token("ENTERO", yytext()); }
  {Identificador}    { return token("IDENTIFICADOR", yytext()); }

  /* Operadores aritméticos */
  "+"   { return token("SUMA", yytext()); }
  "-"   { return token("RESTA", yytext()); }
  "*"   { return token("MULTIPLICACION", yytext()); }
  "/"   { return token("DIVISION", yytext()); }

  /* Operadores */
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

  /* Signos de puntuación */
  "("   { return token("PARENTESIS_IZQ", yytext()); }
  ")"   { return token("PARENTESIS_DER", yytext()); }
  "["   { return token("CORCHETE_IZQ", yytext()); }
  "]"   { return token("CORCHETE_DER", yytext()); }
  "{"   { return token("LLAVE_IZQ", yytext()); }
  "}"   { return token("LLAVE_DER", yytext()); }
  ","   { return token("COMA", yytext()); }
  "."   { return token("PUNTO", yytext()); }
  ":"   { return token("DOS_PUNTOS", yytext()); }

  /* Cadenas de caracteres */
  \"    { cadena.setLength(0);
          cadena_linea   = this.yyline;
          cadena_columna = this.yycolumn;
          yybegin(CADENA);
        }

  /* Comentario multilínea */
  "{*"  { yybegin(COMENTARIO_MULTILINEA); }

  /* Cualquier regla no definida */
  [^]   { throw new Error("Carácter inválido <" + yytext() + ">");
        }
}

<CADENA> {
  \"        { yybegin(YYINITIAL);
                return token("CADENA",
                cadena_linea, cadena_columna,
                cadena.toString());
            }

  "\\n"     { cadena.append('\n'); }
  "\\t"     { cadena.append('\t'); }
  "\\\""    { cadena.append('\"'); }
  "\\\\"    { cadena.append('\\'); }

  /* Fin del archivo */
  <<EOF>>   { throw new Error("Fin del archivo dentro de la cadena: \n" + cadena.toString()); }

  /* Cualquier otro caracter */
  [^]       { cadena.append(yytext()); }
}

<COMENTARIO_MULTILINEA> {
  "*}"      { yybegin(YYINITIAL); }

  /* Fin del archivo */
  <<EOF>>   { throw new Error("Fin del archivo dentro del comentario multilínea"); }

  /* Cualquier otro caracter */
  [^]       { /* Ignorar */ }

}

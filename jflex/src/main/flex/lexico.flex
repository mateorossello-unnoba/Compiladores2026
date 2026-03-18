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

Terminacion = \r | \n | \r\n
Espacio = [ ] | \t | \f
Blanco = {Terminacion} | {Espacio}

ComentarioLinea = "%"[^\r\n]*{Terminacion}?
ComentarioMultilinea = "{*"~"*}"

Identificador = \p{L}(\p{L} | [0-9] | "_")*

Booleano = "true" | "false"
Entero = 0 | [1-9][0-9]*
Flotante = {Entero}"."{Entero} | "."{Entero} | {Entero}"."

%state CADENA

%%

<YYINITIAL> {
  /* Blancos y Comentarios */
  {Blanco}                  { /* Ignorar */ }
  {ComentarioLinea}         { /* Ignorar */ }
  {ComentarioMultilinea}    { /* Ignorar */ }

  /* Corte y Palabras reservadas */
  "FIN"         { return token("FIN", yytext()); }
  "WHILE"       { return token("WHILE", yytext()); }
  "CONTINUE"    { return token("CONTINUE", yytext()); }
  "BREAK"       { return token("BREAK", yytext()); }

  /* Literales */
  {Booleano}        { return token("BOOLEANO", yytext()); }
  {Flotante}        { return token("FLOTANTE", yytext()); }
  {Entero}          { return token("ENTERO", yytext()); }
  {Identificador}   { return token("IDENTIFICADOR", yytext()); }

  /* Operadores */
  "=="  { return token("IGUAL", yytext()); }
  "!="  { return token("DESIGUAL", yytext()); }
  ">="  { return token("MAYOR_IGUAL", yytext()); }
  "<="  { return token("MENOR_IGUAL", yytext()); }
  "&&"  { return token("CONJUNCION", yytext()); }
  "||"  { return token("DISYUNCION", yytext()); }
  "+"   { return token("SUMA", yytext()); }
  "-"   { return token("RESTA", yytext()); }
  "*"   { return token("MULTIPLICACION", yytext()); }
  "/"   { return token("DIVISION", yytext()); }
  ">"   { return token("MAYOR", yytext()); }
  "<"   { return token("MENOR", yytext()); }
  "!"   { return token("NEGACION", yytext()); }

  \"    { cadena.setLength(0);
            cadena_linea   = this.yyline;
            cadena_columna = this.yycolumn;
            yybegin(CADENA);
        }

  /* Cualquier regla no definida */
  [^] { throw new Error("Carácter inválido <" + yytext() + ">"); }
}

<CADENA> {
  \"    { yybegin(YYINITIAL);
            return token("CADENA",
            cadena_linea, cadena_columna,
            cadena.toString());
        }

  "\\n"  { cadena.append('\n'); }
  "\\t"  { cadena.append('\t'); }
  "\\\"" { cadena.append('\"'); }
  "\\\\" { cadena.append('\\'); }

  /* Fin del archivo */
  <<EOF>>   { throw new Error("Fin del archivo dentro de la cadena: \n" + cadena.toString()); }

  /* Cualquier otro caracter */
  [^]   { cadena.append(yytext()); }
}

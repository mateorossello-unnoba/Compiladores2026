# Compilador - Analizador Léxico y Sintáctico (UNNOBA 2026)

Primera entrega del trabajo práctico de Compiladores.  
Implementa un analizador léxico (JFlex) y un analizador sintáctico (CUP) para el lenguaje definido en la consigna, con manejo de indentación significativa, tabla de símbolos e interfaz gráfica.

## Estructura del proyecto

```bash
compilador/
├── pom.xml # Configuración Maven (plugins JFlex y CUP)
├── libs/
│ ├── java-cup-11b-runtime.jar # Librería runtime de CUP
│ └── java-cup-11b.jar # Generador de parser (usado por Maven)
├── src/main/
│ ├── cup/parser.cup # Gramática (sintaxis)
│ ├── flex/lexico.flex # Reglas léxicas (incluye indentación)
│ └── java/unnoba/
│ ├── App.java # Punto de entrada (lanza la GUI)
│ ├── TablaSimbolos.java # Tabla de símbolos
│ └── ui/VentanaCompilador.java # Ventana principal (editor + consola)
└── target/ # Generado por Maven (Lexer.java, Parser.java, clases)
```

> **Importante**: Los archivos `Lexer.java`, `Parser.java` y `sym.java` se generan automáticamente durante la compilación con Maven. No deben editarse a mano.

## Prerrequisitos

- **Java 21** (LTS o superior)
- **Maven 3.6+**
- Sistema operativo: Windows, Linux o macOS (la interfaz usa Swing)

No es necesario instalar JFlex ni CUP por separado; los plugins de Maven los ejecutan automáticamente.

## Compilar y generar los analizadores

Abrir una terminal en la **raíz del proyecto** (donde está `pom.xml`) y ejecutar:

```bash
mvn clean compile
```

Este comando:
1. Ejecuta el plugin de JFlex → genera Lexer.java (en target/generated-sources/jflex/).
2. Ejecuta CUP → genera Parser.java y sym.java (en src/main/java/unnoba/).
3. Compila todas las clases Java.
4. Si modifica lexico.flex o parser.cup, debe repetir este paso.

## Ejecutar la aplicación

### Opción 1 – Usando Maven (más sencilla)

Desde la **carpeta `compilador`** (donde está el `pom.xml`):

```bash
mvn exec:java
```

### Opción 2 – Generar un JAR ejecutable

Desde la carpeta compilador:

```bash
mvn package
```

Luego, para ejecutar el JAR:

```bash
java -jar target/compilador-0.0.1.jar
```

Asegúrese de que libs/java-cup-11b-runtime.jar esté en el mismo directorio que el JAR, o ajuste el classpath.

## Uso de la interfaz gráfica

Al ejecutar se abre una ventana con dos áreas:
- Editor de código (superior): puede escribir o pegar código fuente.
- Consola de salida (inferior): muestra el proceso de análisis (reglas sintácticas aplicadas, errores).

Botones principales:

| Botón           | Acción                                                               |
|-----------------|----------------------------------------------------------------------|
| Cargar Archivo  | Abre un archivo .txt (ej. input.txt) y lo carga en el editor.        |
| Guardar Archivo | Guarda el contenido del editor en un archivo.                        |
| Limpiar Consola | Borra el contenido de la consola.                                    |
| Compilar Código | Ejecuta el analizador léxico + sintáctico sobre el código del editor.|

## ¿Qué ocurre al compilar?

- Se muestran en la consola todas las reglas gramaticales que va aplicando el parser.
- Si no hay errores léxicos ni sintácticos, se genera el archivo tablaSimbolos.txt en la raíz del proyecto.
- Si hay errores, se informan en la consola (con línea y columna) y no se genera la tabla.

## Archivo tablaSimbolos.txt

Formato de columnas: NOMBRE | TOKEN | TIPO | VALOR | LONGITUD
- Los identificadores declarados en la zona de declaraciones (INT: a, b) aparecen con TOKEN = ID y su tipo declarado.
- Las constantes de cadena ("...") se registran como CTE_STR con su contenido y longitud.
- Las variables no guardan su valor (solo tipo y nombre).

Ejemplo de salida:

| NOMBRE                         | TOKEN      | TIPO            | VALOR                                              | LONGITUD  |
|--------------------------------|------------|-----------------|----------------------------------------------------|-----------|
| iterador                       | ID         | INT             | -                                                  | -         |
| limite                         | ID         | INT             | -                                                  | -         |
| -                              | CTE_STR    | -               | Iniciando pruebas del compilador UNNOBA            | 39        |

## Lenguaje soportado

El analizador reconoce completamente la sintaxis definida en el anexo del enunciado:
- Tipos: INT, FLOAT, BOOLEAN, ARRAY[n]
- Declaraciones: TIPO: lista_de_variables
- Programa: delimitado por PROGRAM e indentación significativa.
- Sentencias: asignación (=), IF/ELIF/ELSE, WHILE/ALT_WHILE, BREAK, CONTINUE, PRINT, expresiones aritméticas, relacionales y lógicas.
- Lectura: READ_INT(), READ_FLOAT(), READ_BOOL() como factores.
- Comentarios: multilínea (* ... *) y de línea %.
- Tema especial del grupo: función moda() (reconocida sintácticamente).

## Solución de problemas comunes

| Problema	                                        | Posible solución                                                                                                                                            |
|---------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Lexer.java o Parser.java no se encuentran	        | Ejecute mvn clean compile para regenerarlos.                                                                                                                |
| package java_cup.runtime does not exist	        | Verifique que libs/java-cup-11b-runtime.jar exista y que el pom.xml lo incluya como dependencia del sistema.                                                |
| Error de indentación "Indentación inconsistente"	| No mezcle espacios y tabuladores. El lexer convierte un tabulador a 4 espacios. Revise que todas las líneas del bloque PROGRAM tengan la misma indentación. |
| No se genera tablaSimbolos.txt	                | Solo se genera si el análisis termina sin errores fatales. Revise la consola en busca de errores léxicos o sintácticos.                                     |
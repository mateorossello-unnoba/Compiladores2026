# Compilador - Segunda Entrega (UNNOBA 2026)

Este proyecto es la segunda entrega del trabajo práctico de Compiladores. Implementa un compilador completo para el lenguaje definido en el enunciado, incluyendo:
- Análisis léxico (JFlex) con manejo de indentación significativa.
- Análisis sintáctico (CUP) con recuperación de errores.
- Tabla de símbolos (ts.txt) con tipos y dimensiones de arreglos.
- Árbol de sintaxis abstracta (AST) con generación de archivos .dot y .png (Graphviz).
- Generación de código intermedio LLVM IR (programa.ll).
- Compilación y ejecución del programa generado (mediante clang).
- Interfaz gráfica con editor, consola y menú para mostrar el código IR.

## Estructura del proyecto

```bash
/ (Raíz del proyecto)
├── pom.xml                          # Configuración Maven (plugins JFlex, CUP)
├── libs/
│   ├── java-cup-11b-runtime.jar     # Runtime de CUP
│   └── java-cup-11b.jar             # Generador de parser
├── src/main/
│   ├── cup/parser.cup               # Gramática (sintaxis + acciones semánticas)
│   ├── flex/lexico.flex             # Reglas léxicas (incluye indentación)
│   └── java/ar/edu/unnoba/
│       ├── App.java                 # Punto de entrada (lanza GUI)
│       ├── TablaSimbolos.java       # Tabla de símbolos
│       ├── ast/                     # Nodos del AST (expresiones, sentencias, etc.)
│       ├── llvm/                    # Generador de código LLVM
│       └── ui/VentanaCompilador.java # Ventana principal
└── target/                          # Generado por Maven (Lexer, Parser, sym, clases)
```

> **Importante**: Los archivos `Lexer.java`, `Parser.java` y `sym.java` se generan automáticamente durante la compilación con Maven. No deben editarse a mano.

## Prerrequisitos

- **Java 21** (LTS o superior)
- **Maven 3.6+**
- **Graphviz** (para generar ast.png desde ast.dot)
  - Windows: Instalar desde graphviz.org y agregar dot al PATH.
  - Linux: `sudo apt install graphviz`
  - macOS: `brew install graphviz`
- **LLVM/Clang** (para compilar `programa.ll` a ejecutable)
  - El compilador invoca `clang` mediante **WSL** (Windows Subsystem for Linux). Por lo tanto, no es necesario instalar LLVM nativamente en Windows.
  - Asegúrese de tener WSL habilitado y una distribución Linux (por ejemplo, Ubuntu) instalada.
  - Dentro de WSL, instale `clang` y `lld`:

```bash
sudo apt update
sudo sudo apt install clang -y
```

Para verificar, ejecute `wsl clang --version` en una terminal de Windows.

## Compilar y generar los analizadores

Abrir una terminal en la **raíz del proyecto** (donde está `pom.xml`) y ejecutar:

```bash
mvn clean compile
```

Este comando:

1. Ejecuta el plugin de JFlex → genera `Lexer.java` (en `target/generated-sources/jflex/`).
2. Ejecuta CUP → genera `Parser.java` y `sym.java` (en `src/main/java/ar/edu/unnoba/`).
3. Compila todas las clases Java.
4. Si modifica `lexico.flex` o `parser.cup`, debe repetir este paso.

## Ejecutar la aplicación

### Opción 1 – Usando Maven (más sencilla)

Abrir una terminal en la **raíz del proyecto** (donde está el `pom.xml`) y ejecutar:

```bash
mvn exec:java
```

### Opción 2 – Generar un JAR ejecutable

Desde la misma ruta:

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

| Botón                           | Acción                                                                                                                     |
| ------------------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| Cargar Archivo                  | Abre un archivo .txt (ej. input.txt) y lo carga en el editor.                                                              |
| Guardar Archivo                 | Guarda el contenido del editor en un archivo.                                                                              |
| Limpiar Consola                 | Borra el contenido de la consola.                                                                                          |
| Análisis Léxico                 | Ejecuta únicamente el escáner (JFlex) y lista los tokens.                                                                  |
| Análisis Sintáctico y Semántico | Ejecuta el parser completo, valida la gramática, construye el AST, genera ast.dot/ast.png y la tabla de símbolos (ts.txt). |
| Generar Código LLVM             | Genera el archivo programa.ll con el código intermedio, sin ejecutar el programa.                                          |
| Compilar y Ejecutar             | Realiza todo el proceso anterior y además ejecuta el programa generado, mostrando su salida en la consola.                 |

El código LLVM generado se puede ver en la segunda pestaña (“Código LLVM”) en cualquier momento después de presionar Generar Código LLVM o Compilar y Ejecutar.

## Archivos generados por el compilador

| Archivo                       | Descripción                                                              |
| ----------------------------- | ------------------------------------------------------------------------ |
| tablaSimbolos.txt             | Tabla de símbolos con columnas: NOMBRE | TOKEN | TIPO | VALOR | LONGITUD |
| ast.dot                       | Definición del AST en formato DOT (Graphviz).                            |
| ast.png                       | Imagen del AST generada automáticamente (requiere Graphviz).             |
| programa.ll                   | Código intermedio en lenguaje LLVM IR.                                   |
| programa                      | Ejecutable nativo generado por clang a partir del IR.                    |

## Lenguaje soportado

El compilador implementa todos los aspectos del enunciado:
- Tipos: INT, FLOAT, BOOLEAN, ARRAY[n] (arreglos de floats).
- Declaraciones: TIPO: var1, var2, ... (zona previa al PROGRAM).
- Programa: bloque indentado después de PROGRAM.
- Sentencias: asignación simple (=), asignación a posición de arreglo ([índice] =), IF/ELIF/ELSE, WHILE/ALT_WHILE, BREAK, CONTINUE, PRINT.
- Expresiones: aritméticas (+, -, *, /, - unario), relacionales (==, !=, <, <=, >, >=), lógicas (&&, ||, !).
- Entrada/Salida: READ_INT(), READ_FLOAT(), READ_BOOL(), PRINT (cadenas, números, booleanos, arreglos).
- Arreglos:
  - Literales: [1.5, -2.3, 7.0]
  - Acceso indexado: miArray[expresion] (con verificación de límites en tiempo de ejecución).
  - Broadcast: asignación de escalar a todo el arreglo u operaciones aritméticas/comparaciones escalar con arreglo.
- Comentarios: multilínea (* ... *) y de línea %.
- Tema especial (grupo): función moda(lista) que devuelve el valor que más se repite (float). Si la lista está vacía, imprime “La lista está vacía” y retorna -1.0.

## Solución de problemas comunes

| Problema                                            | Posible solución                                                                                                                                            |
| --------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Lexer.java o Parser.java no se encuentran           | Ejecute mvn clean compile para regenerarlos.                                                                                                                |
| package java_cup.runtime does not exist             | Verifique que libs/java-cup-11b-runtime.jar exista y que el pom.xml lo incluya como dependencia del sistema.                                                |
| Error de indentación "Indentación inconsistente"    | No mezcle espacios y tabuladores. El lexer convierte un tabulador a 4 espacios. Revise que todas las líneas del bloque PROGRAM tengan la misma indentación. |
| No se genera tablaSimbolos.txt                      | Solo se genera si el análisis termina sin errores fatales. Revise la consola en busca de errores léxicos o sintácticos.                                     |
| No se genera ast.png                                | Graphviz no está instalado o dot no está en el PATH. Instalar Graphviz y verificar con dot -V. El archivo ast.dot igual se genera.                          |
| clang no se encuentra al compilar el programa       | Instalar LLVM/Clang y agregarlo al PATH. En Windows, asegurar que la opción “Agregar a PATH” esté marcada durante la instalación.                           |
| El programa ejecutable no corre o no muestra salida | En Windows, verificar que scanf.o esté en el directorio de trabajo. El compilador lo linkea automáticamente.                                                |

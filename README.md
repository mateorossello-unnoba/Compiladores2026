# Compilador - Analizador Léxico y Sintáctico (UNNOBA)

Este proyecto implementa la primera y segunda fase de un compilador (Análisis Léxico y Sintáctico) utilizando **JFlex**, **JCup** y **Java**, gestionado de manera automatizada a través de **Maven** y bajo los lineamientos solicitados en las consignas del trabajo práctico correspondiente.

## Arquitectura y Configuración

El proyecto utiliza la configuración automatizada del plugin de JFlex y JCup para Maven. **No se utiliza una clase manual que genere el Lexer.java** ni archivos de JCup.

En su lugar, el ciclo de vida de Maven se encarga de leer el archivo de reglas léxicas y generar el código fuente de Java de forma transparente antes de compilar el proyecto.

- **Reglas léxicas:** Se encuentran en `compilador/src/main/flex/lexico.flex`
- **Reglas sintácticas:** Se encuentran en `compilador/src/main/cup/parser.cup`
- **Punto de entrada:** La aplicación se ejecuta desde `compilador/src/main/java/unnoba/App.java`

## Compilar y Ejecutar

Cada vez que se modifique el archivo `lexico.flex` o `parser.cup`, se debe abrir un terminal en la carpeta raíz del proyecto (donde se encuentra el archivo `pom.xml`) y ejecutar los siguientes comandos de Maven:

```bash
mvn clean compile
mvn exec:java
```

# Compilador - Analizador Léxico (UNNOBA)

Este proyecto implementa la primera fase de un compilador (Análisis Léxico) utilizando **JFlex** y **Java**, gestionado de manera automatizada a través de **Maven** y bajo los lineamientos solicitados en las consignas del trabajo práctico correspondiente.

## Arquitectura y Configuración

El proyecto utiliza la configuración automatizada del plugin de JFlex para Maven, como recomienda el creador. **No se utiliza una clase manual que genere el Lexer.java**. 

En su lugar, el ciclo de vida de Maven se encarga de leer el archivo de reglas léxicas y generar el código fuente de Java de forma transparente antes de compilar el proyecto.

* **Reglas léxicas:** Se encuentran en `jflex/src/main/flex/lexico.flex`
* **Punto de entrada:** La aplicación se ejecuta desde `jflex/src/main/java/unnoba/App.java`
* **Token:** Estructurado mediante la clase que se encuentra en `jflex/src/main/java/unnoba/Token.java`

## Compilar y Ejecutar

Cada vez que se modifiquen las expresiones regulares o se agreguen nuevos tokens en el archivo `lexico.flex`, se debe abrir un terminal en la carpeta raíz del proyecto (donde se encuentra el archivo `pom.xml`) y ejecutar el siguiente comando de Maven:

```bash
mvn clean compile
mvn exec:java

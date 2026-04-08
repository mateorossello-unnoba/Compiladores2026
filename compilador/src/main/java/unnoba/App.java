package unnoba;

import java.io.FileReader;
import java.util.Scanner;

/**
 * Punto de entrada principal del compilador.
 * Permite ejecutar el analizador léxico y sintáctico leyendo desde consola o desde un archivo de prueba.
 */

public class App {
    private static final String ARCHIVO = "./compilador/src/input.txt";

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        System.out.println("=== Compilador UNNOBA ===");
        System.out.println("¿Desde dónde desea leer?");
        System.out.println("1 - Desde consola");
        System.out.println("2 - Desde archivo (" + ARCHIVO + ")");
        System.out.print("Ingrese su opción: ");

        String opcion = teclado.nextLine().trim();
        Lexer lexico;

        try {
            if (opcion.equals("1")) {
                System.out.println("\nModo consola. Ingrese expresiones línea a línea.");
                System.out.println("Escriba FIN para terminar.\n");
                StringBuilder codigoConsola = new StringBuilder();

                while (true) {
                    String linea = teclado.nextLine();
                    if (linea.trim().equals("FIN")) {
                        break;
                    }
                    codigoConsola.append(linea).append("\n");
                }

                lexico = new Lexer(new java.io.StringReader(codigoConsola.toString()));
            } else if (opcion.equals("2")) {
                System.out.println("\nLeyendo desde: " + ARCHIVO + "\n");
                lexico = new Lexer(new FileReader(ARCHIVO));
            } else {
                System.out.println("Opción inválida. Saliendo del programa.");
                return;
            }

            Parser parser = new Parser(lexico, new java_cup.runtime.ComplexSymbolFactory());

            parser.parse();
            
            System.out.println("\n=== Proceso finalizado con éxito ===");
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
        } finally {
            teclado.close();
        }
    }
}

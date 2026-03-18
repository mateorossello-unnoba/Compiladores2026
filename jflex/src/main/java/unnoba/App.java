package unnoba;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Punto de entrada principal del compilador.
 * Permite ejecutar el analizador léxico leyendo desde consola o desde un archivo de prueba.
 */

public class App {
    private static final String ARCHIVO = "./src/input.txt";

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        System.out.println("=== Analizador Léxico ===");
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
                lexico = new Lexer(new InputStreamReader(System.in));
            } else if (opcion.equals("2")) {
                System.out.println("\nLeyendo desde: " + ARCHIVO + "\n");
                lexico = new Lexer(new FileReader(ARCHIVO));
            } else {
                System.out.println("Opción inválida. Saliendo.");
                return;
            }

            Token token;

            while ((token = lexico.next_token()) != null) {
                System.out.println("Token: " + token);

                if (token.nombre.equals("FIN")) {
                    System.out.println("\nToken FIN recibido. Terminando análisis.");
                    break;
                }
            }
            
            System.out.println("Análisis léxico terminado.");
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        finally {
            teclado.close();
        }
    }
}

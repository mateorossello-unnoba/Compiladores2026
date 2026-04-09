package unnoba;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TablaSimbolos {
    private static class EntradaTablaSimbolos {
        String nombre, token, tipo, valor, longitud;

        EntradaTablaSimbolos(String nombre, String token, String tipo, String valor, String longitud) {
            this.nombre = nombre != null ? nombre : "-";
            this.token = token;
            this.tipo = tipo != null ? tipo : "-";
            this.valor = valor != null ? valor : "-";
            this.longitud = longitud != null ? longitud : "-";
        }
    }

    private List<EntradaTablaSimbolos> tabla = new ArrayList<>();

    public void agregarVariable(String nombre, String tipo) {
        if (tabla.stream().noneMatch(entrada -> entrada.nombre.equals(nombre))) {
            tabla.add(new EntradaTablaSimbolos(nombre, "ID", tipo, "-", "-"));
        }
    }

    public void agregarString(String contenido) {
        String longitud = String.valueOf(contenido.length());
        tabla.add(new EntradaTablaSimbolos("-", "CTE_STR", "-", contenido, longitud));
    }

    public void generarArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            String formato = "%-30s | %-10s | %-15s | %-50s | %-10s%n";
            writer.printf(formato, "NOMBRE", "TOKEN", "TIPO", "VALOR", "LONGITUD");
            writer.println("-".repeat(127));

            for (EntradaTablaSimbolos entradaTablaSimbolos : tabla) {
                writer.printf(formato, entradaTablaSimbolos.nombre, entradaTablaSimbolos.token, entradaTablaSimbolos.tipo, entradaTablaSimbolos.valor, entradaTablaSimbolos.longitud);
            }

            System.out.println("[SISTEMA] Tabla de Símbolos generada según el formato requerido");
        } catch (IOException exception) {
            System.err.println("[ERROR] No se pudo generar la tabla: " + exception.getMessage());
        }
    }
}

package ar.edu.unnoba;

import ar.edu.unnoba.ast.TipoDato;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TablaSimbolos {
    private static class EntradaTablaSimbolos {
        String nombre, token, tipo, valor, longitud;
        Simbolo simbolo;

        EntradaTablaSimbolos(String nombre, String token, String tipo, String valor, String longitud, Simbolo simbolo) {
            this.nombre = nombre != null ? nombre : "-";
            this.token = token;
            this.tipo = tipo != null ? tipo : "-";
            this.valor = valor != null ? valor : "-";
            this.longitud = longitud != null ? longitud : "-";
            this.simbolo = simbolo;
        }
    }

    private List<EntradaTablaSimbolos> tabla = new ArrayList<>();

    public void agregarVariable(String nombre, Simbolo simbolo) {
        if (tabla.stream().noneMatch(entrada -> entrada.nombre.equals(nombre))) {
            String tipoString = (simbolo.getTipoDato() == TipoDato.ARRAY) ? "ARRAY[" + simbolo.getDimensionArreglo() + "]" : simbolo.getTipoDato().name();
            tabla.add(new EntradaTablaSimbolos(nombre, "ID", tipoString, "-", "-", simbolo));
        }
    }

    public void agregarString(String contenido) {
        String longitud = String.valueOf(contenido.length());
        tabla.add(new EntradaTablaSimbolos("-", "CTE_STR", "-", contenido, longitud, null));
    }

    public void generarArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            String formato = "%-30s | %-10s | %-15s | %-50s | %-10s%n";
            writer.printf(formato, "NOMBRE", "TOKEN", "TIPO", "VALOR", "LONGITUD");
            writer.println("-".repeat(127));

            for (EntradaTablaSimbolos entradaTablaSimbolos : tabla) {
                writer.printf(formato, entradaTablaSimbolos.nombre, entradaTablaSimbolos.token, entradaTablaSimbolos.tipo, entradaTablaSimbolos.valor, entradaTablaSimbolos.longitud);
            }

            System.out.println("[SISTEMA] Tabla de Símbolos generada según el formato requerido.");
        } catch (IOException exception) {
            System.err.println("[ERROR] No se pudo generar la tabla: " + exception.getMessage());
        }
    }

    public boolean existeVariable(String nombre) {
        return tabla.stream().anyMatch(entrada -> entrada.nombre.equals(nombre));
    }

    public Simbolo obtenerSimbolo(String nombre) {
        return tabla.stream().filter(entrada -> entrada.nombre.equals(nombre)).findFirst().map(entrada -> entrada.simbolo).orElse(null);
    }
}

package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;

public class Identificador extends Expresion {
    private String nombre;

    public Identificador(String nombre) {
        super("IDENTIFICADOR: " + nombre);
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;

public class Identificador extends Expresion {
    private String nombre;

    public Identificador(String nombre) {
        super(nombre, null);
        this.nombre = nombre;
    }

    public Identificador(String nombre, TipoDato tipoDato, int dimensionArreglo) {
        super(nombre, tipoDato);
        this.nombre = nombre;
        this.dimensionArreglo = dimensionArreglo;
    }

    public String getNombre() {
        return nombre;
    }
}

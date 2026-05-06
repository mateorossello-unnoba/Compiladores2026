package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.Simbolo;
import ar.edu.unnoba.TablaSimbolos;

public class Identificador extends Expresion {
    private String nombre;

    public Identificador(String nombre) {
        super(nombre);
        this.nombre = nombre;
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        if (!tablaSimbolos.existeVariable(nombre)) {
            throw new Exception("Error Semántico: La variable '" + nombre + "' no ha sido declarada.");
        }
        
        Simbolo simbolo = tablaSimbolos.obtenerSimbolo(nombre);

        this.tipoDato = simbolo.getTipoDato();
        this.dimensionArreglo = simbolo.getDimensionArreglo();
    }

    public String getNombre() {
        return nombre;
    }
}

package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

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

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // Si el identificador es un arreglo, se devuelve la referencia al arreglo en lugar de cargar su valor
        if (this.tipoDato == TipoDato.ARRAY) {
            this.setIrReferencia("%" + this.getNombre());
            return "";
        }

        // Obtener un nuevo puntero para cargar el valor de la variable
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);

        String tipo = generadorCodigo.obtenerTipo(this.getTipoDato());

        // Código ejemplo:
        // %puntero.1 = load i32, i32* @variable
        return "  " + puntero + " = load " + tipo + ", " + tipo + "* %" + this.getNombre() + "\n";
    }

    public String getNombre() {
        return nombre;
    }
}

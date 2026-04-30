package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;

public class Moda extends Expresion {
    private final Expresion arreglo;

    public Moda(Expresion arreglo) {
        super("MODA");
        this.arreglo = arreglo;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + arreglo.graficar(miId);
    }
}

package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;

public class Constante extends Expresion {
    private String valorString;

    public Constante(String valorString) {
        super("CONSTANTE: " + valorString.replace("\"", "\\\""));
        this.valorString = valorString;
    }

    public String getValorString() {
        return valorString;
    }
}

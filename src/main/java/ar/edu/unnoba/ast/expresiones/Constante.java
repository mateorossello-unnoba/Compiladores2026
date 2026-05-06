package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class Constante extends Expresion {
    private String valor;

    public Constante(String valorString) {
        super(valorString.replace("\"", "\\\""));
        this.valor = valorString;
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        if (valor.equals("true") || valor.equals("false")) {
            this.tipoDato = TipoDato.BOOLEAN;
        } else if (valor.startsWith("[")) {
            this.tipoDato = TipoDato.ARRAY;
            int comas = valor.length() - valor.replace(",", "").length();
            this.dimensionArreglo = valor.equals("[]") ? 0 : comas + 1;
        } else if (valor.contains(".")) {
            this.tipoDato = TipoDato.FLOAT;
        } else if (valor.startsWith("\"")) {
            this.tipoDato = null;
        } else {
            this.tipoDato = TipoDato.INT;
        }
    }

    public String getValor() {
        return valor;
    }
}

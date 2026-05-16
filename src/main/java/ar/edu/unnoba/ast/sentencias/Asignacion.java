package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Sentencia;

public class Asignacion extends Sentencia {
    private final Identificador variable;
    private final Expresion valor;

    public Asignacion(Identificador variable, Expresion valor) {
        super("=");
        this.variable = variable;
        this.valor = valor;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + variable.graficar(miId) + valor.graficar(miId);
    }

    public Identificador getVariable() {
        return variable;
    }

    public Expresion getValor() {
        return valor;
    }
}

package ar.edu.unnoba.ast.declaraciones;

import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Nodo;
import java.util.List;

public class Declaracion extends Nodo {
    private final String tipo;
    private final List<Identificador> variables;

    public Declaracion(String tipo, List<Identificador> variables) {
        super ("DECLARACIÓN: " + tipo);
        this.tipo = tipo;
        this.variables = variables;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder(super.graficar(idPadre));

        for (Identificador identificador : variables) {
            resultado.append(identificador.graficar(miId));
        }

        return resultado.toString();
    }

    public String getTipo() {
        return tipo;
    }

    public List<Identificador> getVariables() {
        return variables;
    }
}

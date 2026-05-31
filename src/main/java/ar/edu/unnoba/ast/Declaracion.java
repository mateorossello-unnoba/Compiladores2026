package ar.edu.unnoba.ast;

import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.llvm.GeneradorCodigo;
import ar.edu.unnoba.Simbolo;
import java.util.List;

public class Declaracion extends Nodo {
    private final Simbolo simbolo;
    private final List<Identificador> variables;

    public Declaracion(Simbolo simbolo, List<Identificador> variables) {
        super(simbolo.getTipoDato().toString());
        this.simbolo = simbolo;
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

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // La reservación de espacio para las variables se ejecuta en el nodo Programa, por lo que no es necesario generar código aquí.
        return "";
    }

    public Simbolo getSimbolo() {
        return simbolo;
    }

    public List<Identificador> getVariables() {
        return variables;
    }
}

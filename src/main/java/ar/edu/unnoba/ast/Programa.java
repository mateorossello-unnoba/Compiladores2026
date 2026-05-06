package ar.edu.unnoba.ast;

import ar.edu.unnoba.TablaSimbolos;
import java.util.List;

public class Programa extends Nodo {
    private final List<Declaracion> declaraciones;
    private final List<Sentencia> sentencias;

    public Programa(List<Declaracion> declaraciones, List<Sentencia> sentencias) {
        super("PROGRAM");
        this.declaraciones = declaraciones;
        this.sentencias = sentencias;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder();

        resultado.append(super.graficar(idPadre));
        
        for (Declaracion declaracion : declaraciones) {
            resultado.append(declaracion.graficar(miId));
        }
        
        for (Sentencia sentencia : sentencias) {
            resultado.append(sentencia.graficar(miId));
        }
        
        return resultado.toString();
    }

    public void chequearSemantica(TablaSimbolos tablaSimbolos) throws Exception {
        for (Sentencia sentencia : sentencias) {
            sentencia.chequearSemantica(tablaSimbolos, false);
        }
    }
}

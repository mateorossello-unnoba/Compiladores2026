package ar.edu.unnoba.ast;

import ar.edu.unnoba.ast.declaraciones.Declaracion;
import java.util.List;

public class Programa extends Nodo {
    private final List<Declaracion> declaraciones;
    private final List<Sentencia> sentencias;

    public Programa(List<Declaracion> declaraciones, List<Sentencia> sentencias) {
        super("PROGRAM");
        this.declaraciones = declaraciones;
        this.sentencias = sentencias;
    }

    public String graficarArbol() {
        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {\n");
        
        resultado.append(this.graficar(null)); 
        String miId = this.getId();
        
        for (Declaracion declaracion : declaraciones) {
            resultado.append(declaracion.graficar(miId));
        }
        
        for (Sentencia sentencia : sentencias) {
            resultado.append(sentencia.graficar(miId));
        }
        
        resultado.append("}\n");
        return resultado.toString();
    }
}

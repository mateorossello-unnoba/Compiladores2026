package ar.edu.unnoba.ast;

import java.util.List;

public class Clausula extends Nodo {
    public final Expresion condicion;
    public final List<Sentencia> cuerpo;

    public Clausula(String etiqueta, Expresion condicion, List<Sentencia> cuerpo) {
        super(etiqueta);
        this.condicion = condicion;
        this.cuerpo = cuerpo;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder(super.graficar(idPadre));

        resultado.append(condicion.graficar(miId));

        for (Sentencia sentencia : cuerpo) {
            resultado.append(sentencia.graficar(miId));
        }

        return resultado.toString();
    }

    public Expresion getCondicion() {
        return condicion;
    }
    
    public List<Sentencia> getCuerpo() {
        return cuerpo;
    }
}

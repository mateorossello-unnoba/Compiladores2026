package ar.edu.unnoba.ast;

import java.util.List;

public abstract class Nodo {
    private String etiqueta;

    public Nodo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String graficar(String idPadre) {
        StringBuilder grafico = new StringBuilder();
        grafico.append(String.format("%1$s[label=\"%2$s\"]\n", this.getId(), this.getEtiqueta()));
        
        if(idPadre != null && !idPadre.isEmpty()) {
            grafico.append(String.format("%1$s--%2$s\n", idPadre, this.getId()));
        }

        return grafico.toString();
    }

    protected String graficarBloqueVirtual(String etiquetaBloque, List<? extends Nodo> elementos, String idPadre) {
        if (elementos == null || elementos.isEmpty()) return "";

        StringBuilder grafico = new StringBuilder();
        String idBloque = "V" + System.identityHashCode(elementos);

        grafico.append(String.format("%1$s[label=\"%2$s\"]\n", idBloque, etiquetaBloque));
        
        if(idPadre != null && !idPadre.isEmpty()) {
            grafico.append(String.format("%1$s -- %2$s\n", idPadre, idBloque));
        }
        
        for (Nodo elemento : elementos) {
            grafico.append(elemento.graficar(idBloque));
        }

        return grafico.toString();
    }

    protected String getId() {
        return "N" + System.identityHashCode(this);
    }

    protected String getEtiqueta() {
        if (this.etiqueta != null) {
            return this.etiqueta;
        }

        String name = this.getClass().getName();
        int posicion = name.lastIndexOf('.') + 1;
        
        return name.substring(posicion);
    }
}

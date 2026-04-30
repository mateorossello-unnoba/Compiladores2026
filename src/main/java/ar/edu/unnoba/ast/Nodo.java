package ar.edu.unnoba.ast;

public abstract class Nodo {
    private String etiqueta;

    public Nodo(String etiqueta) {
        this.etiqueta = etiqueta;
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
    
    public String graficar(String idPadre) {
        StringBuilder grafico = new StringBuilder();
        grafico.append(String.format("%1$s[label=\"%2$s\"]\n", this.getId(), this.getEtiqueta()));
        
        if(idPadre != null) {
            grafico.append(String.format("%1$s--%2$s\n", idPadre, this.getId()));
        }

        return grafico.toString();
    }
}

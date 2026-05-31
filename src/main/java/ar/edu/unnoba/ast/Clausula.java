package ar.edu.unnoba.ast;

import ar.edu.unnoba.llvm.GeneradorCodigo;
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

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // La generación de código para las cláusulas se maneja en el nodo While, por lo que no es necesario generar código aquí.
        return "";
    }

    public Expresion getCondicion() {
        return condicion;
    }
    
    public List<Sentencia> getCuerpo() {
        return cuerpo;
    }
}

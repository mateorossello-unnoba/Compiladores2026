package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.Sentencia;
import java.util.List;

public class If extends Sentencia {
    private final Expresion condicion;
    private final List<Sentencia> bloqueThen;
    private final List<Sentencia> bloqueElse;

    public If(Expresion condicion, List<Sentencia> bloqueThen, List<Sentencia> bloqueElse) {
        super("IF");
        this.condicion = condicion;
        this.bloqueThen = bloqueThen;
        this.bloqueElse = bloqueElse;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder(super.graficar(idPadre));
        
        // GRAFICAR LA CONDICIÓN
        resultado.append(condicion.graficar(miId));

        // GRAFICAR EL BLOQUE PRINCIPAL THEN
        if (bloqueThen != null) {
            resultado.append(this.graficarBloqueVirtual("BLOQUE_THEN", bloqueThen, miId));
        }

        // GRAFICAR EL BLOQUE ELSE
        if (bloqueElse != null) {
            resultado.append(this.graficarBloqueVirtual("BLOQUE_ELSE", bloqueElse, miId));
        }
        
        return resultado.toString();
    }

    public Expresion getCondicion() {
        return condicion;
    }

    public List<Sentencia> getBloqueThen() {
        return bloqueThen;
    }

    public List<Sentencia> getBloqueElse() {
        return bloqueElse;
    }
}

package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Clausula;
import ar.edu.unnoba.ast.Sentencia;
import java.util.List;

public class If extends Sentencia {
    private final Clausula bloquePrincipal;
    private final List<Clausula> bloquesElif;
    private final List<Sentencia> bloqueElse;

    public If(Clausula bloquePrincipal, List<Clausula> bloquesElif, List<Sentencia> bloqueElse) {
        super("IF");
        this.bloquePrincipal = bloquePrincipal;
        this.bloquesElif = bloquesElif;
        this.bloqueElse = bloqueElse;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder(super.graficar(idPadre));
        
        // GRAFICAR EL BLOQUE PRINCIPAL IF
        resultado.append(bloquePrincipal.graficar(miId));

        // GRAFICAR LOS BLOQUES ELIF
        if (bloquesElif != null) {
            for (Clausula elif : bloquesElif) {
                resultado.append(elif.graficar(miId));
            }
        }

        // GRAFICAR EL BLOQUE ELSE
        if (bloqueElse != null) {
            for (Sentencia sentencia : bloqueElse) resultado.append(sentencia.graficar(miId));
        }
        
        return resultado.toString();
    }
}

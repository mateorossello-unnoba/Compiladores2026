package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Clausula;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;
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
            resultado.append(this.graficarBloqueVirtual("BLOQUE_ELSE", bloqueElse, miId));
        }
        
        return resultado.toString();
    }

    @Override
    public void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception {
        bloquePrincipal.getCondicion().obtenerTipo(tablaSimbolos);

        if (bloquePrincipal.getCondicion().getTipoDato() != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: La condición de la sentencia IF debe ser de tipo BOOLEAN.");
        }
        
        for (Sentencia sentencia : bloquePrincipal.getCuerpo()) {
            sentencia.chequearSemantica(tablaSimbolos, dentroDeCiclo);
        }

        if (bloquesElif != null) {
            for (Clausula elif : bloquesElif) {
                elif.getCondicion().obtenerTipo(tablaSimbolos);

                if (elif.getCondicion().getTipoDato() != TipoDato.BOOLEAN) {
                    throw new Exception("Error Semántico: La condición de la cláusula ELIF debe ser de tipo BOOLEAN.");
                }

                for (Sentencia sentencia : elif.getCuerpo()) {
                    sentencia.chequearSemantica(tablaSimbolos, dentroDeCiclo);
                }
            }
        }

        if (bloqueElse != null) {
            for (Sentencia sentencia : bloqueElse) {
                sentencia.chequearSemantica(tablaSimbolos, dentroDeCiclo);
            }
        }
    }
}

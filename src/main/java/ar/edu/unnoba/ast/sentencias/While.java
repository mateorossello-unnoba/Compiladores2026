package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Clausula;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;
import java.util.List;

public class While extends Sentencia {
    private final Clausula bloquePrincipal;
    private final List<Clausula> bloquesAltWhile;

    public While(Clausula bloquePrincipal, List<Clausula> bloquesAltWhile) {
        super("WHILE");
        this.bloquePrincipal = bloquePrincipal;
        this.bloquesAltWhile = bloquesAltWhile;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        StringBuilder resultado = new StringBuilder(super.graficar(idPadre));
        
        // GRAFICAR EL BLOQUE PRINCIPAL WHILE
        resultado.append(bloquePrincipal.graficar(miId));

        // GRAFICAR LOS BLOQUES ALT WHILE
        if (bloquesAltWhile != null) {
            for (Clausula alternativa : bloquesAltWhile) {
                resultado.append(alternativa.graficar(miId));
            }
        }
        
        return resultado.toString();
    }

    @Override
    public void chequearSemantica(TablaSimbolos tablaSimbolos, boolean dentroDeCiclo) throws Exception {
        bloquePrincipal.getCondicion().obtenerTipo(tablaSimbolos);
        
        if (bloquePrincipal.getCondicion().getTipoDato() != TipoDato.BOOLEAN) {
            throw new Exception("Error Semántico: La condición de la sentencia WHILE debe ser de tipo BOOLEAN.");
        }
        
        for (Sentencia sentencia : bloquePrincipal.getCuerpo()) {
            sentencia.chequearSemantica(tablaSimbolos, true);
        }

        if (bloquesAltWhile != null) {
            for (Clausula alternativa : bloquesAltWhile) {
                alternativa.getCondicion().obtenerTipo(tablaSimbolos);

                if (alternativa.getCondicion().getTipoDato() != TipoDato.BOOLEAN) {
                    throw new Exception("Error Semántico: La condición de la cláusula ALT WHILE debe ser de tipo BOOLEAN.");
                }

                for (Sentencia sentencia : alternativa.getCuerpo()) {
                    sentencia.chequearSemantica(tablaSimbolos, true);
                }
            }
        }
    }
}

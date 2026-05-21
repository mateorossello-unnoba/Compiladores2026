package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Clausula;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;
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
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        String etiquetaInicio = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFin = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        
        generadorCodigo.getPilaBreak().push(etiquetaFin);

        codigo.append("  br label %").append(etiquetaInicio).append("\n\n");
        codigo.append(etiquetaInicio).append(":\n");
        codigo.append(bloquePrincipal.getCondicion().generarCodigo(generadorCodigo));

        String etiquetaCuerpo = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        boolean tieneAltWhile = bloquesAltWhile != null && !bloquesAltWhile.isEmpty();
        String etiquetaAltWhile = tieneAltWhile ? AyudanteGeneradorCodigo.getNuevaEtiqueta() : etiquetaFin;

        codigo.append("  br i1 ").append(bloquePrincipal.getCondicion().getIrReferencia()).append(", label %").append(etiquetaCuerpo).append(", label %").append(etiquetaAltWhile).append("\n\n");
        codigo.append(etiquetaCuerpo).append(":\n");
        generadorCodigo.getPilaContinue().push(etiquetaInicio);
        codigo.append(generadorCodigo.generarBloqueCodigo(bloquePrincipal.getCuerpo()));
        generadorCodigo.getPilaContinue().pop();
        codigo.append("  br label %").append(etiquetaInicio).append("\n\n");

        if (tieneAltWhile) {
            String etiquetaCondicionActual = etiquetaAltWhile;

            for (int i = 0; i < bloquesAltWhile.size(); i++) {
                Clausula alternativa = bloquesAltWhile.get(i);
                
                codigo.append(etiquetaCondicionActual).append(":\n");
                codigo.append(alternativa.getCondicion().generarCodigo(generadorCodigo));

                String etiquetaCuerpoAlternativa = AyudanteGeneradorCodigo.getNuevaEtiqueta();
                boolean esUltimaAlternativa = (i == bloquesAltWhile.size() - 1);
                String etiquetaSiguiente = esUltimaAlternativa ? etiquetaFin : AyudanteGeneradorCodigo.getNuevaEtiqueta();

                codigo.append("  br i1 ").append(alternativa.getCondicion().getIrReferencia()).append(", label %").append(etiquetaCuerpoAlternativa).append(", label %").append(etiquetaSiguiente).append("\n\n");
                codigo.append(etiquetaCuerpoAlternativa).append(":\n");
                generadorCodigo.getPilaContinue().push(etiquetaCondicionActual);
                codigo.append(generadorCodigo.generarBloqueCodigo(alternativa.getCuerpo()));
                generadorCodigo.getPilaContinue().pop();
                codigo.append("  br label %").append(etiquetaCondicionActual).append("\n\n");
                etiquetaCondicionActual = etiquetaSiguiente;
            }
        }

        codigo.append(etiquetaFin).append(":\n");

        generadorCodigo.getPilaBreak().pop();

        return codigo.toString();
    }

    public Clausula getBloquePrincipal() {
        return bloquePrincipal;
    }

    public List<Clausula> getBloquesAltWhile() {
        return bloquesAltWhile;
    }
}

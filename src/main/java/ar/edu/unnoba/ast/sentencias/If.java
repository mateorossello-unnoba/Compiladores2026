package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;
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

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        codigo.append(condicion.generarCodigo(generadorCodigo));
        String referenciaCondicion = condicion.getIrReferencia();

        String etiquetaThen = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFin = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        boolean tieneElse = bloqueElse != null && !bloqueElse.isEmpty();
        String etiquetaElse = tieneElse ? AyudanteGeneradorCodigo.getNuevaEtiqueta() : etiquetaFin;

        codigo.append("  br i1 ").append(referenciaCondicion).append(", label %").append(etiquetaThen).append(", label %").append(etiquetaElse).append("\n\n");
        codigo.append(etiquetaThen).append(":\n");
        codigo.append(generadorCodigo.generarBloqueCodigo(bloqueThen));
        codigo.append("  br label %").append(etiquetaFin).append("\n\n");

        if (tieneElse) {
            codigo.append(etiquetaElse).append(":\n");
            codigo.append(generadorCodigo.generarBloqueCodigo(bloqueElse));
            codigo.append("  br label %").append(etiquetaFin).append("\n\n");
        }

        codigo.append(etiquetaFin).append(":\n");

        return codigo.toString();
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

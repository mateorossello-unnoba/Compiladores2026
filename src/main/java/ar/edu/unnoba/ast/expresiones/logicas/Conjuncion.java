package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Conjuncion extends OperacionLogicaBinaria {
    public Conjuncion(Expresion izquierda, Expresion derecha) {
        super("&&", izquierda, derecha);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        // Reservar espacio de memoria para guardar el resultado
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroResultado).append(" = alloca i1\n");

        // Generar y evaluar primero el lado izquierdo de la conjunción
        codigo.append(this.izquierda.generarCodigo(generadorCodigo));

        // Crear etiquetas para los bloques de salto
        String etiquetaEvaluarDerecha = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFalso = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFin = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        // Si la izquierda es TRUE, evalúa la derecha. Si es FALSE, cortocircuito a FALSE.
        codigo.append("  br i1 ").append(this.izquierda.getIrReferencia()).append(", label %").append(etiquetaEvaluarDerecha).append(", label %").append(etiquetaFalso).append("\n\n");

        // BLOQUE: Evaluar la parte derecha
        codigo.append(etiquetaEvaluarDerecha).append(":\n");
        codigo.append(this.derecha.generarCodigo(generadorCodigo));
        codigo.append("  store i1 ").append(this.derecha.getIrReferencia()).append(", i1* ").append(punteroResultado).append("\n");
        codigo.append("  br label %").append(etiquetaFin).append("\n\n");

        // BLOQUE: Cortocircuito
        codigo.append(etiquetaFalso).append(":\n");
        codigo.append("  store i1 false, i1* ").append(punteroResultado).append("\n");
        codigo.append("  br label %").append(etiquetaFin).append("\n\n");

        // BLOQUE: Fin
        codigo.append(etiquetaFin).append(":\n");
        String valorFinal = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorFinal).append(" = load i1, i1* ").append(punteroResultado).append("\n");

        this.setIrReferencia(valorFinal);
        return codigo.toString();
    }
}

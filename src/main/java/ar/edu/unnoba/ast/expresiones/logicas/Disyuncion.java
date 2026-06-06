package ar.edu.unnoba.ast.expresiones.logicas;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Disyuncion extends OperacionLogicaBinaria {
    public Disyuncion(Expresion izquierda, Expresion derecha) {
        super("||", izquierda, derecha);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        // Reservar espacio de memoria para guardar el resultado
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroResultado).append(" = alloca i1\n");

        // Generar y evaluar primero el lado izquierdo de la disyunción
        codigo.append(this.izquierda.generarCodigo(generadorCodigo));

        // Crear etiquetas para los bloques de salto
        String etiquetaVerdadero = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaEvaluarDerecha = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String etiquetaFin = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        // Si la izquierda es TRUE, cortocircuito a TRUE. Si es FALSE, evaluamos la derecha.
        codigo.append("  br i1 ").append(this.izquierda.getIrReferencia()).append(", label %").append(etiquetaVerdadero).append(", label %").append(etiquetaEvaluarDerecha).append("\n\n");

        // BLOQUE: Cortocircuito
        codigo.append(etiquetaVerdadero).append(":\n");
        codigo.append("  store i1 true, i1* ").append(punteroResultado).append("\n");
        codigo.append("  br label %").append(etiquetaFin).append("\n\n");

        // BLOQUE: Evaluar la parte derecha
        codigo.append(etiquetaEvaluarDerecha).append(":\n");
        codigo.append(this.derecha.generarCodigo(generadorCodigo));
        codigo.append("  store i1 ").append(this.derecha.getIrReferencia()).append(", i1* ").append(punteroResultado).append("\n");
        codigo.append("  br label %").append(etiquetaFin).append("\n\n");

        // BLOQUE: Fin
        codigo.append(etiquetaFin).append(":\n");
        String valorFinal = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorFinal).append(" = load i1, i1* ").append(punteroResultado).append("\n");

        this.setIrReferencia(valorFinal);
        return codigo.toString();
    }
}

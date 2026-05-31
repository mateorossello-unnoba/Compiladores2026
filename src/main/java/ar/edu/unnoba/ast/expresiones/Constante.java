package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Constante extends Expresion {
    private String valor;

    public Constante(String valorString, TipoDato tipoDato, int dimensionArreglo) {
        super(valorString.replace("\"", "\\\""), tipoDato);
        this.valor = valorString;
        this.dimensionArreglo = dimensionArreglo;
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // Obtener un nuevo puntero para almacenar el valor de la constante
        String puntero = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(puntero);
        
        // Código ejemplo:
        // %puntero.1 = fadd double 0.0, 3.14
        return switch (this.tipoDato) {
            case INT -> "  " + puntero + " = add i32 0, " + this.valor + "\n";
            case FLOAT -> "  " + puntero + " = fadd double 0.0, " + this.valor + "\n";
            case BOOLEAN -> "  " + puntero + " = xor i1 " + this.valor + ", false\n";
            case ARRAY -> {
                String valores = this.valor.replace("[", "").replace("]", "").trim();
                StringBuilder valorArreglo = new StringBuilder("[");
                
                if (!valores.isEmpty()) {
                    String[] elementos = valores.split(",");

                    for (int i = 0; i < elementos.length; i++) {
                        valorArreglo.append("double ").append(elementos[i].trim());
                        if (i < elementos.length - 1) valorArreglo.append(", ");
                    }
                }

                valorArreglo.append("]");

                this.setIrReferencia(valorArreglo.toString());
                yield "";
            }
            case STRING -> {
                String nombreString = AyudanteGeneradorCodigo.registrarString(this.valor);
                this.setIrReferencia(nombreString);
                yield "";
            }
            default -> throw new IllegalStateException("Tipo de constante desconocida.");
        };
    }

    public String getValor() {
        return valor;
    }
}

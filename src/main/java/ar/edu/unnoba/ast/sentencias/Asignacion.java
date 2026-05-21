package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.Constante;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Asignacion extends Sentencia {
    private final Identificador variable;
    private final Expresion valor;

    public Asignacion(Identificador variable, Expresion valor) {
        super("=");
        this.variable = variable;
        this.valor = valor;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + variable.graficar(miId) + valor.graficar(miId);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        TipoDato tipoFinal = this.variable.getTipoDato();
        String codigoValor = generadorCodigo.generarConConversion(this.valor, tipoFinal);

        if (tipoFinal == TipoDato.ARRAY) {
            StringBuilder stringBuilder = new StringBuilder();
            int dimensionArreglo = this.variable.getDimensionArreglo();

            String literal = ((Constante)this.valor).getValor();
            String contenido = literal.substring(1, literal.length() - 1);
            String[] elementos = contenido.split(",");

            for (int i = 0; i < dimensionArreglo; i++) {
                String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
                String valorElemento = (i < elementos.length) ? elementos[i].trim() : "0.0";

                stringBuilder.append("  ").append(punteroCelda).append(" = getelementptr [").append(dimensionArreglo).append(" x double], [").append(dimensionArreglo).append(" x double]* %").append(this.variable.getNombre()).append(", i32 0, i32 ").append(i).append("\n");
                stringBuilder.append("  store double ").append(valorElemento).append(", double* ").append(punteroCelda).append("\n");
            }

            return stringBuilder.toString();
        }
        
        String tipo = generadorCodigo.obtenerTipo(this.variable.getTipoDato());
        String instruccionAsignacion = "  store " + tipo + " " + this.valor.getIrReferencia() + ", " + tipo + "* %" + this.variable.getNombre() + "\n";

        // Código ejemplo:
        // %puntero.1 = fadd double 0.0, 3.14
        // store double %puntero.1, double* @variable
        return codigoValor + instruccionAsignacion;
    }

    public Identificador getVariable() {
        return variable;
    }

    public Expresion getValor() {
        return valor;
    }
}

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
        TipoDato tipoValor = this.valor.getTipoDato();

        // Es usado el llamado a instanceof debido a que el valor puede ser una constante de arreglo.
        // Lo cual no es un arreglo en sí mismo sino una expresión con un literal que representa un arreglo.
        // Este atajo es empleado debido a que el análisis semántico no distingue entre ambos casos.
        
        if (tipoFinal == TipoDato.ARRAY) {
            if (tipoValor == TipoDato.ARRAY) {
                if (this.valor instanceof Constante) {
                    return asignarArreglo();
                } else {
                    return copiarArreglo(generadorCodigo);
                }
            } else {
                return asignarArregloEscalar(generadorCodigo);
            }
        }

        return asignarEscalar(generadorCodigo);
    }

    private String asignarArreglo() {
        StringBuilder codigo = new StringBuilder();
        int dimensionArreglo = this.variable.getDimensionArreglo();
        
        String literal = ((Constante) this.valor).getValor();
        String contenido = literal.substring(1, literal.length() - 1);
        String[] elementos = contenido.split(",");

        for (int i = 0; i < dimensionArreglo; i++) {
            String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
            String valorElemento = (i < elementos.length) ? elementos[i].trim() : "0.0";

            codigo.append("  ").append(punteroCelda).append(" = getelementptr [").append(dimensionArreglo).append(" x double], [").append(dimensionArreglo).append(" x double]* %").append(this.variable.getNombre()).append(", i32 0, i32 ").append(i).append("\n");
            codigo.append("  store double ").append(valorElemento).append(", double* ").append(punteroCelda).append("\n");
        }

        return codigo.toString();
    }

    private String copiarArreglo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();
        int dimension = this.variable.getDimensionArreglo();
        String nombreDestino = this.variable.getNombre();

        codigo.append(this.valor.generarCodigo(generadorCodigo));

        String punteroOrigenValor;
        if (this.valor instanceof Identificador) {
            punteroOrigenValor = "%" + ((Identificador) this.valor).getNombre();
        } else {
            punteroOrigenValor = this.valor.getIrReferencia();
        }

        codigo.append("  ; --- Copia de Arreglo ---\n");

        String punteroI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroI).append(" = alloca i32\n");
        codigo.append("  store i32 0, i32* ").append(punteroI).append("\n");

        String condicionCiclo = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String cuerpoCiclo = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String finCiclo = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        codigo.append("  br label %").append(condicionCiclo).append("\n\n");
        
        // Condición:
        codigo.append(condicionCiclo).append(":\n");
        String valorI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorI).append(" = load i32, i32* ").append(punteroI).append("\n");
        String comparar = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(comparar).append(" = icmp slt i32 ").append(valorI).append(", ").append(dimension).append("\n");
        codigo.append("  br i1 ").append(comparar).append(", label %").append(cuerpoCiclo).append(", label %").append(finCiclo).append("\n\n");

        // Cuerpo:
        codigo.append(cuerpoCiclo).append(":\n");
        
        String punteroOrigen = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroOrigen).append(" = getelementptr [").append(dimension).append(" x double], [").append(dimension).append(" x double]* ").append(punteroOrigenValor).append(", i32 0, i32 ").append(valorI).append("\n");
        String valorCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorCelda).append(" = load double, double* ").append(punteroOrigen).append("\n");

        String punteroDestino = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroDestino).append(" = getelementptr [").append(dimension).append(" x double], [").append(dimension).append(" x double]* %").append(nombreDestino).append(", i32 0, i32 ").append(valorI).append("\n");
        codigo.append("  store double ").append(valorCelda).append(", double* ").append(punteroDestino).append("\n");

        // Incrementar i
        String siguienteI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(siguienteI).append(" = add i32 ").append(valorI).append(", 1\n");
        codigo.append("  store i32 ").append(siguienteI).append(", i32* ").append(punteroI).append("\n");
        codigo.append("  br label %").append(condicionCiclo).append("\n\n");

        codigo.append(finCiclo).append(":\n");
        return codigo.toString();
    }

    private String asignarArregloEscalar(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();
        codigo.append("  ; --- Asignación de Escalar a Arreglo ---\n");
        
        codigo.append(this.valor.generarCodigo(generadorCodigo));
        String referenciaValor = this.valor.getIrReferencia();
        int dimension = this.variable.getDimensionArreglo();

        String punteroI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroI).append(" = alloca i32\n");
        codigo.append("  store i32 0, i32* ").append(punteroI).append("\n");

        String condicionCiclo = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String cuerpoCiclo = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String finCiclo = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        codigo.append("  br label %").append(condicionCiclo).append("\n\n");
        
        // Condición
        codigo.append(condicionCiclo).append(":\n");
        String valorI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorI).append(" = load i32, i32* ").append(punteroI).append("\n");
        String comparar = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(comparar).append(" = icmp slt i32 ").append(valorI).append(", ").append(dimension).append("\n");
        codigo.append("  br i1 ").append(comparar).append(", label %").append(cuerpoCiclo).append(", label %").append(finCiclo).append("\n\n");

        // Cuerpo
        codigo.append(cuerpoCiclo).append(":\n");
        String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(punteroCelda).append(" = getelementptr [").append(dimension).append(" x double], [").append(dimension).append(" x double]* %").append(this.variable.getNombre()).append(", i32 0, i32 ").append(valorI).append("\n");
        codigo.append("  store double ").append(referenciaValor).append(", double* ").append(punteroCelda).append("\n");

        // Incrementar
        String siguienteI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(siguienteI).append(" = add i32 ").append(valorI).append(", 1\n");
        codigo.append("  store i32 ").append(siguienteI).append(", i32* ").append(punteroI).append("\n");
        codigo.append("  br label %").append(condicionCiclo).append("\n\n");

        codigo.append(finCiclo).append(":\n");
        return codigo.toString();
    }

    private String asignarEscalar(GeneradorCodigo generadorCodigo) {
        TipoDato tipoDestino = this.variable.getTipoDato();
        String codigoValor = this.valor.generarCodigo(generadorCodigo);
        String tipo = generadorCodigo.obtenerTipo(tipoDestino);
        
        String instruccionAsignacion = "  store " + tipo + " " + this.valor.getIrReferencia() + ", " + tipo + "* %" + this.variable.getNombre() + "\n";
        return codigoValor + instruccionAsignacion;
    }

    public Identificador getVariable() {
        return variable;
    }

    public Expresion getValor() {
        return valor;
    }
}

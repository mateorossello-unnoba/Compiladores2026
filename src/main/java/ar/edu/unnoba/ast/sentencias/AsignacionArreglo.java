package ar.edu.unnoba.ast.sentencias;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.expresiones.Identificador;
import ar.edu.unnoba.ast.Sentencia;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class AsignacionArreglo extends Sentencia {
    private final Identificador identificador;
    private final Expresion indice;
    private final Expresion valor;

    public AsignacionArreglo(Identificador identificador, Expresion indice, Expresion valor) {
        super("ASIGNACIÓN []");
        this.identificador = identificador;
        this.indice = indice;
        this.valor = valor;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + identificador.graficar(miId) + indice.graficar(miId) + valor.graficar(miId);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        String codigoIndice = this.indice.generarCodigo(generadorCodigo);
        codigo.append(codigoIndice);
        String codigoValor = generadorCodigo.generarConConversion(this.valor, TipoDato.FLOAT);
        codigo.append(codigoValor);

        String referenciaIndice = this.indice.getIrReferencia();
        String referenciaValor = this.valor.getIrReferencia();
        String nombreArreglo = this.identificador.getNombre();
        int dimension = this.identificador.getDimensionArreglo();

        codigo.append(GeneradorCodigo.generarControlLimites(nombreArreglo, dimension, referenciaIndice));

        String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();

        // Obtener puntero a la celda del arreglo
        String instruccionPuntero = "  " + punteroCelda + " = getelementptr [" + dimension + " x double], [" + dimension + " x double]* %" + nombreArreglo + ", i32 0, i32 " + referenciaIndice + "\n";
        codigo.append(instruccionPuntero);

        // Almacenar el valor en la celda del arreglo
        String instruccionAlmacenamiento = "  store double " + referenciaValor + ", double* " + punteroCelda + "\n";
        codigo.append(instruccionAlmacenamiento);

        return codigo.toString();
    }

    public Identificador getIdentificador() {
        return identificador;
    }

    public Expresion getIndice() {
        return indice;
    }

    public Expresion getValor() {
        return valor;
    }
}

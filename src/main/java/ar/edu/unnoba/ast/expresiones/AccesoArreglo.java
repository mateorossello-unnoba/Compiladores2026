package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class AccesoArreglo extends Expresion {
    private final Identificador identificador;
    private final Expresion indice;

    public AccesoArreglo(Identificador identificador, Expresion indice) {
        super("ACCESO []", TipoDato.FLOAT);
        this.identificador = identificador;
        this.indice = indice;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + identificador.graficar(miId) + indice.graficar(miId);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        codigo.append(this.indice.generarCodigo(generadorCodigo));
        String referenciaIndice = this.indice.getIrReferencia();

        String nombreArreglo = this.identificador.getNombre();
        int dimensionArreglo = this.identificador.getDimensionArreglo();

        codigo.append(GeneradorCodigo.generarControlLimites(nombreArreglo, dimensionArreglo, referenciaIndice));

        String punteroCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
        String valorCelda = AyudanteGeneradorCodigo.getNuevoPuntero();
        this.setIrReferencia(valorCelda);
        
        // Obtener puntero a la celda del arreglo
        String instruccionPuntero = "  " + punteroCelda + " = getelementptr [" + dimensionArreglo + " x double], [" + dimensionArreglo + " x double]* %" + nombreArreglo + ", i32 0, i32 " + referenciaIndice + "\n";
        codigo.append(instruccionPuntero);

        // Cargar el valor de la celda del arreglo
        String instruccionCarga = "  " + valorCelda + " = load double, double* " + punteroCelda + "\n";
        codigo.append(instruccionCarga);

        return codigo.toString();
    }

    public Identificador getIdentificador() {
        return identificador;
    }

    public Expresion getIndice() {
        return indice;
    }
}

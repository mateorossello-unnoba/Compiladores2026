package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.TablaSimbolos;

public class Moda extends Expresion {
    private final Expresion arreglo;

    public Moda(Expresion arreglo) {
        super("MODA");
        this.arreglo = arreglo;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + arreglo.graficar(miId);
    }

    @Override
    public void obtenerTipo(TablaSimbolos tablaSimbolos) throws Exception {
        arreglo.obtenerTipo(tablaSimbolos);

        if (arreglo.getTipoDato() != TipoDato.ARRAY) {
            throw new Exception("Error Semántico: La función solo admite como argumento ARRAY.");
        }
        
        this.tipoDato = TipoDato.FLOAT;
    }
}

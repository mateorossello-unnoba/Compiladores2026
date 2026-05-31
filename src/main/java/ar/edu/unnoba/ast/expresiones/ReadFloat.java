package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class ReadFloat extends Expresion {
    public ReadFloat() {
        super("READ_FLOAT()", TipoDato.FLOAT);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // Registrar el formato para leer un float
        String formato = AyudanteGeneradorCodigo.registrarString("%lf");
        
        // Reservar espacio en memoria para el float leído
        String punteroMemoria = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionAsignacion = "  " + punteroMemoria + " = alloca double\n";
        
        // Instrucción para llamar a scanf y leer el float desde la entrada estándar
        int longitudFormato = formato.length() + 1;
        String instruccionEscanear = "  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + formato + ", i64 0, i64 0), double* " + punteroMemoria + ")\n";

        // Leer el valor almacenado en memoria después de la llamada a scanf
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionLeerValor = "  " + punteroResultado + " = load double, double* " + punteroMemoria + "\n";

        this.setIrReferencia(punteroResultado);

        // Código ejemplo:
        // @.str.0 = private unnamed_addr constant [4 x i8] c"%lf\00"
        // %puntero.1 = alloca double
        // call i32 (i8*, ...) @scanf(i8* @.str.0, double* %puntero.1)
        // %puntero.2 = load double, double* %puntero.1
        return instruccionAsignacion + instruccionEscanear + instruccionLeerValor;
    }
}

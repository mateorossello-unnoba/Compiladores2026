package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class ReadBool extends Expresion {
    public ReadBool() {
        super("READ_BOOL()", TipoDato.BOOLEAN);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        // Registrar el formato para leer un booleano (leemos un entero con %d y luego lo convertiremos a booleano)
        String formato = AyudanteGeneradorCodigo.registrarString("%d");
        
        // Reservar espacio en memoria para el entero leído
        String punteroMemoria = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionAsignacion = "  " + punteroMemoria + " = alloca i32\n";
        
        // Instrucción para llamar a scanf y leer el entero desde la entrada estándar
        int longitudFormato = formato.length() + 1;
        String instruccionEscanear = "  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([" + longitudFormato + " x i8], [" + longitudFormato + " x i8]* " + formato + ", i64 0, i64 0), i32* " + punteroMemoria + ")\n";

        // Leer el valor almacenado en memoria después de la llamada a scanf
        String punteroResultado = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionLeerValor = "  " + punteroResultado + " = load i32, i32* " + punteroMemoria + "\n";

        // Convertir el entero leído a booleano (0 se considera false, cualquier otro valor se considera true)
        String punteroBooleano = AyudanteGeneradorCodigo.getNuevoPuntero();
        String instruccionConvertir = "  " + punteroBooleano + " = xor i1 " + punteroResultado + ", false\n";

        this.setIrReferencia(punteroBooleano);

        // Código ejemplo:
        // @.str.0 = private unnamed_addr constant [3 x i8] c"%d\00"
        // %puntero.1 = alloca i32
        // call i32 (i8*, ...) @scanf(i8* @.str.0, i32* %puntero.1)
        // %puntero.2 = load i32, i32* %puntero.1
        // %puntero.3 = xor i1 %puntero.2, false
        return instruccionAsignacion + instruccionEscanear + instruccionLeerValor + instruccionConvertir;
    }
}

package ar.edu.unnoba.llvm;

import java.util.ArrayList;
import java.util.List;

public class AyudanteGeneradorCodigo {
    private static int proximoPunteroId = 0;
    private static int proximoPunteroGlobalId = 0;
    private static int proximaEtiquetaId = 0;

    private static int proximoStringId = 0;
    private static List<String> strings = new ArrayList<>();
        
    private AyudanteGeneradorCodigo() {

    }
   
    public static String getNuevoPuntero() {
        proximoPunteroId += 1;
        return "%puntero." + proximoPunteroId;
    }
    
    public static String getNuevoPunteroGlobal() {
        proximoPunteroGlobalId += 1;
        return "@punteroGlobal." + proximoPunteroGlobalId;
    }
    
    public static String getNuevaEtiqueta() {
        proximaEtiquetaId += 1;
        return "etiqueta." + proximaEtiquetaId;
    }

    public static String registrarString(String valor) {
        String nombre = "@.str." + proximoStringId;
        proximoStringId += 1;
        
        int longitud = valor.replace("\\0A", " ").length() + 1;

        String declracion = nombre + " = private unnamed_addr constant [" + longitud + " x i8] c\"" + valor + "\\00\"\n";
        strings.add(declracion);

        return nombre;
    }

    public static String obtenerStrings() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : strings) {
            stringBuilder.append(string);
        }

        return stringBuilder.toString();
    }
}

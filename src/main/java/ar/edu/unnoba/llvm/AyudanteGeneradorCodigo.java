package ar.edu.unnoba.llvm;

import java.util.LinkedHashMap;
import java.util.Map;

public class AyudanteGeneradorCodigo {
    private static int proximoPunteroId = 0;
    private static int proximoPunteroGlobalId = 0;
    private static int proximaEtiquetaId = 0;

    private static int proximoStringId = 0;
    private static Map<String, String> strings = new LinkedHashMap<>();
        
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
        if (strings.containsKey(valor)) {
            return strings.get(valor);
        }

        String nombre = "@.str." + proximoStringId;
        proximoStringId += 1;

        strings.put(valor, nombre);
        return nombre;
    }

    public static String obtenerStrings() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : strings.entrySet()) {
            String valor = entry.getKey();
            String nombre = entry.getValue();

            String contable = valor.replace("\"", "\\22");
            contable = contable.replace("\n", "\\0A");

            int longitud = calcularLongitud(contable) + 1;

            stringBuilder.append(nombre).append(" = private unnamed_addr constant [").append(longitud).append(" x i8] c\"").append(contable).append("\\00\"\n");
        }

        return stringBuilder.toString();
    }

    private static int calcularLongitud(String string) {
        int length = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '\\' && i + 2 < string.length()) {
                i += 2; 
                length++;
            } else {
                length++;
            }
        }

        return length;
    }

    public static void reset() {
        proximoPunteroId = 0;
        proximoPunteroGlobalId = 0;
        proximaEtiquetaId = 0;
        proximoStringId = 0;
        strings.clear();
    }
}

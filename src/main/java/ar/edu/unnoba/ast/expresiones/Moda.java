package ar.edu.unnoba.ast.expresiones;

import ar.edu.unnoba.ast.Expresion;
import ar.edu.unnoba.ast.TipoDato;
import ar.edu.unnoba.llvm.AyudanteGeneradorCodigo;
import ar.edu.unnoba.llvm.GeneradorCodigo;

public class Moda extends Expresion {
    private final Expresion arreglo;

    public Moda(Expresion arreglo) {
        super("MODA", TipoDato.FLOAT);
        this.arreglo = arreglo;
    }

    @Override
    public String graficar(String idPadre) {
        String miId = this.getId();
        return super.graficar(idPadre) + arreglo.graficar(miId);
    }

    @Override
    public String generarCodigo(GeneradorCodigo generadorCodigo) {
        StringBuilder codigo = new StringBuilder();

        // Generar código para obtener el arreglo y su referencia
        codigo.append(arreglo.generarCodigo(generadorCodigo));
        String referenciaArreglo = this.arreglo.getIrReferencia();
        int n = this.arreglo.getDimensionArreglo();

        if (n <= 0) {
            StringBuilder codigoVacio = new StringBuilder();
        
            String mensajeVacio = "La lista esta vacia\\0A";
            String referenciaStringVacio = AyudanteGeneradorCodigo.registrarString(mensajeVacio);
            int longitudReal = mensajeVacio.replace("\\0A", " ").length() + 1;
            
            codigoVacio.append("  call i32 (i8*, ...) @printf(i8* getelementptr ([").append(longitudReal).append(" x i8], [").append(longitudReal).append(" x i8]* ").append(referenciaStringVacio).append(", i32 0, i32 0))\n");

            String modaVacia = AyudanteGeneradorCodigo.getNuevoPuntero();
            this.setIrReferencia(modaVacia);
            codigoVacio.append("  ").append(modaVacia).append(" = fadd double -1.0, 0.0\n");
            
            return codigoVacio.toString();
        }

        // Reservar espacio para variables necesarias
        String punteroMaximaFrecuencia = AyudanteGeneradorCodigo.getNuevoPuntero();
        String punteroModa = AyudanteGeneradorCodigo.getNuevoPuntero();
        String punteroI = AyudanteGeneradorCodigo.getNuevoPuntero();
        String punteroJ = AyudanteGeneradorCodigo.getNuevoPuntero();
        String punteroFrecuenciaActual = AyudanteGeneradorCodigo.getNuevoPuntero();

        codigo.append("  ; --- INICIO ALGORITMO MODA ---\n");
        codigo.append("  ").append(punteroMaximaFrecuencia).append(" = alloca i32\n");
        codigo.append("  ").append(punteroModa).append(" = alloca double\n");
        codigo.append("  ").append(punteroI).append(" = alloca i32\n");
        codigo.append("  ").append(punteroJ).append(" = alloca i32\n");
        codigo.append("  ").append(punteroFrecuenciaActual).append(" = alloca i32\n");

        // Inicializar variables
        codigo.append("  store i32 0, i32* ").append(punteroMaximaFrecuencia).append("\n");
        codigo.append("  store double 0.0, double* ").append(punteroModa).append("\n");
        codigo.append("  store i32 0, i32* ").append(punteroI).append("\n");

        // Etiquetas para el control de flujo
        String condicionBucleExterno = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String cuerpoBucleExterno = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String condicionBucleInterno = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String cuerpoBucleInterno = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String incrementarFrecuencia = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String evaluarMaximaFrecuencia = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String actualizarModa = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String avanzarI = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String avanzarJ = AyudanteGeneradorCodigo.getNuevaEtiqueta();
        String fin = AyudanteGeneradorCodigo.getNuevaEtiqueta();

        // Bucle externo para recorrer cada elemento del arreglo (PUNTERO I)
        codigo.append("  br label %").append(condicionBucleExterno).append("\n\n");
        codigo.append(condicionBucleExterno).append(":\n");
        String valorIndiceI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorIndiceI).append(" = load i32, i32* ").append(punteroI).append("\n");
        String compararBucleExterno = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(compararBucleExterno).append(" = icmp slt i32 ").append(valorIndiceI).append(", ").append(n).append("\n");
        codigo.append("  br i1 ").append(compararBucleExterno).append(", label %").append(cuerpoBucleExterno).append(", label %").append(fin).append("\n\n");

        codigo.append(cuerpoBucleExterno).append(":\n");

        String arregloPunteroI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(arregloPunteroI).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(referenciaArreglo).append(", i32 0, i32 ").append(valorIndiceI).append("\n");
        String arregloValorI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(arregloValorI).append(" = load double, double* ").append(arregloPunteroI).append("\n");

        codigo.append("  store i32 0, i32* ").append(punteroFrecuenciaActual).append("\n");
        codigo.append("  store i32 0, i32* ").append(punteroJ).append("\n");
        codigo.append("  br label %").append(condicionBucleInterno).append("\n\n");

        // Bucle interno para comparar el elemento actual con el resto del arreglo (PUNTERO J)
        codigo.append(condicionBucleInterno).append(":\n");
        String valorIndiceJ = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(valorIndiceJ).append(" = load i32, i32* ").append(punteroJ).append("\n");
        String compararBucleInterno = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(compararBucleInterno).append(" = icmp slt i32 ").append(valorIndiceJ).append(", ").append(n).append("\n");
        codigo.append("  br i1 ").append(compararBucleInterno).append(", label %").append(cuerpoBucleInterno).append(", label %").append(evaluarMaximaFrecuencia).append("\n\n");

        codigo.append(cuerpoBucleInterno).append(":\n");

        String arregloPunteroJ = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(arregloPunteroJ).append(" = getelementptr [").append(n).append(" x double], [").append(n).append(" x double]* ").append(referenciaArreglo).append(", i32 0, i32 ").append(valorIndiceJ).append("\n");
        String arregloValorJ = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(arregloValorJ).append(" = load double, double* ").append(arregloPunteroJ).append("\n");

        String compararIgual = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(compararIgual).append(" = fcmp oeq double ").append(arregloValorI).append(", ").append(arregloValorJ).append("\n");
        codigo.append("  br i1 ").append(compararIgual).append(", label %").append(incrementarFrecuencia).append(", label %").append(avanzarJ).append("\n\n");

        // Incrementar frecuencia si se encuentra una coincidencia
        codigo.append(incrementarFrecuencia).append(":\n");
        String frecuenciaActual = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(frecuenciaActual).append(" = load i32, i32* ").append(punteroFrecuenciaActual).append("\n");
        String nuevaFrecuencia = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(nuevaFrecuencia).append(" = add i32 ").append(frecuenciaActual).append(", 1\n");
        codigo.append("  store i32 ").append(nuevaFrecuencia).append(", i32* ").append(punteroFrecuenciaActual).append("\n");
        codigo.append("  br label %").append(avanzarJ).append("\n\n");

        // Evaluar si la frecuencia actual es mayor que la máxima frecuencia encontrada
        codigo.append(evaluarMaximaFrecuencia).append(":\n");
        String frecuenciaFinal = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(frecuenciaFinal).append(" = load i32, i32* ").append(punteroFrecuenciaActual).append("\n");
        String maximaFrecuencia = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(maximaFrecuencia).append(" = load i32, i32* ").append(punteroMaximaFrecuencia).append("\n");
        String compararMaximaFrecuencia = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(compararMaximaFrecuencia).append(" = icmp sgt i32 ").append(frecuenciaFinal).append(", ").append(maximaFrecuencia).append("\n");
        codigo.append("  br i1 ").append(compararMaximaFrecuencia).append(", label %").append(actualizarModa).append(", label %").append(avanzarI).append("\n\n");

        // Actualizar moda y máxima frecuencia si se encuentra una nueva moda
        codigo.append(actualizarModa).append(":\n");
        codigo.append("  store i32 ").append(frecuenciaFinal).append(", i32* ").append(punteroMaximaFrecuencia).append("\n");
        codigo.append("  store double ").append(arregloValorI).append(", double* ").append(punteroModa).append("\n");
        codigo.append("  br label %").append(avanzarI).append("\n\n");

        // Avanzar el puntero I para el siguiente elemento del arreglo
        codigo.append(avanzarI).append(":\n");
        String proximoValorI = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(proximoValorI).append(" = add i32 ").append(valorIndiceI).append(", 1\n");
        codigo.append("  store i32 ").append(proximoValorI).append(", i32* ").append(punteroI).append("\n");
        codigo.append("  br label %").append(condicionBucleExterno).append("\n\n");

        // Avanzar el puntero J para el siguiente elemento del arreglo
        codigo.append(avanzarJ).append(":\n");
        String proximoValorJ = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(proximoValorJ).append(" = add i32 ").append(valorIndiceJ).append(", 1\n");
        codigo.append("  store i32 ").append(proximoValorJ).append(", i32* ").append(punteroJ).append("\n");
        codigo.append("  br label %").append(condicionBucleInterno).append("\n\n");

        // Bloque fin del algoritmo
        codigo.append(fin).append(":\n");
        String respuestaModa = AyudanteGeneradorCodigo.getNuevoPuntero();
        codigo.append("  ").append(respuestaModa).append(" = load double, double* ").append(punteroModa).append("\n");
        codigo.append("  ; --- FIN ALGORITMO MODA ---\n");

        // Guardar el resultado de la moda en la referencia de esta expresión
        this.setIrReferencia(respuestaModa);
        
        return codigo.toString();
    }
}

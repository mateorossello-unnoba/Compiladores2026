; --- Compilador UNNOBA - 2026 ---
target datalayout = "e-m:w-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-windows-msvc19.33.0"

; --- Cadenas Globales ---
@.str.0 = private unnamed_addr constant [40 x i8] c"Iniciando pruebas del compilador UNNOBA\00"
@.str.1 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.2 = private unnamed_addr constant [40 x i8] c"Ingrese un numero entero para comenzar:\00"
@.str.3 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.4 = private unnamed_addr constant [3 x i8] c"%d\00"
@.str.5 = private unnamed_addr constant [26 x i8] c"El valor supera el limite\00"
@.str.6 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.7 = private unnamed_addr constant [40 x i8] c"El valor es exactamente igual al limite\00"
@.str.8 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.9 = private unnamed_addr constant [36 x i8] c"El valor esta por debajo del limite\00"
@.str.10 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.11 = private unnamed_addr constant [29 x i8] c"Acumulador alcanzo el maximo\00"
@.str.12 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.13 = private unnamed_addr constant [21 x i8] c"Ajustando bandera...\00"
@.str.14 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.15 = private unnamed_addr constant [41 x i8] c"Calculando la moda del arreglo de datos:\00"
@.str.16 = private unnamed_addr constant [6 x i8] c"%s\0A\00"
@.str.17 = private unnamed_addr constant [6 x i8] c"%f\0A\00"
@.str.18 = private unnamed_addr constant [33 x i8] c"Fin de las operaciones con exito\00"
@.str.19 = private unnamed_addr constant [6 x i8] c"%s\0A\00"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)

define i32 @main() {
entrada:
  ; --- Reserva de Memoria ---
  %iterador = alloca i32
  %limite = alloca i32
  %valor_leido = alloca i32
  %acumulador = alloca double
  %promedio = alloca double
  %m = alloca double
  %bandera = alloca i1
  %es_mayor = alloca i1
  %arreglo_datos = alloca [5 x double]
  %arreglo_vacio = alloca [5 x double]

  ; --- Ejecucion del Programa ---
  %puntero.1 = add i32 0, 0
  store i32 %puntero.1, i32* %iterador
  %puntero.2 = add i32 0, 5
  store i32 %puntero.2, i32* %limite
  %puntero.3 = fadd double 0.0, 0.0
  store double %puntero.3, double* %acumulador
  %puntero.4 = xor i1 true, false
  store i1 %puntero.4, i1* %bandera
  %puntero.6 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 0
  store double 1.5, double* %puntero.6
  %puntero.7 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 1
  store double 2.5, double* %puntero.7
  %puntero.8 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 2
  store double 2.5, double* %puntero.8
  %puntero.9 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 3
  store double 3.0, double* %puntero.9
  %puntero.10 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 4
  store double 3.0, double* %puntero.10
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.0, i32 0, i32 0))
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.2, i32 0, i32 0))
  %puntero.13 = alloca i32
  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([8 x i8], [8 x i8]* @.str.4, i64 0, i64 0), i32* %puntero.13)
  %puntero.14 = load i32, i32* %puntero.13
  store i32 %puntero.14, i32* %valor_leido
  %puntero.15 = load i32, i32* %valor_leido
  %puntero.16 = load i32, i32* %limite
  %puntero.17 = icmp sgt i32 %puntero.15, %puntero.16
  %puntero.18 = load i1, i1* %bandera
  %puntero.19 = and i1 %puntero.17, %puntero.18
  br i1 %puntero.19, label %etiqueta.1, label %etiqueta.3

etiqueta.1:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.6, i32 0, i32 0), i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.5, i32 0, i32 0))
  %puntero.21 = xor i1 true, false
  store i1 %puntero.21, i1* %es_mayor
  br label %etiqueta.2

etiqueta.3:
  %puntero.22 = load i32, i32* %valor_leido
  %puntero.23 = load i32, i32* %limite
  %puntero.24 = icmp eq i32 %puntero.22, %puntero.23
  br i1 %puntero.24, label %etiqueta.4, label %etiqueta.6

etiqueta.4:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.8, i32 0, i32 0), i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.7, i32 0, i32 0))
  %puntero.26 = xor i1 false, false
  store i1 %puntero.26, i1* %es_mayor
  br label %etiqueta.5

etiqueta.6:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.10, i32 0, i32 0), i8* getelementptr inbounds ([36 x i8], [36 x i8]* @.str.9, i32 0, i32 0))
  %puntero.28 = xor i1 true, false
  %puntero.29 = xor i1 %puntero.28, true
  store i1 %puntero.29, i1* %es_mayor
  br label %etiqueta.5

etiqueta.5:
  br label %etiqueta.2

etiqueta.2:
  br label %etiqueta.7

etiqueta.7:
  %puntero.30 = load i32, i32* %iterador
  %puntero.31 = load i32, i32* %limite
  %puntero.32 = icmp slt i32 %puntero.30, %puntero.31
  br i1 %puntero.32, label %etiqueta.9, label %etiqueta.10

etiqueta.9:
  %puntero.33 = load double, double* %acumulador
  %puntero.34 = load i32, i32* %iterador
  %puntero.35 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 %puntero.34
  %puntero.36 = load double, double* %puntero.35
  %puntero.37 = fadd double %puntero.33, %puntero.36
  store double %puntero.37, double* %acumulador
  %puntero.38 = load i32, i32* %iterador
  %puntero.39 = add i32 0, 1
  %puntero.40 = add i32 %puntero.38, %puntero.39
  store i32 %puntero.40, i32* %iterador
  %puntero.41 = load double, double* %acumulador
  %puntero.42 = fadd double 0.0, 10.0
  %puntero.43 = fcmp oge double %puntero.41, %puntero.42
  br i1 %puntero.43, label %etiqueta.11, label %etiqueta.12

etiqueta.11:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.12, i32 0, i32 0), i8* getelementptr inbounds ([29 x i8], [29 x i8]* @.str.11, i32 0, i32 0))
  br label %etiqueta.8
  br label %etiqueta.12

etiqueta.12:
  br label %etiqueta.7

etiqueta.10:
  %puntero.45 = load i1, i1* %es_mayor
  %puntero.46 = xor i1 false, false
  %puntero.47 = icmp eq i1 %puntero.45, %puntero.46
  br i1 %puntero.47, label %etiqueta.13, label %etiqueta.8

etiqueta.13:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.14, i32 0, i32 0), i8* getelementptr inbounds ([21 x i8], [21 x i8]* @.str.13, i32 0, i32 0))
  %puntero.49 = xor i1 true, false
  store i1 %puntero.49, i1* %es_mayor
  br label %etiqueta.7
  br label %etiqueta.7

etiqueta.8:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.16, i32 0, i32 0), i8* getelementptr inbounds ([41 x i8], [41 x i8]* @.str.15, i32 0, i32 0))
  store double null, double* %m
  %puntero.51 = load double, double* %m
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.17, i32 0, i32 0), double %puntero.51)
  %puntero.52 = load double, double* %acumulador
  %puntero.53 = fadd double 0.0, 5.
  %puntero.54 = fdiv double %puntero.52, %puntero.53
  store double %puntero.54, double* %promedio
  %puntero.55 = load double, double* %promedio
  %puntero.56 = load double, double* %m
  %puntero.57 = fcmp one double %puntero.55, %puntero.56
  %puntero.58 = load double, double* %acumulador
  %puntero.59 = fadd double 0.0, 0.
  %puntero.60 = fcmp ole double %puntero.58, %puntero.59
  %puntero.61 = or i1 %puntero.57, %puntero.60
  br i1 %puntero.61, label %etiqueta.14, label %etiqueta.15

etiqueta.14:
  call i32 (i8*, ...) @printf(i8* getelementptr ([6 x i8], [6 x i8]* @.str.19, i32 0, i32 0), i8* getelementptr inbounds ([33 x i8], [33 x i8]* @.str.18, i32 0, i32 0))
  br label %etiqueta.15

etiqueta.15:
  ret i32 0
}

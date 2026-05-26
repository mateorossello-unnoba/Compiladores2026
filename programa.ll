; --- Compilador UNNOBA - 2026 ---
target datalayout = "e-m:w-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

; --- Cadenas Globales ---
@.str.0 = private unnamed_addr constant [40 x i8] c"Iniciando pruebas del compilador UNNOBA\00"
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.2 = private unnamed_addr constant [40 x i8] c"Ingrese un numero entero para comenzar:\00"
@.str.3 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.4 = private unnamed_addr constant [3 x i8] c"%d\00"
@.str.5 = private unnamed_addr constant [26 x i8] c"El valor supera el limite\00"
@.str.6 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.7 = private unnamed_addr constant [40 x i8] c"El valor es exactamente igual al limite\00"
@.str.8 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.9 = private unnamed_addr constant [36 x i8] c"El valor esta por debajo del limite\00"
@.str.10 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.11 = private unnamed_addr constant [77 x i8] c"Acceso fuera de rango para 'arreglo_datos' que es un arreglo de longitud 5.\0A\00"
@.str.12 = private unnamed_addr constant [29 x i8] c"Acumulador alcanzo el maximo\00"
@.str.13 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.14 = private unnamed_addr constant [21 x i8] c"Ajustando bandera...\00"
@.str.15 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.16 = private unnamed_addr constant [41 x i8] c"Calculando la moda del arreglo de datos:\00"
@.str.17 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.18 = private unnamed_addr constant [4 x i8] c"%f\0A\00"
@.str.19 = private unnamed_addr constant [33 x i8] c"Fin de las operaciones con exito\00"
@.str.20 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.21 = private unnamed_addr constant [40 x i8] c"Iniciando pruebas del compilador UNNOBA\00"
@.str.22 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.23 = private unnamed_addr constant [40 x i8] c"Ingrese un numero entero para comenzar:\00"
@.str.24 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.25 = private unnamed_addr constant [3 x i8] c"%d\00"
@.str.26 = private unnamed_addr constant [26 x i8] c"El valor supera el limite\00"
@.str.27 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.28 = private unnamed_addr constant [40 x i8] c"El valor es exactamente igual al limite\00"
@.str.29 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.30 = private unnamed_addr constant [36 x i8] c"El valor esta por debajo del limite\00"
@.str.31 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.32 = private unnamed_addr constant [77 x i8] c"Acceso fuera de rango para 'arreglo_datos' que es un arreglo de longitud 5.\0A\00"
@.str.33 = private unnamed_addr constant [29 x i8] c"Acumulador alcanzo el maximo\00"
@.str.34 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.35 = private unnamed_addr constant [21 x i8] c"Ajustando bandera...\00"
@.str.36 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.37 = private unnamed_addr constant [41 x i8] c"Calculando la moda del arreglo de datos:\00"
@.str.38 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.39 = private unnamed_addr constant [4 x i8] c"%f\0A\00"
@.str.40 = private unnamed_addr constant [33 x i8] c"Fin de las operaciones con exito\00"
@.str.41 = private unnamed_addr constant [4 x i8] c"%s\0A\00"

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
  %puntero.87 = add i32 0, 0
  store i32 %puntero.87, i32* %iterador
  %puntero.88 = add i32 0, 5
  store i32 %puntero.88, i32* %limite
  %puntero.89 = fadd double 0.0, 0.0
  store double %puntero.89, double* %acumulador
  %puntero.90 = xor i1 true, false
  store i1 %puntero.90, i1* %bandera
  %puntero.91 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 0
  store double 1.5, double* %puntero.91
  %puntero.92 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 1
  store double 2.5, double* %puntero.92
  %puntero.93 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 2
  store double 2.5, double* %puntero.93
  %puntero.94 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 3
  store double 3.0, double* %puntero.94
  %puntero.95 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 4
  store double 3.0, double* %puntero.95
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.22, i32 0, i32 0), i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.21, i32 0, i32 0))
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.24, i32 0, i32 0), i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.23, i32 0, i32 0))
  %puntero.98 = alloca i32
  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([9 x i8], [9 x i8]* @.str.25, i64 0, i64 0), i32* %puntero.98)
  %puntero.99 = load i32, i32* %puntero.98
  store i32 %puntero.99, i32* %valor_leido
  %puntero.100 = load i32, i32* %valor_leido
  %puntero.101 = load i32, i32* %limite
  %puntero.102 = icmp sgt i32 %puntero.100, %puntero.101
  %puntero.103 = load i1, i1* %bandera
  %puntero.104 = and i1 %puntero.102, %puntero.103
  br i1 %puntero.104, label %etiqueta.28, label %etiqueta.30

etiqueta.28:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.27, i32 0, i32 0), i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.26, i32 0, i32 0))
  %puntero.106 = xor i1 true, false
  store i1 %puntero.106, i1* %es_mayor
  br label %etiqueta.29

etiqueta.30:
  %puntero.107 = load i32, i32* %valor_leido
  %puntero.108 = load i32, i32* %limite
  %puntero.109 = icmp eq i32 %puntero.107, %puntero.108
  br i1 %puntero.109, label %etiqueta.31, label %etiqueta.33

etiqueta.31:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.29, i32 0, i32 0), i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.28, i32 0, i32 0))
  %puntero.111 = xor i1 false, false
  store i1 %puntero.111, i1* %es_mayor
  br label %etiqueta.32

etiqueta.33:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.31, i32 0, i32 0), i8* getelementptr inbounds ([36 x i8], [36 x i8]* @.str.30, i32 0, i32 0))
  %puntero.113 = xor i1 true, false
  %puntero.114 = xor i1 true, %puntero.113
  store i1 %puntero.114, i1* %es_mayor
  br label %etiqueta.32

etiqueta.32:
  br label %etiqueta.29

etiqueta.29:
  br label %etiqueta.34

etiqueta.34:
  %puntero.115 = load i32, i32* %iterador
  %puntero.116 = load i32, i32* %limite
  %puntero.117 = icmp slt i32 %puntero.115, %puntero.116
  br i1 %puntero.117, label %etiqueta.36, label %etiqueta.37

etiqueta.36:
  %puntero.118 = load double, double* %acumulador
  %puntero.119 = load i32, i32* %iterador
  ; --- CONTROL DE LÍMITES PARA ACCESO A ARREGLO ---
  %puntero.120 = icmp sge i32 %puntero.119, 0
  %puntero.121 = icmp slt i32 %puntero.119, 5
  %puntero.122 = and i1 %puntero.120, %puntero.121
  br i1 %puntero.122, label %etiqueta.38, label %etiqueta.39

etiqueta.39:
  call i32 (i8*, ...) @printf(i8* getelementptr ([77 x i8], [77 x i8]* @.str.32, i32 0, i32 0))
  ret i32 1

etiqueta.38:
  %puntero.123 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 %puntero.119
  %puntero.124 = load double, double* %puntero.123
  %puntero.125 = fadd double %puntero.118, %puntero.124
  store double %puntero.125, double* %acumulador
  %puntero.126 = load i32, i32* %iterador
  %puntero.127 = add i32 0, 1
  %puntero.128 = add i32 %puntero.126, %puntero.127
  store i32 %puntero.128, i32* %iterador
  %puntero.129 = load double, double* %acumulador
  %puntero.130 = fadd double 0.0, 10.0
  %puntero.131 = fcmp oge double %puntero.129, %puntero.130
  br i1 %puntero.131, label %etiqueta.40, label %etiqueta.41

etiqueta.40:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.34, i32 0, i32 0), i8* getelementptr inbounds ([29 x i8], [29 x i8]* @.str.33, i32 0, i32 0))
  br label %etiqueta.35
  br label %etiqueta.41

etiqueta.41:
  br label %etiqueta.34

etiqueta.37:
  %puntero.133 = load i1, i1* %es_mayor
  %puntero.134 = xor i1 false, false
  %puntero.135 = icmp eq i1 %puntero.133, %puntero.134
  br i1 %puntero.135, label %etiqueta.42, label %etiqueta.35

etiqueta.42:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.36, i32 0, i32 0), i8* getelementptr inbounds ([21 x i8], [21 x i8]* @.str.35, i32 0, i32 0))
  %puntero.137 = xor i1 true, false
  store i1 %puntero.137, i1* %es_mayor
  br label %etiqueta.34
  br label %etiqueta.34

etiqueta.35:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.38, i32 0, i32 0), i8* getelementptr inbounds ([41 x i8], [41 x i8]* @.str.37, i32 0, i32 0))
  ; --- INICIO ALGORITMO MODA ---
  %puntero.139 = alloca i32
  %puntero.140 = alloca double
  %puntero.141 = alloca i32
  %puntero.142 = alloca i32
  %puntero.143 = alloca i32
  store i32 0, i32* %puntero.139
  store double 0.0, double* %puntero.140
  store i32 0, i32* %puntero.141
  br label %etiqueta.43

etiqueta.43:
  %puntero.144 = load i32, i32* %puntero.141
  %puntero.145 = icmp slt i32 %puntero.144, 5
  br i1 %puntero.145, label %etiqueta.44, label %etiqueta.52

etiqueta.44:
  %puntero.146 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 %puntero.144
  %puntero.147 = load double, double* %puntero.146
  store i32 0, i32* %puntero.143
  store i32 0, i32* %puntero.142
  br label %etiqueta.45

etiqueta.45:
  %puntero.148 = load i32, i32* %puntero.142
  %puntero.149 = icmp slt i32 %puntero.148, 5
  br i1 %puntero.149, label %etiqueta.46, label %etiqueta.48

etiqueta.46:
  %puntero.150 = getelementptr [5 x double], [5 x double]* %arreglo_datos, i32 0, i32 %puntero.148
  %puntero.151 = load double, double* %puntero.150
  %puntero.152 = fcmp oeq double %puntero.147, %puntero.151
  br i1 %puntero.152, label %etiqueta.47, label %etiqueta.51

etiqueta.47:
  %puntero.153 = load i32, i32* %puntero.143
  %puntero.154 = add i32 %puntero.153, 1
  store i32 %puntero.154, i32* %puntero.143
  br label %etiqueta.51

etiqueta.48:
  %puntero.155 = load i32, i32* %puntero.143
  %puntero.156 = load i32, i32* %puntero.139
  %puntero.157 = icmp sgt i32 %puntero.155, %puntero.156
  br i1 %puntero.157, label %etiqueta.49, label %etiqueta.50

etiqueta.49:
  store i32 %puntero.155, i32* %puntero.139
  store double %puntero.147, double* %puntero.140
  br label %etiqueta.50

etiqueta.50:
  %puntero.158 = add i32 %puntero.144, 1
  store i32 %puntero.158, i32* %puntero.141
  br label %etiqueta.43

etiqueta.51:
  %puntero.159 = add i32 %puntero.148, 1
  store i32 %puntero.159, i32* %puntero.142
  br label %etiqueta.45

etiqueta.52:
  %puntero.160 = load double, double* %puntero.140
  ; --- FIN ALGORITMO MODA ---
  store double %puntero.160, double* %m
  %puntero.161 = load double, double* %m
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.39, i32 0, i32 0), double %puntero.161)
  %puntero.162 = load double, double* %acumulador
  %puntero.163 = fadd double 0.0, 5.
  %puntero.164 = fdiv double %puntero.162, %puntero.163
  store double %puntero.164, double* %promedio
  %puntero.165 = load double, double* %promedio
  %puntero.166 = load double, double* %m
  %puntero.167 = fcmp one double %puntero.165, %puntero.166
  %puntero.168 = load double, double* %acumulador
  %puntero.169 = fadd double 0.0, 0.
  %puntero.170 = fcmp ole double %puntero.168, %puntero.169
  %puntero.171 = or i1 %puntero.167, %puntero.170
  br i1 %puntero.171, label %etiqueta.53, label %etiqueta.54

etiqueta.53:
  call i32 (i8*, ...) @printf(i8* getelementptr ([4 x i8], [4 x i8]* @.str.41, i32 0, i32 0), i8* getelementptr inbounds ([33 x i8], [33 x i8]* @.str.40, i32 0, i32 0))
  br label %etiqueta.54

etiqueta.54:
  ret i32 0
}

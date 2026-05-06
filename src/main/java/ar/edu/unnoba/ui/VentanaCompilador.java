package ar.edu.unnoba.ui;

import ar.edu.unnoba.ast.Programa;
import ar.edu.unnoba.Lexer;
import ar.edu.unnoba.Parser;
import ar.edu.unnoba.sym;
import java.awt.*;
import java.awt.event.ActionEvent;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana principal del compilador con interfaz gráfica.
 * Permite cargar archivos de código fuente, editarlos, guardarlos y compilar directamente desde la interfaz.
 */

public class VentanaCompilador extends JFrame {
    private JTextArea areaCodigo;
    private JTextArea areaConsola;

    public VentanaCompilador() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {}

        // Configuración básica de la ventana
        setTitle("Compilador UNNOBA - 2026");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR (Botones) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSuperior.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton botonCargar = crearBotonEstilizado("📂 Cargar Archivo", null);
        JButton botonGuardar = crearBotonEstilizado("💾 Guardar Archivo", null);
        JButton botonLimpiar = crearBotonEstilizado("❌ Limpiar Consola", null);
        JButton botonCompilarLexico = crearBotonEstilizado("▶ Análisis Léxico", null); 
        JButton botonCompilarSintactico = crearBotonEstilizado("▶ Análisis Sintáctico", null);
        JButton botonCompilarSemantico = crearBotonEstilizado("▶ Análisis Semántico", null);

        panelSuperior.add(botonCargar);
        panelSuperior.add(botonGuardar);
        panelSuperior.add(botonLimpiar);
        panelSuperior.add(botonCompilarLexico);
        panelSuperior.add(botonCompilarSintactico);
        panelSuperior.add(botonCompilarSemantico);

        add(panelSuperior, BorderLayout.NORTH);

        // --- ÁREA CENTRAL (Editor de Código) ---
        areaCodigo = new JTextArea();
        areaCodigo.setFont(new Font("Monospaced", Font.PLAIN, 15));
        areaCodigo.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollCodigo = new JScrollPane(areaCodigo);
        scrollCodigo.setBorder(BorderFactory.createTitledBorder(" Editor de Código "));

        // --- ÁREA INFERIOR (Consola de Salida) ---
        areaConsola = new JTextArea();
        areaConsola.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaConsola.setEditable(false);
        areaConsola.setMargin(new Insets(10, 10, 10, 10));
        areaConsola.setBackground(new Color(33, 33, 33));
        areaConsola.setForeground(new Color(152, 251, 152)); 
        
        JScrollPane scrollConsola = new JScrollPane(areaConsola);
        scrollConsola.setBorder(BorderFactory.createTitledBorder(" Consola de Salida "));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCodigo, scrollConsola);
        splitPane.setDividerLocation(380);
        splitPane.setBorder(new EmptyBorder(0, 10, 10, 10));
        add(splitPane, BorderLayout.CENTER);

        botonCargar.addActionListener((ActionEvent event) -> cargarArchivo());
        botonGuardar.addActionListener((ActionEvent event) -> guardarArchivo());
        botonLimpiar.addActionListener((ActionEvent event) -> limpiarConsola());
        botonCompilarLexico.addActionListener((ActionEvent event) -> analizarLexico());
        botonCompilarSintactico.addActionListener((ActionEvent event) -> analizarSintactico());
        botonCompilarSemantico.addActionListener((ActionEvent event) -> analizarSemantico());
    }

    private JButton crearBotonEstilizado(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setRequestFocusEnabled(false);
        boton.setFocusable(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));

        if (fondo != null) {
            boton.setBackground(fondo);
            boton.setOpaque(true);
            boton.setBorderPainted(false);
        }

        return boton;
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser(".");

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
                areaCodigo.setText("");
                String linea;

                while ((linea = bufferedReader.readLine()) != null) {
                    areaCodigo.append(linea + "\n");
                }

                areaConsola.setText("--- Archivo '" + archivo.getName() + "' cargado correctamente ---\n");
            } catch (IOException exception) {
                areaConsola.setText("--- No se pudo leer el archivo ---\n");
            }
        }
    }

    private void guardarArchivo() {
        JFileChooser fileChooser = new JFileChooser(".");

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(archivo))) {
                bufferedWriter.write(areaCodigo.getText());
                areaConsola.setText("--- Archivo '" + archivo.getName() + "' guardado correctamente ---\n");
            } catch (IOException exception) {
                areaConsola.setText("--- No se pudo guardar el archivo ---\n");
            }
        }
    }

    private void limpiarConsola() {
        areaConsola.setText("");
    }

    private void analizarLexico() {
        String codigo = areaCodigo.getText();

        if (codigo.trim().isEmpty()) {
            areaConsola.setText("--- El editor está vacío ---");
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        System.setOut(printStream);
        System.setErr(printStream);

        try {
            System.out.println("--- Iniciando Análisis Léxico ---\n");
            
            Lexer lexico = new Lexer(new StringReader(codigo));
            Symbol token;
            
            while ((token = lexico.next_token()) != null && token.sym != sym.EOF) {
                String nombreToken;
                
                if (token.sym >= 0 && token.sym < sym.terminalNames.length) {
                    nombreToken = sym.terminalNames[token.sym];
                } else {
                    nombreToken = "DESCONOCIDO (" + token.sym + ")";
                }
                
                System.out.println("[LEXER] Token: [" + nombreToken + "] | Lexema: " + token.value);
            }

            System.out.println("\n--- Análisis Léxico finalizado sin errores ---");
        } catch (Error error) {
            System.err.println("\n[ERROR LÉXICO] " + error.getMessage());
        } catch (Exception exception) {
            System.err.println("\nError: " + exception.getMessage());
        } finally {
            System.out.flush();
            System.setOut(oldOut);
            System.setErr(oldErr);
            areaConsola.setText(byteArrayOutputStream.toString());
        }
    }

    private void analizarSintactico() {
        String codigo = areaCodigo.getText();

        if (codigo.trim().isEmpty()) {
            areaConsola.setText("--- El editor está vacío ---");
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        System.setOut(printStream);
        System.setErr(printStream);

        try {
            System.out.println("--- Iniciando Análisis Sintáctico ---\n");

            Lexer lexico = new Lexer(new StringReader(codigo));
            Parser parser = new Parser(lexico, new ComplexSymbolFactory());

            Symbol resultado = parser.parse();
            Programa programa = (Programa) resultado.value;
            generarArbolSintaxisAbstracta(programa);

            System.out.println("\n--- Análisis Sintáctico finalizado sin errores ---");
        } catch (Exception exception) {
            System.err.println("\n[ERROR SINTÁCTICO] " + exception.getMessage());
        } finally {
            System.out.flush();
            System.setOut(oldOut);
            System.setErr(oldErr);
            areaConsola.setText(byteArrayOutputStream.toString());
        }
    }

    private void generarArbolSintaxisAbstracta(Programa programa) {
        try {
            StringBuilder dotCode = new StringBuilder();
            dotCode.append("graph AST {\n");
            dotCode.append("  node [shape=box, style=filled, color=lightblue, fontname=\"Helvetica\"];\n");
            dotCode.append("  edge [color=gray30];\n\n");

            dotCode.append(programa.graficar("")); 
            
            dotCode.append("}\n");

            File dotFile = new File("ast.dot");
            try (FileWriter writer = new FileWriter(dotFile)) {
                writer.write(dotCode.toString());
            }

            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "ast.dot", "-o", "ast.png");
            processBuilder.redirectErrorStream(true);
            Process proceso = processBuilder.start();
            proceso.waitFor();

            System.out.println("[SISTEMA] Árbol de Sintaxis Abstracta generado según el formato requerido");
        } catch (Exception exception) {
            System.err.println("[SISTEMA] Se creó 'ast.dot', pero no se pudo generar la imagen. Corrobore que Graphviz esté instalado y definido en el PATH del sistema.");
        }
    }

    private void analizarSemantico() {
        String codigo = areaCodigo.getText();

        if (codigo.trim().isEmpty()) {
            areaConsola.setText("--- El editor está vacío ---");
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        System.setOut(printStream);
        System.setErr(printStream);

        try {
            System.out.println("--- Iniciando Análisis Semántico ---\n");

            Lexer lexico = new Lexer(new StringReader(codigo));
            Parser parser = new Parser(lexico, new ComplexSymbolFactory());
            Symbol resultado = parser.parse();
            Programa programa = (Programa) resultado.value;
            generarArbolSintaxisAbstracta(programa);
            
            programa.chequearSemantica(parser.tablaSimbolos);

            System.out.println("\n--- Análisis Semántico finalizado sin errores ---");
        } catch (Exception exception) {
            System.err.println("\n" + exception.getMessage());
            System.err.println("\n[ERROR SEMÁNTICO] Error Semántico Fatal.");
        } finally {
            System.out.flush();
            System.setOut(oldOut);
            System.setErr(oldErr);
            areaConsola.setText(byteArrayOutputStream.toString());
        }
    }
}

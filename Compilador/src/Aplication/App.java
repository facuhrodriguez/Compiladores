package Aplication;

import java.io.IOException;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.FileHandler;
import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.Parser;
import CodigoIntermedio.CodigoIntermedio;
import GeneracionCodigoAssembler.GeneradorAssembler;

public class App {
	static FileHandler file;
	static AnalizadorLexico analizadorLexico;
	static AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
	static GeneradorAssembler assembler;
	
	public static void main(String[] args) throws IOException {
		file = new FileHandler(args[0]);
		analizadorLexico = new AnalizadorLexico(file);
		Parser parser = new Parser();
		parser.setLexico(analizadorLexico);
		parser.setSintactico(analizadorSintactico);
		analizadorSintactico.setLexico(analizadorLexico);
		parser.setTS(analizadorLexico.getTS());
		CodigoIntermedio code = new CodigoIntermedio();
		parser.setCodigoIntermedio(code);
		analizadorSintactico.setCodigoIntermedio(code);
		parser.run();

		System.out.println(" ----------------------------------------- ANALIZADOR SINTACTICO -----------------------------------------");
//		analizadorSintactico.printStructures();
		System.out.println('\n');
		System.out.println("------------------------- ANALIZADOR LEXICO ------------------------------");
//		analizadorLexico.printTokens();
		analizadorLexico.printTablaSimbolos();		

//		analizadorLexico.printErrors();	
//		analizadorLexico.printWarnings();
		
		System.out.println("\n" + "\n" + "Estructura de Cï¿½digo Intermedio (Polaca Inversa)");
		code.printPolaca();
		

//		
		System.out.println("\n" + "\n" + "Estructura de Código Intermedio (Polaca Inversa)");
		code.printPolaca();

		System.out.println("Errores \n");
		analizadorLexico.printErrors();	
		analizadorSintactico.printErrors();
		analizadorLexico.printWarnings();
		
		System.out.println("\n" + "Errores Semánticos");

		code.printErrors();
		
		if (analizadorLexico.hayErrores() || analizadorSintactico.hayErrores() || code.hayErrores())
			System.out.println("ERROR - NO SE GENERA CÓDIGO ASSEMBLER POR ERRORES EN EL CODIGO");
		else { 
			System.out.println("\n" + "\n" + "Estructura de Código Intermedio (Polaca Inversa)");
			code.printPolaca();
			assembler = new GeneradorAssembler(code, analizadorLexico.getTS());
			assembler.generarArchivoAssembler();
			analizadorLexico.printTablaSimbolos();
		}
	}

}

package Aplication;

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
	
	public static void main(String[] args) {
		file = new FileHandler(args[0]);
		analizadorLexico = new AnalizadorLexico(file);
		Parser parser = new Parser();
		parser.setLexico(analizadorLexico);
		parser.setSintactico(analizadorSintactico);
		analizadorSintactico.setLexico(analizadorLexico);
		parser.setTS(analizadorLexico.getTS());
		CodigoIntermedio code = new CodigoIntermedio ();
		parser.setCodigoIntermedio(code);
		analizadorSintactico.setCodigoIntermedio(code);
		parser.run();
//
//		System.out.println(" ----------------------------------------- ANALIZADOR SINTACTICO -----------------------------------------");
//		analizadorSintactico.printErrors();
//		analizadorSintactico.printStructures();
//		System.out.println('\n');
//		System.err.println('\n');
//		System.out.println("------------------------- ANALIZADOR LEXICO ------------------------------");
////		analizadorLexico.printTokens();
//		analizadorLexico.printTablaSimbolos();		
//		analizadorLexico.printErrors();	
//		analizadorLexico.printWarnings();
//		
//		System.out.println("\n" + "\n" + "Estructura de Código Intermedio (Polaca Inversa)");
//		code.printPolaca();
//		System.out.println("\n" + "Errores Semánticos");
		//code.printErrors();
		
		assembler = new GeneradorAssembler(code, analizadorLexico.getTS());
		
		assembler.generarAssembler();
		
	}

}

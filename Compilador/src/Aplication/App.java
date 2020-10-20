package Aplication;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.FileHandler;
import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.Parser;
import CodigoIntermedio.CodigoIntermedio;

public class App {
	static FileHandler file;
	static AnalizadorLexico analizadorLexico;
	static AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
	
	public static void main(String[] args) {
		file = new FileHandler(args[0]);
		analizadorLexico = new AnalizadorLexico(file);
		Parser parser = new Parser();
		parser.setLexico(analizadorLexico);
		parser.setSintactico(analizadorSintactico);
		analizadorSintactico.setLexico(analizadorLexico);
		parser.setTS(analizadorLexico.getTS());
		//CodigoIntermedio code = new CodigoIntermedio ();
	//	parser.setCodigoIntermedio(code);
	//	analizadorSintactico.setCodigoIntermedio(code);
		parser.run();

		//System.out.println("Analizador Sintï¿½ctico");
		analizadorSintactico.printErrors();
		analizadorSintactico.printStructures();
//		for (int i=0; i < file.getContentFile().length(); i++)
//			analizadorLexico.yylex();
		System.out.println('\n');
		System.err.println('\n');
		System.out.println("Analizador Léxico");
		analizadorLexico.printTokens();
		analizadorLexico.printTablaSimbolos();		
		analizadorLexico.printErrors();	
		analizadorLexico.printWarnings();
//		code.printPolaca();

	}

}

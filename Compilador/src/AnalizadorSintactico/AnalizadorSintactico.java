package AnalizadorSintactico;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Error;
import CodigoIntermedio.CodigoIntermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AnalizadorSintactico {
	
	private HashMap<String, Integer> structures = new HashMap<String, Integer>();
	private AnalizadorLexico l; 
	private int count;
	protected static final Integer maxProcPar = 3;
	private CodigoIntermedio polaca;
	
	// Estructuras sint�cticas
	protected static final String principalStruct = "Programa principal";
	protected static final String declarativeStruct = "Declaraci�n de variables";
	protected static final String procStruct = "Procedimiento";
	protected static final String invocProcStructure = "Invocaci�n a procedimiento";
	protected static final String outStruct = "Sentencia de salida";
	protected static final String asigStruct = "Asignaci�n";
	protected static final String ifStructure = "Sentencia IF";
	protected static final String whileStructure = "Sentencia WHILE";
	protected static final String loopStructure = "Sentencia LOOP";
	protected static final String conditionStructure = "Condici�n";
	
	
	// Errores sint�cticos
	public static final String errorPrincipal = "Error en la generaci�n del programa principal";
	protected static final String errorDeclarative = "Error en declaraci�n de variables";
	protected static final String errorProc = "Error en la declaraci�n de procedimiento";
	protected static final String errorMaxProcPar = "Cantidad de par�metros excedida (Max: 3)";
	protected static final String errorFactor = "Error en la definici�n de constante";
	protected static final String conditionError = "Error en condici�n.";
	public static final String errorOperatorComp = "Error en el comparador de comparacion";
	public static final String sentencia =  "Error en generaci�n de sentencia";
	public static final String parFinal = "Error: Falta cerrar el par�ntesis";
	public static final String llaveFinal = "Error: Falta cerrar la llave";

	private ArrayList<Error> syntaxErrors;
	
	public AnalizadorSintactico() {
		this.syntaxErrors = new ArrayList<Error>();
		this.count = 0;
		polaca = new CodigoIntermedio();
	}
	
	public AnalizadorSintactico(AnalizadorLexico l) {
		this.l = l;
	}
	
	
	public void setLexico(AnalizadorLexico l) {
		this.l = l;
	}
	
	public void addSyntaxError(Error e) {
		if (e != null)
			this.syntaxErrors.add(e);
	}
	
	public void addSyntaxStruct(String s) {
		if (s != null)
			if (this.structures.containsKey(s))
				this.structures.put(s + ' ' + this.count++, this.l.getLine());
			else 
				this.structures.put(s, this.l.getLine());
	}
	
	public void printStructures() {
		System.out.println('\n');
		System.out.println("Estructuras encontradas");
		for (String key : this.structures.keySet())
			if (key != AnalizadorSintactico.principalStruct)
				System.out.println(key + " en l�nea " + this.structures.get(key));
			else 
				System.out.println(key);
	}
	
	
	public void printErrors() {
		System.out.println("\n");
		if (this.syntaxErrors.size() > 0) {
			System.err.println("ERRORES SINTACTICOS");
			for (Error e : this.syntaxErrors) {
				System.out.println(e);
			}
		}
		else 
			System.out.println("No hay errores sint�cticos");
	}
	
	public void setCodigoIntermedio (CodigoIntermedio i) {
		this.polaca = i;
	}
}

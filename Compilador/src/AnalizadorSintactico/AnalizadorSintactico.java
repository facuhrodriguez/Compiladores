package AnalizadorSintactico;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Error;
import CodigoIntermedio.CodigoIntermedio;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalizadorSintactico {
	
	private HashMap<String, Integer> structures = new HashMap<String, Integer>();
	private AnalizadorLexico l; 
	private int count;
	protected static final Integer maxProcPar = 3;
	private CodigoIntermedio polaca;
	private String nameProc; 
	
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
	protected static final String errorProc = "Error en la declaraci�n de procedimiento en l�nea ";
	protected static final String errorMaxProcPar = "Cantidad de par�metros excedida (Max: 3) en l�nea " ;
	public static final String errorCondition = "Falta la condici�n de la sentencia en l�nea ";
	public static final String errorOperatorComp = "Error en el comparador de comparacion";
	public static final String sentencia =  "Error en generaci�n de sentencia";
	public static final String parFinal = "Error: Falta cerrar el par�ntesis";
	public static final String llaveFinal = "Error: Falta cerrar la llave";
	public static final String conditionError = "Error en la condici�n en l�nea ";
	public static final String errorProcedure = "Error - declaraci�n de procedimiento incorrecta";
	public static final String sinPar = "Error - Condici�n sin par�ntesis en l�nea ";
	public static final String parI = "Error - Falta par�ntesis inicial en la condici�n en l�nea ";
	public static final String errorSentenciaEjecutable = "Error en sentencia ejecutable en l�nea ";
	public static final String sinLlaves = "Faltan llaves de inicio de bloque en l�nea ";
	public static final String faltaEndIf = "Falta la palabra reservada END_IF en l�nea ";

	
	// Chequeos sem�nticos
	public static final String VARIABLE = "Variable";
	public static final String NOMBREPROC = "Nombre de procedimiento";
	public static final String NOMBREPAR = "Nombre de parametro";
	public static final String CONSTANTE = "Constante";
	
	private ArrayList<Error> syntaxErrors;
	
	public AnalizadorSintactico() {
		this.syntaxErrors = new ArrayList<Error>();
		this.count = 0;
		polaca = new CodigoIntermedio();
		this.nameProc = "main";
	}
	
	public AnalizadorSintactico(AnalizadorLexico l) {
		this.l = l;
	}
	
	
	public void setLexico(AnalizadorLexico l) {
		this.l = l;
	}
	
	public void addSyntaxError(Error e) {
		if (e != null && ! this.syntaxErrors.contains(e))
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
		
		if (this.structures.size() == 0) {
			System.out.println("No se encontraron estructuras sint�cticas");
			return;
		}
		System.out.println("Estructuras encontradas: ");
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
	
	public void setNombreProcedimiento(String a) {
		//this.nameProc = this.nameProc.concat("@");
		this.nameProc = a.concat("@").concat(this.nameProc);
	}
	
	public void removeNombreProcedimiento(String a) {
		String aux = this.nameProc.substring(0, nameProc.indexOf("@"));
		this.nameProc = aux.replace(a, "").concat(this.nameProc.substring(nameProc.indexOf("@"), nameProc.length()));
		this.nameProc = this.nameProc.replaceFirst("@", "");
	}
	
	public String getNombreProcedimiento() {
		return this.nameProc;
	}
	
	public boolean hayErrores() {
		return (this.syntaxErrors.size() > 0);
	}
}

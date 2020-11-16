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
	
	// Estructuras sintácticas
	protected static final String principalStruct = "Programa principal";
	protected static final String declarativeStruct = "Declaración de variables";
	protected static final String procStruct = "Procedimiento";
	protected static final String invocProcStructure = "Invocación a procedimiento";
	protected static final String outStruct = "Sentencia de salida";
	protected static final String asigStruct = "Asignación";
	protected static final String ifStructure = "Sentencia IF";
	protected static final String whileStructure = "Sentencia WHILE";
	protected static final String loopStructure = "Sentencia LOOP";
	protected static final String conditionStructure = "Condición";

	
	// Errores sintácticos
	public static final String errorPrincipal = "Error en la generación del programa principal";
	protected static final String errorProc = "Error en la declaración de procedimiento en línea ";
	protected static final String errorMaxProcPar = "Cantidad de parámetros excedida (Max: 3) en línea " ;
	public static final String errorCondition = "Falta la condición de la sentencia en línea ";
	public static final String errorOperatorComp = "Error en el comparador de comparacion";
	public static final String sentencia =  "Error en generación de sentencia";
	public static final String parFinal = "Error: Falta cerrar el paréntesis";
	public static final String llaveFinal = "Error: Falta cerrar la llave";
	public static final String conditionError = "Error en la condición en línea ";
	public static final String errorProcedure = "Error - declaración de procedimiento incorrecta";
	public static final String sinPar = "Error - Condición sin paréntesis en línea ";
	public static final String parI = "Error - Falta paréntesis inicial en la condición en línea ";
	public static final String errorSentenciaEjecutable = "Error en sentencia ejecutable en línea ";
	public static final String sinLlaves = "Faltan llaves de inicio de bloque en línea ";
	public static final String faltaEndIf = "Falta la palabra reservada END_IF en línea ";

	
	// Chequeos semánticos
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
			System.out.println("No se encontraron estructuras sintácticas");
			return;
		}
		System.out.println("Estructuras encontradas: ");
		for (String key : this.structures.keySet())
			if (key != AnalizadorSintactico.principalStruct)
				System.out.println(key + " en línea " + this.structures.get(key));
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
			System.out.println("No hay errores sintácticos");
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
}

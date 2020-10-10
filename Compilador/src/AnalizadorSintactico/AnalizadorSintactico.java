package AnalizadorSintactico;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Error;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AnalizadorSintactico {
	
	private HashMap<String, Integer> structures = new HashMap<String, Integer>();
	private AnalizadorLexico l; 
	private int count;
	protected static final Integer maxProcPar = 3;
	
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
	protected static final String errorDeclarative = "Error en declaración de variables";
	protected static final String errorProc = "Error en la declaración de procedimiento";
	protected static final String errorMaxProcPar = "Cantidad de parámetros excedida (Max: 3)";
	protected static final String errorFactor = "Error en la definición de constante";
	protected static final String conditionError = "Error en condición.";
	public static final String errorOperatorComp = "Error en el comparador de comparacion";
	public static final String sentencia =  "Error en generación de sentencia";
	public static final String parFinal = "Error: Falta cerrar el paréntesis";
	public static final String llaveFinal = "Error: Falta cerrar la llave";

	private ArrayList<Error> syntaxErrors;
	
	public AnalizadorSintactico() {
		this.syntaxErrors = new ArrayList<Error>();
		this.count = 0;
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
}

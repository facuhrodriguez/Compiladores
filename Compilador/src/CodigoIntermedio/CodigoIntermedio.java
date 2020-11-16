package CodigoIntermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import AnalizadorLexico.Error;

public class CodigoIntermedio {
	
	private HashMap<Integer, String> polaca;
	public static Integer polacaNumber;
	private Stack<Integer> stack;
	private Stack<Integer> stackProcedure;
	private List<Error> errors = new ArrayList<Error>();
	private List<String> operadoresBinarios = new ArrayList<String>();
	private List<String> operadoresUnarios = new ArrayList<String>();
	private List<String> operadores = new ArrayList<String>();
	// Errores semánticos
	public static final String VAR_NO_DECLARADA = "Error - Variable no declarada en línea ";
	public static final String VAR_RE_DECLARADA = "Error - Variable re-declarada en línea ";
	public static final String CONSTANTE_NI = "Error - La constante que indica el número de invocaciones debe ser entera - En línea ";
	public static final String ERROR_CONVERSION = "Error - No se puede hacer la conversión a tipo DOUBLE en línea";
	public static final String ERROR_INVOCACION_PAR = "Error - El tipo del parámetro no coincide con los utilizados en la invocación en línea ";
	public static final String ERROR_CANT_PARAM = "Error - La cantidad de parámetros de la invocación no coincide con la declaración del procedimiento en línea ";
	public static final String PROC_NO_DECLARADO = "Error - El procedimiento no existe en línea ";
	public static final String ERROR_INVOCACIONES_PROC = "Error - Cantidad de invocaciones a procedimiento excedida en línea ";
	public static final String ERROR_PARAM_PROC = "Error - El parámetro no existe en la declaración del procedimiento en línea ";
	public CodigoIntermedio() {
		this.polaca = new HashMap<Integer, String>();
		CodigoIntermedio.polacaNumber = 1;
		this.stack = new Stack<Integer>();
		this.stackProcedure = new Stack<Integer>();
		this.setOperadoresBinarios();
		this.setOperadoresUnarios();
		this.setOperadores();
	}
	
	/**
	 * Agrega el operando a la polaca, junto con el paso correspondiente
	 * @param op
	 */
	public void addOperando(String op) {
		
		if (op != null) {
			this.polaca.put(CodigoIntermedio.polacaNumber, op);
			CodigoIntermedio.polacaNumber++;
		}
	}
	
	/**
	 * Agrega el operador a la polaca, junto con el paso correspondiente
	 * @param op
	 */
	public void addOperador(String op) {
		if (op != null) {
			this.polaca.put(CodigoIntermedio.polacaNumber, op);
			CodigoIntermedio.polacaNumber++;
		}
	}
	
	/**
	 * Establece el paso de la polaca
	 * @param l
	 */
	public void setNumber(Integer l) {
		CodigoIntermedio.polacaNumber = l;
	}
	
	/**
	 * Agrega el elemento l a la pila
	 * @param l
	 */
	public void stackUp(Integer l) {
		this.stack.push(l);
	}
	
	/**
	 * Agrega el elemento p a la pila de procedimientos
	 * @param p
	 */
	public void stackUpProcedure(Integer p) {
		this.stackProcedure.push(p);
	}
	
	/**
	 * Obtiene el tope de la pila y lo desapila
	 * @return
	 */
	public Integer getTop() {
		if (!this.stack.isEmpty())
			return this.stack.pop();
		return null;
	}
	
	public Integer getTopProcedure() {
		if (!this.stackProcedure.isEmpty())
			return this.stackProcedure.pop();
		return null;
	}
	
	/**
	 * Agrega la dirección de memoria al paso correspondiente
	 * @param paso
	 * @param op
	 */
	public void addDirection(Integer paso, Integer direction) {
		if (paso != null && this.polaca.containsKey(paso) && direction != null)
			this.polaca.put(paso, direction.toString());
	}
	
	public void printPolaca () {
		System.out.println("\n");
		for (Integer polaca : this.polaca.keySet()) {
			System.out.print(" 	");
			System.out.println(polaca + " 	|	" + this.polaca.get(polaca));
		}
	}
	
	/**
	 * Agrega los errores semánticos encontrados
	 * @param e
	 */
	public void addSemanticError(Error e) {
		this.errors.add(e);
	}

	public void printErrors() {
		System.out.println("\n");
		for (Error e : this.errors) {
			System.out.println(e.toString());
		}
		
	}
	
	public ArrayList<String> getStructure() {
		return (ArrayList<String>) this.polaca.values();
	}
	
	/**
	 * Establece todos los operadores binarios posibles
	 */
	public void setOperadoresBinarios() {
		this.operadoresBinarios.add("+");
		this.operadoresBinarios.add("-");
		this.operadoresBinarios.add("*");
		this.operadoresBinarios.add("/");
		this.operadoresBinarios.add("<");
		this.operadoresBinarios.add(">");
		this.operadoresBinarios.add("<=");
		this.operadoresBinarios.add(">=");
		this.operadoresBinarios.add("==");
		this.operadoresBinarios.add("!=");
		this.operadoresBinarios.add("=");
	}
	
	/**
	 * Establece todos los operadores unarios posibles
	 */
	public void setOperadoresUnarios() {
		this.operadoresUnarios.add("OUT");
		this.operadoresUnarios.add("BI");
		this.operadoresUnarios.add("BF");
	}
	
	/**
	 * Indica si el operador es binario
	 * @param b
	 * @return
	 */
	public boolean isBinary(String b) {
		return this.operadoresBinarios.contains(b);
	}
	

	/**
	 * Indica si el operador es unario
	 * @param b
	 * @return
	 */
	public boolean isUnary(String b) {
		return this.operadoresUnarios.contains(b);
	}
	

	/**
	 * Indica si el String b es operador
	 * @param b
	 * @return
	 */
	public boolean isOperator(String b) {
		return this.operadores.contains(b);
	}
	
	/**
	 * Establece todos los operadores
	 */
	public void setOperadores() {
		this.operadores.add("+");
		this.operadores.add("-");
		this.operadores.add("*");
		this.operadores.add("/");
		this.operadores.add("<");
		this.operadores.add(">");
		this.operadores.add("<=");
		this.operadores.add(">=");
		this.operadores.add("==");
		this.operadores.add("!=");
		this.operadores.add("=");
		this.operadores.add("OUT");
		this.operadores.add("BI");
		this.operadores.add("BF");
	}
	
}

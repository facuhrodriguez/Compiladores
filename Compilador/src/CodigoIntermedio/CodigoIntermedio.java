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
	private List<Error> errors = new ArrayList<Error>();
	
	// Errores semánticos
	public static final String VAR_NO_DECLARADA = "Error - Variable no declarada en línea ";
	public static final String VAR_RE_DECLARADA = "Error - Variable re-declarada en línea ";
	
	public CodigoIntermedio() {
		this.polaca = new HashMap<Integer, String>();
		CodigoIntermedio.polacaNumber = 1;
		this.stack = new Stack<Integer>();
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
	 * Obtiene el tope de la pila y lo desapila
	 * @return
	 */
	public Integer getTop() {
		if (!this.stack.isEmpty())
			return this.stack.pop();
		return null;
	}
	
	
	/**
	 * Agrega la direcciï¿½n de memoria al paso correspondiente
	 * @param paso
	 * @param op
	 */
	public void addDirection(Integer paso, Integer direction) {
		if (this.polaca.containsKey(paso))
			this.polaca.put(paso, direction.toString());
	}
	
	public void printPolaca () {
		System.out.println("\n");
		for (Integer polaca : this.polaca.keySet()) {
			System.out.println(polaca + " " + this.polaca.get(polaca));
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
	
}

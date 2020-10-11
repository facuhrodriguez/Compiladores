package CodigoIntermedio;

import java.util.HashMap;
import java.util.Stack;

public class CodigoIntermedio {
	
	private HashMap<String, Integer> polaca;
	public static Integer polacaNumber;
	private Stack<Integer> stack;
	
	public CodigoIntermedio() {
		this.polaca = new HashMap<String, Integer>();
		CodigoIntermedio.polacaNumber = 0;
		this.stack = new Stack<Integer>();
	}
	
	/**
	 * Agrega el operando a la polaca, junto con el paso correspondiente
	 * @param op
	 */
	public void addOperando(String op) {
		if (op != null) {
			this.polaca.put(op, CodigoIntermedio.polacaNumber);
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
		return this.stack.pop();
	}
	
}

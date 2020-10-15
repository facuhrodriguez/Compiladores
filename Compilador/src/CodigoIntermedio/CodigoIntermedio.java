package CodigoIntermedio;

import java.util.HashMap;
import java.util.Stack;

public class CodigoIntermedio {
	
	private HashMap<Integer, String> polaca;
	public static Integer polacaNumber;
	private Stack<Integer> stack;
	
	public CodigoIntermedio() {
		this.polaca = new HashMap<Integer, String>();
		CodigoIntermedio.polacaNumber = 0;
		this.stack = new Stack<Integer>();
	}
	
	/**
	 * Agrega el operando a la polaca, junto con el paso correspondiente
	 * @param op
	 */
	public void addOperando(String op) {
		System.out.println("op " + op);
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
		return this.stack.pop();
	}
	
	
	/**
	 * Agrega la direcci�n de memoria al paso correspondiente
	 * @param paso
	 * @param op
	 */
	public void addDirection(Integer paso, Integer direction) {
		if (this.polaca.containsKey(paso))
			this.polaca.put(paso, direction.toString());
	}
	
	public void printPolaca () {
		System.out.println("Llego " + this.polaca.keySet());
		for (Integer polaca : this.polaca.keySet()) {
			System.out.println("paso " + polaca + " " + this.polaca.get(polaca));
		}
	}
	
}

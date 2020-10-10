package AnalizadorLexico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class TablaDeSimbolos {
	private Hashtable <String, Token > simbolos;
	private Iterator i;
	
	public TablaDeSimbolos () {
		simbolos = new Hashtable<String,Token>();		
	}
	
	
	/**
	 * Agrega el Token nuevo a la tabla
	 * @param lexema
	 * @param t
	 */
	public void addToken (String lexema , Token t) {
		if (t != null)
			simbolos.put(lexema, t);
		i = (Iterator) simbolos.keys();
	}
	/**
	 * Devuelve el Token de la tabla
	 * @param lexema
	 * @return
	 */
	public Token getToken (String lexema) {
		return simbolos.get(lexema);
	}
	
	public Collection<Token> getTokens() {
		Collection<Token> values = this.simbolos.values();
		return values;
	}
	
	
	/**
	 * Imprime por pantalla la tabla de simbolo en el momento. Cuando el iterador llega
	 al final, este se resetea al principio de la tabla de nuevo*/
	@Override 
	public String toString () {
		int number = 0;
		while (this.i.hasNext() ){
			System.out.print(number + " ");
			String lexema = (String) i.next();
			Token t = simbolos.get(lexema);
			number++;
			return t.toString() +" "+ lexema + "\n";
		}
		this.i = (Iterator) simbolos.keys();
		return "";
		
	}
	
}

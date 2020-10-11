package AnalizadorLexico;

import java.util.Collection;
import java.util.Hashtable;

public class TablaDeSimbolos {
	private Hashtable <String, Token > simbolos;
	
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
//	public String toString () {
//		int number = 0;
//		return "";
////		while (this.i.hasNext() ){
////			System.out.print(number + " ");
////			String lexema = (String) i.next();
////			Token t = simbolos.get(lexema);
////			number++;
////			return t.toString() +" "+ lexema + "\n";
////		}
////		this.i = (Iterator) simbolos.keys();
////		return "";
//		
//	}
	
}

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
	
	
	public boolean exist(String l) {
		return this.simbolos.containsKey(l);
	}
	
	public void print() {
		for (Token t : this.getTokens()) {
			if ( t.getAttr("TIPO") == null || t.getAttr("TIPO") == "") 
				t.addAttr("TIPO", "-");
			if (t.getAttr("USO") == null)
				t.addAttr("USO", "-");
			if (t.getAttr("AMBITO") == null)
				t.addAttr("AMBITO", "-");
			System.out.println("-----------------------------------------------------------------------------------------------"
					+ "--------------------------------------------------------------------------------------------------------");
			System.out.println(t.getAttr("NUMERO DE TOKEN") + "|		|" + t.getAttr("NOMBRE") + "|				|"
					+	t.getAttr("TIPO") +  "|				|" + t.getAttr("USO") + "|				|" + t.getAttr("AMBITO"));
			
			}
	}


	public void removeToken(String lexema) {
		this.simbolos.remove(lexema);
		
	}
	
}

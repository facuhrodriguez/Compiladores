package AnalizadorLexico;

import java.util.Collection;
import java.util.Hashtable;

import AnalizadorSintactico.AnalizadorSintactico;

public class TablaDeSimbolos {
	private Hashtable <String, Token > simbolos;
	private Integer count = 0;
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
	
	public Token getTokenByName(String name) {
		for (Token t : this.simbolos.values())
			if (t.getAttr("NOMBRE_ANT") != null && t.getAttr("NOMBRE_ANT").equals(name))
				return t;
		return null;
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
			if (t.getAttr("FORMA DE PASAJE") == null)
				t.addAttr("FORMA DE PASAJE", "-");
			if (t.getAttr("CANT. INVOCACIONES") == null) 
				t.addAttr("CANT. INVOCACIONES", "-");
			System.out.println("-----------------------------------------------------------------------------------------------"
					+ "--------------------------------------------------------------------------------------------------------");
			System.out.println(t.getAttr("NUMERO DE TOKEN") + "|		|" + t.getAttr("NOMBRE") + "|				|"
					+	t.getAttr("TIPO") +  "|				|" + t.getAttr("USO") + "|				|" + t.getAttr("AMBITO") + "|      |" +
					t.getAttr("FORMA DE PASAJE") + "| 			|" + t.getAttr("CANT. INVOCACIONES"));
			
			}
	}


	public void removeToken(String lexema) {
		if (lexema != null)
			this.simbolos.remove(lexema);
		
	}
	
	
	public String generarAssembler() {
		StringBuilder assembler = new StringBuilder();
		for (Token t : this.getTokens()) {
			if ((t.getAttr("USO") != null) && (t.getAttr("USO").toString() == AnalizadorSintactico.VARIABLE)){
				// Constante entera sin signo
				if (t.getAttr("TIPO") == AnalizadorLexico.TYPE_UINT) 
					assembler.append("_" + t.getAttr("NOMBRE")+ " DW ? " + System.lineSeparator());
				else 
					// Constante Double
					assembler.append("_" + t.getAttr("NOMBRE") + " QWORD ? " + System.lineSeparator());
			}
			else {
				if (((Short) t.getAttr("NUMERO DE TOKEN") == AnalizadorLexico.CONSTANTE) && 
					(t.getAttr("TIPO").toString() == AnalizadorLexico.TYPE_UINT )) 
					assembler.append("_" + t.getAttr("NOMBRE") + " DW " + t.getAttr("NOMBRE") + System.lineSeparator());
				if (((Short) t.getAttr("NUMERO DE TOKEN") == AnalizadorLexico.CONSTANTE) && 
					(t.getAttr("TIPO").toString() == AnalizadorLexico.TYPE_DOUBLE ))
					assembler.append("@" + this.getCount() + " QWORD " + t.getAttr("NOMBRE") + System.lineSeparator());
				if (((Short) t.getAttr("NUMERO DE TOKEN") == AnalizadorLexico.CADENA) && 
						(t.getAttr("TIPO").toString().toUpperCase() == AnalizadorLexico.TYPE_CADENA ))
					assembler.append(t.getAttr("NOMBRE") + " DB " + "\"" + t.getAttr("NOMBRE") + "\"" + ", 0" + System.lineSeparator());
			}			
		}
		return assembler.toString();
	}
	
	public Integer getCount() {
		this.count++;
		return this.count;
	}
}

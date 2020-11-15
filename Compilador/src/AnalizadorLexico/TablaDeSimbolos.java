package AnalizadorLexico;

import java.io.FileWriter;
import java.io.IOException;
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
		FileWriter writer;
		try {
			writer = new FileWriter("../tablaSimbolo.csv");
			String cabecera = ("NUMERO " +";" + "NAME " +";"+ "TIPO " + ";"+  
					"			 USO" +";" + "AMBITO" +";" + "FORMA DE PASAJE"  +";" + "CANT. INVOCACIONES" + ";" + "VALOR");
			writer.append(cabecera);
			writer.append(System.lineSeparator());
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
				if (t.getAttr("VALOR") == null)
					t.addAttr("VALOR", "-");
				String save = (t.getAttr("NUMERO DE TOKEN") + ";" + t.getAttr("NOMBRE") + ";"
						+	 t.getAttr("TIPO") +  ";" + t.getAttr("USO") + ";" + t.getAttr("AMBITO") + ";" +
						  t.getAttr("FORMA DE PASAJE") + ";" +  t.getAttr("CANT. INVOCACIONES") + ";" + t.getAttr("VALOR"));
				writer.append(save);
				writer.append(System.lineSeparator());
				}
			
	
			try {
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		


	public void removeToken(String lexema) {
		this.simbolos.remove(lexema);
		
	}
	
}

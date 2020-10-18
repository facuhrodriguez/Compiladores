package AnalizadorLexico;

import java.util.Hashtable;
import java.util.Iterator;

public class Token {
	private Hashtable <String, Object >attr;
	private Iterator<String> i;
	
	public Token () {
		attr = new Hashtable <String,Object>();
	}
	
	public Token(short identificador, Object value, String type) {
		this.attr = new Hashtable<String, Object>();
		this.addAttr("NUMERO DE TOKEN", identificador);
		this.addAttr("VALOR", value);
		this.addAttr("TIPO", type);
		
	}
	
	/**
	 * 
	 * @param clave
	 * @param obj
	 */
	public void addAttr (String clave, Object obj) {
		attr.put (clave, obj);
		i = (Iterator<String>) attr.keys();
	}
	
	
	public Object getAttr (String clave) {
		return attr.get(clave);
	}

	@Override
	public String toString() {
		while (i.hasNext()) {
			String key = (String) i.next();
			Object value = (Object) attr.get(key);
			return  " " +value + "               " + this.toString() + "                  ";
		}
		i = (Iterator<String>) attr.keys();
		return " " ;
	}
	
	
	
	
}

package AnalizadorLexico;

import java.util.Hashtable;

public class Token {
	private Hashtable <String, Object >attr;

	
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
	}
	
	
	public Object getAttr (String clave) {
		return attr.get(clave);
	}

	@Override
	public String toString() {
		String t = "";
		for (String key : attr.keySet()) {
			t = t + attr.get(key) + "				";
		}
		return t;
	}
	
}
	
	
	

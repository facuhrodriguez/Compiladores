package AnalizadorLexico;

import java.util.Hashtable;
import java.util.Iterator;

public class Token {
	private Hashtable <String, Object >attr;
	private Iterator i;
	
	public Token () {
		attr = new Hashtable <String,Object>();
	}
	
	public Token(short identificador, Object value, String type) {
		this.attr = new Hashtable<String, Object>();
		this.addAttr("NUMERO DE TOKEN", identificador);
		this.addAttr("VALOR", value);
		this.addAttr("TIPO", type);
		
	}
	
	public void addAttr (String clave, Object obj) {
		attr.put (clave, obj);
		i = (Iterator) attr.keys();
	}
	
	public Object getAttr (String clave) {
		return attr.get(clave);
	}
	
	
	/**
	 * imprime todos los atributos de la tabla de hash del token, luego cuando
	 ya no tiene mas atributos, resetea el iterador para ubicarse en el inicio de nuevo
	 */
	@Override
	public String toString () {
		while (i.hasNext()) {
			String key = (String) i.next();
			Object value = (Object) attr.get(key);
			return  " " +value + "               " + this.toString() + "                  ";
		}
		i = (Iterator) attr.keys();
		return " " ;

	}
}

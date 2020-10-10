package AnalizadorLexico;

public class AddOperators implements AccionSemantica {
	private AnalizadorLexico l;
	private int c;
	private Boolean back;
	
	public AddOperators(AnalizadorLexico l, Boolean b, int c) {
		this.l = l;
		this.c = c;
		this.back = b;
	}
	
	@Override
	public int run() {
		if (this.back)
			this.l.unget();
		else 
			if (c == l.COMPARACION || c == l.DISTINTO || c == l.MAYORIGUAL || c == l.MENORIGUAL)
				this.l.addBuffer('=');
			else
				this.l.addBuffer((char) this.c);
		Token t = new Token((short) this.c, this.l.getBuffer(), "OPERADOR");
		this.l.addToken(t);
		// Devuelvo el n�mero ASCII asociado al caracter
		return this.c;
		
	}

}

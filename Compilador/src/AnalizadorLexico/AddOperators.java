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
			if (c == AnalizadorLexico.COMPARACION || c == AnalizadorLexico.DISTINTO || 
				c == AnalizadorLexico.MAYORIGUAL || c == AnalizadorLexico.MENORIGUAL)
				this.l.addBuffer('=');

		Token t = new Token((short) this.c, this.l.getBuffer(), "OPERADOR");
		this.l.addToken(t);
		// Devuelvo el número ASCII asociado al caracter
		return this.c;
		
	}

}

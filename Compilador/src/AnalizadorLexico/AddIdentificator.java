package AnalizadorLexico;

public class AddIdentificator implements AccionSemantica {
	
	private AnalizadorLexico l;
	public AddIdentificator(AnalizadorLexico l) {
		this.l = l;
	}
	
	
	@Override
	public int run() {
		
		this.l.unget();
		String lexema = this.l.getBuffer();
		
		// Si la longitud del identificador es más grande que lo permitido, se envía un warning,
		// y se trunca.
		
		if (this.l.getBuffer().length() > AnalizadorLexico.MAX_CAR_ID ) {
			lexema = this.l.getBuffer().substring(0, AnalizadorLexico.MAX_CAR_ID - 1);
			Error e = new Error(AnalizadorLexico.WARNING_IDENTIFICADOR, this.l, this.l.getLine());
			this.l.addWarning(e);
		}
		
		Token token = new Token(AnalizadorLexico.IDENTIFICADOR, lexema, "");
		this.l.addToken(token);
		this.l.addOnTablaDeSimbolos(lexema, token);
		this.l.setYylval(lexema);
		return AnalizadorLexico.IDENTIFICADOR;
	}
	

}

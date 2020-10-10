package AnalizadorLexico;

public class AddChain implements AccionSemantica {

	private AnalizadorLexico l;
	
	public AddChain(AnalizadorLexico l) {
		this.l = l;
	}
	
	@Override
	public int run() {
		String value = this.l.getBuffer().replace('\'', ' ');
		Token token = new Token (AnalizadorLexico.CADENA, value, "CADENA");
		this.l.addOnTablaDeSimbolos(value, token);
		this.l.addToken(token);
		this.l.setYylval(value);
		return AnalizadorLexico.CADENA;
		
	}

}

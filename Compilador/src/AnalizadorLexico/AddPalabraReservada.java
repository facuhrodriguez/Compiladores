package AnalizadorLexico;

public class AddPalabraReservada implements AccionSemantica {
	
	private AnalizadorLexico l;

	
	public AddPalabraReservada(AnalizadorLexico l) {
		this.l = l;
	}
	
	@Override
	public int run() {
		
		String buffer = this.l.getBuffer();
		short tokenNumber = 0;
		this.l.unget();
		switch (buffer) {
		case "IF": 
			tokenNumber = AnalizadorLexico.IF;
			break;
		case "THEN":
			tokenNumber = AnalizadorLexico.THEN;
			break;
		case "ELSE":
			tokenNumber = AnalizadorLexico.ELSE;
			break;
		case "END_IF":
			tokenNumber = AnalizadorLexico.END_IF;
			break;
		case "OUT":
			tokenNumber = AnalizadorLexico.OUT;
			break;
		case "FUNC":
			tokenNumber = AnalizadorLexico.FUNC;
			break;
		case "RETURN":
			tokenNumber = AnalizadorLexico.RETURN;
			break;
		case "UINT":
			tokenNumber = AnalizadorLexico.UINT;
			break;
		case "DOUBLE":
			tokenNumber = AnalizadorLexico.DOUBLE;
			break;
		case "WHILE":
			tokenNumber = AnalizadorLexico.WHILE;
			break;
		case "LOOP":
			tokenNumber = AnalizadorLexico.LOOP;
			break;
		case "UP":
			tokenNumber = AnalizadorLexico.UP;
			break;
		case "DOWN":
			tokenNumber = AnalizadorLexico.DOWN;
			break;
		case "NI":
			tokenNumber = AnalizadorLexico.NI;
			break;
		case "PROC":
			tokenNumber = AnalizadorLexico.PROC;
			break;
		case "REF":
			tokenNumber = AnalizadorLexico.REF;
			break;
		default:
			this.l.addError(new Error(AnalizadorLexico.ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this.l, this.l.getLine()));
			return tokenNumber;
			
		} 
		
		Token token = new Token (tokenNumber, buffer, "PALABRA RESERVADA");
		this.l.addToken(token);
		this.l.addOnTablaDeSimbolos(buffer, token);
		return tokenNumber;
	}

}

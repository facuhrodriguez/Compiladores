package AnalizadorLexico;

public class AddConstant implements AccionSemantica {
	
	private AnalizadorLexico l;
	private Character type;
	
	public AddConstant(AnalizadorLexico l, Character type) {
		this.l = l;
		this.type = type;
	}
	
	@Override
	public int run() {
		
		this.l.unget();
		
		// Si la constante es de tipo double
		if (this.type.equals('D')) {
			Double number = 0.0;
			double d = 0.0;
			String buffer = this.l.getBuffer();
			// Si la constante tiene exponente
			if (buffer.contains("d")) {
				Character sign = buffer.charAt(buffer.indexOf('d') + 1);
				Integer indexSign = buffer.indexOf(sign);
				Integer exp = Integer.parseInt(buffer.substring(indexSign + 1, buffer.length()));
				double mantissa = Double.parseDouble(buffer.substring(0, buffer.indexOf('d')));
				if (sign.equals('+')) 
					 d = Math.pow(mantissa, exp);
				 else 
					 d = Math.pow(mantissa, -exp);
				if (d > 0)
					number = MyDouble.checkPositiveRange(d);
				else 
					number = MyDouble.checkNegativeRange(d, mantissa);
			} else {
				d = Double.parseDouble(buffer);
				if (d > 0) 
					number = MyDouble.checkPositiveRange(d);
				else 
					number = MyDouble.checkNegativeRange(d, 0.0);
			}
				
			if (MyDouble.truncate) {
				Error warning = new Error(AnalizadorLexico.WARNING_CONSTANT_DOUBLE, this.l, this.l.getLine());
				warning.setReason(buffer);
				this.l.addWarning(warning);
				
			}
			Token token = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.TYPE_DOUBLE);
			this.l.addToken(token);
			this.l.addOnTablaDeSimbolos(token.getAttr("VALOR").toString(), token);	
			this.l.setYylval(token.getAttr("VALOR").toString());
			return AnalizadorLexico.CONSTANTE;
		}
		
		// Si es de tipo Constante sin signo
		else {
			Integer number;
			try {
				number = Integer.parseInt(this.l.getBuffer().substring(0, this.l.getBuffer().length() - 3));
				if ((number < AnalizadorLexico.MIN_CONSTANT_UI)) {
					number = AnalizadorLexico.MIN_CONSTANT_UI;
					this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
				} else {
					if ((number > AnalizadorLexico.MAX_CONSTANT_UI)) {
						number = AnalizadorLexico.MAX_CONSTANT_UI;
						this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
					}
				}
			}
			catch(NumberFormatException n) {
				this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
				number = AnalizadorLexico.MAX_CONSTANT_UI;
			}
			
			Token token = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.TYPE_UINT);
			this.l.addOnTablaDeSimbolos(token.getAttr("VALOR").toString(), token);
			this.l.addToken(token);
			this.l.setYylval(token.getAttr("VALOR").toString());
			return AnalizadorLexico.CONSTANTE;
		}
		
	}
		
		
}
	
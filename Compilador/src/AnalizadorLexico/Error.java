package AnalizadorLexico;

public class Error implements AccionSemantica {
	protected String message;
	protected AnalizadorLexico l;
	
	public Error(String m, AnalizadorLexico l) {
		this.message = m;
		this.l = l;
		this.setMessage();
	}
	
	public String showMessage() {
		return this.message;
	}
	
	public void setMessage() {
		this.message = "Línea " + this.l.getLine() + ": " + this.message;
	}

	@Override
	public int run() {
		this.l.addError(this);
		this.l.panicMode();
		return -1;
	}
	
	@Override
	public String toString() {
		return this.showMessage();
	}
}

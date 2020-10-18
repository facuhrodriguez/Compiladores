package AnalizadorLexico;

public class Error implements AccionSemantica {
	protected String message;
	protected AnalizadorLexico l;
	protected Integer line;
	
	public Error(String m, AnalizadorLexico l, int line) {
		this.message = m;
		this.l = l;
		this.line = line;
	}
	
	public String showMessage() {
		return this.message + line;
	}
	

	@Override
	public int run() {
		this.line = this.l.getLine();
		this.l.addError(this);
		this.l.panicMode();
		return -1;
	}
	
	@Override
	public String toString() {
		return this.showMessage();
	}
}

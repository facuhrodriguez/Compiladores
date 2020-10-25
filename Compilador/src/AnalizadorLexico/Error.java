package AnalizadorLexico;

public class Error implements AccionSemantica {
	protected String message;
	protected AnalizadorLexico l;
	protected Integer line;
	protected String reason;
	
	public Error(String m, AnalizadorLexico l, int line) {
		this.message = m;
		this.l = l;
		this.line = line;
		this.reason = "";
	}
	
	public String showMessage() {
		return this.message + line;
	}
	

	public String getReason() {
		return this.reason;
	}
	
	public void setReason(String r) {
		this.reason = r;
	}
	
	@Override
	public int run() {
		this.line = this.l.getLine();
		this.l.addError(this);
		this.l.panicMode();
		return -1;
	}

	
	@Override
	public boolean equals(Object obj) {
		Error e = (Error) obj;
		return (this.toString().equals(e.toString()));
	}

	@Override
	public String toString() {
		return this.showMessage();
	}
	

}

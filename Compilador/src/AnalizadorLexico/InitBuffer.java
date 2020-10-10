package AnalizadorLexico;

public class InitBuffer implements AccionSemantica {
	private AnalizadorLexico l;
	
	@Override
	public int run() {
		this.l.initBuffer();
		return 0;
	}
	
	public InitBuffer(AnalizadorLexico l) {
		this.l = l;
	}

}

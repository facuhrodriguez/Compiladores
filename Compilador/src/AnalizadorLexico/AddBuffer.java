package AnalizadorLexico;

public class AddBuffer implements AccionSemantica {
	
	private AnalizadorLexico l;
	
	public AddBuffer(AnalizadorLexico l) {
		this.l = l;
	}
	
	@Override
	public int run() {
		this.l.addBuffer(l.currentCharacter());
		return 0;
	}
	

}

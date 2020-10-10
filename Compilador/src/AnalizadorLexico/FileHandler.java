package AnalizadorLexico;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
	private String path;
	private BufferedReader reader;
	private String content = null;
	static Integer lineNumber = 0;
	private Integer pos;
	private Integer currentLine;
	private final Character SL = '\n';
	
	public FileHandler(String path) {
		this.path = path;
		this.pos = 0;
		this.currentLine = 1;
		try {
			this.reader = new BufferedReader(new FileReader(this.path));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
			reader.close();
			this.content = stringBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void goBack() {
		this.pos--;
	}
	
	public String getContentFile() {
		return this.content;
	}
	
	public Integer longBuffer() {
		return this.content.length();
	}
	
	public Character getAtPos(Integer i) {
		return this.content.charAt(i);
	}
	
	public Integer getCurrentLineNumber() {
		return this.currentLine;
	}
	
	public boolean endFile() {
		return (this.pos == this.content.length());
		
	}
	
	public Character getCurrentChar() {
		if (this.content.charAt(pos) == this.SL)
			this.currentLine++;
		return this.content.charAt(pos);
	}
	
	public void forward() {
		if (pos < this.content.length())
			this.pos++;
	}
}

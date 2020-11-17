package GeneracionCodigoAssembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.TablaDeSimbolos;
import AnalizadorLexico.Token;
import AnalizadorSintactico.AnalizadorSintactico;
import CodigoIntermedio.CodigoIntermedio;

public class GeneradorAssembler {
	
	private CodigoIntermedio polaca;
	private TablaDeSimbolos ts;
	private Stack<String> stack;

	public HashMap<String, Boolean> registros = new HashMap<String, Boolean>();
	
	public GeneradorAssembler(CodigoIntermedio p, TablaDeSimbolos ts) {
		this.polaca = p;
		this.ts = ts;
		this.setRegistros();
		this.stack = new Stack<String>();
	}
	
	public void generarArchivoAssembler() throws IOException {
		String path = "./salida.asm";
		File assembler = new File(path);
		
		// Si el archivo no existe es creado
        if (!assembler.exists()) {
            assembler.createNewFile();
        }
        
        FileWriter fw = new FileWriter(assembler);
        BufferedWriter bw = new BufferedWriter(fw);
        
        bw.write(".386" + System.lineSeparator() 
        		+ ".model flat" + System.lineSeparator()
        		+ "include \\masm32\\include\\windows.inc" + System.lineSeparator()
        		+ "include \\masm32\\include\\kernel32.inc" + System.lineSeparator()
        		+ "include \\masm32\\include\\user32.inc" + System.lineSeparator()
        		+ "includelib \\masm32\\lib\\kernel32.lib" + System.lineSeparator()
        		+ "includelib \\masm32\\lib\\user32.lib" + System.lineSeparator());
        bw.write(".data" + System.lineSeparator());
        bw.write(this.ts.generarAssembler() +  System.lineSeparator());
        bw.write(".code" + System.lineSeparator());
        bw.write("start: " + System.lineSeparator() );
        // Escribir código para todo el programa
        //bw.write()
        bw.write("end start");
        
	}
	
	
	public void generarAssembler() {
		StringBuilder assembler = new StringBuilder();
		Collection<String> polaca = this.polaca.getStructure();
		for (String op : polaca) {
			if (! this.polaca.isOperator(op)) 
				this.stack.push(op);
			else {
				if (this.polaca.isBinary(op)) {
					String var2 = this.stack.pop();
					String var1 = this.stack.pop();
					Token t1 = this.ts.getToken(var1);
					Token t2 = this.ts.getToken(var2);
					// Situación 1
					if (t1 != null && t2 != null && t1.getAttr("TIPO") == t2.getAttr("TIPO")) {
						if (t1.getAttr("TIPO") == AnalizadorLexico.TYPE_UINT) {
							switch (op) {
							case "+":
								String regSuma = getRegistroLibre();
								this.setEstadoRegistro(regSuma, true);
								assembler.append("MOV "+ regSuma + "," + "_" + var1 + System.lineSeparator());
								assembler.append("ADD " + regSuma + "," + "_" + var2 + System.lineSeparator());
								this.stack.push(regSuma);
								break;  
							case "-":
								String regResta = getRegistroLibre();
								this.setEstadoRegistro(regResta, true);
								assembler.append("MOV " + regResta + "," + "_" + var1 + System.lineSeparator());
								assembler.append("SUB " + regResta + "," + "_" + var2 + System.lineSeparator());
								// TODO: Crear labels de chequeos en tiempo de ejecución
								assembler.append("JS " + "label" );
								this.stack.push(regResta);
								break;
							case "*":
								String regMult = getRegistroLibreMultDiv();
								this.setEstadoRegistro(regMult, true);
								assembler.append("MOV " + regMult + "," + "_" + var1 + System.lineSeparator());
								assembler.append("IMUL " + regMult + "," + "_" + var2 + System.lineSeparator());
								this.stack.push(regMult);
								break;
							case "/":
								String regDiv = getRegistroLibreMultDiv();
								this.setEstadoRegistro(regDiv, true);
								assembler.append("MOV " + regDiv + "," + "_" + var1 + System.lineSeparator());
								assembler.append("IDIV " + regDiv + "," + "_" + var2 + System.lineSeparator());
								assembler.append("CMP " + regDiv + 0 + System.lineSeparator());
								// TODO: Crear labels de chequeos de div por cero
								assembler.append("JE " + "label" + System.lineSeparator());
								this.stack.push(regDiv);
								break;
							case "=":
								String regAsig = getRegistroLibre();
								this.setEstadoRegistro(regAsig, true);
								assembler.append("MOV " + regAsig + "," + var2 + System.lineSeparator());
								assembler.append("MOV " + var1 + "," + regAsig + System.lineSeparator());
								this.setEstadoRegistro(regAsig, false);
								break;
							case "==":
								String regCompIgual = getRegistroLibre();
								this.setEstadoRegistro(regCompIgual, true);
								assembler.append("MOV " + regCompIgual + "," + var1 + System.lineSeparator());
								assembler.append("CMP " + regCompIgual + "," + var2 + System.lineSeparator());
								this.stack.push(regCompIgual);
								break;
							case "!=":
								String regCompDist = getRegistroLibre();
								this.setEstadoRegistro(regCompDist, true);
								assembler.append("MOV " + regCompDist + "," + var1 + System.lineSeparator());
								assembler.append("CMP " + regCompDist + "," + var2 + System.lineSeparator());
								this.stack.push(regCompDist);
								break;
							case "<=": 
								String regCompMenorI = getRegistroLibre();
								this.setEstadoRegistro(regCompMenorI, true);
								assembler.append("MOV " + regCompMenorI + "," + var1 + System.lineSeparator());
								assembler.append("CMP " + regCompMenorI + "," + var2 + System.lineSeparator());
								this.stack.push(regCompMenorI);
								break;
							case ">=":
								String regComMayorI = getRegistroLibre();
								this.setEstadoRegistro(regComMayorI, true);
								assembler.append("MOV " + regComMayorI + "," + var1 + System.lineSeparator());
								assembler.append("CMP " + regComMayorI + "," + var2 + System.lineSeparator());
								this.stack.push(regComMayorI);
								break;
							case "<":
								String regCompMen = getRegistroLibre();
								this.setEstadoRegistro(regCompMen, true);
								assembler.append("MOV " + regCompMen + "," + var1 + System.lineSeparator());
								assembler.append("CMP " + regCompMen + "," + var2 + System.lineSeparator());
								this.stack.push(regCompMen);
								break;
							case ">":
								String regCompMay = getRegistroLibre();
								this.setEstadoRegistro(regCompMay, true);
								assembler.append("MOV " + regCompMay + "," + var1 + System.lineSeparator());
								assembler.append("CMP " + regCompMay + "," + var2 + System.lineSeparator());
								this.stack.push(regCompMay);
								break;
							default:
								break;
							}
						}
					} 
					else {
						// Situación 2
						if (esRegistro(var1) && t2 != null && ( t2.getAttr("USO") == AnalizadorSintactico.VARIABLE || 
									t2.getAttr("USO") == AnalizadorSintactico.CONSTANTE)) {
							if (t2.getAttr("TIPO") == AnalizadorLexico.TYPE_UINT){
								switch (op) {
								case "+":
									assembler.append("ADD " + var1 + "," + "_" + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;  
								case "-":
									assembler.append("SUB " + var1 + "," + "_" + var2 + System.lineSeparator());
									// TODO: Crear labels de chequeos en tiempo de ejecución
									assembler.append("JS " + "label" + System.lineSeparator());
									this.stack.push(var1);
									break;
								case "*":
									assembler.append("IMUL " + var1 + "," + "_" + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								case "/":
									assembler.append("IDIV " + var1 + "," + "_" + var2 + System.lineSeparator());
									assembler.append("CMP " + var1 + 0 + System.lineSeparator());
									// TODO: Crear labels de chequeos de div por cero
									assembler.append("JE " + "label" + System.lineSeparator());
									this.stack.push(var1);
									break;
								case "=":
									assembler.append("MOV " + "_" + var2 + "," + var1 + System.lineSeparator());
									setEstadoRegistro(var1, false);
									this.stack.push(var2);
									break;
								case "==":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								case "!=":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								case "<=": 
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								case ">=":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								case "<":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								case ">":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									this.stack.push(var1);
									break;
								default:
									break;
								}
							}
						}
						else {
							// Situación 3
							if (esRegistro(var1) && esRegistro(var2)) {
								switch (op) {
								case "+":
									assembler.append("ADD " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;  
								case "-":
									assembler.append("SUB " + var1 + "," + var2 + System.lineSeparator());
									// TODO: Crear labels de chequeos en tiempo de ejecución
									assembler.append("JS " + "label" );
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case "*":
									assembler.append("IMUL " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case "/":
									assembler.append("IDIV " + var1 + "," + var2 + System.lineSeparator());
									assembler.append("CMP " + var1 + 0 + System.lineSeparator());
									// TODO: Crear labels de chequeos de div por cero
									assembler.append("JE " + "label" + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case "==":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case "!=":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case "<=": 
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case ">=":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case "<":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								case ">":
									assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
									setEstadoRegistro(var2, false);
									this.stack.push(var1);
									break;
								default:
									break;
								}
							}
							else {
								// Situación 4
								if (esRegistro(var2) && t1!= null && ( t1.getAttr("USO") == AnalizadorSintactico.VARIABLE || 
										t1.getAttr("USO") == AnalizadorSintactico.CONSTANTE)) {
									switch (op) {
									case "+":
										assembler.append("ADD " + var2 + "," + "_" + var1 + System.lineSeparator());
										this.stack.push(var2);
										break;  
									case "-":
										String regRes = this.getRegistroLibre();
										setEstadoRegistro(regRes, true);
										assembler.append("MOV " + regRes + "_" + var1 + System.lineSeparator());
										assembler.append("SUB " + regRes + "," + "_" + var2 + System.lineSeparator());
										// TODO: Crear labels de chequeos en tiempo de ejecución
										assembler.append("JS " + "label" + System.lineSeparator());
										setEstadoRegistro(var2, false);
										this.stack.push(var1);
										break;
									case "*":
										assembler.append("IMUL " + var2 + "," + "_" + var1 + System.lineSeparator());
										this.stack.push(var2);
										break;
									case "/":
										String regDiv = this.getRegistroLibre();
										setEstadoRegistro(regDiv, true);
										assembler.append("MOV " + regDiv + "_" + var1 + System.lineSeparator());
										assembler.append("IDIV " + regDiv + "," + "_" + var2 + System.lineSeparator());
										assembler.append("CMP " + var1 + 0 + System.lineSeparator());
										// TODO: Crear labels de chequeos de div por cero
										assembler.append("JE " + "label" + System.lineSeparator());
										setEstadoRegistro(var2, false);
										this.stack.push(var1);
										break;
									case "==":
										assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
										this.stack.push(var2);
										break;
									case "!=":
										assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
										this.stack.push(var2);
										break;
									case "<=": 
										assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
										this.stack.push(var2);
										break;
									case ">=":
										assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
										this.stack.push(var2);
										break;
									case "<":
										assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
										this.stack.push(var2);
										break;
									case ">":
										assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
										this.stack.push(var2);
										break;
									default:
										break;
									}
								}
							}
						}
					}
						
				} else {
					if (this.polaca.isUnary(op)) {
						String var1 = this.stack.pop();
					}
				}
			}
			}
			System.out.println(assembler.toString());
		}

	
	public void setRegistros() {
		this.registros.put("AX", false);
		this.registros.put("BX", false);
		this.registros.put("CX", false);
		this.registros.put("DX", false);
	}
	
	public String getRegistroLibre() {
		Collection<String> reg = this.registros.keySet();
		for (String r : reg) {
			if (!this.registros.get(r) && r != "AX" )
				return r;
		}
		return "AX";
	}
	
	public String getRegistroLibreMultDiv() {
		if (!this.registros.get("AX"))
			return "AX";
		return null;
	}
	
	public void setEstadoRegistro(String registro, Boolean estado) {
		this.registros.put(registro, estado);
	}
	
	
	public boolean esRegistro(String reg) {
		return this.registros.containsKey(reg);
	}
}

package GeneracionCodigoAssembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private String labelDivisionCero = "LabelDivisionCero";
	private String labelRestaNegativa = "LabelRestaNegativa";
	private String tipoSalto = "";

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
        		+ ".MODEL flat, stdcall" + System.lineSeparator()
        		+ "option casemap:none" + System.lineSeparator()
        		+ "include \\masm32\\include\\windows.inc" + System.lineSeparator()
        		+ "include \\masm32\\include\\kernel32.inc" + System.lineSeparator()
        		+ "include \\masm32\\include\\user32.inc" + System.lineSeparator()
        		+ "includelib \\masm32\\lib\\kernel32.lib" + System.lineSeparator()
        		+ "includelib \\masm32\\lib\\user32.lib" + System.lineSeparator());
        bw.write(".data" + System.lineSeparator());
        bw.write(this.ts.generarAssembler() +  System.lineSeparator());
        bw.write("DivisionCero db \"Error al intentar dividir por 0\", 0" + System.lineSeparator()
		+ "RestaNegativa db \"Ocurrió overflow(resta negativa) en la resta\", 0" + System.lineSeparator()
		+ "CodigoTerminado db \"Código finalizado correctamente\", 0" + System.lineSeparator() 
		+ "estado DW ?" + System.lineSeparator());
        bw.write(".code" + System.lineSeparator());
        bw.write("start: " + System.lineSeparator() );
        bw.write(this.generarAssembler());
        bw.write("invoke MessageBox, NULL, addr CodigoTerminado, addr CodigoTerminado, MB_OK" + System.lineSeparator());
        bw.write("invoke ExitProcess, 0" + System.lineSeparator());
        bw.write("LabelDivisionCero:" + System.lineSeparator());
        bw.write("invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK" + System.lineSeparator());
        bw.write("invoke ExitProcess, 0" + System.lineSeparator());
        bw.write("LabelRestaNegativa:" + System.lineSeparator());
        bw.write("invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK" + System.lineSeparator());
        bw.write("invoke ExitProcess, 0" + System.lineSeparator());
        bw.write("end start ");
        bw.close();
        
	}
	
	
	public String generarAssembler() {
		StringBuilder assembler = new StringBuilder();
		Integer count = 0;
		Collection<String> polaca = this.polaca.getStructure();
		for (String op : polaca) {
			count++;
			if (! this.polaca.isOperator(op) && !this.polaca.isLabel(op) && !this.polaca.isProc(op) ) 
				this.stack.push(op);
			else {
				if (this.polaca.isProc(op)) {
					op = op.replaceFirst(":", "");
					assembler.append( op + ":" + System.lineSeparator());
				} else {
					if (this.polaca.isLabel(op)) {
						assembler.append(op + ":" + System.lineSeparator());
					} else {
						
						if (this.polaca.isOperator(op) && this.polaca.isBinary(op)) {
							String var2 = this.stack.pop();
							String var1 = this.stack.pop();
							Token t1 = this.ts.getToken(var1);
							Token t2 = this.ts.getToken(var2);
							// Situación 1
							if (t1 != null && t2 != null) {
								if (! tiposIncompatibles(t1, t2)) {
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
											assembler.append("SUB " + regResta  + "," + "_" + var2 + System.lineSeparator());
											assembler.append("JS " + labelRestaNegativa + System.lineSeparator());
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
											assembler.append("CMP " + "_" + var2 + "," + 0 + System.lineSeparator());
											assembler.append("JE " + labelDivisionCero + System.lineSeparator());
											this.setEstadoRegistro(regDiv, true);
											assembler.append("MOV " + regDiv + "," + "_" + var1 + System.lineSeparator());
											assembler.append("IDIV " + "_" + var2 + System.lineSeparator());
											this.stack.push(regDiv);
											break;
										case "=":
											String regAsig = getRegistroLibre();
											this.setEstadoRegistro(regAsig, true);
											assembler.append("MOV " + regAsig + "," + "_" + var2 + System.lineSeparator());
											assembler.append("MOV " + "_" + var1 + "," + regAsig + System.lineSeparator());
											this.setEstadoRegistro(regAsig, false);
											break;
										case "==":
											String regCompIgual = getRegistroLibre();
											this.setEstadoRegistro(regCompIgual, true);
											assembler.append("MOV " + regCompIgual + "," + "_" + var1 + System.lineSeparator());
											assembler.append("CMP " + regCompIgual + "," + "_" + var2 + System.lineSeparator());
											this.stack.push(regCompIgual);
											tipoSalto = "JNE ";
											break;
										case "!=":
											String regCompDist = getRegistroLibre();
											this.setEstadoRegistro(regCompDist, true);
											assembler.append("MOV " + regCompDist + "," + "_" + var1 + System.lineSeparator());
											assembler.append("CMP " + regCompDist + "," + "_" + var2 + System.lineSeparator());
											this.stack.push(regCompDist);
											tipoSalto = "JE ";
											break;
										case "<=": 
											String regCompMenorI = getRegistroLibre();
											this.setEstadoRegistro(regCompMenorI, true);
											assembler.append("MOV " + regCompMenorI + "," + "_" + var1 + System.lineSeparator());
											assembler.append("CMP " + regCompMenorI + "," +  "_" + var2 + System.lineSeparator());
											this.stack.push(regCompMenorI);
											tipoSalto = "JNBE ";
											break;
										case ">=":
											String regComMayorI = getRegistroLibre();
											this.setEstadoRegistro(regComMayorI, true);
											assembler.append("MOV " + regComMayorI + "," +  "_" + var1 + System.lineSeparator());
											assembler.append("CMP " + regComMayorI + "," +  "_" + var2 + System.lineSeparator());
											this.stack.push(regComMayorI);
											tipoSalto = "JNAE ";
											break;
										case "<":
											String regCompMen = getRegistroLibre();
											this.setEstadoRegistro(regCompMen, true);
											assembler.append("MOV " + regCompMen + "," +  "_" + var1 + System.lineSeparator());
											assembler.append("CMP " + regCompMen + "," +  "_" + var2 + System.lineSeparator());
											this.stack.push(regCompMen);
											tipoSalto = "JNB ";
											break;
										case ">":
											String regCompMay = getRegistroLibre();
											this.setEstadoRegistro(regCompMay, true);
											assembler.append("MOV " + regCompMay + "," +  "_" + var1 + System.lineSeparator());
											assembler.append("CMP " + regCompMay + "," +  "_" + var2 + System.lineSeparator());
											this.stack.push(regCompMay);
											tipoSalto = "JNA ";
											break;
										default:
											break;
										}
									} else {
										this.getAssemblerDouble(op, var1, var2, count);
									}
								} else {
									System.err.println("ERROR - NO SE GENERA CODIGO INTERMEDIO POR INCOMPATIBILIDAD DE TIPOS");
									System.exit(0);
									
								}
							}else {
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
												assembler.append("JS " + labelRestaNegativa + System.lineSeparator());
												this.stack.push(var1);
												break;
											case "*":
												assembler.append("IMUL " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												break;
											case "/":
												assembler.append("CMP " + var1 + "," +  0 + System.lineSeparator());
												assembler.append("JE " + labelDivisionCero + System.lineSeparator());
												assembler.append("IDIV " + "_" + var1 + System.lineSeparator());
												this.stack.push(var1);
												break;
											case "=":
												assembler.append("MOV " + "_" + var2 + "," + var1 + System.lineSeparator());
												setEstadoRegistro(var1, false);
												this.stack.push(var2);
												break;
											case "==":
												assembler.append("CMP " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												tipoSalto = "JNE ";
												break;
											case "!=":
												assembler.append("CMP " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												tipoSalto = "JE ";
												break;
											case "<=": 
												assembler.append("CMP " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												tipoSalto = "JNBE ";
												break;
											case ">=":
												assembler.append("CMP " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												tipoSalto = "JNAE ";
												break;
											case "<":
												assembler.append("CMP " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												tipoSalto = "JNB ";
												break;
											case ">":
												assembler.append("CMP " + var1 + "," + "_" + var2 + System.lineSeparator());
												this.stack.push(var1);
												tipoSalto = "JBE ";
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
												assembler.append("SUB " +  var1 + "," + var2 + System.lineSeparator());
												assembler.append("JS " + labelRestaNegativa + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												break;
											case "*":
												assembler.append("IMUL " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												break;
											case "/":
												assembler.append("CMP " + "_" + var2 + "," +  0 + System.lineSeparator());
												assembler.append("JE " + labelDivisionCero + System.lineSeparator());
												assembler.append("IDIV " + "_" + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												break;
											case "==":
												assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												tipoSalto = "JNE ";
												break;
											case "!=":
												assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												tipoSalto = "JE ";
												break;
											case "<=": 
												assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												tipoSalto = "JNBE ";
												break;
											case ">=":
												assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												tipoSalto = "JNAE ";
												break;
											case "<":
												assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												tipoSalto = "JNB ";
												break;
											case ">":
												assembler.append("CMP " + var1 + "," + var2 + System.lineSeparator());
												setEstadoRegistro(var2, false);
												this.stack.push(var1);
												tipoSalto = "JNA ";
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
													assembler.append("SUB " + regRes  + "," + var2 + System.lineSeparator());
													assembler.append("JS " + labelRestaNegativa + System.lineSeparator());
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
													assembler.append("CMP " + var2 + "," +  0 + System.lineSeparator());
													assembler.append("JE " + labelDivisionCero + System.lineSeparator());
													assembler.append("MOV " + regDiv + "_" + var1 + System.lineSeparator());
													assembler.append("IDIV " + var2 + System.lineSeparator());
													setEstadoRegistro(var2, false);
													this.stack.push(var1);
													break;
												case "==":
													assembler.append("CMP " + "_" + var1 + "," + var2 + System.lineSeparator());
													this.stack.push(var2);
													tipoSalto = "JNE ";
													break;
												case "!=":
													assembler.append("CMP " + "_" + var1 + "," + var2 + System.lineSeparator());
													this.stack.push(var2);
													tipoSalto = "JE ";
													break;
												case "<=": 
													assembler.append("CMP " + "_" + var1 + "," + var2 + System.lineSeparator());
													this.stack.push(var2);
													tipoSalto = "JNBE ";
													break;
												case ">=":
													assembler.append("CMP " + "_" + var1 + "," + var2 + System.lineSeparator());
													this.stack.push(var2);
													tipoSalto = "JNAE ";
													break;
												case "<":
													assembler.append("CMP " + "_" + var1 + "," + var2 + System.lineSeparator());
													this.stack.push(var2);
													tipoSalto = "JNB ";
													break;
												case ">":
													assembler.append("CMP " + "_" + var1 + "," + var2 + System.lineSeparator());
													this.stack.push(var2);
													tipoSalto = "JNA ";
													break;
												default:
													break;
												}
											}
										}
									}
							}
						 
					} else {
						if (this.polaca.isOperator(op) && this.polaca.isUnary(op)) {
							String var1 = this.stack.pop();
							switch (op) {
							case "BI":
								assembler.append("JMP " + "L"+ var1 + System.lineSeparator());
								break;
							case "BF":
								assembler.append(tipoSalto + "L" + var1 + System.lineSeparator());
								break;
							case "OUT":
								assembler.append("invoke MessageBox, NULL, addr " + var1 + ", addr " + var1 + "," + "MB_OK" + System.lineSeparator());
								break;
							default:
								break;
							}
							
						}
					}
				} 
			}
			
			}
			}
			return assembler.toString();
		}

	
	private boolean tiposIncompatibles(Token t1, Token t2) {
		if (t1 != null && t2 != null && t1.getAttr("TIPO") == t2.getAttr("TIPO")) 
			return false;
		return true;
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
			if (!this.registros.get(r) && r != "AX" && r!= "DX" )
				return r;
		}
		return "AX";
	}
	
	public String getRegistroLibreMultDiv() {
		if (!this.registros.get("AX"))
			return "AX";
		if (!this.registros.get("DX"))
			return "DX";
		return null;
	}
	
	public void setEstadoRegistro(String registro, Boolean estado) {
		this.registros.put(registro, estado);
	}
	
	
	public boolean esRegistro(String reg) {
		return this.registros.containsKey(reg);
	}
	
	public void assemblerTS() {
		System.out.println(this.ts.generarAssembler());
	}
	
	public String getAssemblerDouble(String op, String var1, String var2, Integer c) {
		StringBuilder assembler = new StringBuilder();
		Token t;
		switch (op) {
			case "+":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FADD " + System.lineSeparator());
				assembler.append("FSTP " + "@aux" + c + System.lineSeparator());
				t = new Token(AnalizadorLexico.CONSTANTE, "@aux" + c, "VARIABLE AUXILIAR");
				this.ts.addToken("@aux" + c, t);
				stack.push("@aux"+c);
				break;  
			case "-":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FSUB " + System.lineSeparator());
				assembler.append("FSTP " + "@aux" + c + System.lineSeparator());
				t = new Token(AnalizadorLexico.CONSTANTE, "@aux" + c, "VARIABLE AUXILIAR");
				this.ts.addToken("@aux" + c, t);
				stack.push("@aux" + c);
				break;
			case "*":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FMUL " + System.lineSeparator());
				assembler.append("FSTP " + "@aux" + c + System.lineSeparator());
				t = new Token(AnalizadorLexico.CONSTANTE, "@aux" + c, "VARIABLE AUXILIAR");
				this.ts.addToken("@aux" + c, t);
				stack.push("@aux" + c);
				break;
			case "/":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FTST "+System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				assembler.append("JE "+ labelDivisionCero +System.lineSeparator());
				assembler.append("FDIV "+System.lineSeparator());
				assembler.append("FSTP "+"@aux"+ c +System.lineSeparator());
				t = new Token(AnalizadorLexico.CONSTANTE, "@aux" + c, "VARIABLE AUXILIAR");
				this.ts.addToken("@aux" + c, t);
				this.stack.push("@aux"+c);
				break;
			case "=":
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FSTP " + var1 + System.lineSeparator());
				break;
			case "==":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FCOMPP " + System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				tipoSalto = "JNE ";
				break;
			case "!=":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FCOMPP " + System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				tipoSalto = "JE ";
				break;
			case "<=": 
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FCOMPP " + System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				tipoSalto = "JNLE ";
				break;
			case ">=":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FCOMPP " + System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				tipoSalto = "JNGE ";
				break;
			case "<":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FCOMPP " + System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				tipoSalto = "JNL ";
				break;
			case ">":
				assembler.append("FLD " + var1 + System.lineSeparator());
				assembler.append("FLD " + var2 + System.lineSeparator());
				assembler.append("FCOMPP " + System.lineSeparator());
				assembler.append("FSTSW estado "+System.lineSeparator());
				assembler.append("MOV AX,estado "+System.lineSeparator());
				assembler.append("SAHF "+System.lineSeparator());
				tipoSalto = "JNG ";
				break;
			default:
				break;
		}
		return assembler.toString();
	}
}

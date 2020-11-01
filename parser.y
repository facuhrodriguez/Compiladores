%token IDENTIFICADOR
%token CONSTANTE
%token CADENA
%token IF
%token THEN
%token ELSE
%token END_IF
%token OUT
%token FUNC
%token RETURN
%token UINT
%token DOUBLE
%token WHILE
%token LOOP
%token REF
%token PROC
%token NI
%token UP
%token DOWN
%token COMPARACION
%token MENORIGUAL
%token DISTINTO
%token MAYORIGUAL
%start programa


%%

%{
package AnalizadorSintactico;
import AnalizadorLexico.*;
import AnalizadorLexico.Error;
import CodigoIntermedio.*;
%}

programa : sentencias { this.s.addSyntaxStruct( AnalizadorSintactico.principalStruct );}
		 | error ';' { this.s.addSyntaxError(new Error(AnalizadorSintactico.errorPrincipal, this.l, this.l.getLine()));}
		 ;

sentencias : sentencia 
		   | sentencias sentencia
		   ;
		   
sentencia : sentencia_declarativa 
		  | sentencia_ejecutable
		  ;

sentencias_ejecutables : sentencia_ejecutable
					   | sentencias_ejecutables sentencia_ejecutable
					   | error sentencia_ejecutable {this.s.addSyntaxError(new Error(AnalizadorSintactico.errorSentenciaEjecutable, this.l, this.l.getLine()));}
					   ;

sentencia_declarativa : declaraciones_id { }
					  | declaraciones_procedimiento
					  ;


declaraciones_id : declaracion_id ';' { this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
				 | declaraciones_id ',' declaracion_id
				 
				 ;

declaracion_id : tipo IDENTIFICADOR { String lexema = $2.sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("NOMBRE_ANT", lexema);
									  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
									  t.addAttr("TIPO", $1.sval);
									  t.addAttr("USO", AnalizadorSintactico.VARIABLE);
									  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
									  this.ts.removeToken(lexema);
									  this.ts.addToken((String) t.getAttr("NOMBRE"), t);
									 }
			   ;

tipo : UINT { $$ = new ParserVal(AnalizadorLexico.TYPE_UINT);}
	 | DOUBLE { $$ = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
	 ;

declaraciones_procedimiento : encabezado_procedimiento '{' sentencias '}' { String lexema = $1.sval;
																			Token t = this.ts.getToken(lexema);
																			t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																			this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																			this.s.removeNombreProcedimiento((String) t.getAttr("NOMBRE"));}
							;

encabezado_procedimiento : PROC IDENTIFICADOR '(' parametros ')' NI '=' CONSTANTE { this.s.addSyntaxStruct( AnalizadorSintactico.procStruct );
																					this.count = 0;
																					String lexema = $2.sval;
																					Token t = this.ts.getToken(lexema);
																					t.addAttr("NOMBRE_ANT", lexema);
																					t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																					t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																					this.s.setNombreProcedimiento(lexema);
																					this.ts.removeToken(lexema);
																					this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																					$$.sval = (String) t.getAttr("NOMBRE");
																					}
						 | PROC IDENTIFICADOR '(' ')' NI '=' CONSTANTE  { this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); 
																		  $$.sval = $2.sval;
																		  String lexema = $2.sval;
																		  Token t = this.ts.getToken(lexema);
																		  t.addAttr("NOMBRE_ANT", lexema);
																		  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																		  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																		  this.s.setNombreProcedimiento(lexema);
																		  this.ts.removeToken(lexema);
																		  this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																		  $$.sval = (String) t.getAttr("NOMBRE");
																	    }
																		
						 | PROC '(' { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
						 ;

parametros : declaracion_par { this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							}
		   | parametros ',' declaracion_par { this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											}
		   | REF declaracion_par {  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
									
								}
		   ;

declaracion_par : tipo IDENTIFICADOR { String lexema = $2.sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", $1.sval);
									  t.addAttr("USO", AnalizadorSintactico.NOMBREPAR);
									  this.ts.addToken(lexema, t);
									 }


sentencia_ejecutable : asignaciones ';' { this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
					 | sentencia_seleccion  
					 | sentencia_control
					 | sentencia_salida ';' { this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
					 | invocaciones_procedimiento ';' { this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
					 ;

asignaciones : lado_izquierdo '=' expresion_aritmetica {  polaca.addOperando($1.sval);
													      polaca.addOperador("=");
													   }
			 | lado_izquierdo COMPARACION expresion_aritmetica { this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
			 ;

lado_izquierdo : IDENTIFICADOR { $$ = $1;
								String lexema = $1.sval;
								lexema = lexema.concat("@").concat(this.s.getNombreProcedimiento());
								Token t = this.ts.getToken(lexema);
								if (t == null) 
									this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
							    else {
									if (!this.existe_en_ambito(t))
										this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
									else {
										t.addAttr("USO", AnalizadorSintactico.VARIABLE);
										this.ts.addToken(lexema, t);
									}
								}
							}
			   ;

expresion_aritmetica : termino
					 | expresion_aritmetica '+' termino {  polaca.addOperador("+"); }
					 | expresion_aritmetica '-' termino {  polaca.addOperador("-"); }
					 ;
					 
termino : factor {  // termino : factor 
					$$ = $1; }
		| '(' DOUBLE ')' factor 
		| termino '*' factor { polaca.addOperador("*");} 
		| termino '/' factor { polaca.addOperador("/");}
		;

factor : '-' CONSTANTE { String valor = yylval.sval;
	if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.TYPE_UINT) {
		this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
		Token t = new Token(AnalizadorLexico.CONSTANTE, 0, AnalizadorLexico.TYPE_UINT);
		this.ts.addToken(valor, t);
		$$ = new ParserVal(valor);
	} else 
		if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.TYPE_DOUBLE) {
			Double number = MyDouble.check(this.l);
			if (MyDouble.truncate) { 
				if (!this.l.warningExist(this.l.getBuffer()))
					this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_DOUBLE, this.l, this.l.getLine()));
			}
				
			Token t = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.TYPE_DOUBLE);
			this.ts.addToken(valor, t);
			$$ = new ParserVal(valor);
	} 
	   polaca.addOperando($$.sval);
	 }

		| IDENTIFICADOR { // factor : IDENTIFICADOR
						 $$ = $1;
						 polaca.addOperando($$.sval);
						}
		| CONSTANTE { 	// factor : CONSTANTE 
						$$ = $1;
						polaca.addOperando($$.sval);
					}
		
	   | CADENA { // factor : cadena
					$$ = $1; 
					polaca.addOperando($$.sval); }
	   ;
	   
sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido END_IF { 
															  this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
															  // Desapila dirección incompleta 
															  Integer pasoIncompleto = polaca.getTop(); 	
															  // Completo el destino de BI
															  polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);
															 } 
					| IF '(' ')' cuerpo_if_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 
					| IF '(' condicion cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
					| IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF { 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			// Desapila dirección incompleta 
																		    //Integer pasoIncompleto = polaca.getTop(); 	
																		    // Completo el destino de BI
																		    //polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);
																			}
					| IF '(' ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 																
					| IF condicion cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}											
					| IF '(' condicion ')' cuerpo_if_mal_definido END_IF { this.s.addSyntaxError( new Error( AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					| IF condicion ')' cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
					| IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_mal_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					| IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_mal_definido END_IF{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					| IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					;


cuerpo_if_bien_definido: '{' sentencias '}' { // Desapila dirección incompleta 
											  Integer pasoIncompleto = polaca.getTop();
											  // Completa el destino de la BF
											  polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber+2);
											  // Apilo paso incompleto
											  polaca.stackUp(CodigoIntermedio.polacaNumber);
											  // Crea paso incompleto
											  polaca.addOperador("");
											  // Agrego etiqueta BI
											  polaca.addOperador("BI");
											  
											  } 
					   ;
cuerpo_if_mal_definido : sentencias 
					   ;
		  
cuerpo_else_bien_definido : '{' sentencias '}'  {	this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
													// Desapila dirección incompleta 
													Integer pasoIncompleto = polaca.getTop(); 	
													// Completo el destino de BI
													polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber); }
						  ;

cuerpo_else_mal_definido:  sentencias 
						;


condicion : expresion_aritmetica operador expresion_aritmetica { polaca.addOperador($2.sval);
																// Apilo paso incompleto
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
																 polaca.addOperando("");
																 // Creo el paso BF
																polaca.addOperando("BF");
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
		  | condicion operador expresion_aritmetica
		  ;

sentencia_salida : OUT '(' CADENA ')'
				 ;

invocaciones_procedimiento : IDENTIFICADOR '(' parametros_invocacion')'
						   | IDENTIFICADOR '(' ')'
				           ;

parametros_invocacion : IDENTIFICADOR ':' IDENTIFICADOR
					  ;

sentencia_control : WHILE '(' condicion ')' LOOP cuerpo_while_bien_definido { this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; } 
				  | WHILE '(' ')' LOOP cuerpo_while_bien_definido { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 				
				  | WHILE condicion LOOP cuerpo_while_bien_definido { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
				  | WHILE '(' condicion ')' LOOP cuerpo_while_mal_definido { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
				  ;
				  
cuerpo_while_bien_definido : '{' sentencias_ejecutables '}' 

cuerpo_while_mal_definido :  sentencia_ejecutable 

			

operador : '<' { $$.sval = "<"; }
		 | '>' { $$.sval = ">"; }
		 | MAYORIGUAL { $$.sval = $1.sval; }
		 | MENORIGUAL { $$.sval = $1.sval;}
		 | DISTINTO { $$.sval = $1.sval; }
		 | COMPARACION { $$.sval = $1.sval;}
		 ;

%%

AnalizadorLexico l;
AnalizadorSintactico s;
TablaDeSimbolos ts;
Integer count = 0;
CodigoIntermedio polaca;


public void setLexico(AnalizadorLexico l) {
	this.l = l;
}

public void setSintactico(AnalizadorSintactico s) {
	this.s = s;
}

public void setCodigoIntermedio(CodigoIntermedio p) {
	this.polaca = p;
}

public void setTS(TablaDeSimbolos t) {
	this.ts = t;
}

public int yylex() {
	int val = l.yylex();
	this.yylval = new ParserVal(this.l.getYylval());
    return val;
}

public void yyerror(String s) {
	if(s.contains("under"))
		System.out.println("par:"+s);
}

public boolean existe_en_ambito(Token var) {
	String ambitoVar = (String) var.getAttr("AMBITO");
	String nombreVar = (String) var.getAttr("NOMBRE_ANT");
	if (ambitoVar == null)
		return false;
	for (Token t : this.ts.getTokens()){
		if (t.getAttr("AMBITO") != null && t.getAttr("NOMBRE_ANT") != null)
		if ( ((String) t.getAttr("AMBITO")).contains(ambitoVar) && 
			((String) t.getAttr("NOMBRE_ANT")).equals(nombreVar) )
			return true;
	}
	return false;
}



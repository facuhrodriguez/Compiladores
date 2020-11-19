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
									  this.ts.removeToken(lexema);
									   if (checkAmbitoDeclaracion($2.sval, this.s.getNombreProcedimiento())  != null) {
										polaca.addSemanticError(new Error(CodigoIntermedio.VAR_RE_DECLARADA, this.l, this.l.getLine()));
									  } else {
										
										t.addAttr("NOMBRE_ANT", lexema);
										t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
										t.addAttr("TIPO", $1.sval);
										t.addAttr("USO", AnalizadorSintactico.VARIABLE);
										t.addAttr("AMBITO", this.s.getNombreProcedimiento());
										this.ts.addToken((String) t.getAttr("NOMBRE"), t);
									}
									}
			   ;

tipo : UINT { $$ = new ParserVal(AnalizadorLexico.TYPE_UINT);}
	 | DOUBLE { $$ = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
	 ;

declaraciones_procedimiento : encabezado_procedimiento '{' sentencias '}' { String lexema = $1.sval;
																			Token t = this.ts.getToken(lexema);
																			t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																			this.s.removeNombreProcedimiento((String) t.getAttr("NOMBRE_ANT"));
																			}
							;

encabezado_procedimiento : PROC IDENTIFICADOR '(' parametros ')' NI '=' CONSTANTE { 
																					this.s.addSyntaxStruct( AnalizadorSintactico.procStruct );
																					String invoc = $8.sval;
																					Token tInvoc = this.ts.getToken(invoc);
																					if (tInvoc.getAttr("TIPO").equals(AnalizadorLexico.TYPE_UINT)){
																						String lexema = $2.sval;
																						Token t = this.ts.getToken(lexema);
																						this.ts.removeToken(lexema);
																						t.addAttr("NOMBRE_ANT", lexema);
																						t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																						t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																						t.addAttr("CANT. INVOCACIONES", $8.sval);
																						t.addAttr("INVOCACIONES DISPONIBLES", $8.sval);
																						t.addAttr("CANT. PARAMETROS", this.count);
																						this.s.setNombreProcedimiento(lexema);
																						this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																						$$.sval = (String) t.getAttr("NOMBRE");
																						// Apilar paso incompleto
																						polaca.stackUpProcedure(CodigoIntermedio.polacaNumber);
																						polaca.addOperador(("L:").concat(t.getAttr("NOMBRE").toString()));
																					} else 
																						polaca.addSemanticError(new Error(CodigoIntermedio.CONSTANTE_NI, this.l, this.l.getLine()));
																						this.count = 0;
																					}
						 | PROC IDENTIFICADOR '(' ')' NI '=' CONSTANTE  { this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); 
																		  $$.sval = $2.sval;
																		  String lexema = $2.sval;
																		  String invoc = $7.sval;
																		  Token tInvoc = this.ts.getToken(invoc);
																		  Token t = this.ts.getToken(lexema);
																		   this.ts.removeToken(lexema);
																		  if (tInvoc.getAttr("TIPO").equals(AnalizadorLexico.TYPE_UINT)){
																			  t.addAttr("NOMBRE_ANT", lexema);
																			  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																			  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																			  t.addAttr("CANT. INVOCACIONES", $7.sval);
																			  t.addAttr("CANT. PARAMETROS", 0);
																			  t.addAttr("INVOCACIONES DISPONIBLES", $7.sval);
																			  this.s.setNombreProcedimiento(lexema);
																			  this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																			  // Apilar paso incompleto
																			  polaca.stackUpProcedure(CodigoIntermedio.polacaNumber);
																			  $$.sval = (String) t.getAttr("NOMBRE");
																			  polaca.addOperador(("L:").concat(t.getAttr("NOMBRE").toString()));
																	    } else 
																			polaca.addSemanticError(new Error(CodigoIntermedio.CONSTANTE_NI, this.l, this.l.getLine()));
																		}
						 | PROC '(' { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
						 ;

parametros : declaracion_par { this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							  Token t = this.ts.getToken($1.sval);
							  String lexema = $1.sval;
							  this.ts.removeToken(lexema);
							  t.addAttr("FORMA DE PASAJE", "COPIA VALOR");
							  t.addAttr("NOMBRE_ANT", lexema);
							  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
							  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
							  this.ts.addToken( (String) t.getAttr("NOMBRE"), t);
							}
		   | parametros ',' declaracion_par { this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											  Token t = this.ts.getToken($3.sval);
											  String lexema = $3.sval;
											  this.ts.removeToken(lexema);
											  t.addAttr("FORMA DE PASAJE", "COPIA VALOR");
											  t.addAttr("NOMBRE_ANT", lexema);
											  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
											  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
											  this.ts.addToken( (String) t.getAttr("NOMBRE"), t);
											}
		   | parametros ',' REF declaracion_par { this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											  Token t = this.ts.getToken($4.sval);
											  String lexema = $4.sval;
											  this.ts.removeToken($4.sval);
											  t.addAttr("FORMA DE PASAJE", "REFERENCIA");
											  t.addAttr("NOMBRE_ANT", lexema);
											  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
											  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
											  this.ts.addToken( (String) t.getAttr("NOMBRE"), t);
											}
		   | REF declaracion_par {  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
									String lexema = $2.sval;
									Token t = this.ts.getToken($2.sval);
									this.ts.removeToken($2.sval);
									t.addAttr("FORMA DE PASAJE", "REFERENCIA");
									t.addAttr("NOMBRE_ANT", lexema);
									t.addAttr("AMBITO", this.s.getNombreProcedimiento());
									t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
									this.ts.addToken( (String) t.getAttr("NOMBRE"), t);
									
								}
		   ;

declaracion_par : tipo IDENTIFICADOR { String lexema = $2.sval;
									  Token t = this.ts.getToken(lexema);	
									  t.addAttr("TIPO", $1.sval);
									  t.addAttr("USO", AnalizadorSintactico.NOMBREPAR);
									  this.ts.addToken(lexema, t);
									  $$.sval = $2.sval;
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

lado_izquierdo : IDENTIFICADOR { 
								String lexema = $1.sval;
								this.ts.removeToken(lexema);
								// lexema = lexema.concat("@").concat(this.s.getNombreProcedimiento());
								Token t = this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento());
								//this.ts.removeToken(lexema);
								if (t == null) {
									
									// Token t2 = this.ts.getTokenByName($1.sval);
									//if (t2 == null || this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento() ) == null)
										//this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
									//else {
									//	String ambito = this.s.getNombreProcedimiento();
									//	String nombreVar = $1.sval;
									//	if (this.checkAmbitoUso(nombreVar, ambito) != null) {
									//		this.ts.removeToken((String) t2.getAttr("NOMBRE"));
									//		t2.addAttr("USO", AnalizadorSintactico.VARIABLE);
									//		this.ts.addToken((String) t2.getAttr("NOMBRE"), t2);
									//	} else {
											this.ts.removeToken(lexema);
											this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
										//}
									//}
								} else {
								//	if ( !this.checkAmbitoUso(t, this.s.getNombreProcedimiento(), $1.sval)){ 
								//		this.ts.removeToken(lexema);
								//		this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
								//	}else {
								//		Token tAux = this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento());
								//		this.ts.removeToken((String) tAux.getAttr("NOMBRE"));
										t.addAttr("USO", AnalizadorSintactico.VARIABLE);
										$$.sval = t.getAttr("NOMBRE").toString();
									//}
								}
							}
			   ;

expresion_aritmetica : termino { $$.sval = $1.sval; }
					 | expresion_aritmetica '+' termino {  polaca.addOperador("+"); }
					 | expresion_aritmetica '-' termino {  polaca.addOperador("-"); }
					 ;
					 
termino : factor {  // termino : factor 
					$$ = $1; }
		| '(' DOUBLE ')' factor { String lexema = $4.sval;
								  Token t = this.ts.getToken(lexema);
								  this.ts.removeToken(lexema);
								  if (t.getAttr("TIPO").equals(AnalizadorLexico.TYPE_UINT)) {
									Double d = Double.parseDouble( t.getAttr("NOMBRE").toString());
									t.addAttr("NOMBRE", d.toString());
									t.addAttr("TIPO", AnalizadorLexico.TYPE_DOUBLE);
									this.ts.addToken(lexema, t);
								} else 
									this.polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_CONVERSION, this.l, this.l.getLine()));
								}
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
						 String lexema = $1.sval;
						 this.ts.removeToken(lexema);
						lexema = lexema.concat("@").concat(this.s.getNombreProcedimiento());
						Token t = this.ts.getToken(lexema);
						// Busca en las variable que están en el mismo ambiente
						if (t == null)  {
							Token t1 = this.ts.getTokenByName($1.sval);
							this.ts.removeToken($1.sval);
							if (t1 == null || this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento()) == null ) {
								
								this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
							} else {
								Token tAux = this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento());
								this.ts.removeToken((String) tAux.getAttr("NOMBRE"));
								tAux.addAttr("USO", AnalizadorSintactico.VARIABLE);
								this.ts.addToken((String) tAux.getAttr("NOMBRE"), tAux);
								polaca.addOperando(tAux.getAttr("NOMBRE").toString());
							}
								
						} else {
							if ( this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento()) == null) {
								this.ts.removeToken(lexema);
								this.polaca.addSemanticError(new Error(CodigoIntermedio.VAR_NO_DECLARADA, this.l, this.l.getLine()));
							 }else {
								Token tAux = this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento());
								this.ts.removeToken((String) tAux.getAttr("NOMBRE"));
								tAux.addAttr("USO", AnalizadorSintactico.VARIABLE);
								this.ts.addToken((String) tAux.getAttr("NOMBRE"), tAux);
								polaca.addOperando(t.getAttr("NOMBRE").toString());
							}
						}
						
						}
		| CONSTANTE { 	// factor : CONSTANTE 
						$$ = $1;
						polaca.addOperando($$.sval);
					}
		
	   | CADENA { // factor : cadena
					$$ = $1; 
					 }
	   ;
	   
sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido END_IF { 
															  this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
															  // Desapila dirección incompleta 
															  Integer pasoIncompleto = polaca.getTop(); 	
															  // Completo el destino de BI
															  polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);
															  polaca.addOperador("L".concat(CodigoIntermedio.polacaNumber.toString()));
															 } 
					| IF '(' ')' cuerpo_if_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 
					| IF '(' condicion cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
					| IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF { 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			polaca.addOperador("L".concat(CodigoIntermedio.polacaNumber.toString()));
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
											  // Agrego Etiqueta L-NUMEROPOLACA
											  polaca.addOperador(("L").concat((CodigoIntermedio.polacaNumber).toString()));
											  
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

sentencia_salida : OUT '(' CADENA ')' { polaca.addOperando($3.sval);
										polaca.addOperador("OUT");}
				 ;

invocaciones_procedimiento : IDENTIFICADOR '(' parametros_invocacion')' {   String id = $1.sval;
																		  Token t = this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento());
																		  this.ts.removeToken(id);
																		  if ( t == null) {
																			this.polaca.addSemanticError(new Error(CodigoIntermedio.PROC_NO_DECLARADO, this.l, this.l.getLine()));
																		  }
																		  else {
																			  t = this.ts.getToken((String) t.getAttr("NOMBRE"));
																			  if ( (Integer) t.getAttr("CANT. PARAMETROS") != this.countParameter ) {
																				this.polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_CANT_PARAM, this.l, this.l.getLine()));
																				this.countParameter = 0;
																			 } else {
																			  
																			  Integer previousCount = Integer.parseInt(t.getAttr("INVOCACIONES DISPONIBLES").toString());
																			  if (previousCount <= 0)
																				polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_INVOCACIONES_PROC, this.l, this.l.getLine()));
																			  else {
																				t.addAttr("INVOCACIONES DISPONIBLES", (previousCount-1));
																				// Apilo paso incompleto
																			  Integer paso = polaca.getTopProcedure();
																			  polaca.addOperador("");
																			  polaca.addDirectionProc(CodigoIntermedio.polacaNumber - 1, t.getAttr("NOMBRE").toString());
																			  polaca.addOperador("BI");
																			  
																			  }
																			}
																		}
																		}
																		  
						   | IDENTIFICADOR '(' ')' {
													 String id = $1.sval;
													 Token t = this.checkAmbitoUso($1.sval, this.s.getNombreProcedimiento());
													 this.ts.removeToken(id);
													 if ( t == null) {
														this.polaca.addSemanticError(new Error(CodigoIntermedio.PROC_NO_DECLARADO, this.l, this.l.getLine()));
													  }
													  else {
														 t = this.ts.getToken((String) t.getAttr("NOMBRE"));
														  if ( (Integer) t.getAttr("CANT. PARAMETROS")  > 0) {
															this.polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_CANT_PARAM, this.l, this.l.getLine()));
															this.countParameter = 0;
														 } else {
														  // Apilo paso incompleto
														   Integer previousCount = Integer.parseInt(t.getAttr("INVOCACIONES DISPONIBLES").toString());
														  if (previousCount <= 0)
															polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_INVOCACIONES_PROC, this.l, this.l.getLine()));
														  else {
															  t.addAttr("INVOCACIONES DISPONIBLES", (previousCount-1));
															  Integer paso = polaca.getTopProcedure();
															  polaca.addOperador("");
															  polaca.addDirectionProc(CodigoIntermedio.polacaNumber - 1, t.getAttr("NOMBRE").toString());
															  polaca.addOperador("BI");
															
															  }
														  }
														  
														}
													}
				           ;

parametros_invocacion : IDENTIFICADOR ':' IDENTIFICADOR { this.countParameter++;
														  String lexemaProc = $1.sval;
														  String lexemaPar = $3.sval;
														  Token t = this.checkAmbitoUso(lexemaProc, this.s.getNombreProcedimiento());
														  Token t1 = this.checkAmbitoUso(lexemaPar, this.s.getNombreProcedimiento());
														   if ( t == null || t1 == null || t.getAttr("USO")== null ||  !( t.getAttr("USO").toString().equals(AnalizadorSintactico.NOMBREPAR)))
															polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_PARAM_PROC, this.l, this.l.getLine()));
														  else {
															if (t.getAttr("TIPO") == null || !t.getAttr("TIPO").toString().equals(t1.getAttr("TIPO"))) {
																polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_INVOCACION_PAR, this.l, this.l.getLine()));
															}
														
															}
														}
															
					  | parametros_invocacion ',' IDENTIFICADOR ':' IDENTIFICADOR {
														this.countParameter++;
														String lexemaProc = $3.sval;
														  String lexemaPar = $5.sval;
														  Token t = this.checkAmbitoUso(lexemaProc, this.s.getNombreProcedimiento());
														  Token t1 = this.checkAmbitoUso(lexemaPar, this.s.getNombreProcedimiento());
														   if ( t == null || t1 == null || t.getAttr("USO")== null ||  !( t.getAttr("USO").toString().equals(AnalizadorSintactico.NOMBREPAR)))
															polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_PARAM_PROC, this.l, this.l.getLine()));
														  else {
															if (t.getAttr("TIPO") == null || !t.getAttr("TIPO").toString().equals(t1.getAttr("TIPO"))) {
																polaca.addSemanticError(new Error(CodigoIntermedio.ERROR_INVOCACION_PAR, this.l, this.l.getLine()));
															}
														
														}
												
					  }
					  ;

sentencia_control : inicio_while '(' condicion ')' LOOP cuerpo_while_bien_definido { this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; 
																					  // Desapilo el tope de la pila 
																					  Integer paso = polaca.getTop();
																					  Integer pasoInicio = polaca.getTop();
																					  polaca.addDirection(paso, CodigoIntermedio.polacaNumber + 2);
																					  polaca.addOperando("");
																					  polaca.addDirection(CodigoIntermedio.polacaNumber - 1, pasoInicio);
																					  polaca.addOperando("BI");
																					  polaca.addOperador(("L").concat((CodigoIntermedio.polacaNumber).toString()));
																					  } 
				  | inicio_while '(' ')' LOOP cuerpo_while_bien_definido { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 				
				  | inicio_while condicion LOOP cuerpo_while_bien_definido { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
				  | inicio_while '(' condicion ')' LOOP cuerpo_while_mal_definido { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
				  ;
				  
inicio_while : WHILE { // Apilamos el número de paso donde comienza la condición
						polaca.stackUp(CodigoIntermedio.polacaNumber);
						polaca.addOperando(("L").concat(CodigoIntermedio.polacaNumber.toString()));
						}
						
cuerpo_while_bien_definido : '{' sentencias_ejecutables '}' 

cuerpo_while_mal_definido :  sentencia_ejecutable 

			

operador : '<' { $$.sval = "<"; }
		 | '>' { $$.sval = ">"; }
		 | MAYORIGUAL { $$.sval = ">="; }
		 | MENORIGUAL { $$.sval = "<=";}
		 | DISTINTO { $$.sval = "!="; }
		 | COMPARACION { $$.sval = "==";}
		 ;

%%

AnalizadorLexico l;
AnalizadorSintactico s;
TablaDeSimbolos ts;
Integer count = 0;
CodigoIntermedio polaca;
Integer countParameter = 0;

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

public Token checkAmbitoDeclaracion(String nombreVar, String ambitoVar) {
	if (ambitoVar == null)
		return null;
	for (Token t : this.ts.getTokens()){
		if (this.checkAmbitoDeclaracion(t, ambitoVar, nombreVar))
			return t;
	}
	return null;
}

public Token checkAmbitoUso(String nombreVar, String ambitoVar) {
	if (ambitoVar == null)
		return null;
	for (Token t : this.ts.getTokens()){
		if (this.checkAmbitoUso(t, ambitoVar, nombreVar))
			return t;
	}
	return null;
}

public  Integer countAmbito(String t) {
	return (int) t.chars().filter(ch -> ch == '@').count();
}

public boolean checkAmbitoDeclaracion(Token t, String ambitoVar, String nombreVar) {
	String ambitoT = (String) t.getAttr("AMBITO");
	return ( (t.getAttr("AMBITO") != null) &&  (t.getAttr("NOMBRE_ANT") != null)
				&& ( ambitoT.contains(ambitoVar) ) && 
				( (String) t.getAttr("NOMBRE_ANT")).equals(nombreVar)  &&
				(this.countAmbito(ambitoVar) >= this.countAmbito(ambitoT)) );
}

public boolean checkAmbitoUso(Token t, String ambitoVar, String nombreVar) {
	String ambitoT = (String) t.getAttr("AMBITO");
	return ( (t.getAttr("AMBITO") != null) &&  (t.getAttr("NOMBRE_ANT") != null)
				&& ( ambitoT.contains(ambitoVar) || ambitoVar.contains(ambitoT) ) && 
				( (String) t.getAttr("NOMBRE_ANT")).equals(nombreVar)  &&
				(this.countAmbito(ambitoVar) >= this.countAmbito(ambitoT) ));
}

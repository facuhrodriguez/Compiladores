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
package CodigoIntermedio;
import AnalizadorLexico.*;
import AnalizadorLexico.Error;
%}

programa : sentencias { this.s.addSyntaxStruct( AnalizadorSintactico.principalStruct );}
		 | error ';' { this.s.addSyntaxError(new Error(AnalizadorSintactico.errorPrincipal, this.l, this.l.getLine()));}
		 ;

sentencias : sentencia 
		   | sentencias sentencia
		   | error sentencia { this.s.addSyntaxError( new Error(AnalizadorSintactico.sentencia, this.l, this.l.getLine()));}
		   ;
		   
sentencia : sentencia_declarativa 
		  | sentencia_ejecutable
		  ;

sentencias_ejecutables : sentencia_ejecutable
					   | sentencias_ejecutables sentencia_ejecutable
					   ;

sentencia_declarativa : declaraciones_id
					  | declaraciones_procedimiento
					  ;


declaraciones_id : declaracion_id ';' { this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
				 | declaraciones_id ',' declaracion_id
				 | declaracion_id { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorDeclarative, this.l, this.l.getLine() )); }
				 ;

declaracion_id : tipo IDENTIFICADOR 
			   ;

tipo : UINT { }
	 | DOUBLE {  }
	 ;

declaraciones_procedimiento : encabezado_procedimiento '{' sentencias '}' { this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); };
							;

encabezado_procedimiento : PROC IDENTIFICADOR '(' parametros ')' NI '=' CONSTANTE
						 | PROC IDENTIFICADOR '(' ')' NI '=' CONSTANTE 
						 ;

parametros : declaracion_id { this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							}
		   | parametros ',' declaracion_id { this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											}
		   | REF declaracion_id {  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
								}
		   ;

sentencia_ejecutable : asignaciones ';' { this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
					 | sentencia_seleccion  
					 | sentencia_control
					 | sentencia_salida ';' { this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
					 | invocaciones_procedimiento ';' { this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
					 ;

asignaciones : lado_izquierdo '=' expresion_aritmetica { // polaca.addAsig($1);
													     // polaca.addAsigOperador($2);
													   }
			 | lado_izquierdo COMPARACION expresion_aritmetica { this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
			 ;

lado_izquierdo : IDENTIFICADOR { $$ = $1; }
			   ;

expresion_aritmetica : termino
					 | expresion_aritmetica '+' termino { /* polaca.addOperador($2); */}
					 | expresion_aritmetica '-' termino { /* polaca.addOperador($2); */}
					 ;
					 
termino : factor {  // termino : factor 
					$$ = $1; }
		| '(' DOUBLE ')' factor 
		| termino '*' factor 
		| termino '/' factor
		;

factor : '-' CONSTANTE { String lexema = $$.sval;
		 if (this.ts.getToken(lexema).getAttr("TIPO") == AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO) {
			Token t = new Token(AnalizadorLexico.CONSTANTE, 0, AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO);
			this.ts.addToken(lexema, t);
			$$ = new ParserVal(lexema);
		 } else 
			if (this.ts.getToken(lexema).getAttr("TIPO") == AnalizadorLexico.CONSTANTE_DOUBLE) {
				Double d = Double.parseDouble((String) this.ts.getToken(lexema).getAttr("VALOR"));
				Double number = MyDouble.checkNegativeRange(-d, 0.0);
				Token t = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.CONSTANTE_DOUBLE);
				this.ts.addToken(lexema, t);
				$$ = new ParserVal(lexema);
		 } 
		  //// polaca.addOperando($$);
		  }

		| lado_izquierdo { // factor : IDENTIFICADOR
						 $$ = $1;
						 // polaca.addOperando($$);
						}
		| CONSTANTE { 	// factor : CONSTANTE 
						$$ = $1;
						// polaca.addOperando($$);
					}
		
	   | CADENA { // factor : cadena
					$$ = $1; 
					/* polaca.addOperando($$); */}
	   ;
	   
sentencia_seleccion : IF '(' condicion ')' cuerpo_if END_IF { 
															  this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
															  //Desapila dirección incompleta
															  //Integer pasoIncompleto = // polaca.getTop(); 	
															  // Completa el destino de la BF
															  // polaca.addDirection(pasoIncompleto, CodigoIntermedio.// polacaNumber+2);
															  // Crea paso incompleto
															  // polaca.addOperador('');
															  // Apila el número del paso incompleto
															  // polaca.stackUp(CodigoIntermedio.// polacaNumber);
															  // Se crea el paso BI
															  // polaca.addOperador('BI');
															 
															 } 
					| IF '(' condicion cuerpo_if { this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
					| IF '(' condicion ')' cuerpo_if ELSE cuerpo_else END_IF { 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			
																		} 
					;

cuerpo_if : '{' sentencias_ejecutables  '}'
		  | sentencia_ejecutable
		  ;
		  
cuerpo_else : '{' sentencias_ejecutables '}'
			| sentencia_ejecutable
			;

condicion : expresion_aritmetica operador expresion_aritmetica { // polaca.addOperando('');
																 // Apilo paso incompleto
																 // polaca.stackUp(CodigoIntermedio.// polacaNumber);
																 // Creo el paso BF
																 // polaca.addOperando('BF');
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
		  | condicion error expresion_aritmetica { this.s.addSyntaxError( new Error(AnalizadorSintactico.conditionError, this.l, this.l.getLine())); }
		  | condicion operador expresion_aritmetica
		  ;

sentencia_salida : OUT '(' CADENA ')'
				 ;

invocaciones_procedimiento : IDENTIFICADOR '(' parametros_invocacion')'
						   | IDENTIFICADOR '(' ')'
				           ;

parametros_invocacion : IDENTIFICADOR ':' IDENTIFICADOR
					  ;

sentencia_control : WHILE '(' condicion ')' LOOP '{' sentencias_ejecutables'}' { this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; } 
				  | WHILE '(' condicion ')' LOOP sentencia_ejecutable { this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ;} 
				  ;
				  
operador : '<' { $$ = $1; }
		 | '>' { $$ = $1; }
		 | MAYORIGUAL { $$ = $1; }
		 | MENORIGUAL { $$ = $1; }
		 | DISTINTO { $$ = $1; }
		 | COMPARACION { $$ = $1; }
		 ;
%%

AnalizadorLexico l;
AnalizadorSintactico s;
TablaDeSimbolos ts;
Integer count = 0;
CodigoIntermedio // polaca;

public void setLexico(AnalizadorLexico l) {
	this.l = l;
}

public void setSintactico(AnalizadorSintactico s) {
	this.s = s;
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



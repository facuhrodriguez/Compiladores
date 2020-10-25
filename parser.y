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

sentencia_declarativa : declaraciones_id
					  | declaraciones_procedimiento
					  ;


declaraciones_id : declaracion_id ';' { this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
				 | declaraciones_id ',' declaracion_id
				 
				 ;

declaracion_id : tipo IDENTIFICADOR { String lexema = $2.sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", $1.sval);
									  t.addAttr("USO", AnalizadorSintactico.VARIABLE);
									  this.ts.addToken(lexema, t);
									 }
			   ;

tipo : UINT { $$ = new ParserVal(AnalizadorLexico.TYPE_UINT);}
	 | DOUBLE { $$ = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
	 ;

declaraciones_procedimiento : encabezado_procedimiento '{' sentencias '}' { this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); };
							;

encabezado_procedimiento : PROC IDENTIFICADOR '(' parametros ')' NI '=' CONSTANTE { String lexema = $2.sval; }
						 | PROC IDENTIFICADOR '(' ')' NI '=' CONSTANTE  { String lexema = $2.sval; 
																		  Token t = this.ts.getToken(lexema);
																		  t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																		  this.ts.addToken(lexema, t);}
																		
						 | PROC '(' { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
						 ;

parametros : declaracion_id { this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							  String lexema = $1.sval;
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

asignaciones : lado_izquierdo '=' expresion_aritmetica {  polaca.addOperando($1.sval);
													      polaca.addOperador($2.sval);
													   }
			 | lado_izquierdo COMPARACION expresion_aritmetica { this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
			 ;

lado_izquierdo : IDENTIFICADOR { $$ = $1;
								String lexema = $1.sval;
								Token t = this.ts.getToken(lexema);
							    t.addAttr("USO", AnalizadorSintactico.VARIABLE);
							    this.ts.addToken(lexema, t);
								 }
			   ;

expresion_aritmetica : termino
					 | expresion_aritmetica '+' termino {  polaca.addOperador($2.sval); }
					 | expresion_aritmetica '-' termino {  polaca.addOperador($2.sval); }
					 ;
					 
termino : factor {  // termino : factor 
					$$ = $1; }
		| '(' DOUBLE ')' factor 
		| termino '*' factor 
		| termino '/' factor
		;

factor : '-' CONSTANTE { String valor = yylval.sval;
	if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.TYPE_UINT) {
		this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
		Token t = new Token(AnalizadorLexico.CONSTANTE, 0, AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO);
		this.ts.addToken(valor, t);
		$$ = new ParserVal(valor);
	} else 
		if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.TYPE_DOUBLE) {
			Double number = MyDouble.check(this.l);
			if (MyDouble.truncate) { 
				if (!this.l.warningExist(this.l.getBuffer()))
					this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_DOUBLE, this.l, this.l.getLine()));
			}
				
			Token t = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.CONSTANTE_DOUBLE);
			this.ts.addToken(valor, t);
			$$ = new ParserVal(valor);
	} 
	   polaca.addOperando($$.sval);
	 }

		| lado_izquierdo { // factor : IDENTIFICADOR
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
					| IF '(' ')' cuerpo_if_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 
					| IF '(' condicion cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
					| IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF { 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			}
					| IF '(' ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));} 																
					| IF condicion cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}											
					| IF '(' condicion ')' cuerpo_if_mal_definido END_IF { this.s.addSyntaxError( new Error( AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					| IF condicion ')' cuerpo_if_bien_definido END_IF { this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
					| IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_mal_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					| IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_mal_definido END_IF{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					| IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_bien_definido END_IF { this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
					;


cuerpo_if_bien_definido: '{' sentencias_ejecutables '}' 
					   ;
cuerpo_if_mal_definido : sentencias_ejecutables 
					   ;
		  
cuerpo_else_bien_definido : '{' sentencias_ejecutables '}' 
						  ;

cuerpo_else_mal_definido:  sentencias_ejecutables 
						;


condicion : expresion_aritmetica operador expresion_aritmetica { polaca.addOperando("");
																 // Apilo paso incompleto
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
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



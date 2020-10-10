//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 30 "parser.y"
package AnalizadorSintactico;
import AnalizadorLexico.*;
import AnalizadorLexico.Error;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENTIFICADOR=257;
public final static short CONSTANTE=258;
public final static short CADENA=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short OUT=264;
public final static short FUNC=265;
public final static short RETURN=266;
public final static short UINT=267;
public final static short DOUBLE=268;
public final static short WHILE=269;
public final static short LOOP=270;
public final static short REF=271;
public final static short PROC=272;
public final static short NI=273;
public final static short UP=274;
public final static short DOWN=275;
public final static short COMPARACION=276;
public final static short MENORIGUAL=277;
public final static short DISTINTO=278;
public final static short MAYORIGUAL=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    1,    2,    2,    5,    5,    3,
    3,    6,    6,    6,    8,    9,    9,    7,   10,   10,
   11,   11,   11,    4,    4,    4,    4,    4,   12,   12,
   17,   18,   18,   18,   19,   19,   19,   19,   20,   20,
   20,   20,   13,   13,   13,   22,   22,   23,   23,   21,
   21,   21,   15,   16,   16,   25,   14,   14,   24,   24,
   24,   24,   24,   24,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    2,    1,
    1,    2,    3,    1,    2,    1,    1,    4,    8,    7,
    1,    3,    2,    2,    1,    1,    2,    2,    3,    3,
    1,    1,    3,    3,    1,    4,    3,    3,    1,    1,
    2,    1,    6,    4,    8,    3,    1,    3,    1,    3,
    3,    3,    4,    4,    3,    3,    8,    6,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   16,   17,    0,    0,    0,    0,
    3,    6,    7,    0,   11,    0,    0,    0,    0,   25,
   26,    0,    0,    0,    2,    5,    0,    0,    0,    0,
    0,    4,    0,   12,   15,    0,   24,   27,   28,    0,
    0,    0,   55,    0,   31,   40,   42,    0,    0,   39,
    0,    0,   35,    0,    0,    0,    0,   13,    0,    0,
    0,    0,    0,   54,    0,   41,   64,   62,   63,   61,
    0,    0,   59,   60,    0,    0,    0,    0,    0,    0,
   47,   44,    0,   53,    0,    0,    0,   21,    0,   18,
   56,    0,    0,    0,    0,   37,   38,    0,    8,    0,
    0,    0,    0,   23,    0,    0,    0,   36,   46,    9,
    0,   43,    0,   58,    0,   22,    0,    0,   49,    0,
    0,   20,    0,    0,   45,   57,   19,   48,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,  100,   14,   15,   16,   17,   18,
   89,   19,   20,   21,   22,   23,   24,   51,   52,   53,
   54,   82,  120,   83,   44,
};
final static short yysindex[] = {                       -61,
  104,  -31,  -12,   -4,    0,    0,    2, -240,    0, -139,
    0,    0,    0,    5,    0,    8, -205,  -77,   10,    0,
    0,   18,   32,  -58,    0,    0,  -27,   65, -180,   65,
   46,    0, -211,    0,    0,  123,    0,    0,    0,   65,
   65,   37,    0,   56,    0,    0,    0, -167, -154,    0,
  -23,  -13,    0,   27,   71,  -30,   -8,    0, -139, -112,
   20,   20, -142,    0,   75,    0,    0,    0,    0,    0,
   65,   65,    0,    0,   65,   -7,   -7,   65, -209, -111,
    0,    0,   65,    0, -150, -211, -149,    0,   -1,    0,
    0,   -7,  -13,  -13,   20,    0,    0,   20,    0,  -81,
 -169,   20,  -92,    0,   66, -211, -138,    0,    0,    0,
  -82,    0, -209,    0, -127,    0,   73, -209,    0, -126,
  -71,    0, -122,  -55,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -56,    0,    0,    0,    0,    0,    0,    0,  138,
    0,    0,    0,    7,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,    0,    0,    0,
   80,   83,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -35,   21,   51,    0,    0,   57,    0,    0,
    0,   81,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  111,   25,    0,  286,  -40,    0,    0,  -10,    0,    0,
    0,    0,    0,    0,    0,    0,   31,  -25,   28,   -2,
  110,   74,    0,  100,    0,
};
final static int YYTABLESIZE=410;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         32,
   14,   32,   41,   32,   31,   33,   10,   33,   27,   33,
   85,   79,   90,   43,   61,   62,   31,   32,   32,   71,
   32,   72,   58,   33,   33,   26,   33,   28,   76,   73,
  113,   74,   87,   77,   32,   29,   73,   49,   74,  107,
  118,   30,  106,  109,   14,   36,   88,    2,   33,   95,
    3,   35,   98,  126,    4,    5,    6,  102,   50,    7,
   50,   34,   71,   34,   72,   34,   34,   80,   37,  128,
   50,   50,  121,   96,   97,  104,   38,  124,   55,   34,
   34,   32,   34,   26,   32,   57,   73,   33,   74,  108,
   39,   50,  111,  112,   63,  116,   64,   51,   93,   94,
   65,   50,   50,   66,   48,   50,   50,   50,   50,   49,
   50,   84,   50,   50,   91,   92,   51,    2,   51,  103,
    3,   52,   50,  105,    4,   14,  115,    5,    6,    7,
  122,   10,    8,  123,  117,  127,  125,    1,   30,   56,
   52,   29,   52,   34,    2,    2,   60,    3,    3,   79,
   75,    4,    4,  101,    5,    6,    7,    7,    0,    8,
    0,    0,   25,    0,    2,    0,    0,    3,    0,    0,
    0,    4,    0,   50,    2,    2,    7,    3,    3,   51,
    0,    4,    4,    0,    0,    2,    7,    7,    3,    0,
    0,    0,    4,    0,    1,    2,    0,    7,    3,    0,
    0,    2,    4,   52,    3,    5,    6,    7,    4,    0,
    8,    0,    0,    7,   32,   32,    0,   40,   32,   31,
   33,   33,   32,    0,   33,   78,    0,   32,   33,   42,
    0,    0,    0,   33,   32,   32,   32,   32,    0,    0,
   33,   33,   33,   33,    0,   67,   68,   69,   70,   45,
   46,   47,   67,   68,   69,   70,    0,   14,    5,    6,
   14,    0,   86,   10,   14,    0,   10,   14,   14,   14,
   10,    0,   14,   10,   10,   10,   34,   34,   10,    0,
   34,    0,   78,    2,   34,    0,    3,    0,    0,   34,
    4,    0,    0,    0,    0,    7,   34,   34,   34,   34,
    0,    0,   67,   68,   69,   70,   50,   50,    0,    0,
   50,    0,   51,   51,   50,    0,   51,    0,    0,   50,
   51,   45,   46,   47,    0,   51,   50,   50,   50,   50,
    0,    0,   51,   51,   51,   51,   52,   52,    0,   81,
   52,    0,    0,    0,   52,    0,    0,    0,    0,   52,
    0,    0,    0,    0,    0,    0,   52,   52,   52,   52,
    2,    0,    0,    3,   99,   81,    0,    4,    0,    0,
    5,    6,    7,    0,    0,    8,    0,    0,   59,    2,
    0,    0,    3,    0,    0,  110,    4,    0,  114,    5,
    6,    7,    0,    0,    8,    0,  119,    0,   99,    0,
    0,    0,    0,   99,    0,    0,  110,    0,    0,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   61,   45,   61,   41,    0,   43,   40,   45,
   41,  123,  125,   41,   40,   41,  257,   59,   60,   43,
   62,   45,   33,   59,   60,    1,   62,   40,   42,   60,
  123,   62,   41,   47,   10,   40,   60,   45,   62,   41,
  123,   40,   44,  125,   44,  123,   57,  257,   44,   75,
  260,  257,   78,  125,  264,  267,  268,   83,   28,  269,
   30,   41,   43,   43,   45,   45,   59,   41,   59,  125,
   40,   41,  113,   76,   77,   86,   59,  118,  259,   59,
   60,  123,   62,   59,   60,   40,   60,  123,   62,   92,
   59,   41,  262,  263,   58,  106,   41,   41,   71,   72,
  268,   71,   72,  258,   40,   75,   76,   77,   78,   45,
   60,   41,   62,   83,  257,   41,   60,  257,   62,  270,
  260,   41,   92,  273,  264,  125,   61,  267,  268,  269,
  258,  125,  272,   61,  273,  258,  263,    0,   59,   30,
   60,   59,   62,  123,  257,  257,   36,  260,  260,  123,
   51,  264,  264,   80,  267,  268,  269,  269,   -1,  272,
   -1,   -1,   59,   -1,  257,   -1,   -1,  260,   -1,   -1,
   -1,  264,   -1,  123,  257,  257,  269,  260,  260,  123,
   -1,  264,  264,   -1,   -1,  257,  269,  269,  260,   -1,
   -1,   -1,  264,   -1,  256,  257,   -1,  269,  260,   -1,
   -1,  257,  264,  123,  260,  267,  268,  269,  264,   -1,
  272,   -1,   -1,  269,  256,  257,   -1,  276,  260,  276,
  256,  257,  264,   -1,  260,  256,   -1,  269,  264,  257,
   -1,   -1,   -1,  269,  276,  277,  278,  279,   -1,   -1,
  276,  277,  278,  279,   -1,  276,  277,  278,  279,  257,
  258,  259,  276,  277,  278,  279,   -1,  257,  267,  268,
  260,   -1,  271,  257,  264,   -1,  260,  267,  268,  269,
  264,   -1,  272,  267,  268,  269,  256,  257,  272,   -1,
  260,   -1,  256,  257,  264,   -1,  260,   -1,   -1,  269,
  264,   -1,   -1,   -1,   -1,  269,  276,  277,  278,  279,
   -1,   -1,  276,  277,  278,  279,  256,  257,   -1,   -1,
  260,   -1,  256,  257,  264,   -1,  260,   -1,   -1,  269,
  264,  257,  258,  259,   -1,  269,  276,  277,  278,  279,
   -1,   -1,  276,  277,  278,  279,  256,  257,   -1,   54,
  260,   -1,   -1,   -1,  264,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,
  257,   -1,   -1,  260,   79,   80,   -1,  264,   -1,   -1,
  267,  268,  269,   -1,   -1,  272,   -1,   -1,  256,  257,
   -1,   -1,  260,   -1,   -1,  100,  264,   -1,  103,  267,
  268,  269,   -1,   -1,  272,   -1,  111,   -1,  113,   -1,
   -1,   -1,   -1,  118,   -1,   -1,  121,   -1,   -1,  124,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IDENTIFICADOR","CONSTANTE","CADENA","IF",
"THEN","ELSE","END_IF","OUT","FUNC","RETURN","UINT","DOUBLE","WHILE","LOOP",
"REF","PROC","NI","UP","DOWN","COMPARACION","MENORIGUAL","DISTINTO",
"MAYORIGUAL",
};
final static String yyrule[] = {
"$accept : programa",
"programa : sentencias",
"programa : error ';'",
"sentencias : sentencia",
"sentencias : sentencias sentencia",
"sentencias : error sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencias_ejecutables : sentencia_ejecutable",
"sentencias_ejecutables : sentencias_ejecutables sentencia_ejecutable",
"sentencia_declarativa : declaraciones_id",
"sentencia_declarativa : declaraciones_procedimiento",
"declaraciones_id : declaracion_id ';'",
"declaraciones_id : declaraciones_id ',' declaracion_id",
"declaraciones_id : declaracion_id",
"declaracion_id : tipo IDENTIFICADOR",
"tipo : UINT",
"tipo : DOUBLE",
"declaraciones_procedimiento : encabezado_procedimiento '{' sentencias '}'",
"encabezado_procedimiento : PROC IDENTIFICADOR '(' parametros ')' NI '=' CONSTANTE",
"encabezado_procedimiento : PROC IDENTIFICADOR '(' ')' NI '=' CONSTANTE",
"parametros : declaracion_id",
"parametros : parametros ',' declaracion_id",
"parametros : REF declaracion_id",
"sentencia_ejecutable : asignaciones ';'",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_salida ';'",
"sentencia_ejecutable : invocaciones_procedimiento ';'",
"asignaciones : lado_izquierdo '=' expresion_aritmetica",
"asignaciones : lado_izquierdo COMPARACION expresion_aritmetica",
"lado_izquierdo : IDENTIFICADOR",
"expresion_aritmetica : termino",
"expresion_aritmetica : expresion_aritmetica '+' termino",
"expresion_aritmetica : expresion_aritmetica '-' termino",
"termino : factor",
"termino : '(' DOUBLE ')' factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"factor : lado_izquierdo",
"factor : CONSTANTE",
"factor : '-' CONSTANTE",
"factor : CADENA",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if END_IF",
"sentencia_seleccion : IF '(' condicion cuerpo_if",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if ELSE cuerpo_else END_IF",
"cuerpo_if : '{' sentencias_ejecutables '}'",
"cuerpo_if : sentencia_ejecutable",
"cuerpo_else : '{' sentencias_ejecutables '}'",
"cuerpo_else : sentencia_ejecutable",
"condicion : expresion_aritmetica operador expresion_aritmetica",
"condicion : condicion error expresion_aritmetica",
"condicion : condicion operador expresion_aritmetica",
"sentencia_salida : OUT '(' CADENA ')'",
"invocaciones_procedimiento : IDENTIFICADOR '(' parametros_invocacion ')'",
"invocaciones_procedimiento : IDENTIFICADOR '(' ')'",
"parametros_invocacion : IDENTIFICADOR ':' IDENTIFICADOR",
"sentencia_control : WHILE '(' condicion ')' LOOP '{' sentencias_ejecutables '}'",
"sentencia_control : WHILE '(' condicion ')' LOOP sentencia_ejecutable",
"operador : '<'",
"operador : '>'",
"operador : MAYORIGUAL",
"operador : MENORIGUAL",
"operador : DISTINTO",
"operador : COMPARACION",
};

//#line 182 "parser.y"

AnalizadorLexico l;
AnalizadorSintactico s;
TablaDeSimbolos ts;
Integer count = 0;

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


//#line 405 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 35 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.principalStruct );}
break;
case 2:
//#line 36 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorPrincipal, this.l));}
break;
case 5:
//#line 41 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sentencia, this.l));}
break;
case 12:
//#line 57 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
break;
case 14:
//#line 59 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorDeclarative, this.l )); }
break;
case 16:
//#line 65 "parser.y"
{ }
break;
case 17:
//#line 66 "parser.y"
{  }
break;
case 18:
//#line 69 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); }
break;
case 21:
//#line 76 "parser.y"
{ this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l));
								this.count = 0;
							  }
							}
break;
case 22:
//#line 82 "parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l));
											}
break;
case 23:
//#line 86 "parser.y"
{  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l));
										this.count = 0;
									}
								}
break;
case 24:
//#line 94 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
break;
case 27:
//#line 98 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
break;
case 28:
//#line 99 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
break;
case 30:
//#line 103 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l));}
break;
case 31:
//#line 106 "parser.y"
{ yyval = val_peek(0); }
break;
case 35:
//#line 114 "parser.y"
{  /* termino : factor */
					yyval = val_peek(0); }
break;
case 39:
//#line 121 "parser.y"
{ /* factor : IDENTIFICADOR*/
						 yyval = val_peek(0); }
break;
case 40:
//#line 123 "parser.y"
{ /* factor : CONSTANTE */
					yyval = val_peek(0); }
break;
case 41:
//#line 125 "parser.y"
{ String lexema = yyval.sval;
						 if (this.ts.getToken(lexema).getAttr("TIPO") == AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO) {
							Token t = new Token(AnalizadorLexico.CONSTANTE, 0, AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO);
							this.ts.addToken(lexema, t);
							yyval = new ParserVal(lexema);
						 } else 
							if (this.ts.getToken(lexema).getAttr("TIPO") == AnalizadorLexico.CONSTANTE_DOUBLE) {
								Double d = Double.parseDouble((String) this.ts.getToken(lexema).getAttr("VALOR"));
								Double number = MyDouble.checkNegativeRange(-d, 0.0);
								Token t = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.CONSTANTE_DOUBLE);
								this.ts.addToken(lexema, t);
								yyval = new ParserVal(lexema);
						 } 
						}
break;
case 42:
//#line 139 "parser.y"
{ yyval = val_peek(0); }
break;
case 43:
//#line 142 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure ) ;}
break;
case 44:
//#line 143 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l));}
break;
case 45:
//#line 144 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure ) ;}
break;
case 50:
//#line 155 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); }
break;
case 51:
//#line 156 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.conditionError, this.l)); }
break;
case 57:
//#line 170 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; }
break;
case 58:
//#line 171 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ;}
break;
case 59:
//#line 174 "parser.y"
{ }
break;
case 60:
//#line 175 "parser.y"
{ }
break;
case 61:
//#line 176 "parser.y"
{  }
break;
case 62:
//#line 177 "parser.y"
{ }
break;
case 63:
//#line 178 "parser.y"
{ }
break;
case 64:
//#line 179 "parser.y"
{  }
break;
//#line 720 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################

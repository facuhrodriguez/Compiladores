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
import CodigoIntermedio.*;
//#line 22 "Parser.java"




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
    0,    0,    1,    1,    2,    2,    5,    5,    5,    3,
    3,    6,    6,    8,    9,    9,    7,   10,   10,   10,
   11,   11,   11,   12,    4,    4,    4,    4,    4,   13,
   13,   18,   19,   19,   19,   20,   20,   20,   20,   21,
   21,   21,   21,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   23,   25,   24,   26,   22,   22,
   16,   17,   17,   28,   15,   15,   15,   15,   29,   30,
   27,   27,   27,   27,   27,   27,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    2,    2,    1,
    1,    2,    3,    2,    1,    1,    4,    8,    7,    2,
    1,    3,    2,    2,    2,    1,    1,    2,    2,    3,
    3,    1,    1,    3,    3,    1,    4,    3,    3,    2,
    1,    1,    1,    6,    5,    5,    8,    7,    4,    6,
    5,    8,    8,    8,    3,    1,    3,    1,    3,    3,
    4,    4,    3,    3,    6,    5,    4,    6,    3,    1,
    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   15,   16,    0,    0,    0,    0,
    3,    5,    6,    0,   11,    0,    0,    0,    0,   26,
   27,    0,    0,    0,    2,    0,   41,   42,   43,    0,
    0,    0,    0,   36,    0,    0,    0,    0,    0,   20,
    4,    0,   12,   14,    0,   25,   28,   29,    0,    0,
    0,   63,    0,    0,    0,    0,    0,   40,   76,   74,
   75,   73,    0,    0,   71,   72,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   13,    0,
    0,    0,    0,   62,    0,    0,    0,    0,    0,    0,
    0,   38,   39,    0,    0,   49,    0,   61,    0,    0,
    0,   67,    0,    0,    0,    0,   21,   17,   64,   37,
    0,   45,    0,    0,    0,   46,   55,   51,   66,    0,
    0,    7,    0,   23,    0,   24,    0,    0,    0,    0,
    0,   44,    0,   50,   70,   65,   68,    9,   69,    8,
    0,   22,    0,    0,   48,    0,    0,    0,    0,    0,
   19,    0,   57,   47,   52,   54,   53,   18,
};
final static short yydgoto[] = {                          9,
  146,   11,   12,   13,  123,   14,   15,   16,   17,   18,
  106,  107,   19,   20,   21,   22,   23,   24,   32,   33,
   34,   35,   72,  130,  115,  148,   73,   53,  102,  137,
};
final static short yysindex[] = {                      -125,
  -27,   16,   41,   37,    0,    0,   50,  -37,    0,   62,
    0,    0,    0,   29,    0,   35, -159,  -17,   54,    0,
    0,   61,   65,  -48,    0,  -30,    0,    0,    0,   38,
 -149,   14,   33,    0,   -2, -144,   44,  -54,  105,    0,
    0, -165,    0,    0,   62,    0,    0,    0,   56,   56,
   88,    0,  107,  108, -118,   34,    6,    0,    0,    0,
    0,    0,   56,   56,    0,    0,   56,   59,   59,   62,
   34,  -95,   56,  131,  -89,   10,   67,  -11,    0,  -84,
   -3,   -3,  -70,    0,   59, -151, -106,  -69,   33,   33,
   -3,    0,    0,  -71,  -63,    0,   -3,    0,   67,  -79,
   64,    0, -165,  -81,  -62,    9,    0,    0,    0,    0,
   80,    0,   62, -140, -135,    0,    0,    0,    0,  -52,
 -104,    0, -105,    0,  143,    0, -165,  -66,   62,  -50,
  -90,    0,  -90,    0,    0,    0,    0,    0,    0,    0,
  -43,    0,  157,  -58,    0,   62,  -44,  -42,  -33,  -14,
    0,  -32,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -45,    0,    0,    0,    0,    0,    0,    0,  232,
    0,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  174,  195,    0,    0,    0,    0,    0,    0,  -36,  -31,
  -26,    0,    0,    0,    0,    0,    2,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -126,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   -8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   46,   25,    0,  -13,    0,    0,    0,  217,  -34,    0,
    0,  -78,    0,    0,    0,    0,    0,    0,  -12,   77,
   49,   15,   43,  -68,    0,  129,  234,    0,  -72,    0,
};
final static int YYTABLESIZE=334;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   10,   33,   40,   33,   34,   65,   34,   66,   34,   35,
   52,   35,   50,   35,   59,   32,   70,   33,   33,  139,
   33,   38,   34,   34,  124,   34,  119,   35,   35,  104,
   35,   25,  129,   59,   41,   59,   81,   82,   71,   63,
  108,   64,   60,  105,   57,   10,   87,  136,  142,  128,
  100,   76,  127,  117,   91,   26,   63,   65,   64,   66,
   97,   60,  147,   60,  149,   65,  153,   66,  105,   65,
  101,   66,   42,   65,   68,   66,   36,   55,   56,   69,
   30,   33,   31,   55,   75,   31,   34,  122,   31,   37,
   80,   35,  105,   43,   31,   55,   59,   44,   86,   88,
   31,    5,    6,   31,   41,   45,  135,  138,   58,  140,
  111,  112,   46,   95,   74,   94,   92,   93,   41,   47,
   70,  131,  132,   48,   60,   10,  133,  134,   70,  114,
    1,    2,  113,  110,    3,   56,   56,   41,    4,   89,
   90,    5,    6,    7,   78,   83,    8,   84,   85,   54,
    2,    2,    2,    3,    3,    3,   70,    4,    4,    4,
    5,    6,    7,    7,    7,    8,    2,   96,   41,    3,
   41,   98,    2,    4,  144,    3,    5,    6,    7,    4,
   99,    8,    5,    6,    7,    2,  109,    8,    3,  101,
  120,  125,    4,  116,  126,    5,    6,    7,    2,  118,
    8,    3,  129,  141,    2,    4,  143,    3,    5,    6,
    7,    4,  145,    8,  151,   77,    7,  152,  154,   39,
  155,   59,   60,   61,   62,  158,   51,   49,   33,  156,
   32,    1,   31,   34,   33,   33,   33,   33,   35,   34,
   34,   34,   34,   59,   35,   35,   35,   35,  157,   59,
   59,   59,   59,   30,   58,    5,    6,   10,   79,  103,
   10,  150,   10,   10,   10,   67,    0,   10,   10,   10,
    0,   60,   10,   59,   60,   61,   62,   60,   60,   60,
   60,   59,   60,   61,   62,   59,   60,   61,   62,   59,
   60,   61,   62,    0,   27,   28,   29,   27,   28,   29,
   27,   28,   29,    0,    0,   54,   27,   28,   29,    0,
    0,   54,   27,   28,   29,   27,   28,   29,    2,  121,
    2,    3,    0,    3,    0,    4,    0,    4,    5,    6,
    7,    0,    7,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   40,   45,   41,   60,   43,   62,   45,   41,
   41,   43,   61,   45,   41,   61,  123,   59,   60,  125,
   62,    7,   59,   60,  103,   62,   99,   59,   60,   41,
   62,   59,  123,   60,   10,   62,   49,   50,   41,   43,
  125,   45,   41,   78,   30,    0,   41,  120,  127,   41,
   41,   37,   44,  125,   67,   40,   43,   60,   45,   62,
   73,   60,  131,   62,  133,   60,  125,   62,  103,   60,
  123,   62,   44,   60,   42,   62,   40,   40,   41,   47,
   40,  123,   45,   40,   41,   45,  123,  101,   45,   40,
   45,  123,  127,   59,   45,   40,  123,  257,   56,   57,
   45,  267,  268,   45,   80,  123,  120,  121,  258,  123,
  262,  263,   59,   71,  259,   70,   68,   69,   94,   59,
  123,  262,  263,   59,  123,  125,  262,  263,  123,   87,
  256,  257,   87,   85,  260,  262,  263,  113,  264,   63,
   64,  267,  268,  269,   40,   58,  272,   41,   41,  268,
  257,  257,  257,  260,  260,  260,  123,  264,  264,  264,
  267,  268,  269,  269,  269,  272,  257,  263,  144,  260,
  146,   41,  257,  264,  129,  260,  267,  268,  269,  264,
  270,  272,  267,  268,  269,  257,  257,  272,  260,  123,
  270,  273,  264,  263,  257,  267,  268,  269,  257,  263,
  272,  260,  123,   61,  257,  264,  273,  260,  267,  268,
  269,  264,  263,  272,  258,  270,  269,   61,  263,  257,
  263,  276,  277,  278,  279,  258,  257,  276,  270,  263,
  276,    0,   59,  270,  276,  277,  278,  279,  270,  276,
  277,  278,  279,  270,  276,  277,  278,  279,  263,  276,
  277,  278,  279,   59,  263,  267,  268,  257,   42,  271,
  260,  133,  262,  263,  264,   32,   -1,  267,  268,  269,
   -1,  270,  272,  276,  277,  278,  279,  276,  277,  278,
  279,  276,  277,  278,  279,  276,  277,  278,  279,  276,
  277,  278,  279,   -1,  257,  258,  259,  257,  258,  259,
  257,  258,  259,   -1,   -1,  268,  257,  258,  259,   -1,
   -1,  268,  257,  258,  259,  257,  258,  259,  257,  256,
  257,  260,   -1,  260,   -1,  264,   -1,  264,  267,  268,
  269,   -1,  269,  272,
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
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencias_ejecutables : sentencia_ejecutable",
"sentencias_ejecutables : sentencias_ejecutables sentencia_ejecutable",
"sentencias_ejecutables : error sentencia_ejecutable",
"sentencia_declarativa : declaraciones_id",
"sentencia_declarativa : declaraciones_procedimiento",
"declaraciones_id : declaracion_id ';'",
"declaraciones_id : declaraciones_id ',' declaracion_id",
"declaracion_id : tipo IDENTIFICADOR",
"tipo : UINT",
"tipo : DOUBLE",
"declaraciones_procedimiento : encabezado_procedimiento '{' sentencias '}'",
"encabezado_procedimiento : PROC IDENTIFICADOR '(' parametros ')' NI '=' CONSTANTE",
"encabezado_procedimiento : PROC IDENTIFICADOR '(' ')' NI '=' CONSTANTE",
"encabezado_procedimiento : PROC '('",
"parametros : declaracion_par",
"parametros : parametros ',' declaracion_par",
"parametros : REF declaracion_par",
"declaracion_par : tipo IDENTIFICADOR",
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
"factor : '-' CONSTANTE",
"factor : IDENTIFICADOR",
"factor : CONSTANTE",
"factor : CADENA",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' ')' cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF",
"sentencia_seleccion : IF '(' ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF",
"sentencia_seleccion : IF condicion cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_mal_definido END_IF",
"sentencia_seleccion : IF condicion ')' cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_mal_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_mal_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_bien_definido END_IF",
"cuerpo_if_bien_definido : '{' sentencias '}'",
"cuerpo_if_mal_definido : sentencias",
"cuerpo_else_bien_definido : '{' sentencias '}'",
"cuerpo_else_mal_definido : sentencias",
"condicion : expresion_aritmetica operador expresion_aritmetica",
"condicion : condicion operador expresion_aritmetica",
"sentencia_salida : OUT '(' CADENA ')'",
"invocaciones_procedimiento : IDENTIFICADOR '(' parametros_invocacion ')'",
"invocaciones_procedimiento : IDENTIFICADOR '(' ')'",
"parametros_invocacion : IDENTIFICADOR ':' IDENTIFICADOR",
"sentencia_control : WHILE '(' condicion ')' LOOP cuerpo_while_bien_definido",
"sentencia_control : WHILE '(' ')' LOOP cuerpo_while_bien_definido",
"sentencia_control : WHILE condicion LOOP cuerpo_while_bien_definido",
"sentencia_control : WHILE '(' condicion ')' LOOP cuerpo_while_mal_definido",
"cuerpo_while_bien_definido : '{' sentencias_ejecutables '}'",
"cuerpo_while_mal_definido : sentencia_ejecutable",
"operador : '<'",
"operador : '>'",
"operador : MAYORIGUAL",
"operador : MENORIGUAL",
"operador : DISTINTO",
"operador : COMPARACION",
};

//#line 315 "parser.y"

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


//#line 435 "Parser.java"
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
//#line 36 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.principalStruct );}
break;
case 2:
//#line 37 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorPrincipal, this.l, this.l.getLine()));}
break;
case 9:
//#line 50 "parser.y"
{this.s.addSyntaxError(new Error(AnalizadorSintactico.errorSentenciaEjecutable, this.l, this.l.getLine()));}
break;
case 10:
//#line 53 "parser.y"
{ }
break;
case 12:
//#line 58 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
break;
case 14:
//#line 63 "parser.y"
{ String lexema = val_peek(0).sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("NOMBRE_ANT", lexema);
									  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.VARIABLE);
									  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
									  this.ts.removeToken(lexema);
									  this.ts.addToken((String) t.getAttr("NOMBRE"), t);
									 }
break;
case 15:
//#line 75 "parser.y"
{ yyval = new ParserVal(AnalizadorLexico.TYPE_UINT);}
break;
case 16:
//#line 76 "parser.y"
{ yyval = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
break;
case 17:
//#line 79 "parser.y"
{ String lexema = val_peek(3).sval;
																			Token t = this.ts.getToken(lexema);
																			t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																			this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																			this.s.removeNombreProcedimiento((String) t.getAttr("NOMBRE"));}
break;
case 18:
//#line 86 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct );
																					this.count = 0;
																					String lexema = val_peek(6).sval;
																					Token t = this.ts.getToken(lexema);
																					t.addAttr("NOMBRE_ANT", lexema);
																					t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																					t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																					this.s.setNombreProcedimiento(lexema);
																					this.ts.removeToken(lexema);
																					this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																					yyval.sval = (String) t.getAttr("NOMBRE");
																					}
break;
case 19:
//#line 98 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); 
																		  yyval.sval = val_peek(5).sval;
																		  String lexema = val_peek(5).sval;
																		  Token t = this.ts.getToken(lexema);
																		  t.addAttr("NOMBRE_ANT", lexema);
																		  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																		  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																		  this.s.setNombreProcedimiento(lexema);
																		  this.ts.removeToken(lexema);
																		  this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																		  yyval.sval = (String) t.getAttr("NOMBRE");
																	    }
break;
case 20:
//#line 111 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
break;
case 21:
//#line 114 "parser.y"
{ this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							}
break;
case 22:
//#line 120 "parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											}
break;
case 23:
//#line 124 "parser.y"
{  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
									
								}
break;
case 24:
//#line 133 "parser.y"
{ String lexema = val_peek(0).sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.NOMBREPAR);
									  this.ts.addToken(lexema, t);
									 }
break;
case 25:
//#line 141 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
break;
case 28:
//#line 145 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
break;
case 29:
//#line 146 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
break;
case 30:
//#line 149 "parser.y"
{  polaca.addOperando(val_peek(2).sval);
													      polaca.addOperador("=");
													   }
break;
case 31:
//#line 152 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
break;
case 32:
//#line 155 "parser.y"
{ yyval = val_peek(0);
								String lexema = val_peek(0).sval;
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
break;
case 34:
//#line 173 "parser.y"
{  polaca.addOperador("+"); }
break;
case 35:
//#line 174 "parser.y"
{  polaca.addOperador("-"); }
break;
case 36:
//#line 177 "parser.y"
{  /* termino : factor */
					yyval = val_peek(0); }
break;
case 38:
//#line 180 "parser.y"
{ polaca.addOperador("*");}
break;
case 39:
//#line 181 "parser.y"
{ polaca.addOperador("/");}
break;
case 40:
//#line 184 "parser.y"
{ String valor = yylval.sval;
	if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.TYPE_UINT) {
		this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
		Token t = new Token(AnalizadorLexico.CONSTANTE, 0, AnalizadorLexico.TYPE_UINT);
		this.ts.addToken(valor, t);
		yyval = new ParserVal(valor);
	} else 
		if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.TYPE_DOUBLE) {
			Double number = MyDouble.check(this.l);
			if (MyDouble.truncate) { 
				if (!this.l.warningExist(this.l.getBuffer()))
					this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_DOUBLE, this.l, this.l.getLine()));
			}
				
			Token t = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.TYPE_DOUBLE);
			this.ts.addToken(valor, t);
			yyval = new ParserVal(valor);
	} 
	   polaca.addOperando(yyval.sval);
	 }
break;
case 41:
//#line 205 "parser.y"
{ /* factor : IDENTIFICADOR*/
						 yyval = val_peek(0);
						 polaca.addOperando(yyval.sval);
						}
break;
case 42:
//#line 209 "parser.y"
{ 	/* factor : CONSTANTE */
						yyval = val_peek(0);
						polaca.addOperando(yyval.sval);
					}
break;
case 43:
//#line 214 "parser.y"
{ /* factor : cadena*/
					yyval = val_peek(0); 
					polaca.addOperando(yyval.sval); }
break;
case 44:
//#line 219 "parser.y"
{ 
															  this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
															  /* Desapila dirección incompleta */
															  Integer pasoIncompleto = polaca.getTop(); 	
															  /* Completo el destino de BI*/
															  polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);
															 }
break;
case 45:
//#line 226 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 46:
//#line 227 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
break;
case 47:
//#line 228 "parser.y"
{ 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			/* Desapila dirección incompleta */
																		    /*Integer pasoIncompleto = polaca.getTop(); 	*/
																		    /* Completo el destino de BI*/
																		    /*polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);*/
																			}
break;
case 48:
//#line 236 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 49:
//#line 237 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 50:
//#line 238 "parser.y"
{ this.s.addSyntaxError( new Error( AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 51:
//#line 239 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
break;
case 52:
//#line 240 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 53:
//#line 241 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 54:
//#line 242 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 55:
//#line 246 "parser.y"
{ /* Desapila dirección incompleta */
											  Integer pasoIncompleto = polaca.getTop();
											  /* Completa el destino de la BF*/
											  polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber+2);
											  /* Apilo paso incompleto*/
											  polaca.stackUp(CodigoIntermedio.polacaNumber);
											  /* Crea paso incompleto*/
											  polaca.addOperador("");
											  /* Agrego etiqueta BI*/
											  polaca.addOperador("BI");
											  
											  }
break;
case 57:
//#line 262 "parser.y"
{	this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
													/* Desapila dirección incompleta */
													Integer pasoIncompleto = polaca.getTop(); 	
													/* Completo el destino de BI*/
													polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber); }
break;
case 59:
//#line 273 "parser.y"
{ polaca.addOperador(val_peek(1).sval);
																/* Apilo paso incompleto*/
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
																 polaca.addOperando("");
																 /* Creo el paso BF*/
																polaca.addOperando("BF");
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
break;
case 65:
//#line 294 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; }
break;
case 66:
//#line 295 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 67:
//#line 296 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 68:
//#line 297 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 71:
//#line 306 "parser.y"
{ yyval.sval = "<"; }
break;
case 72:
//#line 307 "parser.y"
{ yyval.sval = ">"; }
break;
case 73:
//#line 308 "parser.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 309 "parser.y"
{ yyval.sval = val_peek(0).sval;}
break;
case 75:
//#line 310 "parser.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 311 "parser.y"
{ yyval.sval = val_peek(0).sval;}
break;
//#line 938 "Parser.java"
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

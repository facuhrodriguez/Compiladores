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






//#line 30 ".\parser.y"
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
   11,   11,   11,    4,    4,    4,    4,    4,   12,   12,
   17,   18,   18,   18,   19,   19,   19,   19,   20,   20,
   20,   20,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   22,   24,   23,   25,   21,   21,   15,   16,   16,
   27,   14,   14,   14,   28,   29,   26,   26,   26,   26,
   26,   26,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    2,    2,    1,
    1,    2,    3,    2,    1,    1,    4,    8,    7,    2,
    1,    3,    2,    2,    1,    1,    2,    2,    3,    3,
    1,    1,    3,    3,    1,    4,    3,    3,    2,    1,
    1,    1,    6,    5,    8,    4,    6,    5,    8,    8,
    8,    3,    1,    3,    1,    3,    3,    4,    4,    3,
    3,    6,    4,    6,    3,    1,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   15,   16,    0,    0,    0,    0,
    3,    5,    6,    0,   11,    0,    0,    0,    0,   25,
   26,    0,    0,    0,    2,    0,   31,   41,   42,    0,
    0,   40,    0,    0,   35,    0,    0,    0,    0,    0,
   20,    4,    0,   12,   14,    0,   24,   27,   28,    0,
    0,    0,   60,    0,    0,    0,    0,   39,   72,   70,
   71,   69,    0,    0,   67,   68,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   13,    0,    0,
    0,    0,   59,    0,    0,    0,    0,    0,    0,   37,
   38,    0,    7,    0,    0,   46,    0,   58,    0,    0,
   63,    0,    0,   21,    0,   17,   61,   36,    0,    0,
    0,   44,    9,   52,    8,   48,    0,    0,   23,    0,
    0,    0,    0,   43,    0,   47,   66,   62,   64,   65,
    0,   22,    0,    0,    0,    0,    0,    0,    0,   19,
    0,    0,   45,   49,   51,   50,   18,   54,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   93,  135,   14,   15,   16,   17,   18,
  105,   19,   20,   21,   22,   23,   24,   33,   34,   35,
   36,   72,  136,  111,  137,   73,   54,  101,  129,
};
final static short yysindex[] = {                      -149,
  -22,  -15,  -34,   13,    0,    0,    5,  -27,    0, -133,
    0,    0,    0,    4,    0,   18, -196,  -60,   22,    0,
    0,   26,   40,  -21,    0,  -25,    0,    0,    0,  -37,
 -193,    0,   14,   33,    0,   -2, -190,  -37,   24,   74,
    0,    0, -172,    0,    0, -133,    0,    0,    0,   38,
   38,   70,    0,   89,   99, -124,    6,    0,    0,    0,
    0,    0,   38,   38,    0,    0,   38,   48,   48, -147,
   25, -112,   38,  112,   10,   31,  -11,    0, -108,  -10,
  -10, -102,    0,   48,  -91, -106,   33,   33,  -10,    0,
    0, -119,    0,  -83, -105,    0,  -10,    0, -107, -147,
    0, -172, -111,    0,    8,    0,    0,    0, -119, -158,
 -130,    0,    0,    0,    0,    0,  -77,  -69,    0,  106,
 -172, -103,  -85,    0,  -85,    0,    0,    0,    0,    0,
  -90,    0,  115, -147, -119,  -81,  -78,  -74,  -73,    0,
  -65,  -58,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -17,    0,    0,    0,    0,    0,    0,    0,  194,
    0,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  137,
  138,    0,    0,    0,    0,    0,  -36,  -31,  -26,    0,
    0,    0,    0,    0,    0,    0,    2,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -120,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  152,   12,    0,  298,    3,    0,    0,  -23,    0,    0,
    0,    0,    0,    0,    0,    0,  307,   39,   83,   32,
   64,  -30,   78,    0,   79,  172,    0,   90,    0,
};
final static int YYTABLESIZE=440;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         32,
   10,   32,   56,   32,   33,   30,   33,   31,   33,   34,
   31,   34,   41,   34,   56,   53,  106,   32,   32,   78,
   32,   42,   33,   33,   26,   33,   86,   34,   34,  103,
   34,   70,   63,   56,   64,   56,   25,  134,   71,   51,
   95,  114,   57,   31,   38,  100,   85,   43,  122,   31,
   99,  121,   37,  104,  110,  130,   63,   65,   64,   66,
   45,   57,   46,   57,   58,   65,  148,   66,   74,   65,
   39,   66,   94,   65,   68,   66,   44,   56,  119,   69,
   47,   32,   31,   65,   48,   66,   33,  109,   80,   81,
   42,   34,   31,   57,    5,    6,   56,  132,   49,   90,
   91,   75,  118,  123,  124,   89,    1,    2,   92,    2,
    3,   97,    3,   77,    4,  108,    4,    5,    6,    7,
   70,    7,    8,    2,   57,   10,    3,   82,   70,   83,
    4,  125,  126,    5,    6,    7,  142,    2,    8,   84,
    3,   53,   53,   55,    4,   87,   88,   70,    2,    7,
   96,    3,   98,  100,  107,    4,  112,  116,    5,    6,
    7,  120,  117,    8,   92,    2,  131,  140,    3,  133,
   92,    2,    4,    2,    3,  141,    3,    7,    4,    2,
    4,  143,    3,    7,  144,    7,    4,    2,  145,  146,
    3,    7,  147,    1,    4,   30,   29,   79,    2,    7,
   55,    3,  138,  139,   67,    4,  128,    0,    0,    0,
    7,    0,    0,    0,    0,    0,    0,    0,    0,   27,
   28,   29,   27,   28,   29,    0,    0,    0,   32,   40,
   55,   52,    0,   33,   32,   32,   32,   32,   34,   33,
   33,   33,   33,   56,   34,   34,   34,   34,    0,   56,
   56,   56,   56,    0,   50,    5,    6,   10,   31,  102,
   10,   27,   28,   29,   10,    0,    0,   10,   10,   10,
    0,   57,   10,   59,   60,   61,   62,   57,   57,   57,
   57,   59,   60,   61,   62,   59,   60,   61,   62,   59,
   60,   61,   62,   76,   27,   28,   29,   13,    0,   59,
   60,   61,   62,    0,   27,   28,   29,   13,    0,   32,
    0,    0,    0,   32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,    0,
    0,    0,    0,   13,   32,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,   32,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   32,
   32,    0,    0,   32,   32,   32,   13,    0,    0,   32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  113,
   32,  115,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  115,    0,    0,    0,
    0,    0,    0,    0,  127,  115,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  115,    0,    0,    0,    0,    0,    0,  115,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   40,   45,   41,   40,   43,   45,   45,   41,
   45,   43,   40,   45,   41,   41,  125,   59,   60,   43,
   62,   10,   59,   60,   40,   62,   57,   59,   60,   41,
   62,  123,   43,   60,   45,   62,   59,  123,   41,   61,
   71,  125,   41,   61,   40,  123,   41,   44,   41,   45,
   41,   44,   40,   77,   85,  125,   43,   60,   45,   62,
  257,   60,  123,   62,  258,   60,  125,   62,  259,   60,
    7,   62,   70,   60,   42,   62,   59,   40,  102,   47,
   59,  123,   45,   60,   59,   62,  123,   85,   50,   51,
   79,  123,   45,   30,  267,  268,  123,  121,   59,   68,
   69,   38,  100,  262,  263,   67,  256,  257,  256,  257,
  260,   73,  260,   40,  264,   84,  264,  267,  268,  269,
  123,  269,  272,  257,  123,  125,  260,   58,  123,   41,
  264,  262,  263,  267,  268,  269,  134,  257,  272,   41,
  260,  262,  263,  268,  264,   63,   64,  123,  257,  269,
  263,  260,   41,  123,  257,  264,  263,  263,  267,  268,
  269,  273,  270,  272,  256,  257,   61,  258,  260,  273,
  256,  257,  264,  257,  260,   61,  260,  269,  264,  257,
  264,  263,  260,  269,  263,  269,  264,  257,  263,  263,
  260,  269,  258,    0,  264,   59,   59,   46,  257,  269,
  263,  260,  125,  125,   33,  264,  117,   -1,   -1,   -1,
  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  257,  258,  259,   -1,   -1,   -1,  270,  257,
  268,  257,   -1,  270,  276,  277,  278,  279,  270,  276,
  277,  278,  279,  270,  276,  277,  278,  279,   -1,  276,
  277,  278,  279,   -1,  276,  267,  268,  257,  276,  271,
  260,  257,  258,  259,  264,   -1,   -1,  267,  268,  269,
   -1,  270,  272,  276,  277,  278,  279,  276,  277,  278,
  279,  276,  277,  278,  279,  276,  277,  278,  279,  276,
  277,  278,  279,  270,  257,  258,  259,    0,   -1,  276,
  277,  278,  279,   -1,  257,  258,  259,   10,   -1,    3,
   -1,   -1,   -1,    7,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   30,   -1,   -1,   -1,
   -1,   -1,   -1,   46,   38,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   50,   51,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   63,
   64,   -1,   -1,   67,   68,   69,   79,   -1,   -1,   73,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   92,
   84,   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  109,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  117,  118,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  135,   -1,   -1,   -1,   -1,   -1,   -1,  142,
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
"factor : '-' CONSTANTE",
"factor : lado_izquierdo",
"factor : CONSTANTE",
"factor : CADENA",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_bien_definido END_IF",
"sentencia_seleccion : IF condicion cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_mal_definido END_IF",
"sentencia_seleccion : IF condicion ')' cuerpo_if_bien_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_bien_definido ELSE cuerpo_else_mal_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_mal_definido END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if_mal_definido ELSE cuerpo_else_bien_definido END_IF",
"cuerpo_if_bien_definido : '{' sentencias_ejecutables '}'",
"cuerpo_if_mal_definido : sentencias_ejecutables",
"cuerpo_else_bien_definido : '{' sentencias_ejecutables '}'",
"cuerpo_else_mal_definido : sentencias_ejecutables",
"condicion : expresion_aritmetica operador expresion_aritmetica",
"condicion : condicion operador expresion_aritmetica",
"sentencia_salida : OUT '(' CADENA ')'",
"invocaciones_procedimiento : IDENTIFICADOR '(' parametros_invocacion ')'",
"invocaciones_procedimiento : IDENTIFICADOR '(' ')'",
"parametros_invocacion : IDENTIFICADOR ':' IDENTIFICADOR",
"sentencia_control : WHILE '(' condicion ')' LOOP cuerpo_while_bien_definido",
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

//#line 254 ".\parser.y"

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


//#line 434 "Parser.java"
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
//#line 36 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.principalStruct );}
break;
case 2:
//#line 37 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorPrincipal, this.l, this.l.getLine()));}
break;
case 9:
//#line 50 ".\parser.y"
{this.s.addSyntaxError(new Error(AnalizadorSintactico.errorSentenciaEjecutable, this.l, this.l.getLine()));}
break;
case 12:
//#line 58 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
break;
case 14:
//#line 63 ".\parser.y"
{ String lexema = val_peek(0).sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.VARIABLE);
									  this.ts.addToken(lexema, t);
									 }
break;
case 15:
//#line 71 ".\parser.y"
{ yyval = new ParserVal(AnalizadorLexico.TYPE_UINT);}
break;
case 16:
//#line 72 ".\parser.y"
{ yyval = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
break;
case 17:
//#line 75 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); }
break;
case 18:
//#line 78 ".\parser.y"
{ String lexema = val_peek(6).sval; }
break;
case 19:
//#line 79 ".\parser.y"
{ String lexema = val_peek(5).sval; 
																		  Token t = this.ts.getToken(lexema);
																		  t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																		  this.ts.addToken(lexema, t);}
break;
case 20:
//#line 84 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
break;
case 21:
//#line 87 ".\parser.y"
{ this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							  String lexema = val_peek(0).sval;
							}
break;
case 22:
//#line 94 ".\parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											}
break;
case 23:
//#line 98 ".\parser.y"
{  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
								}
break;
case 24:
//#line 106 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
break;
case 27:
//#line 110 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
break;
case 28:
//#line 111 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
break;
case 29:
//#line 114 ".\parser.y"
{  polaca.addOperando(val_peek(2).sval);
													      polaca.addOperador(val_peek(1).sval);
													   }
break;
case 30:
//#line 117 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
break;
case 31:
//#line 120 ".\parser.y"
{ yyval = val_peek(0);
								String lexema = val_peek(0).sval;
								Token t = this.ts.getToken(lexema);
							    t.addAttr("USO", AnalizadorSintactico.VARIABLE);
							    this.ts.addToken(lexema, t);
								 }
break;
case 33:
//#line 129 ".\parser.y"
{  polaca.addOperador(val_peek(1).sval); }
break;
case 34:
//#line 130 ".\parser.y"
{  polaca.addOperador(val_peek(1).sval); }
break;
case 35:
//#line 133 ".\parser.y"
{  /* termino : factor */
					yyval = val_peek(0); }
break;
case 39:
//#line 140 ".\parser.y"
{ String valor = yylval.sval;
	if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO) {
		this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_UI, this.l, this.l.getLine()));
		Token t = new Token(AnalizadorLexico.CONSTANTE, 0, AnalizadorLexico.CONSTANTE_ENTERA_SIN_SIGNO);
		this.ts.addToken(valor, t);
		yyval = new ParserVal(valor);
	} else 
		if (this.ts.getToken(valor).getAttr("TIPO") == AnalizadorLexico.CONSTANTE_DOUBLE) {
			Double number = MyDouble.check(this.l);
			if (MyDouble.truncate) { 
				if (!this.l.warningExist(this.l.getBuffer()))
					this.l.addWarning(new Error(AnalizadorLexico.WARNING_CONSTANT_DOUBLE, this.l, this.l.getLine()));
			}
				
			Token t = new Token(AnalizadorLexico.CONSTANTE, number, AnalizadorLexico.CONSTANTE_DOUBLE);
			this.ts.addToken(valor, t);
			yyval = new ParserVal(valor);
	} 
	   polaca.addOperando(yyval.sval);
	 }
break;
case 40:
//#line 161 ".\parser.y"
{ /* factor : IDENTIFICADOR*/
						 yyval = val_peek(0);
						 polaca.addOperando(yyval.sval);
						}
break;
case 41:
//#line 165 ".\parser.y"
{ 	/* factor : CONSTANTE */
						yyval = val_peek(0);
						polaca.addOperando(yyval.sval);
					}
break;
case 42:
//#line 170 ".\parser.y"
{ /* factor : cadena*/
					yyval = val_peek(0); 
					polaca.addOperando(yyval.sval); }
break;
case 43:
//#line 175 ".\parser.y"
{ 
															  this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
															  /*Desapila dirección incompleta*/
															  /*Integer pasoIncompleto = // polaca.getTop(); 	*/
															  /* Completa el destino de la BF*/
															  /* polaca.addDirection(pasoIncompleto, CodigoIntermedio.// polacaNumber+2);*/
															  /* Crea paso incompleto*/
															  /* polaca.addOperador('');*/
															  /* Apila el número del paso incompleto*/
															  /* polaca.stackUp(CodigoIntermedio.// polacaNumber);*/
															  /* Se crea el paso BI*/
															  /* polaca.addOperador('BI');*/
															 
															 }
break;
case 44:
//#line 189 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
break;
case 45:
//#line 190 ".\parser.y"
{ 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			
																		}
break;
case 46:
//#line 194 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 47:
//#line 195 ".\parser.y"
{ this.s.addSyntaxError( new Error( AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 48:
//#line 196 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
break;
case 49:
//#line 197 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 50:
//#line 198 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 51:
//#line 199 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 56:
//#line 215 ".\parser.y"
{ polaca.addOperando("");
																 /* Apilo paso incompleto*/
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
																 /* Creo el paso BF*/
																 polaca.addOperando("BF");
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
break;
case 62:
//#line 235 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; }
break;
case 63:
//#line 236 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 64:
//#line 237 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 67:
//#line 246 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 68:
//#line 247 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 69:
//#line 248 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 70:
//#line 249 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 71:
//#line 250 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 72:
//#line 251 ".\parser.y"
{ yyval = val_peek(0); }
break;
//#line 847 "Parser.java"
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

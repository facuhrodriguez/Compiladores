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
   27,    0,    0,    0,    2,    0,   32,   42,   43,    0,
    0,   41,    0,    0,   36,    0,    0,    0,    0,    0,
   20,    4,    0,   12,   14,    0,   25,   28,   29,    0,
    0,    0,   63,    0,    0,    0,    0,    0,   40,   76,
   74,   75,   73,    0,    0,   71,   72,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   13,
    0,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,   38,   39,    0,    0,   49,    0,   61,    0,
    0,    0,   67,    0,    0,    0,    0,   21,   17,   64,
   37,    0,   45,    0,    0,    0,   46,   55,   51,   66,
    0,    0,    7,    0,   23,    0,   24,    0,    0,    0,
    0,    0,   44,    0,   50,   70,   65,   68,    9,   69,
    8,    0,   22,    0,    0,   48,    0,    0,    0,    0,
    0,   19,    0,   57,   47,   52,   54,   53,   18,
};
final static short yydgoto[] = {                          9,
  147,   11,   12,   13,  124,   14,   15,   16,   17,   18,
  107,  108,   19,   20,   21,   22,   23,   24,   33,   34,
   35,   36,   73,  131,  116,  149,   74,   54,  103,  138,
};
final static short yysindex[] = {                      -133,
  -27,   12,   41,   23,    0,    0,   50,  -37,    0, -119,
    0,    0,    0,   21,    0,   -9, -182,  -32,   35,    0,
    0,   53,   58,  -48,    0,  -30,    0,    0,    0,   38,
 -138,    0,   14,   69,    0,   -2, -137,   44,  -54,   88,
    0,    0, -135,    0,    0, -119,    0,    0,    0,   56,
   56,   79,    0,   99,  103, -112,   37,    6,    0,    0,
    0,    0,    0,   56,   56,    0,    0,   56,   59,   59,
 -119,   37,  -98,   56,  134,  -89,   10,   71,  -11,    0,
  -84,   -3,   -3,  -70,    0,   59, -120, -106,  -68,   69,
   69,   -3,    0,    0,  -71,  -63,    0,   -3,    0,   71,
  -78, -151,    0, -135,  -69,  -50,   36,    0,    0,    0,
    0,   80,    0, -119, -116,  -94,    0,    0,    0,    0,
  -52,   64,    0, -105,    0,  152,    0, -135,  -55, -119,
  -44,  -90,    0,  -90,    0,    0,    0,    0,    0,    0,
    0,  -43,    0,  160,  -58,    0, -119,  -33,   -8,   -4,
   -1,    0,  -25,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -45,    0,    0,    0,    0,    0,    0,    0,  226,
    0,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  173,  195,    0,    0,    0,    0,    0,    0,  -36,
  -31,  -26,    0,    0,    0,    0,    0,    2,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    3,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   27,  239,    0,  -14,    0,    0,    0,  224,  -35,    0,
    0,  -79,    0,    0,    0,    0,    0,  324,  -13,  126,
   33,   15,   42,  -86,    0,  137,  261,    0,  -65,    0,
};
final static int YYTABLESIZE=410;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   10,   33,   41,   33,   34,   66,   34,   67,   34,   35,
   53,   35,   51,   35,   59,   32,   71,   33,   33,  140,
   33,   39,   34,   34,  125,   34,   10,   35,   35,  105,
   35,   25,  130,   59,  120,   59,   82,   83,   72,   64,
  109,   65,   60,  106,   58,  148,   88,  150,  143,   44,
  101,   26,   77,  118,   92,  137,   64,   66,   65,   67,
   98,   60,   37,   60,   43,   66,  154,   67,  106,   66,
  102,   67,   81,   66,   45,   67,  129,   56,   57,  128,
   30,   33,   31,   56,   76,   31,   34,  123,   31,   38,
   46,   35,  106,   47,   31,   56,   59,   95,   87,   89,
   31,   93,   94,   31,  122,    2,  136,  139,    3,  141,
   69,   48,    4,   96,  114,   70,   49,    7,  111,   59,
   71,   75,    1,    2,   60,   10,    3,   79,   71,  115,
    4,    5,    6,    5,    6,    7,   84,    2,    8,   85,
    3,  112,  113,   86,    4,  132,  133,    5,    6,    7,
    2,    2,    8,    3,    3,   55,  145,    4,    4,   71,
    5,    6,    7,    7,   97,    8,    2,  134,  135,    3,
   56,   56,    2,    4,   99,    3,    5,    6,    7,    4,
  100,    8,    5,    6,    7,    2,  110,    8,    3,   90,
   91,  121,    4,  102,  117,    5,    6,    7,    2,  119,
    8,    3,  130,  126,    2,    4,  127,    3,    5,    6,
    7,    4,  142,    8,  152,   78,    7,  144,  146,   40,
  153,   60,   61,   62,   63,    1,   52,   50,   33,  155,
   32,   31,  159,   34,   33,   33,   33,   33,   35,   34,
   34,   34,   34,   59,   35,   35,   35,   35,   42,   59,
   59,   59,   59,   30,  156,    5,    6,   10,  157,  104,
   10,  158,   10,   10,   10,   58,   80,   10,   10,   10,
  151,   60,   10,   60,   61,   62,   63,   60,   60,   60,
   60,   60,   61,   62,   63,   60,   61,   62,   63,   60,
   61,   62,   63,   68,   27,   28,   29,   27,   28,   29,
   27,   28,   29,    0,    0,   55,   27,   28,   29,    0,
    0,   55,   27,   28,   29,   27,   28,   29,    0,   42,
    2,    0,    0,    3,    0,    0,   32,    4,    0,    0,
   32,    0,    7,   42,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   42,   32,    0,    0,    0,    0,    0,    0,
    0,   32,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   32,   32,    0,    0,    0,    0,    0,
    0,    0,    0,   42,    0,   42,    0,   32,   32,    0,
    0,   32,   32,   32,    0,    0,    0,   32,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   32,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   40,   45,   41,   60,   43,   62,   45,   41,
   41,   43,   61,   45,   41,   61,  123,   59,   60,  125,
   62,    7,   59,   60,  104,   62,    0,   59,   60,   41,
   62,   59,  123,   60,  100,   62,   50,   51,   41,   43,
  125,   45,   41,   79,   30,  132,   41,  134,  128,   59,
   41,   40,   38,  125,   68,  121,   43,   60,   45,   62,
   74,   60,   40,   62,   44,   60,  125,   62,  104,   60,
  123,   62,   46,   60,  257,   62,   41,   40,   41,   44,
   40,  123,   45,   40,   41,   45,  123,  102,   45,   40,
  123,  123,  128,   59,   45,   40,  123,   71,   57,   58,
   45,   69,   70,   45,  256,  257,  121,  122,  260,  124,
   42,   59,  264,   72,   88,   47,   59,  269,   86,  258,
  123,  259,  256,  257,  123,  125,  260,   40,  123,   88,
  264,  267,  268,  267,  268,  269,   58,  257,  272,   41,
  260,  262,  263,   41,  264,  262,  263,  267,  268,  269,
  257,  257,  272,  260,  260,  268,  130,  264,  264,  123,
  267,  268,  269,  269,  263,  272,  257,  262,  263,  260,
  262,  263,  257,  264,   41,  260,  267,  268,  269,  264,
  270,  272,  267,  268,  269,  257,  257,  272,  260,   64,
   65,  270,  264,  123,  263,  267,  268,  269,  257,  263,
  272,  260,  123,  273,  257,  264,  257,  260,  267,  268,
  269,  264,   61,  272,  258,  270,  269,  273,  263,  257,
   61,  276,  277,  278,  279,    0,  257,  276,  270,  263,
  276,   59,  258,  270,  276,  277,  278,  279,  270,  276,
  277,  278,  279,  270,  276,  277,  278,  279,   10,  276,
  277,  278,  279,   59,  263,  267,  268,  257,  263,  271,
  260,  263,  262,  263,  264,  263,   43,  267,  268,  269,
  134,  270,  272,  276,  277,  278,  279,  276,  277,  278,
  279,  276,  277,  278,  279,  276,  277,  278,  279,  276,
  277,  278,  279,   33,  257,  258,  259,  257,  258,  259,
  257,  258,  259,   -1,   -1,  268,  257,  258,  259,   -1,
   -1,  268,  257,  258,  259,  257,  258,  259,   -1,   81,
  257,   -1,   -1,  260,   -1,   -1,    3,  264,   -1,   -1,
    7,   -1,  269,   95,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  114,   30,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   38,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   50,   51,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  145,   -1,  147,   -1,   64,   65,   -1,
   -1,   68,   69,   70,   -1,   -1,   -1,   74,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   86,
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
"factor : lado_izquierdo",
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

//#line 286 "parser.y"

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
	String ambitoVar = var.getAttr("AMBITO");
	String nombreVar = var.getAttr("NOMBRE");
	for (Token t : this.ts.getTokens()){
		if ( ((String) t.getAttr("AMBITO")).contains(ambitoVar) && 
			((String) t.getAttr("NOMBRE")).equals(nombreVar) )
			return true;
	return false;
}
	
}


//#line 447 "Parser.java"
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
									  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.VARIABLE);
									  t.addAttr("AMBITO", this.s.getNombreProcedimiento());
									  this.ts.addToken(lexema, t);
									 }
break;
case 15:
//#line 73 "parser.y"
{ yyval = new ParserVal(AnalizadorLexico.TYPE_UINT);}
break;
case 16:
//#line 74 "parser.y"
{ yyval = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
break;
case 17:
//#line 77 "parser.y"
{ String lexema = val_peek(3).sval;
																			Token t = this.ts.getToken(lexema);
																			t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																			this.ts.addToken(lexema, t);
																			this.s.removeNombreProcedimiento(lexema);}
break;
case 18:
//#line 84 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct );
																					this.count = 0;
																					String lexema = val_peek(6).sval;
																					Token t = this.ts.getToken(lexema);
																					t.addAttr("AMBITO", this.s.getNombreProcedimiento());
																					t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																					this.s.setNombreProcedimiento(lexema);
																					yyval.sval = val_peek(6).sval;
																					}
break;
case 19:
//#line 93 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); 
																		  yyval.sval = val_peek(5).sval;
																		  String lexema = val_peek(5).sval;
																		  Token t = this.ts.getToken(lexema);
																		  t.addAttr("NOMBRE", lexema.concat("@").concat(this.s.getNombreProcedimiento()));
																		  this.s.setNombreProcedimiento(val_peek(5).sval);
																	     }
break;
case 20:
//#line 101 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
break;
case 21:
//#line 104 "parser.y"
{ this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							}
break;
case 22:
//#line 110 "parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											}
break;
case 23:
//#line 114 "parser.y"
{  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
									
								}
break;
case 24:
//#line 123 "parser.y"
{ String lexema = val_peek(0).sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.NOMBREPAR);
									  this.ts.addToken(lexema, t);
									 }
break;
case 25:
//#line 131 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
break;
case 28:
//#line 135 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
break;
case 29:
//#line 136 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
break;
case 30:
//#line 139 "parser.y"
{  polaca.addOperando(val_peek(2).sval);
													      polaca.addOperador(val_peek(1).sval);
													   }
break;
case 31:
//#line 142 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
break;
case 32:
//#line 145 "parser.y"
{ yyval = val_peek(0);
								String lexema = val_peek(0).sval;
								Token t = this.ts.getToken(lexema);
							    t.addAttr("USO", AnalizadorSintactico.VARIABLE);
								
								if (!this.existe_en_ambito(t))
									this.polaca.addError(new Error(CodigoIntermedio.VAR_NO_DECLARADA));
								else 
									this.ts.addToken(lexema, t);
								}
break;
case 34:
//#line 158 "parser.y"
{  polaca.addOperador(val_peek(1).sval); }
break;
case 35:
//#line 159 "parser.y"
{  polaca.addOperador(val_peek(1).sval); }
break;
case 36:
//#line 162 "parser.y"
{  /* termino : factor */
					yyval = val_peek(0); }
break;
case 40:
//#line 169 "parser.y"
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
//#line 190 "parser.y"
{ /* factor : IDENTIFICADOR*/
						 yyval = val_peek(0);
						 polaca.addOperando(yyval.sval);
						}
break;
case 42:
//#line 194 "parser.y"
{ 	/* factor : CONSTANTE */
						yyval = val_peek(0);
						polaca.addOperando(yyval.sval);
					}
break;
case 43:
//#line 199 "parser.y"
{ /* factor : cadena*/
					yyval = val_peek(0); 
					polaca.addOperando(yyval.sval); }
break;
case 44:
//#line 204 "parser.y"
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
case 45:
//#line 218 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 46:
//#line 219 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
break;
case 47:
//#line 220 "parser.y"
{ 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			}
break;
case 48:
//#line 223 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 49:
//#line 224 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 50:
//#line 225 "parser.y"
{ this.s.addSyntaxError( new Error( AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 51:
//#line 226 "parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
break;
case 52:
//#line 227 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 53:
//#line 228 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 54:
//#line 229 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 59:
//#line 245 "parser.y"
{ polaca.addOperando("");
																 /* Apilo paso incompleto*/
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
																 /* Creo el paso BF*/
																 polaca.addOperando("BF");
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
break;
case 65:
//#line 265 "parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; }
break;
case 66:
//#line 266 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 67:
//#line 267 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 68:
//#line 268 "parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 71:
//#line 277 "parser.y"
{ yyval = val_peek(0); }
break;
case 72:
//#line 278 "parser.y"
{ yyval = val_peek(0); }
break;
case 73:
//#line 279 "parser.y"
{ yyval = val_peek(0); }
break;
case 74:
//#line 280 "parser.y"
{ yyval = val_peek(0); }
break;
case 75:
//#line 281 "parser.y"
{ yyval = val_peek(0); }
break;
case 76:
//#line 282 "parser.y"
{ yyval = val_peek(0); }
break;
//#line 905 "Parser.java"
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

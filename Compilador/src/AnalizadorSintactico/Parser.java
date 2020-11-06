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
   11,   11,   11,   11,   12,    4,    4,    4,    4,    4,
   13,   13,   18,   19,   19,   19,   20,   20,   20,   20,
   21,   21,   21,   21,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   23,   25,   24,   26,   22,
   22,   16,   17,   17,   28,   15,   15,   15,   15,   29,
   30,   31,   27,   27,   27,   27,   27,   27,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    2,    2,    1,
    1,    2,    3,    2,    1,    1,    4,    8,    7,    2,
    1,    3,    4,    2,    2,    2,    1,    1,    2,    2,
    3,    3,    1,    1,    3,    3,    1,    4,    3,    3,
    2,    1,    1,    1,    6,    5,    5,    8,    7,    4,
    6,    5,    8,    8,    8,    3,    1,    3,    1,    3,
    3,    4,    4,    3,    3,    6,    5,    4,    6,    1,
    3,    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   15,   16,   70,    0,    0,    0,
    3,    5,    6,    0,   11,    0,    0,    0,    0,   27,
   28,    0,    0,    0,    0,    2,    0,   42,   43,   44,
    0,    0,    0,    0,   37,    0,    0,    0,   20,    4,
    0,   12,   14,    0,   26,   29,   30,    0,    0,    0,
    0,    0,   64,    0,    0,    0,    0,    0,   41,   78,
   76,   77,   75,    0,    0,   73,   74,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   13,    0,    0,    0,
    0,    0,    0,    0,   63,    0,    0,    0,    0,    0,
    0,    0,   39,   40,    0,    0,   50,    0,   62,    0,
    0,    0,    0,   21,   17,    0,    0,    0,   68,   65,
   38,    0,   46,    0,    0,    0,   47,   56,   52,   24,
    0,   25,    0,    0,   67,    0,    0,    7,    0,    0,
    0,    0,   45,    0,   51,    0,    0,   22,    0,   72,
   66,   69,    9,   71,    8,    0,   49,    0,    0,    0,
    0,    0,   19,   23,    0,   58,   48,   53,   55,   54,
   18,
};
final static short yydgoto[] = {                          9,
  148,   11,   12,   13,  129,   14,   15,   16,   17,   18,
  103,  104,   19,   20,   21,   22,   23,   24,   33,   34,
   35,   36,   73,  131,  116,  150,   74,   54,   25,  109,
  142,
};
final static short yysindex[] = {                        63,
  -22,    9,   41,   25,    0,    0,    0,  -37,    0, -122,
    0,    0,    0,   31,    0,  -17, -177,  -35,   32,    0,
    0,   35,   53,  -48,   50,    0,  -30,    0,    0,    0,
   38, -142,   14,   -7,    0,   -2, -139,   87,    0,    0,
 -149,    0,    0, -122,    0,    0,    0,   56,   56,   44,
  -54,   79,    0,   99,  103, -115,   42,    6,    0,    0,
    0,    0,    0,   56,   56,    0,    0,   56,   59,   59,
 -122,   42,  -88,   56,  140,  -11,    0,  -84,    7,    7,
  -83,   10,   67,  -62,    0,   59, -140, -106,  -63,   -7,
   -7,    7,    0,    0,  -71,  -60,    0,    7,    0, -149,
  -69,  -50,  -19,    0,    0,   67,  -57,   65,    0,    0,
    0,   95,    0, -122, -132,  -94,    0,    0,    0,    0,
  154,    0, -161,  -47,    0,  -52, -121,    0, -105, -122,
  -44,  -90,    0,  -90,    0,  -28, -149,    0,  160,    0,
    0,    0,    0,    0,    0,  -58,    0, -122,  -14,   -9,
   -8,   -4,    0,    0,  -25,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,  -45,    0,    0,    0,    0,    0,    0,    0,  232,
    0,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  203,  207,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -36,
  -31,  -26,    0,    0,    0,    0,    0,    2,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    4,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   61,   46,    0,  -81,    0,    0,    0,  230,  -23,    0,
    0,  -68,    0,    0,    0,    0,    0,    0,   60,   92,
   29,   13,   45,  -21,    0,  170,  261,    0,    0,  -33,
    0,
};
final static int YYTABLESIZE=335;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         34,
   10,   34,   39,   34,   35,   66,   35,   67,   35,   36,
   53,   36,   49,   36,   60,   33,   71,   34,   34,  144,
   34,  124,   35,   35,  123,   35,  128,   36,   36,  101,
   36,  120,  130,   60,   69,   60,   26,   51,   72,   70,
  105,   42,   61,   58,  140,  143,   88,  145,   27,   64,
  107,   65,  102,  118,  138,   40,   64,   66,   65,   67,
   10,   61,   82,   61,   37,   66,  156,   67,  154,   66,
  108,   67,  125,   66,   41,   67,  102,   56,   57,   43,
   31,   34,   32,   56,   81,   32,   35,   44,   32,   50,
   45,   36,  141,   46,   32,   56,   60,   93,   94,  102,
   32,   87,   89,   32,   78,    5,    6,   79,   80,  137,
  149,   47,  151,  102,  111,   59,   96,    5,    6,   75,
   71,  112,  113,   40,   61,   10,   76,   92,   71,  132,
  133,   95,  115,   98,    2,    2,   84,    3,    3,   85,
   40,    4,    4,   86,    5,    6,    7,    7,  114,    8,
    2,    2,   55,    3,    3,   90,   91,    4,    4,   40,
    5,    6,    7,    7,   71,    8,    2,  134,  135,    3,
   57,   57,    2,    4,   97,    3,    5,    6,    7,    4,
   99,    8,    5,    6,    7,    2,  106,    8,    3,  108,
  146,   40,    4,   40,  110,    5,    6,    7,    2,  117,
    8,    3,  119,  121,    2,    4,  122,    3,    5,    6,
    7,    4,  126,    8,  136,   83,    7,  130,  147,   38,
  155,   60,   61,   62,   63,  139,   52,   48,   34,  153,
   33,    1,  161,   35,   34,   34,   34,   34,   36,   35,
   35,   35,   35,   60,   36,   36,   36,   36,  157,   60,
   60,   60,   60,  158,  159,    5,    6,   10,  160,  100,
   10,   32,   10,   10,   10,   31,   59,   10,   10,   10,
   77,   61,   10,   60,   61,   62,   63,   61,   61,   61,
   61,   60,   61,   62,   63,   60,   61,   62,   63,   60,
   61,   62,   63,   68,   28,   29,   30,   28,   29,   30,
   28,   29,   30,  152,    0,   55,   28,   29,   30,    0,
    0,   55,   28,   29,   30,   28,   29,   30,    1,    2,
  127,    2,    3,    0,    3,    0,    4,    0,    4,    5,
    6,    7,    0,    7,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   40,   45,   41,   60,   43,   62,   45,   41,
   41,   43,   61,   45,   41,   61,  123,   59,   60,  125,
   62,   41,   59,   60,   44,   62,  108,   59,   60,   41,
   62,  100,  123,   60,   42,   62,   59,   25,   41,   47,
  125,   59,   41,   31,  126,  127,   41,  129,   40,   43,
   41,   45,   76,  125,  123,   10,   43,   60,   45,   62,
    0,   60,   50,   62,   40,   60,  125,   62,  137,   60,
  123,   62,  106,   60,   44,   62,  100,   40,   41,  257,
   40,  123,   45,   40,   41,   45,  123,  123,   45,   40,
   59,  123,  126,   59,   45,   40,  123,   69,   70,  123,
   45,   57,   58,   45,   44,  267,  268,   48,   49,  271,
  132,   59,  134,  137,   86,  258,   72,  267,  268,  259,
  123,  262,  263,   78,  123,  125,   40,   68,  123,  262,
  263,   71,   88,   74,  257,  257,   58,  260,  260,   41,
   95,  264,  264,   41,  267,  268,  269,  269,   88,  272,
  257,  257,  268,  260,  260,   64,   65,  264,  264,  114,
  267,  268,  269,  269,  123,  272,  257,  262,  263,  260,
  262,  263,  257,  264,  263,  260,  267,  268,  269,  264,
   41,  272,  267,  268,  269,  257,  270,  272,  260,  123,
  130,  146,  264,  148,  257,  267,  268,  269,  257,  263,
  272,  260,  263,  273,  257,  264,  257,  260,  267,  268,
  269,  264,  270,  272,   61,  270,  269,  123,  263,  257,
   61,  276,  277,  278,  279,  273,  257,  276,  270,  258,
  276,    0,  258,  270,  276,  277,  278,  279,  270,  276,
  277,  278,  279,  270,  276,  277,  278,  279,  263,  276,
  277,  278,  279,  263,  263,  267,  268,  257,  263,  271,
  260,   59,  262,  263,  264,   59,  263,  267,  268,  269,
   41,  270,  272,  276,  277,  278,  279,  276,  277,  278,
  279,  276,  277,  278,  279,  276,  277,  278,  279,  276,
  277,  278,  279,   33,  257,  258,  259,  257,  258,  259,
  257,  258,  259,  134,   -1,  268,  257,  258,  259,   -1,
   -1,  268,  257,  258,  259,  257,  258,  259,  256,  257,
  256,  257,  260,   -1,  260,   -1,  264,   -1,  264,  267,
  268,  269,   -1,  269,  272,
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
"parametros : parametros ',' REF declaracion_par",
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
"sentencia_control : inicio_while '(' condicion ')' LOOP cuerpo_while_bien_definido",
"sentencia_control : inicio_while '(' ')' LOOP cuerpo_while_bien_definido",
"sentencia_control : inicio_while condicion LOOP cuerpo_while_bien_definido",
"sentencia_control : inicio_while '(' condicion ')' LOOP cuerpo_while_mal_definido",
"inicio_while : WHILE",
"cuerpo_while_bien_definido : '{' sentencias_ejecutables '}'",
"cuerpo_while_mal_definido : sentencia_ejecutable",
"operador : '<'",
"operador : '>'",
"operador : MAYORIGUAL",
"operador : MENORIGUAL",
"operador : DISTINTO",
"operador : COMPARACION",
};

//#line 343 ".\parser.y"

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


//#line 442 "Parser.java"
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
case 10:
//#line 53 ".\parser.y"
{ }
break;
case 12:
//#line 58 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
break;
case 14:
//#line 63 ".\parser.y"
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
//#line 75 ".\parser.y"
{ yyval = new ParserVal(AnalizadorLexico.TYPE_UINT);}
break;
case 16:
//#line 76 ".\parser.y"
{ yyval = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
break;
case 17:
//#line 79 ".\parser.y"
{ String lexema = val_peek(3).sval;
																			Token t = this.ts.getToken(lexema);
																			t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																			this.ts.addToken((String) t.getAttr("NOMBRE"), t);
																			this.s.removeNombreProcedimiento((String) t.getAttr("NOMBRE"));}
break;
case 18:
//#line 86 ".\parser.y"
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
//#line 98 ".\parser.y"
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
//#line 111 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
break;
case 21:
//#line 114 ".\parser.y"
{ this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							  Token t = this.ts.getToken(val_peek(0).sval);
							  t.addAttr("FORMA DE PASAJE", "COPIA VALOR");
							  this.ts.addToken(val_peek(0).sval, t);
							}
break;
case 22:
//#line 123 ".\parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											  Token t = this.ts.getToken(val_peek(0).sval);
											  t.addAttr("FORMA DE PASAJE", "COPIA VALOR");
											  this.ts.addToken(val_peek(0).sval, t);
											}
break;
case 23:
//#line 130 ".\parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											  Token t = this.ts.getToken(val_peek(0).sval);
											  t.addAttr("FORMA DE PASAJE", "REFERENCIA");
											  this.ts.addToken(val_peek(0).sval, t);
											}
break;
case 24:
//#line 137 ".\parser.y"
{  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
									Token t = this.ts.getToken(val_peek(0).sval);
									t.addAttr("FORMA DE PASAJE", "REFERENCIA");
									this.ts.addToken(val_peek(0).sval, t);
									
								}
break;
case 25:
//#line 149 ".\parser.y"
{ String lexema = val_peek(0).sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.NOMBREPAR);
									  this.ts.addToken(lexema, t);
									  yyval.sval = val_peek(0).sval;
									 }
break;
case 26:
//#line 158 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
break;
case 29:
//#line 162 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
break;
case 30:
//#line 163 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
break;
case 31:
//#line 166 ".\parser.y"
{  polaca.addOperando(val_peek(2).sval);
													      polaca.addOperador("=");
													   }
break;
case 32:
//#line 169 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.errorOperatorComp, this.l, this.l.getLine()));}
break;
case 33:
//#line 172 ".\parser.y"
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
case 35:
//#line 190 ".\parser.y"
{  polaca.addOperador("+"); }
break;
case 36:
//#line 191 ".\parser.y"
{  polaca.addOperador("-"); }
break;
case 37:
//#line 194 ".\parser.y"
{  /* termino : factor */
					yyval = val_peek(0); }
break;
case 39:
//#line 197 ".\parser.y"
{ polaca.addOperador("*");}
break;
case 40:
//#line 198 ".\parser.y"
{ polaca.addOperador("/");}
break;
case 41:
//#line 201 ".\parser.y"
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
case 42:
//#line 222 ".\parser.y"
{ /* factor : IDENTIFICADOR*/
						 yyval = val_peek(0);
						 polaca.addOperando(yyval.sval);
						}
break;
case 43:
//#line 226 ".\parser.y"
{ 	/* factor : CONSTANTE */
						yyval = val_peek(0);
						polaca.addOperando(yyval.sval);
					}
break;
case 44:
//#line 231 ".\parser.y"
{ /* factor : cadena*/
					yyval = val_peek(0); 
					polaca.addOperando(yyval.sval); }
break;
case 45:
//#line 236 ".\parser.y"
{ 
															  this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
															  /* Desapila dirección incompleta */
															  Integer pasoIncompleto = polaca.getTop(); 	
															  /* Completo el destino de BI*/
															  polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);
															 }
break;
case 46:
//#line 243 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 47:
//#line 244 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parFinal, this.l, this.l.getLine()));}
break;
case 48:
//#line 245 ".\parser.y"
{ 
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
																			/* Desapila dirección incompleta */
																		    /*Integer pasoIncompleto = polaca.getTop(); 	*/
																		    /* Completo el destino de BI*/
																		    /*polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber);*/
																			}
break;
case 49:
//#line 253 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 50:
//#line 254 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 51:
//#line 255 ".\parser.y"
{ this.s.addSyntaxError( new Error( AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 52:
//#line 256 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
break;
case 53:
//#line 257 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 54:
//#line 258 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 55:
//#line 259 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 56:
//#line 263 ".\parser.y"
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
case 58:
//#line 279 ".\parser.y"
{	this.s.addSyntaxStruct( AnalizadorSintactico.ifStructure );
													/* Desapila dirección incompleta */
													Integer pasoIncompleto = polaca.getTop(); 	
													/* Completo el destino de BI*/
													polaca.addDirection(pasoIncompleto, CodigoIntermedio.polacaNumber); }
break;
case 60:
//#line 290 ".\parser.y"
{ polaca.addOperador(val_peek(1).sval);
																/* Apilo paso incompleto*/
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
																 polaca.addOperando("");
																 /* Creo el paso BF*/
																polaca.addOperando("BF");
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
break;
case 66:
//#line 311 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; 
																					  /* Desapilo el tope de la pila */
																					  Integer paso = polaca.getTop();
																					  Integer pasoInicio = polaca.getTop();
																					  polaca.addDirection(paso, CodigoIntermedio.polacaNumber + 2);
																					  polaca.addOperando("");
																					  polaca.addDirection(CodigoIntermedio.polacaNumber - 1, pasoInicio);
																					  polaca.addOperando("BI");
																					  }
break;
case 67:
//#line 320 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorCondition, this.l, this.l.getLine()));}
break;
case 68:
//#line 321 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinPar, this.l, this.l.getLine()));}
break;
case 69:
//#line 322 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.sinLlaves, this.l, this.l.getLine())); }
break;
case 70:
//#line 325 ".\parser.y"
{ /* Apilamos el número de paso donde comienza la condición*/
						polaca.stackUp(CodigoIntermedio.polacaNumber);}
break;
case 73:
//#line 334 ".\parser.y"
{ yyval.sval = "<"; }
break;
case 74:
//#line 335 ".\parser.y"
{ yyval.sval = ">"; }
break;
case 75:
//#line 336 ".\parser.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 337 ".\parser.y"
{ yyval.sval = val_peek(0).sval;}
break;
case 77:
//#line 338 ".\parser.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 78:
//#line 339 ".\parser.y"
{ yyval.sval = val_peek(0).sval;}
break;
//#line 978 "Parser.java"
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

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
    0,    0,    1,    1,    2,    2,    5,    5,    3,    3,
    6,    6,    8,    9,    9,    7,   10,   10,   10,   11,
   11,   11,    4,    4,    4,    4,    4,    4,   12,   12,
   17,   18,   18,   18,   19,   19,   19,   19,   20,   20,
   20,   20,   13,   13,   13,   13,   13,   22,   22,   23,
   23,   21,   21,   21,   15,   16,   16,   25,   14,   14,
   14,   24,   24,   24,   24,   24,   24,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    2,    1,    1,
    2,    3,    2,    1,    1,    4,    8,    7,    2,    1,
    3,    2,    2,    1,    1,    2,    2,    1,    3,    3,
    1,    1,    3,    3,    1,    4,    3,    3,    2,    1,
    1,    1,    6,    5,    8,    4,    5,    3,    1,    3,
    1,    3,    3,    3,    4,    4,    3,    3,    8,    6,
    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   14,   15,    0,    0,    0,    0,
    3,    5,    6,    0,   10,    0,    0,    0,    0,   24,
   25,    0,    0,    0,   61,    2,    0,   31,   41,   42,
    0,    0,   40,    0,    0,   35,    0,    0,    0,    0,
   19,    0,    4,    0,   11,   13,    0,   23,   26,   27,
    0,    0,    0,   57,    0,    0,    0,    0,   39,   67,
   65,   66,   64,    0,    0,   62,   63,    0,    0,    0,
    0,    0,    0,   49,    0,    0,    0,    0,    0,   12,
    0,    0,    0,    0,   56,    0,    0,    0,    0,    0,
    0,   37,   38,    0,    7,    0,    0,   46,    0,   55,
    0,    0,    0,    0,   20,    0,   16,   58,   36,    0,
   44,   48,    8,   47,    0,   22,    0,    0,    0,    0,
   43,    0,   60,    0,   21,    0,    0,   51,    0,    0,
   18,    0,    0,   45,   59,   17,   50,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   96,   14,   15,   16,   17,   18,
  106,   19,   20,   21,   22,   23,   24,   34,   35,   36,
   37,   75,  129,   76,   55,
};
final static short yysindex[] = {                       216,
  -35,    9,   79,   16,    0,    0,   20,  -40,    0,  235,
    0,    0,    0,   11,    0,    6, -189,  -53,   12,    0,
    0,   13,   14,  -54,    0,    0,  -36,    0,    0,    0,
  136, -184,    0,  105,  -15,    0,   41, -180,  109,   43,
    0, -195,    0, -234,    0,    0,  235,    0,    0,    0,
  109,  109,   26,    0,   40,   44, -181,   65,    0,    0,
    0,    0,    0,  109,  109,    0,    0,  109,  -43,  -43,
  128, -203,  -71,    0, -174,  109,   49,  -25,  121,    0,
  -85,  -34,  -34, -166,    0,  -43,  -71, -170,  -15,  -15,
  -34,    0,    0,  -34,    0,  149, -167,    0,  -34,    0,
  109, -172, -234, -173,    0,   23,    0,    0,    0, -221,
    0,    0,    0,    0,  -65,    0,   38, -234, -171,  143,
    0, -203,    0, -154,    0,   46, -203,    0, -158,  169,
    0, -150,  171,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
   21,  -48,    0,    0,    0,    0,    0,    0,    0,  110,
    0,    0,    0,   15,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -37,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -152,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   50,   54,    0,    0,    0,    0,    0,  -31,   35,
   71,    0,    0,   95,    0,    0,    0,    0,  101,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   67,    7,    0,  378,  -79,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,  393,  -32,  -14,  -23,
  -13,  -28,    0,   81,    0,
};
final static int YYTABLESIZE=511;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         41,
   28,   32,   80,   32,   54,   32,   52,   32,   64,   33,
   65,   33,   31,   33,    9,  102,   43,   58,   82,   83,
   28,   32,   32,   26,   32,   78,   69,   33,   33,   88,
   33,   70,    5,    6,   66,   91,   67,  105,   94,  107,
  120,  121,  130,   99,   97,   92,   93,  133,   27,   89,
   90,   72,   42,    2,   44,   38,    3,  122,  110,   39,
    4,  116,  109,  119,   45,    7,  118,   46,   94,   47,
   48,   49,   50,   59,   25,   34,  125,   34,   77,   34,
   85,   73,   79,   84,   86,   32,   56,   43,   98,  100,
  108,   33,  111,   34,   34,  114,   34,  115,  124,  117,
   66,  126,   67,  131,  134,   87,  132,  136,   30,    1,
   28,   52,   29,   81,   68,    0,    0,    0,   31,    0,
    0,    0,    0,   32,   66,   28,   67,    0,    0,    0,
   52,    0,   52,    0,    0,   53,    0,    0,    0,    9,
    0,   54,    0,    0,    0,    0,    0,   64,   57,   65,
    0,    0,    0,   32,   53,    0,   53,   34,    0,    0,
   54,  104,   54,   72,   66,    0,   67,   57,    0,    0,
   42,    2,   32,    0,    3,   57,    0,    0,    4,    0,
   32,    5,    6,    7,   42,    2,    8,   72,    3,    0,
   42,    2,    4,   52,    3,    0,    0,    7,    4,    0,
    0,    0,    0,    7,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   28,   29,   30,   40,   53,   32,   32,
   53,   51,   32,   54,   33,   33,   32,   31,   33,    0,
  101,   32,   33,    0,   25,    0,    0,   33,   32,   32,
   32,   32,    0,    0,   33,   33,   33,   33,    0,    0,
   60,   61,   62,   63,    0,    0,   28,   28,    0,    0,
   28,    0,   28,   28,   28,  127,    0,   28,   28,   28,
    9,    9,   28,  112,    9,    0,   28,   28,    9,    0,
   28,    9,    9,    9,   28,    0,    9,   28,   28,   28,
   34,   34,   28,  135,   34,  137,   71,    2,   34,    0,
    3,    0,    0,   34,    4,    0,    0,    0,    0,    7,
   34,   34,   34,   34,    0,    0,   60,   61,   62,   63,
   71,    2,    0,    0,    3,    0,   52,   52,    4,    0,
   52,    0,    0,    7,   52,   28,   29,   30,    0,   52,
   60,   61,   62,   63,    0,    0,   52,   52,   52,   52,
   53,   53,    0,    0,   53,    0,   54,   54,   53,    0,
   54,    0,    0,   53,   54,   28,   29,   30,    0,   54,
   53,   53,   53,   53,    0,    0,   54,   54,   54,   54,
   60,   61,   62,   63,   28,   29,   30,    5,    6,    0,
    0,  103,   28,   29,   30,   33,    0,   25,   42,    2,
    0,    0,    3,   56,   42,    2,    4,    0,    3,    0,
    0,    7,    4,    0,   74,    0,    0,    7,    0,    0,
    0,    0,    0,   33,   42,    2,   42,    2,    3,    0,
    3,   33,    4,    0,    4,   74,    0,    7,    0,    7,
    0,    0,    0,   33,   33,    0,    0,    0,    0,   95,
   74,    0,    0,    0,    0,    0,   33,   33,    0,    0,
   33,   33,   33,   33,   74,    0,    0,    0,   33,    0,
    0,    1,    2,  113,    0,    3,    0,    0,   33,    4,
    0,    0,    5,    6,    7,    0,    0,    8,    0,    0,
   42,    2,  123,   33,    3,    0,    0,  128,    4,   95,
    0,    5,    6,    7,   95,    0,    8,  113,    0,    0,
  113,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   45,   44,   41,   41,   43,   61,   45,   43,   41,
   45,   43,   61,   45,    0,   41,   10,   31,   51,   52,
    0,   59,   60,   59,   62,   39,   42,   59,   60,   58,
   62,   47,  267,  268,   60,   68,   62,   79,   71,  125,
  262,  263,  122,   76,   73,   69,   70,  127,   40,   64,
   65,  123,  256,  257,   44,   40,  260,  123,   87,   40,
  264,  103,   86,   41,   59,  269,   44,  257,  101,  123,
   59,   59,   59,  258,  270,   41,  118,   43,  259,   45,
   41,   41,   40,   58,   41,  123,  268,   81,  263,   41,
  257,  123,  263,   59,   60,  263,   62,  270,   61,  273,
   60,  273,   62,  258,  263,   41,   61,  258,   59,    0,
  263,   41,   59,   47,   34,   -1,   -1,   -1,   40,   -1,
   -1,   -1,   -1,   45,   60,  125,   62,   -1,   -1,   -1,
   60,   -1,   62,   -1,   -1,   41,   -1,   -1,   -1,  125,
   -1,   41,   -1,   -1,   -1,   -1,   -1,   43,   40,   45,
   -1,   -1,   -1,   45,   60,   -1,   62,  123,   -1,   -1,
   60,   41,   62,  123,   60,   -1,   62,   40,   -1,   -1,
  256,  257,   45,   -1,  260,   40,   -1,   -1,  264,   -1,
   45,  267,  268,  269,  256,  257,  272,  123,  260,   -1,
  256,  257,  264,  123,  260,   -1,   -1,  269,  264,   -1,
   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  259,  257,  123,  256,  257,
  257,  276,  260,  123,  256,  257,  264,  276,  260,   -1,
  256,  269,  264,   -1,  270,   -1,   -1,  269,  276,  277,
  278,  279,   -1,   -1,  276,  277,  278,  279,   -1,   -1,
  276,  277,  278,  279,   -1,   -1,  256,  257,   -1,   -1,
  260,   -1,  262,  263,  264,  123,   -1,  267,  268,  269,
  256,  257,  272,  125,  260,   -1,  256,  257,  264,   -1,
  260,  267,  268,  269,  264,   -1,  272,  267,  268,  269,
  256,  257,  272,  125,  260,  125,  256,  257,  264,   -1,
  260,   -1,   -1,  269,  264,   -1,   -1,   -1,   -1,  269,
  276,  277,  278,  279,   -1,   -1,  276,  277,  278,  279,
  256,  257,   -1,   -1,  260,   -1,  256,  257,  264,   -1,
  260,   -1,   -1,  269,  264,  257,  258,  259,   -1,  269,
  276,  277,  278,  279,   -1,   -1,  276,  277,  278,  279,
  256,  257,   -1,   -1,  260,   -1,  256,  257,  264,   -1,
  260,   -1,   -1,  269,  264,  257,  258,  259,   -1,  269,
  276,  277,  278,  279,   -1,   -1,  276,  277,  278,  279,
  276,  277,  278,  279,  257,  258,  259,  267,  268,   -1,
   -1,  271,  257,  258,  259,    3,   -1,  270,  256,  257,
   -1,   -1,  260,  268,  256,  257,  264,   -1,  260,   -1,
   -1,  269,  264,   -1,   37,   -1,   -1,  269,   -1,   -1,
   -1,   -1,   -1,   31,  256,  257,  256,  257,  260,   -1,
  260,   39,  264,   -1,  264,   58,   -1,  269,   -1,  269,
   -1,   -1,   -1,   51,   52,   -1,   -1,   -1,   -1,   72,
   73,   -1,   -1,   -1,   -1,   -1,   64,   65,   -1,   -1,
   68,   69,   70,   71,   87,   -1,   -1,   -1,   76,   -1,
   -1,  256,  257,   96,   -1,  260,   -1,   -1,   86,  264,
   -1,   -1,  267,  268,  269,   -1,   -1,  272,   -1,   -1,
  256,  257,  115,  101,  260,   -1,   -1,  120,  264,  122,
   -1,  267,  268,  269,  127,   -1,  272,  130,   -1,   -1,
  133,
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
"sentencia_ejecutable : error",
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
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if END_IF",
"sentencia_seleccion : IF '(' condicion cuerpo_if END_IF",
"sentencia_seleccion : IF '(' condicion ')' cuerpo_if ELSE cuerpo_else END_IF",
"sentencia_seleccion : IF condicion cuerpo_if END_IF",
"sentencia_seleccion : IF condicion ')' cuerpo_if END_IF",
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
"sentencia_control : error LOOP",
"operador : '<'",
"operador : '>'",
"operador : MAYORIGUAL",
"operador : MENORIGUAL",
"operador : DISTINTO",
"operador : COMPARACION",
};

//#line 240 ".\parser.y"

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


//#line 440 "Parser.java"
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
case 11:
//#line 57 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.declarativeStruct ); }
break;
case 13:
//#line 62 ".\parser.y"
{ String lexema = val_peek(0).sval;
									  Token t = this.ts.getToken(lexema);
									  t.addAttr("TIPO", val_peek(1).sval);
									  t.addAttr("USO", AnalizadorSintactico.VARIABLE);
									  this.ts.addToken(lexema, t);
									 }
break;
case 14:
//#line 70 ".\parser.y"
{ yyval = new ParserVal(AnalizadorLexico.TYPE_UINT);}
break;
case 15:
//#line 71 ".\parser.y"
{ yyval = new ParserVal( AnalizadorLexico.TYPE_DOUBLE); }
break;
case 16:
//#line 74 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.procStruct ); }
break;
case 17:
//#line 77 ".\parser.y"
{ String lexema = val_peek(6).sval; }
break;
case 18:
//#line 78 ".\parser.y"
{ String lexema = val_peek(5).sval; 
																		  Token t = this.ts.getToken(lexema);
																		  t.addAttr("USO", AnalizadorSintactico.NOMBREPROC);
																		  this.ts.addToken(lexema, t);}
break;
case 19:
//#line 83 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.errorProcedure, this.l, this.l.getLine()));}
break;
case 20:
//#line 86 ".\parser.y"
{ this.count++;
							  if (this.count > AnalizadorSintactico.maxProcPar){ 
								this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
								this.count = 0;
							  }
							  String lexema = val_peek(0).sval;
							}
break;
case 21:
//#line 93 ".\parser.y"
{ this.count++;
											 if (this.count > AnalizadorSintactico.maxProcPar) 
												this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
											}
break;
case 22:
//#line 97 ".\parser.y"
{  this.count++;
								   if (this.count > AnalizadorSintactico.maxProcPar){ 
										this.s.addSyntaxError( new Error(AnalizadorSintactico.errorMaxProcPar, this.l, this.l.getLine()));
										this.count = 0;
									}
								}
break;
case 23:
//#line 105 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.asigStruct ); 
										  }
break;
case 26:
//#line 109 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.outStruct ); }
break;
case 27:
//#line 110 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.invocProcStructure ); }
break;
case 28:
//#line 111 ".\parser.y"
{this.s.addSyntaxError(new Error(AnalizadorSintactico.errorSentenciaEjecutable, this.l, this.l.getLine()));}
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
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.parI, this.l, this.l.getLine()));}
break;
case 52:
//#line 206 ".\parser.y"
{ polaca.addOperando("");
																 /* Apilo paso incompleto*/
																 polaca.stackUp(CodigoIntermedio.polacaNumber);
																 /* Creo el paso BF*/
																 polaca.addOperando("BF");
																this.s.addSyntaxStruct( AnalizadorSintactico.conditionStructure ); 
																}
break;
case 53:
//#line 213 ".\parser.y"
{ this.s.addSyntaxError( new Error(AnalizadorSintactico.conditionError, this.l, this.l.getLine())); }
break;
case 59:
//#line 227 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ; }
break;
case 60:
//#line 228 ".\parser.y"
{ this.s.addSyntaxStruct( AnalizadorSintactico.whileStructure ) ;}
break;
case 61:
//#line 229 ".\parser.y"
{ this.s.addSyntaxError(new Error(AnalizadorSintactico.whileStructure, this.l, this.l.getLine())) ; }
break;
case 62:
//#line 232 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 63:
//#line 233 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 64:
//#line 234 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 65:
//#line 235 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 66:
//#line 236 ".\parser.y"
{ yyval = val_peek(0); }
break;
case 67:
//#line 237 ".\parser.y"
{ yyval = val_peek(0); }
break;
//#line 841 "Parser.java"
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

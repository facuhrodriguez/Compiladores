package AnalizadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import AnalizadorSintactico.AnalizadorSintactico;

public class AnalizadorLexico {

	private final int FINAL = -1;
	public static final Integer MAX_CAR_ID = 20;
	private HashMap<Integer, Integer> input;
	private AnalizadorSintactico as;

	// Caracteres ASCII
	private static final Integer LETRAMINUSCULA = 97;
	private static final Integer LETRAMAYUSCULA = 65;
	private static final Integer DIGITO = 48;
	private static final Integer MAYOR = 62;
	private static final Integer MENOR = 60;
	private static final Integer IGUAL = 61;
	private static final Integer ADMIRACION = 33;
	private static final Integer MAS = 43;
	private static final Integer MENOS = 45;
	private static final Integer ASTERISCO = 42;
	private static final Integer BARRA = 47;
	private static final Integer LLAVEINICIAL = 123;
	private static final Integer LLAVEFINAL = 125;
	private static final Integer PARENTESISINICIAL = 40;
	private static final Integer PARENTESISFINAL = 41;
	private static final Integer LETRAD = 100;
	private static final Integer GUIONBAJO = 95;
	private static final Integer LETRAU = 117;
	private static final Integer LETRAI = 105;
	private static final Integer COMA = 44;
	private static final Integer PUNTOYCOMA = 59;
	private static final Integer PUNTO = 46;
	private static final Integer SL = 10;
	private static final Integer TAB = 9;
	private static final Integer FINARCHIVO = 36;
	private static final Integer PORCENTAJE = 37;
	private static final Integer COMILLASIMPLE = 39;
	private static final Integer ESPACIO = 32;
	private static final Integer OTROCARACTER = 24;
	private static final Integer RETORNOCARRO = 13;
	private static final Integer DOSPUNTOS = 58;

	// Nï¿½mero de Tokens

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

	public static final String CONSTANTE_ENTERA_SIN_SIGNO = "CONSTANTE ENTERA SIN SIGNO";
	public static final String CONSTANTE_DOUBLE = "CONSTANTE DOUBLE";

	// Rangos de constantes
	public static final Integer MAX_CONSTANT_UI = (int) (Math.pow(2, 16) - 1);
	public static final Integer MIN_CONSTANT_UI = 0;
	public static final Integer MAX_EXPONENT_DOUBLE = 308;
	public static final Integer MIN_EXPONENT_DOUBLE = -308;

	// Warnings
	public static final String WARNING_IDENTIFICADOR = "Warning - Identificador muy largo.";
	public static final String WARNING_CONSTANT_UI = "Warning - Constante entera sin signo fuera de rango";
	public static final String WARNING_CONSTANT_DOUBLE = "Warning - Constante double fuera de rango";

	// Errores
	public static final String ERROR_CARACTER_NO_RECONOCIDO = "Error - Caracter no reconocido";
	public static final String ERROR_CONSTANTE_MAL_DECLARADA = "Error - Constante mal declarada";
	public static final String ERROR_CONSTANTE_ENTERA_MAL_DECLARADA = "Error - Constante entera sin signo mal declarada.";
	public static final String ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA = "Error - Constante double mal declarada.";
	public static final String ERROR_IDENTIFICADOR_MAL_DECLARADO = "Error - Identificador mal declarado.";
	public static final String ERROR_PALABRA_RESERVADA_MAL_DEFINIDA = "Error - Palabra reservada mal definida.";

	// Estructuras
	private AccionSemantica[][] matAS;
	private int[][] matState;
	private Vector<Error> errors;
	private Vector<Error> warnings;
	private PalabrasReservadas pr;
	private FileHandler fh;
	private TablaDeSimbolos table;
	private ArrayList<Token> tokens;
	
	private String tempBuffer;
	private String yylval; 

	public AnalizadorLexico(FileHandler f) {

		this.fh = f;
		this.errors = new Vector<Error>();
		this.warnings = new Vector<Error>();
		this.matState= new int [21][29];
		this.matAS = new AccionSemantica[21][29];
		this.initMatState();
		this.initASMat();
		this.table = new TablaDeSimbolos();
		this.input = new HashMap<Integer, Integer>();
		this.matchInput();
		this.tokens = new ArrayList<Token>();
	}

	/**
	 * Mapea cada caracter procesado con la columna correspondiente de la matriz de estados
	 */
	private void matchInput() {

		this.input.put(AnalizadorLexico.LETRAMINUSCULA, 0);
		this.input.put(AnalizadorLexico.LETRAMAYUSCULA, 1);
		this.input.put(AnalizadorLexico.DIGITO, 2);
		this.input.put(AnalizadorLexico.GUIONBAJO, 3);
		this.input.put(AnalizadorLexico.PUNTO, 4);
		this.input.put(AnalizadorLexico.ASTERISCO, 5);
		this.input.put(AnalizadorLexico.BARRA, 6);
		this.input.put(AnalizadorLexico.MAS, 7);
		this.input.put(AnalizadorLexico.MENOS, 8);
		this.input.put(AnalizadorLexico.IGUAL, 9);
		this.input.put(AnalizadorLexico.MENOR, 10);
		this.input.put(AnalizadorLexico.MAYOR, 11);
		this.input.put(AnalizadorLexico.ADMIRACION, 12);
		this.input.put(AnalizadorLexico.PORCENTAJE, 13);
		this.input.put(AnalizadorLexico.COMILLASIMPLE, 14);
		this.input.put(AnalizadorLexico.LLAVEINICIAL, 15);
		this.input.put(AnalizadorLexico.LLAVEFINAL, 16);
		this.input.put(AnalizadorLexico.COMA, 17);
		this.input.put(AnalizadorLexico.PUNTOYCOMA, 18);
		this.input.put(AnalizadorLexico.DOSPUNTOS, 19);
		this.input.put(AnalizadorLexico.PARENTESISINICIAL, 20);
		this.input.put(AnalizadorLexico.PARENTESISFINAL, 21);
		this.input.put(AnalizadorLexico.LETRAD, 22);
		this.input.put(AnalizadorLexico.LETRAU, 23);
		this.input.put(AnalizadorLexico.LETRAI, 24);
		this.input.put(AnalizadorLexico.ESPACIO, 26);
		this.input.put(AnalizadorLexico.TAB, 26);
		this.input.put(AnalizadorLexico.SL, 27);
		this.input.put(AnalizadorLexico.FINARCHIVO, 28);

	}

	/**
	 * Retorna la columna asociada al caracter de entrada
	 * @param c
	 * @return
	 */
	private Integer getColumn(Character c) {
		int asciiCod = (int) c;
		if (asciiCod == 100)
			return input.get(AnalizadorLexico.LETRAD);
		if (asciiCod == 117)
			return input.get(AnalizadorLexico.LETRAU);
		if (asciiCod == 105)
			return input.get(AnalizadorLexico.LETRAI);
		if (asciiCod <= 57 && asciiCod >= 48) {
			return input.get(AnalizadorLexico.DIGITO);
		} else if ((asciiCod <= 90 && asciiCod >= 65)) {
			return input.get(AnalizadorLexico.LETRAMAYUSCULA);
		} else if ((asciiCod >= 97 && asciiCod <= 122 && asciiCod != AnalizadorLexico.LETRAD
				&& asciiCod != AnalizadorLexico.LETRAU && asciiCod != AnalizadorLexico.LETRAI)) {
			return input.get(AnalizadorLexico.LETRAMINUSCULA);
		}
		if (asciiCod == AnalizadorLexico.RETORNOCARRO || asciiCod == AnalizadorLexico.SL)
			return input.get(AnalizadorLexico.SL);
		if (!input.containsKey(asciiCod))
			return input.get(AnalizadorLexico.OTROCARACTER);
		return input.get(asciiCod);
	}

	/**
	 * Inicializa la matriz de estados
	 */
	private void initMatState() {
		// 0
		this.matState[0][0] = 1;
		this.matState[0][1] = 17;
		this.matState[0][2] = 2;
		this.matState[0][3] = FINAL;
		this.matState[0][4] = 6;
		this.matState[0][5] = FINAL;
		this.matState[0][6] = 9;
		this.matState[0][7] = FINAL;
		this.matState[0][8] = FINAL;
		this.matState[0][9] = 12;
		this.matState[0][10] = 14;
		this.matState[0][11] = 13;
		this.matState[0][12] = 15;
		this.matState[0][13] = FINAL;
		this.matState[0][14] = 16;
		this.matState[0][15] = FINAL;
		this.matState[0][16] = FINAL;
		this.matState[0][17] = FINAL;
		this.matState[0][18] = FINAL;
		this.matState[0][19] = FINAL;
		this.matState[0][20] = FINAL;
		this.matState[0][21] = FINAL;
		this.matState[0][22] = 1;
		this.matState[0][23] = 1;
		this.matState[0][24] = 1;
		this.matState[0][25] = FINAL;
		this.matState[0][26] = 0;
		this.matState[0][27] = 0;
		this.matState[0][28] = FINAL;
		
		// 1
		this.matState[1][0] = 1;
		this.matState[1][1] = FINAL;
		this.matState[1][2] = 1;
		this.matState[1][3] = 1;
		this.matState[1][4] = FINAL;
		this.matState[1][5] = FINAL;
		this.matState[1][6] = FINAL;
		this.matState[1][7] = FINAL;
		this.matState[1][8] = FINAL;
		this.matState[1][9] = FINAL;
		this.matState[1][10] = FINAL;
		this.matState[1][11] = FINAL;
		this.matState[1][12] = FINAL;
		this.matState[1][13] = FINAL;
		this.matState[1][14] = FINAL;
		this.matState[1][15] = FINAL;
		this.matState[1][16] = FINAL;
		this.matState[1][17] = FINAL;
		this.matState[1][18] = FINAL;
		this.matState[1][19] = FINAL;
		this.matState[1][20] = FINAL;
		this.matState[1][21] = FINAL;
		this.matState[1][22] = 1;
		this.matState[1][23] = 1;
		this.matState[1][24] = 1;
		this.matState[1][25] = FINAL;
		this.matState[1][26] = FINAL;
		this.matState[1][27] = FINAL;
		this.matState[1][28] = FINAL;
		
		// 2
		this.matState[2][0] = FINAL;
		this.matState[2][1] = FINAL;
		this.matState[2][2] = 2;
		this.matState[2][3] = 3;
		this.matState[2][4] = 20;
		this.matState[2][5] = FINAL;
		this.matState[2][6] = FINAL;
		this.matState[2][7] = FINAL;
		this.matState[2][8] = FINAL;
		this.matState[2][9] = FINAL;
		this.matState[2][10] = FINAL;
		this.matState[2][11] = FINAL;
		this.matState[2][12] = FINAL;
		this.matState[2][13] = FINAL;
		this.matState[2][14] = FINAL;
		this.matState[2][15] = FINAL;
		this.matState[2][16] = FINAL;
		this.matState[2][17] = FINAL;
		this.matState[2][18] = FINAL;
		this.matState[2][19] = FINAL;
		this.matState[2][20] = FINAL;
		this.matState[2][21] = FINAL;
		this.matState[2][22] = FINAL;
		this.matState[2][23] = FINAL;
		this.matState[2][24] = FINAL;
		this.matState[2][25] = FINAL;
		this.matState[2][26] = FINAL;
		this.matState[2][27] = FINAL;
		this.matState[2][28] = FINAL;
		
		// 3
		this.matState[3][0] = FINAL;
		this.matState[3][1] = FINAL;
		this.matState[3][2] = FINAL;
		this.matState[3][3] = FINAL;
		this.matState[3][4] = FINAL;
		this.matState[3][5] = FINAL;
		this.matState[3][6] = FINAL;
		this.matState[3][7] = FINAL;
		this.matState[3][8] = FINAL;
		this.matState[3][9] = FINAL;
		this.matState[3][10] = FINAL;
		this.matState[3][11] = FINAL;
		this.matState[3][12] = FINAL;
		this.matState[3][13] = FINAL;
		this.matState[3][14] = FINAL;
		this.matState[3][15] = FINAL;
		this.matState[3][16] = FINAL;
		this.matState[3][17] = FINAL;
		this.matState[3][18] = FINAL;
		this.matState[3][19] = FINAL;
		this.matState[3][20] = FINAL;
		this.matState[3][21] = FINAL;
		this.matState[3][22] = FINAL;
		this.matState[3][23] = 4;
		this.matState[3][24] = FINAL;
		this.matState[3][25] = FINAL;
		this.matState[3][26] = FINAL;
		this.matState[3][27] = FINAL;
		this.matState[3][28] = FINAL;
		
		// 4
		this.matState[4][0] = FINAL;
		this.matState[4][1] = FINAL;
		this.matState[4][2] = FINAL;
		this.matState[4][3] = FINAL;
		this.matState[4][4] = FINAL;
		this.matState[4][5] = FINAL;
		this.matState[4][6] = FINAL;
		this.matState[4][7] = FINAL;
		this.matState[4][8] = FINAL;
		this.matState[4][9] = FINAL;
		this.matState[4][10] = FINAL;
		this.matState[4][11] = FINAL;
		this.matState[4][12] = FINAL;
		this.matState[4][13] = FINAL;
		this.matState[4][14] = FINAL;
		this.matState[4][15] = FINAL;
		this.matState[4][16] = FINAL;
		this.matState[4][17] = FINAL;
		this.matState[4][18] = FINAL;
		this.matState[4][19] = FINAL;
		this.matState[4][20] = FINAL;
		this.matState[4][21] = FINAL;
		this.matState[4][22] = FINAL;
		this.matState[4][23] = FINAL;
		this.matState[4][24] = 5;
		this.matState[4][25] = FINAL;
		this.matState[4][26] = FINAL;
		this.matState[4][27] = FINAL;
		this.matState[4][28] = FINAL;
		
		// 5
		this.matState[5][0] = FINAL;
		this.matState[5][1] = FINAL;
		this.matState[5][2] = FINAL;
		this.matState[5][3] = FINAL;
		this.matState[5][4] = FINAL;
		this.matState[5][5] = FINAL;
		this.matState[5][6] = FINAL;
		this.matState[5][7] = FINAL;
		this.matState[5][8] = FINAL;
		this.matState[5][9] = FINAL;
		this.matState[5][10] = FINAL;
		this.matState[5][11] = FINAL;
		this.matState[5][12] = FINAL;
		this.matState[5][13] = FINAL;
		this.matState[5][14] = FINAL;
		this.matState[5][15] = FINAL;
		this.matState[5][16] = FINAL;
		this.matState[5][17] = FINAL;
		this.matState[5][18] = FINAL;
		this.matState[5][19] = FINAL;
		this.matState[5][20] = FINAL;
		this.matState[5][21] = FINAL;
		this.matState[5][22] = FINAL;
		this.matState[5][23] = FINAL;
		this.matState[5][24] = FINAL;
		this.matState[5][25] = FINAL;
		this.matState[5][26] = FINAL;
		this.matState[5][27] = FINAL;
		this.matState[5][28] = FINAL;
		
		// 6
		this.matState[6][0] = FINAL;
		this.matState[6][1] = FINAL;
		this.matState[6][2] = 20;
		this.matState[6][3] = FINAL;
		this.matState[6][4] = FINAL;
		this.matState[6][5] = FINAL;
		this.matState[6][6] = FINAL;
		this.matState[6][7] = FINAL;
		this.matState[6][8] = FINAL;
		this.matState[6][9] = FINAL;
		this.matState[6][10] = FINAL;
		this.matState[6][11] = FINAL;
		this.matState[6][12] = FINAL;
		this.matState[6][13] = FINAL;
		this.matState[6][14] = FINAL;
		this.matState[6][15] = FINAL;
		this.matState[6][16] = FINAL;
		this.matState[6][17] = FINAL;
		this.matState[6][18] = FINAL;
		this.matState[6][19] = FINAL;
		this.matState[6][20] = FINAL;
		this.matState[6][21] = FINAL;
		this.matState[6][22] = 7;
		this.matState[6][23] = FINAL;
		this.matState[6][24] = FINAL;
		this.matState[6][25] = FINAL;
		this.matState[6][26] = FINAL;
		this.matState[6][27] = FINAL;
		this.matState[6][28] = FINAL;
		
		// 7
		this.matState[7][0] = FINAL;
		this.matState[7][1] = FINAL;
		this.matState[7][2] = FINAL;
		this.matState[7][3] = FINAL;
		this.matState[7][4] = FINAL;
		this.matState[7][5] = FINAL;
		this.matState[7][6] = FINAL;
		this.matState[7][7] = 8;
		this.matState[7][8] = 8;
		this.matState[7][9] = FINAL;
		this.matState[7][10] = FINAL;
		this.matState[7][11] = FINAL;
		this.matState[7][12] = FINAL;
		this.matState[7][13] = FINAL;
		this.matState[7][14] = FINAL;
		this.matState[7][15] = FINAL;
		this.matState[7][16] = FINAL;
		this.matState[7][17] = FINAL;
		this.matState[7][18] = FINAL;
		this.matState[7][19] = FINAL;
		this.matState[7][20] = FINAL;
		this.matState[7][21] = FINAL;
		this.matState[7][22] = FINAL;
		this.matState[7][23] = FINAL;
		this.matState[7][24] = FINAL;
		this.matState[7][25] = FINAL;
		this.matState[7][26] = FINAL;
		this.matState[7][27] = FINAL;
		this.matState[7][28] = FINAL;
		
		// 8
		this.matState[8][0] = FINAL;
		this.matState[8][1] = FINAL;
		this.matState[8][2] = 19;
		this.matState[8][3] = FINAL;
		this.matState[8][4] = FINAL;
		this.matState[8][5] = FINAL;
		this.matState[8][6] = FINAL;
		this.matState[8][7] = FINAL;
		this.matState[8][8] = FINAL;
		this.matState[8][9] = FINAL;
		this.matState[8][10] = FINAL;
		this.matState[8][11] = FINAL;
		this.matState[8][12] = FINAL;
		this.matState[8][13] = FINAL;
		this.matState[8][14] = FINAL;
		this.matState[8][15] = FINAL;
		this.matState[8][16] = FINAL;
		this.matState[8][17] = FINAL;
		this.matState[8][18] = FINAL;
		this.matState[8][19] = FINAL;
		this.matState[8][20] = FINAL;
		this.matState[8][21] = FINAL;
		this.matState[8][22] = FINAL;
		this.matState[8][23] = FINAL;
		this.matState[8][24] = FINAL;
		this.matState[8][25] = FINAL;
		this.matState[8][26] = FINAL;
		this.matState[8][27] = FINAL;
		this.matState[8][28] = FINAL;
		
		// 9
		this.matState[9][0] = FINAL;
		this.matState[9][1] = FINAL;
		this.matState[9][2] = FINAL;
		this.matState[9][3] = FINAL;
		this.matState[9][4] = FINAL;
		this.matState[9][5] = FINAL;
		this.matState[9][6] = FINAL;
		this.matState[9][7] = FINAL;
		this.matState[9][8] = FINAL;
		this.matState[9][9] = FINAL;
		this.matState[9][10] = FINAL;
		this.matState[9][11] = FINAL;
		this.matState[9][12] = FINAL;
		this.matState[9][13] = 10;
		this.matState[9][14] = FINAL;
		this.matState[9][15] = FINAL;
		this.matState[9][16] = FINAL;
		this.matState[9][17] = FINAL;
		this.matState[9][18] = FINAL;
		this.matState[9][19] = FINAL;
		this.matState[9][20] = FINAL;
		this.matState[9][21] = FINAL;
		this.matState[9][22] = FINAL;
		this.matState[9][23] = FINAL;
		this.matState[9][24] = FINAL;
		this.matState[9][25] = FINAL;
		this.matState[9][26] = FINAL;
		this.matState[9][27] = FINAL;
		this.matState[9][28] = FINAL;
		
		// 10
		this.matState[10][0] = 10;
		this.matState[10][1] = 10;
		this.matState[10][2] = 10;
		this.matState[10][3] = 10;
		this.matState[10][4] = 10;
		this.matState[10][5] = 10;
		this.matState[10][6] = 10;
		this.matState[10][7] = 10;
		this.matState[10][8] = 10;
		this.matState[10][9] = 10;
		this.matState[10][10] = 10;
		this.matState[10][11] = 10;
		this.matState[10][12] = 10;
		this.matState[10][13] = 11;
		this.matState[10][14] = 10;
		this.matState[10][15] = 10;
		this.matState[10][16] = 10;
		this.matState[10][17] = 10;
		this.matState[10][18] = 10;
		this.matState[10][19] = 10;
		this.matState[10][20] = 10;
		this.matState[10][21] = 10;
		this.matState[10][22] = 10;
		this.matState[10][23] = 10;
		this.matState[10][24] = 10;
		this.matState[10][25] = 10;
		this.matState[10][26] = 10;
		this.matState[10][27] = 10;
		this.matState[10][28] = 10;
		
		// 11
		this.matState[11][0] = 10;
		this.matState[11][1] = 10;
		this.matState[11][2] = 10;
		this.matState[11][3] = 10;
		this.matState[11][4] = 10;
		this.matState[11][5] = 10;
		this.matState[11][6] = 0;
		this.matState[11][7] = 10;
		this.matState[11][8] = 10;
		this.matState[11][9] = 10;
		this.matState[11][10] = 10;
		this.matState[11][11] = 10;
		this.matState[11][12] = 10;
		this.matState[11][13] = 10;
		this.matState[11][14] = 10;
		this.matState[11][15] = 10;
		this.matState[11][16] = 10;
		this.matState[11][17] = 10;
		this.matState[11][18] = 10;
		this.matState[11][19] = 10;
		this.matState[11][20] = 10;
		this.matState[11][21] = 10;
		this.matState[11][22] = 10;
		this.matState[11][23] = 10;
		this.matState[11][24] = 10;
		this.matState[11][25] = 10;
		this.matState[11][26] = 10;
		this.matState[11][27] = 10;
		this.matState[11][28] = 10;
		
		// 12
		this.matState[12][0] = FINAL;
		this.matState[12][1] = FINAL;
		this.matState[12][2] = FINAL;
		this.matState[12][3] = FINAL;
		this.matState[12][4] = FINAL;
		this.matState[12][5] = FINAL;
		this.matState[12][6] = FINAL;
		this.matState[12][7] = FINAL;
		this.matState[12][8] = FINAL;
		this.matState[12][9] = FINAL;
		this.matState[12][10] = FINAL;
		this.matState[12][11] = FINAL;
		this.matState[12][12] = FINAL;
		this.matState[12][13] = FINAL;
		this.matState[12][14] = FINAL;
		this.matState[12][15] = FINAL;
		this.matState[12][16] = FINAL;
		this.matState[12][17] = FINAL;
		this.matState[12][18] = FINAL;
		this.matState[12][19] = FINAL;
		this.matState[12][20] = FINAL;
		this.matState[12][21] = FINAL;
		this.matState[12][22] = FINAL;
		this.matState[12][23] = FINAL;
		this.matState[12][24] = FINAL;
		this.matState[12][25] = FINAL;
		this.matState[12][26] = FINAL;
		this.matState[12][27] = FINAL;
		this.matState[12][28] = FINAL;
		
		// 13
		this.matState[13][0] = FINAL;
		this.matState[13][1] = FINAL;
		this.matState[13][2] = FINAL;
		this.matState[13][3] = FINAL;
		this.matState[13][4] = FINAL;
		this.matState[13][5] = FINAL;
		this.matState[13][6] = FINAL;
		this.matState[13][7] = FINAL;
		this.matState[13][8] = FINAL;
		this.matState[13][9] = FINAL;
		this.matState[13][10] = FINAL;
		this.matState[13][11] = FINAL;
		this.matState[13][12] = FINAL;
		this.matState[13][13] = FINAL;
		this.matState[13][14] = FINAL;
		this.matState[13][15] = FINAL;
		this.matState[13][16] = FINAL;
		this.matState[13][17] = FINAL;
		this.matState[13][18] = FINAL;
		this.matState[13][19] = FINAL;
		this.matState[13][20] = FINAL;
		this.matState[13][21] = FINAL;
		this.matState[13][22] = FINAL;
		this.matState[13][23] = FINAL;
		this.matState[13][24] = FINAL;
		this.matState[13][25] = FINAL;
		this.matState[13][26] = FINAL;
		this.matState[13][28] = FINAL;
		
		// 14
		this.matState[14][0] = FINAL;
		this.matState[14][1] = FINAL;
		this.matState[14][2] = FINAL;
		this.matState[14][3] = FINAL;
		this.matState[14][4] = FINAL;
		this.matState[14][5] = FINAL;
		this.matState[14][6] = FINAL;
		this.matState[14][7] = FINAL;
		this.matState[14][8] = FINAL;
		this.matState[14][9] = FINAL;
		this.matState[14][10] = FINAL;
		this.matState[14][11] = FINAL;
		this.matState[14][12] = FINAL;
		this.matState[14][13] = FINAL;
		this.matState[14][14] = FINAL;
		this.matState[14][15] = FINAL;
		this.matState[14][16] = FINAL;
		this.matState[14][17] = FINAL;
		this.matState[14][18] = FINAL;
		this.matState[14][19] = FINAL;
		this.matState[14][20] = FINAL;
		this.matState[14][21] = FINAL;
		this.matState[14][22] = FINAL;
		this.matState[14][23] = FINAL;
		this.matState[14][24] = FINAL;
		this.matState[14][25] = FINAL;
		this.matState[14][26] = FINAL;
		this.matState[14][27] = FINAL;
		this.matState[14][28] = FINAL;
		
		// 15
		this.matState[15][0] = FINAL;
		this.matState[15][1] = FINAL;
		this.matState[15][2] = FINAL;
		this.matState[15][3] = FINAL;
		this.matState[15][4] = FINAL;
		this.matState[15][5] = FINAL;
		this.matState[15][6] = FINAL;
		this.matState[15][7] = FINAL;
		this.matState[15][8] = FINAL;
		this.matState[15][9] = FINAL;
		this.matState[15][10] = FINAL;
		this.matState[15][11] = FINAL;
		this.matState[15][12] = FINAL;
		this.matState[15][13] = FINAL;
		this.matState[15][14] = FINAL;
		this.matState[15][15] = FINAL;
		this.matState[15][16] = FINAL;
		this.matState[15][17] = FINAL;
		this.matState[15][18] = FINAL;
		this.matState[15][19] = FINAL;
		this.matState[15][20] = FINAL;
		this.matState[15][21] = FINAL;
		this.matState[15][22] = FINAL;
		this.matState[15][23] = FINAL;
		this.matState[15][24] = FINAL;
		this.matState[15][25] = FINAL;
		this.matState[15][26] = FINAL;
		this.matState[15][27] = FINAL;
		this.matState[15][28] = FINAL;
		
		// 16
		this.matState[16][0] = 16;
		this.matState[16][1] = 16;
		this.matState[16][2] = 16;
		this.matState[16][3] = 16;
		this.matState[16][4] = 16;
		this.matState[16][5] = 16;
		this.matState[16][6] = 16;
		this.matState[16][7] = 16;
		this.matState[16][8] = 16;
		this.matState[16][9] = 16;
		this.matState[16][10] = 16;
		this.matState[16][11] = 16;
		this.matState[16][12] = 16;
		this.matState[16][13] = 16;
		this.matState[16][14] = FINAL;
		this.matState[16][15] = 16;
		this.matState[16][16] = 16;
		this.matState[16][17] = 16;
		this.matState[16][18] = 16;
		this.matState[16][19] = 16;
		this.matState[16][20] = 16;
		this.matState[16][21] = 16;
		this.matState[16][22] = 16;
		this.matState[16][23] = 16;
		this.matState[16][24] = 16;
		this.matState[16][25] = 16;
		this.matState[16][26] = 16;
		this.matState[16][27] = FINAL;
		this.matState[16][28] = FINAL;
		
		// 17
		this.matState[17][0] = FINAL;
		this.matState[17][1] = 17;
		this.matState[17][2] = FINAL;
		this.matState[17][3] = 18;
		this.matState[17][4] = FINAL;
		this.matState[17][5] = FINAL;
		this.matState[17][6] = FINAL;
		this.matState[17][7] = FINAL;
		this.matState[17][8] = FINAL;
		this.matState[17][9] = FINAL;
		this.matState[17][10] = FINAL;
		this.matState[17][11] = FINAL;
		this.matState[17][12] = FINAL;
		this.matState[17][13] = FINAL;
		this.matState[17][14] = FINAL;
		this.matState[17][15] = FINAL;
		this.matState[17][16] = FINAL;
		this.matState[17][17] = FINAL;
		this.matState[17][18] = FINAL;
		this.matState[17][19] = FINAL;
		this.matState[17][20] = FINAL;
		this.matState[17][21] = FINAL;
		this.matState[17][22] = FINAL;
		this.matState[17][23] = FINAL;
		this.matState[17][24] = FINAL;
		this.matState[17][25] = FINAL;
		this.matState[17][26] = FINAL;
		this.matState[17][27] = FINAL;
		this.matState[17][28] = FINAL;
		
		// 18
		this.matState[18][0] = FINAL;
		this.matState[18][1] = 18;
		this.matState[18][2] = FINAL;
		this.matState[18][3] = FINAL;
		this.matState[18][4] = FINAL;
		this.matState[18][5] = FINAL;
		this.matState[18][6] = FINAL;
		this.matState[18][7] = FINAL;
		this.matState[18][8] = FINAL;
		this.matState[18][9] = FINAL;
		this.matState[18][10] = FINAL;
		this.matState[18][11] = FINAL;
		this.matState[18][12] = FINAL;
		this.matState[18][13] = FINAL;
		this.matState[18][14] = FINAL;
		this.matState[18][15] = FINAL;
		this.matState[18][16] = FINAL;
		this.matState[18][17] = FINAL;
		this.matState[18][18] = FINAL;
		this.matState[18][19] = FINAL;
		this.matState[18][20] = FINAL;
		this.matState[18][21] = FINAL;
		this.matState[18][22] = FINAL;
		this.matState[18][23] = FINAL;
		this.matState[18][24] = FINAL;
		this.matState[18][25] = FINAL;
		this.matState[18][26] = FINAL;
		this.matState[18][27] = FINAL;
		this.matState[18][28] = FINAL;	
		
		// Estado 19
		this.matState[19][0] = FINAL;
		this.matState[19][1] = FINAL;
		this.matState[19][2] = 19;
		this.matState[19][3] = FINAL;
		this.matState[19][4] = FINAL;
		this.matState[19][5] = FINAL;
		this.matState[19][6] = FINAL;
		this.matState[19][7] = FINAL;
		this.matState[19][8] = FINAL;
		this.matState[19][9] = FINAL;
		this.matState[19][10] = FINAL;
		this.matState[19][11] = FINAL;
		this.matState[19][12] = FINAL;
		this.matState[19][13] = FINAL;
		this.matState[19][14] = FINAL;
		this.matState[19][15] = FINAL;
		this.matState[19][16] = FINAL;
		this.matState[19][17] = FINAL;
		this.matState[19][18] = FINAL;
		this.matState[19][19] = FINAL;
		this.matState[19][20] = FINAL;
		this.matState[19][21] = FINAL;
		this.matState[19][22] = FINAL;
		this.matState[19][23] = FINAL;
		this.matState[19][24] = FINAL;
		this.matState[19][25] = FINAL;
		this.matState[19][26] = FINAL;
		this.matState[19][27] = FINAL;
		this.matState[19][28] = FINAL;
		
		//20
		this.matState[20][0] = FINAL;
		this.matState[20][1] = FINAL;
		this.matState[20][2] = 20;
		this.matState[20][3] = FINAL; 
		this.matState[20][4] = FINAL;
		this.matState[20][5] = FINAL;
		this.matState[20][6] = FINAL;
		this.matState[20][7] = FINAL;
		this.matState[20][8] = FINAL; 
		this.matState[20][9] = FINAL;
		this.matState[20][10] = FINAL;
		this.matState[20][11] = FINAL;
		this.matState[20][12] = FINAL;
		this.matState[20][13] = FINAL;
		this.matState[20][14] = FINAL;
		this.matState[20][15] = FINAL;
		this.matState[20][16] = FINAL;
		this.matState[20][17] = FINAL;
		this.matState[20][18] = FINAL;
		this.matState[20][19] = FINAL;
		this.matState[20][20] = FINAL;
		this.matState[20][21] = FINAL;
		this.matState[20][22] = 7;
		this.matState[20][23] = FINAL;
		this.matState[20][24] = FINAL;
		this.matState[20][25] = FINAL;
		this.matState[20][26] = FINAL;
		this.matState[20][27] = FINAL;
		this.matState[20][28] = FINAL;
	}

	/**
	 * Inicializa la matriz de Acciones Semï¿½nticas
	 */
	private void initASMat() {
		// Estado 0
		this.matAS[0][0] = new AddBuffer(this);
		this.matAS[0][1] = new AddBuffer(this);
		this.matAS[0][2] = new AddBuffer(this);
		this.matAS[0][3] = new AddBuffer(this);
		this.matAS[0][4] = new AddBuffer(this);
		this.matAS[0][5] = new AddOperators(this, false, (int) '*');
		this.matAS[0][6] = new AddBuffer(this);
		this.matAS[0][7] = new AddOperators(this, false, (int) '+');
		this.matAS[0][8] = new AddOperators(this, false, (int) '-');
		this.matAS[0][9] = new AddBuffer(this);
		this.matAS[0][10] = new AddBuffer(this);
		this.matAS[0][11] = new AddBuffer(this);
		this.matAS[0][12] = new AddBuffer(this);
		this.matAS[0][13] = new AddOperators(this, false, (int) '%');
		this.matAS[0][14] = null;
		this.matAS[0][15] = new AddOperators(this, false, (int) '{');
		this.matAS[0][16] = new AddOperators(this, false, (int) '}');
		this.matAS[0][17] = new AddOperators(this, false, (int) ',');
		this.matAS[0][18] = new AddOperators(this, false, (int) ';');
		this.matAS[0][19] = new AddOperators(this, false, (int) ':');
		this.matAS[0][20] = new AddOperators(this, false, (int) '(');
		this.matAS[0][21] = new AddOperators(this, false, (int) ')');
		this.matAS[0][22] = new AddBuffer(this);
		this.matAS[0][23] = new AddBuffer(this);
		this.matAS[0][24] = new AddBuffer(this);;
		this.matAS[0][25] = new Error(AnalizadorLexico.ERROR_CARACTER_NO_RECONOCIDO, this);
		this.matAS[0][26] = null;
		this.matAS[0][27] = null;
		this.matAS[0][28] = null; 

		// Estado 1
		this.matAS[1][0] = new AddBuffer(this);
		this.matAS[1][1] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][2] = new AddBuffer(this);
		this.matAS[1][3] = new AddBuffer(this);
		this.matAS[1][4] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][5] = new AddIdentificator(this);
		this.matAS[1][6] = new AddIdentificator(this);
		this.matAS[1][7] = new AddIdentificator(this);
		this.matAS[1][8] = new AddIdentificator(this);
		this.matAS[1][9] = new AddIdentificator(this);
		this.matAS[1][10] = new AddIdentificator(this);
		this.matAS[1][11] = new AddIdentificator(this);
		this.matAS[1][12] = new AddIdentificator(this);
		this.matAS[1][13] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][14] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][15] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][16] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][17] = new AddIdentificator(this);
		this.matAS[1][18] = new AddIdentificator(this);
		this.matAS[1][19] = new AddIdentificator(this);
		this.matAS[1][20] = new AddIdentificator(this);
		this.matAS[1][21] = new AddIdentificator(this);
		this.matAS[1][22] = new AddBuffer(this);
		this.matAS[1][23] = new AddBuffer(this);
		this.matAS[1][24] = new AddBuffer(this);
		this.matAS[1][25] = new Error(AnalizadorLexico.ERROR_IDENTIFICADOR_MAL_DECLARADO, this);
		this.matAS[1][26] = new AddIdentificator(this);
		this.matAS[1][27] = new AddIdentificator(this);
		this.matAS[1][28] = new AddIdentificator(this);

		// Estado 2
		this.matAS[2][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][2] = new AddBuffer(this);
		this.matAS[2][3] = new AddBuffer(this);
		this.matAS[2][4] = new AddBuffer(this);
		this.matAS[2][5] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][6] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][7] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][8] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][9] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][10] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][11] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][12] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][17] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][18] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][26] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][27] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);
		this.matAS[2][28] = new Error(AnalizadorLexico.ERROR_CONSTANTE_MAL_DECLARADA, this);

		// Estado 3
		this.matAS[3][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][2] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][5] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][6] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][7] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][8] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][9] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][10] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][11] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][12] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][17] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][18] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][23] = new AddBuffer(this);
		this.matAS[3][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][26] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][27] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[3][28] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		
		// Estado 4
		this.matAS[4][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][2] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][5] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][6] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][7] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][8] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][9] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][10] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][11] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][12] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][17] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][18] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][24] = new AddBuffer(this);
		this.matAS[4][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][26] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][27] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[4][28] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		// Estado 5
		this.matAS[5][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][2] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][5] = new AddConstant(this, 'U');
		this.matAS[5][6] = new AddConstant(this, 'U');
		this.matAS[5][7] = new AddConstant(this, 'U');
		this.matAS[5][8] = new AddConstant(this, 'U');
		this.matAS[5][9] = new AddConstant(this, 'U');
		this.matAS[5][10] = new AddConstant(this, 'U');
		this.matAS[5][11] = new AddConstant(this, 'U');
		this.matAS[5][12] = new AddConstant(this, 'U');
		this.matAS[5][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][17] = new AddConstant(this, 'U');
		this.matAS[5][18] = new AddConstant(this, 'U');
		this.matAS[5][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][21] = new AddConstant(this, 'U');
		this.matAS[5][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_ENTERA_MAL_DECLARADA, this);
		this.matAS[5][26] = new AddConstant(this, 'U');
		this.matAS[5][27] = new AddConstant(this, 'U');
		this.matAS[5][28] = new AddConstant(this, 'U');
		
		// Estado 6
		this.matAS[6][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][2] = new AddBuffer(this);
		this.matAS[6][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][5] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][6] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][7] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][8] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][9] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][10] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][11] =new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][12] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][17] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][18] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][22] = new AddBuffer(this);
		this.matAS[6][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[6][26] = new AddOperators(this, false, (int) '.');
		this.matAS[6][27] = new AddOperators(this, false, (int) '.');
		this.matAS[6][28] = new AddOperators(this, false, (int) '.');

		// Estado 7
		this.matAS[7][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][2] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][5] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][6] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][7] = new AddBuffer(this);
		this.matAS[7][8] = new AddBuffer(this);
		this.matAS[7][9] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][10] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][11] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][12] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][17] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][18] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][26] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][27] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[7][28] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		// Estado 8
		this.matAS[8][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][2] = new AddBuffer(this);
		this.matAS[8][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][5] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][6] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][7] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][8] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][9] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][10] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][11] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][12] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][17] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][18] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][26] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][27] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[8][28] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		// Estado 9
		this.matAS[9][0] = new AddOperators(this, true, '/');
		this.matAS[9][1] = new AddOperators(this, true, '/');
		this.matAS[9][2] = new AddOperators(this, true, '/');
		this.matAS[9][3] = new AddOperators(this, true, '/');
		this.matAS[9][4] = new AddOperators(this, true, '/');
		this.matAS[9][5] = new AddOperators(this, true, '/');
		this.matAS[9][6] = new AddOperators(this, true, '/');
		this.matAS[9][7] = new AddOperators(this, true, '/');
		this.matAS[9][8] = new AddOperators(this, true, '/');
		this.matAS[9][9] = new AddOperators(this, true, '/');
		this.matAS[9][10] = new AddOperators(this, true, '/');
		this.matAS[9][11] = new AddOperators(this, true, '/');
		this.matAS[9][12] = new AddOperators(this, true, '/');
		this.matAS[9][13] = null;
		this.matAS[9][14] = new AddOperators(this, true, '/');
		this.matAS[9][15] = new AddOperators(this, true, '/');
		this.matAS[9][16] = new AddOperators(this, true, '/');
		this.matAS[9][17] = new AddOperators(this, true, '/');
		this.matAS[9][18] = new AddOperators(this, true, '/');
		this.matAS[9][19] = new AddOperators(this, true, '/');
		this.matAS[9][20] = new AddOperators(this, true, '/');
		this.matAS[9][21] = new AddOperators(this, true, '/');
		this.matAS[9][22] = new AddOperators(this, true, '/');
		this.matAS[9][23] = new AddOperators(this, true, '/');
		this.matAS[9][24] = new AddOperators(this, true, '/');
		this.matAS[9][25] = new AddOperators(this, false, '/');
		this.matAS[9][26] = new AddOperators(this, false, '/');
		this.matAS[9][27] = new AddOperators(this, false, '/');
		this.matAS[9][28] = new AddOperators(this, false, '/');
		
		// Estado 10
		this.matAS[10][0] = null;
		this.matAS[10][1] = null;
		this.matAS[10][2] = null;
		this.matAS[10][3] = null;
		this.matAS[10][4] = null;
		this.matAS[10][5] = null;
		this.matAS[10][6] = null;
		this.matAS[10][7] = null;
		this.matAS[10][8] = null;
		this.matAS[10][9] = null;
		this.matAS[10][10] = null;
		this.matAS[10][11] = null;
		this.matAS[10][12] = null;
		this.matAS[10][13] = null;
		this.matAS[10][14] = null;
		this.matAS[10][15] = null;
		this.matAS[10][16] = null;
		this.matAS[10][17] = null;
		this.matAS[10][18] = null;
		this.matAS[10][19] = null;
		this.matAS[10][20] = null;
		this.matAS[10][21] = null;
		this.matAS[10][22] = null;
		this.matAS[10][23] = null;
		this.matAS[10][24] = null;
		this.matAS[10][25] = null;
		this.matAS[10][26] = null;
		this.matAS[10][27] = null;
		this.matAS[10][28] = null;
		
		// Estado 11
		this.matAS[11][0] = null;
		this.matAS[11][1] = null;
		this.matAS[11][2] = null;
		this.matAS[11][3] = null;
		this.matAS[11][4] = null;
		this.matAS[11][5] = null;
		this.matAS[11][6] = null;
		this.matAS[11][7] = null;
		this.matAS[11][8] = null;
		this.matAS[11][9] = null;
		this.matAS[11][10] = null;
		this.matAS[11][11] = null;
		this.matAS[11][12] = null;
		this.matAS[11][13] = null;
		this.matAS[11][14] = null;
		this.matAS[11][15] = null;
		this.matAS[11][16] = null;
		this.matAS[11][17] = null;
		this.matAS[11][18] = null;
		this.matAS[11][19] = null;
		this.matAS[11][20] = null;
		this.matAS[11][21] = null;
		this.matAS[11][22] = null;
		this.matAS[11][23] = null;
		this.matAS[11][24] = null;
		this.matAS[11][25] = null;
		this.matAS[11][26] = null;
		this.matAS[11][27] = null;
		this.matAS[11][28] = null;
		
		// Estado 12
		this.matAS[12][0] = new AddOperators(this, true, (int) '=');
		this.matAS[12][1] = new AddOperators(this, true, (int) '=');
		this.matAS[12][2] = new AddOperators(this, true, (int) '=');
		this.matAS[12][3] = new AddOperators(this, true, (int) '=');
		this.matAS[12][4] = new AddOperators(this, true, (int) '=');
		this.matAS[12][5] = new AddOperators(this, true, (int) '=');
		this.matAS[12][6] = new AddOperators(this, true, (int) '=');
		this.matAS[12][7] = new AddOperators(this, true, (int) '=');
		this.matAS[12][8] = new AddOperators(this, true, (int) '=');
		this.matAS[12][9] = new AddOperators(this, false, AnalizadorLexico.COMPARACION);
		this.matAS[12][10] = new AddOperators(this, true, (int) '=');
		this.matAS[12][11] = new AddOperators(this, true, (int) '=');
		this.matAS[12][12] = new AddOperators(this, true, (int) '=');
		this.matAS[12][13] = new AddOperators(this, true, (int) '=');
		this.matAS[12][14] = new AddOperators(this, true, (int) '=');
		this.matAS[12][15] = new AddOperators(this, true, (int) '=');
		this.matAS[12][16] = new AddOperators(this, true, (int) '=');
		this.matAS[12][17] = new AddOperators(this, true, (int) '=');
		this.matAS[12][18] = new AddOperators(this, true, (int) '=');
		this.matAS[12][19] = new AddOperators(this, true, (int) '=');
		this.matAS[12][20] = new AddOperators(this, true, (int) '=');
		this.matAS[12][21] = new AddOperators(this, true, (int) '=');
		this.matAS[12][22] = new AddOperators(this, true, (int) '=');
		this.matAS[12][23] = new AddOperators(this, true, (int) '=');
		this.matAS[12][24] = new AddOperators(this, true, (int) '=');
		this.matAS[12][25] = new AddOperators(this, false, (int) '=');
		this.matAS[12][26] = new AddOperators(this, false, (int) '=');
		this.matAS[12][27] = new AddOperators(this, false, (int) '=');
		this.matAS[12][28] = new AddOperators(this, false, (int) '=');
		
		// Estado 13
		this.matAS[13][0] = new AddOperators(this, true, (int) '>');
		this.matAS[13][1] = new AddOperators(this, true, (int) '>');
		this.matAS[13][2] = new AddOperators(this, true, (int) '>');
		this.matAS[13][3] = new AddOperators(this, true, (int) '>');
		this.matAS[13][4] = new AddOperators(this, true, (int) '>');
		this.matAS[13][5] = new AddOperators(this, true, (int) '>');
		this.matAS[13][6] = new AddOperators(this, true, (int) '>');
		this.matAS[13][7] = new AddOperators(this, true, (int) '>');
		this.matAS[13][8] = new AddOperators(this, true, (int) '>');
		this.matAS[13][9] = new AddOperators(this, false, AnalizadorLexico.MAYORIGUAL);
		this.matAS[13][10] = new AddOperators(this, true, (int) '>');
		this.matAS[13][11] = new AddOperators(this, true, (int) '>');
		this.matAS[13][12] = new AddOperators(this, true, (int) '>');
		this.matAS[13][13] = new AddOperators(this, true, (int) '>');
		this.matAS[13][14] = new AddOperators(this, true, (int) '>');
		this.matAS[13][15] = new AddOperators(this, true, (int) '>');
		this.matAS[13][16] = new AddOperators(this, true, (int) '>');
		this.matAS[13][17] = new AddOperators(this, true, (int) '>');
		this.matAS[13][18] = new AddOperators(this, true, (int) '>');
		this.matAS[13][19] = new AddOperators(this, true, (int) '>');
		this.matAS[13][20] = new AddOperators(this, true, (int) '>');
		this.matAS[13][21] = new AddOperators(this, true, (int) '>');
		this.matAS[13][22] = new AddOperators(this, true, (int) '>');
		this.matAS[13][23] = new AddOperators(this, true, (int) '>');
		this.matAS[13][24] = new AddOperators(this, true, (int) '>');
		this.matAS[13][25] = new AddOperators(this, false, (int) '>');
		this.matAS[13][26] = new AddOperators(this, false, (int) '>');
		this.matAS[13][27] = new AddOperators(this, false, (int) '>');
		this.matAS[13][28] = new AddOperators(this, false, (int) '>');
		
		// Estado 14
		this.matAS[14][0] = new AddOperators(this, true, (int) '<');
		this.matAS[14][1] = new AddOperators(this, true, (int) '<');
		this.matAS[14][2] = new AddOperators(this, true, (int) '<');
		this.matAS[14][3] = new AddOperators(this, true, (int) '<');
		this.matAS[14][4] = new AddOperators(this, true, (int) '<');
		this.matAS[14][5] = new AddOperators(this, true, (int) '<');
		this.matAS[14][6] = new AddOperators(this, true, (int) '<');
		this.matAS[14][7] = new AddOperators(this, true, (int) '<');
		this.matAS[14][8] = new AddOperators(this, true, (int) '<');
		this.matAS[14][9] = new AddOperators(this, false, AnalizadorLexico.MENORIGUAL);
		this.matAS[14][10] = new AddOperators(this, true, (int) '<');
		this.matAS[14][11] = new AddOperators(this, true, (int) '<');
		this.matAS[14][12] = new AddOperators(this, true, (int) '<');
		this.matAS[14][13] = new AddOperators(this, true, (int) '<');
		this.matAS[14][14] = new AddOperators(this, true, (int) '<');
		this.matAS[14][15] = new AddOperators(this, true, (int) '<');
		this.matAS[14][16] = new AddOperators(this, true, (int) '<');
		this.matAS[14][17] = new AddOperators(this, true, (int) '<');
		this.matAS[14][18] = new AddOperators(this, true, (int) '<');
		this.matAS[14][19] = new AddOperators(this, true, (int) '<');
		this.matAS[14][20] = new AddOperators(this, true, (int) '<');
		this.matAS[14][21] = new AddOperators(this, true, (int) '<');
		this.matAS[14][22] = new AddOperators(this, true, (int) '<');
		this.matAS[14][23] = new AddOperators(this, true, (int) '<');
		this.matAS[14][24] = new AddOperators(this, true, (int) '<');
		this.matAS[14][25] = new AddOperators(this, false, (int) '<');
		this.matAS[14][26] = new AddOperators(this, false, (int) '<');
		this.matAS[14][27] = new AddOperators(this, false, (int) '<');
		this.matAS[14][28] = new AddOperators(this, false, (int) '<');
		
		// Estado 15
		this.matAS[15][0] = new AddOperators(this, true, (int) '!');
		this.matAS[15][1] = new AddOperators(this, true, (int) '!');
		this.matAS[15][2] = new AddOperators(this, true, (int) '!');
		this.matAS[15][3] = new AddOperators(this, true, (int) '!');
		this.matAS[15][4] = new AddOperators(this, true, (int) '!');
		this.matAS[15][5] = new AddOperators(this, true, (int) '!');
		this.matAS[15][6] = new AddOperators(this, true, (int) '!');
		this.matAS[15][7] = new AddOperators(this, true, (int) '!');
		this.matAS[15][8] = new AddOperators(this, true, (int) '!');
		this.matAS[15][9] = new AddOperators(this, false, AnalizadorLexico.DISTINTO);
		this.matAS[15][10] = new AddOperators(this, true, (int) '!');
		this.matAS[15][11] = new AddOperators(this, true, (int) '!');
		this.matAS[15][12] = new AddOperators(this, true, (int) '!');
		this.matAS[15][13] = new AddOperators(this, true, (int) '!');
		this.matAS[15][14] = new AddOperators(this, true, (int) '!');
		this.matAS[15][15] = new AddOperators(this, true, (int) '!');
		this.matAS[15][16] = new AddOperators(this, true, (int) '!');
		this.matAS[15][17] = new AddOperators(this, true, (int) '!');
		this.matAS[15][18] = new AddOperators(this, true, (int) '!');
		this.matAS[15][19] = new AddOperators(this, true, (int) '!');
		this.matAS[15][20] = new AddOperators(this, true, (int) '!');
		this.matAS[15][21] = new AddOperators(this, true, (int) '!');
		this.matAS[15][22] = new AddOperators(this, true, (int) '!');
		this.matAS[15][23] = new AddOperators(this, true, (int) '!');
		this.matAS[15][24] = new AddOperators(this, true, (int) '!');
		this.matAS[15][25] = new AddOperators(this, false, (int) '!');
		this.matAS[15][26] = new AddOperators(this, false, (int) '!');
		this.matAS[15][27] = new AddOperators(this, false, (int) '!');
		this.matAS[15][28] = new AddOperators(this, false, (int) '!');
		
		// Estado 16
		this.matAS[16][0] = new AddBuffer(this);
		this.matAS[16][1] = new AddBuffer(this);
		this.matAS[16][2] = new AddBuffer(this);
		this.matAS[16][3] = new AddBuffer(this);
		this.matAS[16][4] = new AddBuffer(this);
		this.matAS[16][5] = new AddBuffer(this);
		this.matAS[16][6] = new AddBuffer(this);
		this.matAS[16][7] = new AddBuffer(this);
		this.matAS[16][8] = new AddBuffer(this);
		this.matAS[16][9] = new AddBuffer(this);
		this.matAS[16][10] = new AddBuffer(this);
		this.matAS[16][11] = new AddBuffer(this);
		this.matAS[16][12] = new AddBuffer(this);
		this.matAS[16][13] = new AddBuffer(this);
		this.matAS[16][14] = new AddChain(this);
		this.matAS[16][15] = new AddBuffer(this);
		this.matAS[16][16] = new AddBuffer(this);
		this.matAS[16][17] = new AddBuffer(this);
		this.matAS[16][18] = new AddBuffer(this);
		this.matAS[16][19] = new AddBuffer(this);
		this.matAS[16][20] = new AddBuffer(this);
		this.matAS[16][21] = new AddBuffer(this);
		this.matAS[16][22] = new AddBuffer(this);
		this.matAS[16][23] = new AddBuffer(this);
		this.matAS[16][24] = new AddBuffer(this);
		this.matAS[16][25] = new AddBuffer(this);
		this.matAS[16][26] = new AddBuffer(this);
		this.matAS[16][27] = new AddChain(this);
		this.matAS[16][28] = new AddChain(this);
		
		// Estado 17
		this.matAS[17][0] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][1] = new AddBuffer(this);
		this.matAS[17][2] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][3] = new AddBuffer(this);
		this.matAS[17][4] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][5] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][6] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][7] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][8] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][9] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][10] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][11] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][12] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][13] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][14] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][15] =  new AddPalabraReservada(this);
		this.matAS[17][16] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][17] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][18] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][19] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][20] = new AddPalabraReservada(this);
		this.matAS[17][21] = new AddPalabraReservada(this);
		this.matAS[17][22] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][23] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][24] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][25] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[17][26] = new AddPalabraReservada(this);
		this.matAS[17][27] = new AddPalabraReservada(this);
		this.matAS[17][28] = new AddPalabraReservada(this);
		
		// Estado 18
		this.matAS[18][0] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][1] = new AddBuffer(this);
		this.matAS[18][2] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][3] = new AddBuffer(this);
		this.matAS[18][4] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][5] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][6] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][7] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][8] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][9] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][10] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][11] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][12] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][13] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][14] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][15] =  new AddPalabraReservada(this);
		this.matAS[18][16] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][17] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][18] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][19] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][20] = new AddPalabraReservada(this);
		this.matAS[18][21] = new AddPalabraReservada(this);
		this.matAS[18][22] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][23] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][24] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][25] = new Error(ERROR_PALABRA_RESERVADA_MAL_DEFINIDA, this);
		this.matAS[18][26] = new AddPalabraReservada(this);
		this.matAS[18][27] = new AddPalabraReservada(this);
		this.matAS[18][28] = new AddPalabraReservada(this);
		
		// Estado 19
		this.matAS[19][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][2] = new AddBuffer(this);
		this.matAS[19][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][5] = new AddConstant(this, 'D');
		this.matAS[19][6] = new AddConstant(this, 'D');
		this.matAS[19][7] = new AddConstant(this, 'D');
		this.matAS[19][8] = new AddConstant(this, 'D');
		this.matAS[19][9] = new AddConstant(this, 'D');
		this.matAS[19][10] = new AddConstant(this, 'D');
		this.matAS[19][11] = new AddConstant(this, 'D');
		this.matAS[19][12] = new AddConstant(this, 'D');
		this.matAS[19][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][17] = new AddConstant(this, 'D');
		this.matAS[19][18] = new AddConstant(this, 'D');
		this.matAS[19][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][22] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[19][26] = new AddConstant(this, 'D');
		this.matAS[19][27] = new AddConstant(this, 'D');
		this.matAS[19][28] = new AddConstant(this, 'D');
		
		// Estado 20
		this.matAS[20][0] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][1] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][2] = new AddBuffer(this);
		this.matAS[20][3] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][4] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][5] = new AddConstant(this, 'D');
		this.matAS[20][6] = new AddConstant(this, 'D');
		this.matAS[20][7] = new AddConstant(this, 'D');
		this.matAS[20][8] = new AddConstant(this, 'D');
		this.matAS[20][9] = new AddConstant(this, 'D');
		this.matAS[20][10] = new AddConstant(this, 'D');
		this.matAS[20][11] = new AddConstant(this, 'D');
		this.matAS[20][12] = new AddConstant(this, 'D');
		this.matAS[20][13] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][14] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][15] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][16] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][17] = new AddConstant(this, 'D');
		this.matAS[20][18] = new AddConstant(this, 'D');
		this.matAS[20][19] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][20] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][21] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][22] = new AddBuffer(this);
		this.matAS[20][23] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][24] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][25] = new Error(AnalizadorLexico.ERROR_CONSTANTE_DOUBLE_MAL_DECLARADA, this);
		this.matAS[20][26] = new AddConstant(this, 'D');
		this.matAS[20][27] = new AddConstant(this, 'D');
		this.matAS[20][28] = new AddConstant(this, 'D');
	}

	/**
	 * Obtiene el buffer temporal
	 * @return
	 */
	public String getBuffer() {
		return this.tempBuffer;
	}

	/**
	 * Agrega un caracter al buffer temporal.
	 * @param c
	 */
	public void addBuffer(Character c) {
		if (c != null) {
			int codAscii = (int) c;
			if (codAscii != AnalizadorLexico.SL && codAscii != AnalizadorLexico.TAB 
				&& codAscii != AnalizadorLexico.ESPACIO )
			this.tempBuffer = this.tempBuffer.concat(c.toString());
		}
	}

	/**
	 * Realiza el anï¿½lisis lï¿½xico
	 * @return
	 */
	public int yylex() {
		Integer state = 0;
		Character input;
		Integer numbToken = 1;
		this.tempBuffer = "";
		while (state != this.FINAL && !fh.endFile()) {
			input = fh.getCurrentChar();
			int column = getColumn(input);
			if (this.matAS[state][column] != null) {
				numbToken = this.matAS[state][column].run();
			}
			state = this.matState[state][column];
			this.fh.forward();
		}
		
		// Si llego a un estado final, se agrega el token encontrado.
		if (state == this.FINAL) 
			state = 0;
		
		if (fh.endFile())
			return 0;
		
		return numbToken;
	}

	/**
	 * Devuelve a la entrada el ï¿½ltimo caracter leido
	 */
	public void unget() {
		this.fh.goBack();
	}

	/**
	 * Inicializa el buffer temporal
	 */
	public void initBuffer() {
		if (this.tempBuffer.length() >= 0)
			this.tempBuffer = "";
	}

	/**
	 * Agrega el warning al vector de warnings.
	 * @param error
	 */
	public void addWarning(Error error) {
		if (error != null)
			this.warnings.add(error);
	}

	/**
	 * Devuelve las palabras reservadas reconocidas por el Analizador Lï¿½xico.
	 * @return
	 */
	public PalabrasReservadas getPalabrasReservadas() {
		return this.pr;
	}

	/**
	 * Agrega un token a la tabla de sï¿½mbolos
	 * @param token
	 * @param lexema
	 */
	public void addOnTablaDeSimbolos(String lexema, Token token) {
		this.table.addToken(lexema, token);
	}
	
	/**
	 * Devuelve la lï¿½nea actual del cï¿½digo.
	 * @return
	 */
	public Integer getLine() {
		return this.fh.getCurrentLineNumber();
	}
	
	
	/**
	 * Agrega el error e al vector de errores.
	 * @param e
	 */
	public void addError(Error e) {
		if (e != null)
			this.errors.add(e);
	}

	/**
	 * Retorna el caracter ledo actualmente.
	 * @return
	 */
	public Character currentCharacter() {
		return this.fh.getCurrentChar();
	}
	
	/**
	 * Imprime los tokens encontrados
	 */
	public void printTokens() {
		System.out.println("\n");
		System.out.println("TOKENS");
		System.out.println("NUMERO " + "               NAME            " + "       TYPE");
		for (Token t : this.tokens)
			System.out.println(t.toString());
	}
	
	/**
	 * Agrega el token t a la estructura de tokens
	 * @param t
	 */
	public void addToken(Token t) {
		if (t != null)
			this.tokens.add(t);
	}
	
	
	/**
	 * Imprime los warnings encontrados.
	 */
	public void printWarnings() {
		System.out.println("\n");
		if (this.warnings.size() > 0) {
			System.err.println("WARNINGS LEXICOS");
			for (Error e : this.warnings) {
				System.err.println(e.toString());
			}
		}
		else 
			System.out.println("No hay warnings léxicos");
	}
	
	
	/**
	 * Imprime los errores lï¿½xicos encontrados
	 */
	public void printErrors() {
		System.out.println("\n");
		if (this.errors.size() > 0) {
			System.err.println("ERRORES LEXICOS");
			for (Error e : this.errors) {
				System.err.println(e.toString());
			}
		}
		else 
			System.out.println("No hay errores léxicos");
	}
	
	/**
	 * Imprime el contenido de la tabla de sï¿½mbolos.
	 */
	public void printTablaSimbolos() {
		System.out.println("\n");
		System.out.println("---------------- TABLA DE SIMBOLOS ----------------" + "\n" );
		System.out.println("TOKEN " + "               NAME            " + "       TYPE");
		for (Token t : this.table.getTokens())
			System.out.println("" + t.toString());
	}

	
	/**
	 * En los casos que haya errores lï¿½xicos, se ejecuta el modo pï¿½nico.
	 * Descarta todos los caracteres hasta que se encuentra con un salto de lï¿½nea, tabulaciï¿½n
	 * o retorno de carro.
	 */
	public void panicMode() {
		int currentChar;
		boolean charBreak = false;
		while (!charBreak && !this.fh.endFile()) {
			currentChar = (int) this.currentCharacter();
			if (currentChar == AnalizadorLexico.ESPACIO || currentChar == AnalizadorLexico.TAB 
				|| currentChar == AnalizadorLexico.SL || currentChar == AnalizadorLexico.RETORNOCARRO)
				charBreak = true;
			else {
				this.addBuffer(this.currentCharacter());
				this.fh.forward();
			}
		}	
	}

	public String getYylval() {
		return yylval;
	}

	public void setYylval(String yylval) {
		this.yylval = yylval;
	}
	
	public void setSintactico(AnalizadorSintactico s) {
		this.as = s;
	}
	
	public TablaDeSimbolos getTS() {
		return this.table;
	}
}
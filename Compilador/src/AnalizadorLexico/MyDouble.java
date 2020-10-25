package AnalizadorLexico;

public class MyDouble {
	
	private static Double minValuePos = Math.pow(2.2250738585072014, -308);
	private static Double minValueNeg = -Math.pow(1.7976931348623157, 308);
	private static Double maxValuePos = Double.MAX_VALUE;
	private static Double maxValueNeg = -Math.pow(-2.2250738585072014, -308);
	private static Integer minExp = -308;
	private static Integer maxExp = 308;
	public static boolean truncate = false;
	
	public static Double getMaxValuePos() {
		return maxValuePos;
	}
	
	public static Double getMinValuePos() {
		return minValuePos;
	}
	
	public static Double getMinValueNeg() {
		return minValueNeg;
	}
	
	public static Double getMaxValueNeg() {
		return maxValueNeg;
	}
	
	public static Integer getMaxExp() {
		return maxExp;
	}
	
	public static Integer getMinExp() {
		return minExp;
	}
	
	public static Double checkPositiveRange(Double d) {
		if (d.isInfinite() || d > getMaxValuePos()) {
			truncate = true;
			return getMaxValuePos();
		}
		if (d < minValuePos) {
			truncate = true;
			return getMinValuePos();
		}
		truncate = false;
		return d;
		
	}
	
	public static Double checkNegativeRange(Double d, Double mantissa) {
		if ( (d == 0 && mantissa != 0) || (d < getMinValueNeg() )) {
			truncate = true;
			return getMinValueNeg();
		}
		if ( (d == 0 && mantissa !=0) || (d > getMaxValueNeg()) ) {
			truncate = true;
			return getMaxValueNeg();
		}
		truncate = false;
		return d;
	}
	
	public static Double check(AnalizadorLexico l) {
		Double number = 0.0;
		double d = 0.0;
		String buffer = l.getBuffer();
		// Si la constante tiene exponente
		if (buffer.contains("d")) {
			Character sign = buffer.charAt(buffer.indexOf('d') + 1);
			Integer indexSign = buffer.indexOf(sign);
			Integer exp = Integer.parseInt(buffer.substring(indexSign + 1, buffer.length()));
			double mantissa = Double.parseDouble('-' + buffer.substring(0, buffer.indexOf('d')));
			if (sign.equals('+')) 
				 d = Math.pow(mantissa, exp);
			 else 
				 d = Math.pow(mantissa, -exp);
			if (d > 0)
				number = MyDouble.checkPositiveRange(d);
			else 
				number = MyDouble.checkNegativeRange(d, mantissa);
		} else {
			d = Double.parseDouble('-' + buffer);
			if (d > 0) 
				number = MyDouble.checkPositiveRange(d);
			else 
				number = MyDouble.checkNegativeRange(d, 0.0);
		}
		return number;
	}
}

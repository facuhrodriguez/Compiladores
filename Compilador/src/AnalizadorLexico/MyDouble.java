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
}

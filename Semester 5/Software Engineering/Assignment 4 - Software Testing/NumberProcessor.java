public class NumberProcessor {
	private Calculator calculator;
	
	public void setCalculator(Calculator calculator) {
		this.calculator = calculator;
	}
	
	public int process(int x, int y) {
		if (x < 0) {
			int result = calculator.calculate(x,y);
			
			if (result < 10) {
				return result;
			} else {
				return result - 10;
			}
		} else {
			return x + y;
		}
	}
}

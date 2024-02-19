public class CalculatorStub implements Calculator {
    private int result;

    public void setResult(int result){
        this.result = result;
    }

    @Override
    public int calculate(int x, int y){
        return result;
    }
}

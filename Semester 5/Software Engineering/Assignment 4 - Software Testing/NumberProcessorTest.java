import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberProcessorTest {

    private NumberProcessor numberProcessor;
    private CalculatorStub calculator;

    @BeforeEach
    public void setup(){
        
        numberProcessor = new NumberProcessor();
        calculator = new CalculatorStub();
        numberProcessor.setCalculator(calculator);
    }

    @Test
    public void firstParametreIsPositive(){

        Assertions.assertEquals(10, numberProcessor.process(5, 5));
    }

    @Test 
    public void firstParametreIsZero(){

        Assertions.assertEquals(5, numberProcessor.process(0, 5));
    }

    @Test 
    public void firstParametreIsNegativeAndResultIsSmallerThanTen(){
        
        calculator.setResult(5);
        Assertions.assertEquals(5, numberProcessor.process(-5, 10));
    }

    @Test 
    public void firstParametreIsNegativeAndResultIsExactlyTen(){

        calculator.setResult(10);
        Assertions.assertEquals(0, numberProcessor.process(-5, 5));
    }

    @Test
    public void firstParametreIsNegativeAndResultIsBiggerThanTen(){

        calculator.setResult(15);
        Assertions.assertEquals(5, numberProcessor.process(-5, 5));
    }

    @Test
    public void firstParametreIsNegativeAndCalculatorNotSet(){

        numberProcessor.setCalculator(null);
        Assertions.assertThrows(
            NullPointerException.class, () -> { 
                numberProcessor.process(-1, 0);
        });
    }

    @Test 
    public void firstParametreNonNegativeAndCalculatorNotSet(){

        numberProcessor.setCalculator(null);
        Assertions.assertEquals(10, numberProcessor.process(5, 5));
    }
}

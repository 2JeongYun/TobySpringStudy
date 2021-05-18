package learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcTest {

    private Calculator calculator;
    private String filePath;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator(new BufferedReaderContext());
        filePath = "./src/test/resources/numbers.txt";
    }

    @Test
    public void 숫자_더하기() throws IOException {
        assertThat(calculator.calcSum(filePath)).isEqualTo(10);
    }

    @Test
    public void 숫자_곱하기() throws IOException {
        assertThat(calculator.calcMultiply(filePath)).isEqualTo(24);
    }

    @Test
    public void 문자_연결하기() throws IOException {
        assertThat(calculator.concatenateStrings(filePath)).isEqualTo("1234");
    }
}

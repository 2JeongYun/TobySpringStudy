package learningtest.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";

        assertThat(name.length()).isEqualTo(6);

        Method lengthMethod = name.getClass().getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name)).isEqualTo(6);

        assertThat(name.charAt(0)).isEqualTo('S');

        Method charAtMethod = name.getClass().getMethod("charAt", int.class);
        assertThat((char) charAtMethod.invoke(name, 0)).isEqualTo('S');
    }
}

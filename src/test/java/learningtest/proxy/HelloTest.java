package learningtest.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("JY")).isEqualTo("Hello JY");
        assertThat(hello.sayHi("JY")).isEqualTo("Hi JY");
        assertThat(hello.sayThankYou("JY")).isEqualTo("Thank You JY");
    }

    @Test
    public void upperCaseProxy() {
        Hello hello = new HelloUpperCase(new HelloTarget());
        assertThat(hello.sayHello("JY")).isEqualTo("HELLO JY");
        assertThat(hello.sayHi("JY")).isEqualTo("HI JY");
        assertThat(hello.sayThankYou("JY")).isEqualTo("THANK YOU JY");
    }

    @Test
    public void dynamicProxy() {
        Hello proxyHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UpperCaseHandler(new HelloTarget())
        );

        assertThat(proxyHello.sayHello("JY")).isEqualTo("HELLO JY");
        assertThat(proxyHello.sayHi("JY")).isEqualTo("HI JY");
        assertThat(proxyHello.sayThankYou("JY")).isEqualTo("THANK YOU JY");
    }
}

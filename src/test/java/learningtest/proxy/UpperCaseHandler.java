package learningtest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UpperCaseHandler implements InvocationHandler {

    Object target;

    public UpperCaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if (ret instanceof String)
            return ((String) ret).toUpperCase();
        else
            return ret;
    }
}

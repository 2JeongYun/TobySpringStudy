package service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

@RequiredArgsConstructor
// 범용성을 위해 생성할 오브젝트 타입을 Object로 설정
public class TransactionProxyFactoryBean implements FactoryBean<Object> {

    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;
    private final Class<?> serviceInterface;

    @Override
    public Object getObject() throws Exception {
        TransactionHandler transactionHandler = new TransactionHandler(
                target, transactionManager, pattern
        );
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {serviceInterface},
                transactionHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        // 싱글톤 빈이 아니라는 뜻이 아님.
        // getObject()가 매번 같은 오브젝트를 리턴하지 않는다는 의미.
        return false;
    }
}

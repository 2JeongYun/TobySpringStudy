package service;

import domain.user.Level;
import domain.user.User;
import domain.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static service.UserService.MIN_LOGIN_FOR_SILVER;
import static service.UserService.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTransactionTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private UserService userService;
    private ArrayList<User> users;

    @BeforeEach
    public void setUp() {
        userDao.deleteAll();
        UserDao userDaoProxy = (UserDao) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserDao.class},
                new InvocationHandler() {

                    private UserDao target = userDao;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("update")) {
                            User user = (User) args[0];
                            if (user.getId().equals("3"))
                                throw new RuntimeException();
                        }

                        return method.invoke(target, args);
                    }
                }
        );
        UserServiceImpl userServiceImpl = new UserServiceImpl(userDaoProxy, mock(MailSender.class));
        userService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {UserService.class},
                new TransactionHandler(userServiceImpl, transactionManager, "upgradeLevels")
        );

        User user1 = User.builder()
                .id("1")
                .email("e1")
                .recommend(1)
                .login(MIN_LOGIN_FOR_SILVER)
                .level(Level.BASIC)
                .name("user1")
                .password("pw1")
                .build();
        User user2 = User.builder()
                .id("2")
                .email("e2")
                .recommend(1)
                .login(MIN_LOGIN_FOR_SILVER)
                .level(Level.BASIC)
                .name("user2")
                .password("pw2")
                .build();
        User user3 = User.builder()
                .id("3")
                .email("e3")
                .recommend(MIN_RECOMMEND_FOR_GOLD)
                .login(MIN_LOGIN_FOR_SILVER)
                .level(Level.SILVER)
                .name("user3")
                .password("pw3")
                .build();
        User user4 = User.builder()
                .id("4")
                .email("e4")
                .recommend(1)
                .login(MIN_LOGIN_FOR_SILVER)
                .level(Level.BASIC)
                .name("user4")
                .password("pw4")
                .build();
        users = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
    }

    @Test
    public void 등업_일관성() throws Exception {
        //given
        for (User user : users) {
            userService.add(user);
        }

        //when
        assertThrows(RuntimeException.class, () -> {
            userService.upgradeLevels();
        });

        //then
        assertThat(userDao.get(users.get(0).getId()))
                .usingRecursiveComparison()
                .isEqualTo(users.get(0));
    }
}

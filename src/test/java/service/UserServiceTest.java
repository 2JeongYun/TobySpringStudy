package service;

import domain.user.Level;
import domain.user.User;
import domain.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static service.UserService.MIN_LOGIN_FOR_SILVER;
import static service.UserService.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        userDao.deleteAll();
        User user1 = User.builder()
                .id("1")
                .name("name1")
                .password("password1")
                .email("email1")
                .login(MIN_LOGIN_FOR_SILVER - 1)
                .recommend(10)
                .level(Level.BASIC)
                .build();
        User user2 = User.builder()
                .id("2")
                .name("name2")
                .password("password2")
                .email("email2")
                .login(MIN_LOGIN_FOR_SILVER)
                .recommend(10)
                .level(Level.BASIC)
                .build();
        User user3 = User.builder()
                .id("3")
                .name("name3")
                .password("password3")
                .email("email3")
                .login(51)
                .recommend(MIN_RECOMMEND_FOR_GOLD)
                .level(Level.SILVER)
                .build();
        User user4 = User.builder()
                .id("4")
                .name("name4")
                .password("password4")
                .email("email4")
                .login(51)
                .recommend(30)
                .level(Level.GOLD)
                .build();
        users = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
    }

    @Test
    public void 등업_테스트() throws Exception {
        //given
        for (User user : users) {
            userDao.add(user);
        }

        //when
        userService.upgradeLevels();

        //then
        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), true);
        checkLevel(users.get(3), false);
    }

    @Test
    public void 유저_등록할때_등급_설정한다() throws Exception {
        //given
        User userWithLevel = users.get(3);
        User userWithoutLevel = User.builder()
                .id("id")
                .name("name")
                .password("password")
                .email("email")
                .recommend(0)
                .login(0)
                .build();

        //when
        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        //then
        checkLevel(userWithLevel, false);
        assertThat(userDao.get(userWithoutLevel.getId()).getLevel())
                .isEqualTo(userWithoutLevel.getLevel());
    }

    private void checkLevel(User user, boolean upgrade) {
        User userUpdate = userDao.get(user.getId());
        if (upgrade) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().getNextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }
}

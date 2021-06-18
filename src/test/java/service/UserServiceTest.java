package service;

import domain.user.Level;
import domain.user.User;
import domain.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static service.UserService.MIN_LOGIN_FOR_SILVER;
import static service.UserService.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao mockUserDao;

    @Mock
    private MailSender mockMailSender;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> users;

    @BeforeEach
    public void setUp() {
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
    public void 등업_테스트() {
        //given
        given(mockUserDao.getAll()).willReturn(users);

        //when
        userService.upgradeLevels();

        //then
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        assertThat(users.get(2).getLevel()).isEqualTo(Level.GOLD);
        verify(mockUserDao).update(users.get(1));
        verify(mockUserDao).update(users.get(2));
        verify(mockUserDao, times(0)).update(users.get(0));
        verify(mockUserDao, times(0)).update(users.get(3));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(2).getEmail());
    }

    @Test
    public void 유저_등록할때_등급_설정한다() {
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
        assertThat(userWithLevel.getLevel()).isEqualTo(users.get(3).getLevel());
        assertThat(userWithoutLevel.getLevel()).isEqualTo(Level.BASIC);
    }
}

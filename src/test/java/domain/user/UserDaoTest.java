package domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userDao.deleteAll();
        user1 = User.builder()
                .id("id1")
                .name("name1")
                .password("password1")
                .email("email1")
                .level(Level.BASIC)
                .login(1)
                .recommend(1)
                .build();
        user2 = User.builder()
                .id("id2")
                .name("name2")
                .password("password2")
                .email("email2")
                .level(Level.SILVER)
                .login(40)
                .recommend(3)
                .build();
    }

    @Test
    public void 유저_저장하고_조회한다() {
        //given
        userDao.add(user1);
        userDao.add(user2);

        //when
        User result = userDao.get(user2.getId());

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    public void 유저_개수_조회한다() {
        //given
        userDao.add(user1);
        userDao.add(user2);

        //when
        int result = userDao.getCount();

        //then
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void 유저_전부_조회한다() {
        //given
        userDao.add(user1);
        userDao.add(user2);

        //when
        List<User> result = userDao.getAll();

        //then
        assertThat(result).hasSize(2);
        assertThat(result).usingRecursiveFieldByFieldElementComparator().containsExactly(user1, user2);
    }

    @Test
    public void 유저_전부_조회_결과_없을때_빈리스트() {
        //given

        //when
        List<User> result = userDao.getAll();

        //then
        assertThat(result).hasSize(0);
    }

    @Test
    public void 유저_전부_삭제한다() {
        //given
        userDao.add(user1);
        userDao.add(user2);

        //when
        userDao.deleteAll();

        //then
        int result = userDao.getCount();
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void 유저_조회_결과_없을때_예외발생() {
        //given
        assertThat(userDao.getCount()).isEqualTo(0);

        //when

        //then
        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.get("unknown_id");
        });
    }

    @Test
    public void 중복된_키_등록시_예외발생() throws Exception {
        //given
        userDao.add(user1);

        //when

        //then
        assertThrows(DuplicateKeyException.class, () -> {
            userDao.add(User.builder()
                .id(user1.getId())
                .name("name2")
                .password("password2")
                .email("email2")
                .level(Level.SILVER)
                .login(40)
                .recommend(2)
                .build());
        });
    }

    @Test
    public void 유저_수정한다() throws Exception {
        //given
        userDao.add(user1);
        userDao.add(user2);
        user1.setName("변경된 이름");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        //when
        userDao.update(user1);

        //then
        User result = userDao.get(user1.getId());
        assertThat(result).usingRecursiveComparison().isEqualTo(user1);
        assertThat(user2)
                .usingRecursiveComparison()
                .isEqualTo(userDao.get(user2.getId()));
    }
}

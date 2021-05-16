package learningtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/dummyContext.xml")
public class JUnitObjectTest {

    @Autowired
    private ApplicationContext context;

    static private ApplicationContext prevContext = null;
    static private Set<JUnitObjectTest> objects = new HashSet<>();

    @Test
    public void JUnit_오브젝트_생성1() throws Exception {
        assertThat(objects).doesNotContain(this);
        objects.add(this);
    }

    @Test
    public void JUnit_오브젝트_생성2() throws Exception {
        assertThat(objects).doesNotContain(this);
        objects.add(this);
    }

    @Test
    public void JUnit_오브젝트_생성3() throws Exception {
        assertThat(objects).doesNotContain(this);
        objects.add(this);
    }

    @Test
    public void 컨텍스트_오브젝트_공유1() throws Exception {
        assertThat(prevContext).isIn(null, this.context);
        prevContext = this.context;
    }

    @Test
    public void 컨텍스트_오브젝트_공유2() throws Exception {
        assertThat(prevContext).isIn(null, this.context);
        prevContext = this.context;
    }
}

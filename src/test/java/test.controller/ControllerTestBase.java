package test.controller;

import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.TestBase;

import java.sql.Date;

@Ignore
public class ControllerTestBase extends TestBase {
    public static final String USER_1_ID = "15622743170";
    public static final String USER_1_PW = "123456";
    public static final String USER_2_ID = "12345678911";
    public static final String USER_2_PW = "654321";

    public static final String OBJECT_ID_1 = "k2DyygK4RP8nquWmNl0wD7qXhJ44vJWK";

    public MockHttpSession session1;
    public MockHttpSession session2;

    public User user1 = null;
    public User user2 = null;

    @Autowired
    public WebApplicationContext webApplicationContext;
    public MockMvc mockMvc;

    @Autowired
    public UserService userService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user1 = new User();
        user1.setBirthDate(Date.valueOf("1995-01-05"));
        user1.setUserId(USER_1_ID);
        user1.setPassword(USER_1_PW);
        user1.setName("wyf");
        user1.setGender("male");
        user1.setLocation("至九");
        user1 = userService.createUserWithRawPassword(user1);

        user2 = new User();
        user2.setBirthDate(Date.valueOf("1995-01-05"));
        user2.setUserId(USER_2_ID);
        user2.setPassword(USER_2_PW);
        user2.setName("wyyyyyf");
        user2.setGender("female");
        user2.setLocation("至十");
        user2 = userService.createUserWithRawPassword(user2);

        session1 = new MockHttpSession();
        session1.putValue("userId", USER_1_ID);
        session2 = new MockHttpSession();
        session2.putValue("userId", USER_2_ID);
    }

}

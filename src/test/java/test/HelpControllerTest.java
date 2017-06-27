package test;


import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.HelpService;
import com.sysu.yizhu.business.services.QuestionService;
import com.sysu.yizhu.business.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelpControllerTest extends TestBase {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private MockHttpSession session1 = new MockHttpSession();
    private MockHttpSession session2 = new MockHttpSession();

    private User user1 = null;
    private User user2 = null;
    private Question question = null;
    private Answer answer = null;
    private AnswerAgree answerAgree = null;
    private Integer helpId = null;

    private static final String USER_1_ID = "15622743170";
    private static final String USER_1_PW = "123456";
    private static final String USER_2_ID = "12345678911";
    private static final String USER_2_PW = "654321";

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HelpService helpService;

    @Before
    public void setupAndRegister() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user1 = new User();
        user1.setBirthDate(Date.valueOf("1995-01-05"));
        user1.setUserId(USER_1_ID);
        user1.setPassword(USER_1_PW);
        user1.setName("wyf");
        user1.setGender("male");
        user1.setLocation("至九");
        user1 = userService.createUserWithRawPassword(user1);
        session1.putValue("userId", USER_1_ID);

        user2 = new User();
        user2.setBirthDate(Date.valueOf("1995-01-05"));
        user2.setUserId(USER_2_ID);
        user2.setPassword(USER_2_PW);
        user2.setName("wyyyyyf");
        user2.setGender("female");
        user2.setLocation("至十");
        user2 = userService.createUserWithRawPassword(user2);
        session2.putValue("userId", USER_2_ID);

        question = questionService.createQuestion(user1, "提问", "内容");
        answer = questionService.createAnswer(question, user1, "回答");
        answerAgree = questionService.setAgreement(Boolean.TRUE, user1, answer);

    }

    @Test
    public void test() throws Exception {
        testHelpPush();
        testHelpAllValidId();
        testHelpGetId();
        testHelpResponse();
        testHelpGetResponserId();
        testHelpFinish();
    }

    private void testHelpPush() throws Exception {
        mockMvc.perform(post("/help/push")
                .session(session2)
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "120")
                .param("detail", "120")
                .param("needs", "5"))
                .andExpect(status().is(450));

        mockMvc.perform(post("/help/push")
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "120")
                .param("detail", "120")
                .param("needs", "5"))
                .andExpect(status().is(401));

        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "120")
                .param("detail", "120")
                .param("needs", "11"))
                .andExpect(status().is(402));

        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "120")
                .param("title", "120")
                .param("detail", "120")
                .param("needs", "5"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "-200")
                .param("title", "120")
                .param("detail", "120")
                .param("needs", "5"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "120")
                .param("detail", "120")
                .param("needs", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.helpId").isNumber());
        helpId = helpService.getAllValidHelpId().get(0);
    }

    private void testHelpAllValidId() throws Exception {
        mockMvc.perform(get("/help/allValidId"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    private void testHelpGetId() throws Exception {
        mockMvc.perform(get("/help/get/1234"))
                .andExpect(status().is(404));
        mockMvc.perform(get("/help/get/" + helpId.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.helpId").isNumber())
                .andExpect(jsonPath("$.latitude").isNumber())
                .andExpect(jsonPath("$.longitude").isNumber())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.detail").isString())
                .andExpect(jsonPath("$.needs").isNumber())
                .andExpect(jsonPath("$.responseNum").isNumber())
                .andExpect(jsonPath("$.finished").isBoolean())
                .andExpect(jsonPath("$.pushUserId").isString());
    }

    private void testHelpResponse() throws Exception {
        mockMvc.perform(post("/help/response")
                .session(session2)
                .param("helpId", helpId.toString()))
                .andExpect(status().is(450));

        mockMvc.perform(post("/help/response")
                .param("helpId", helpId.toString()))
                .andExpect(status().is(401));

        mockMvc.perform(post("/help/response")
                .session(session1)
                .param("helpId", "1234"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/help/response")
                .session(session1)
                .param("helpId", helpId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isString());
    }

    private void testHelpGetResponserId() throws Exception {
        mockMvc.perform(get("/help/response/1234"))
                .andExpect(status().is(404));
        mockMvc.perform(get("/help/response/" + helpId.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    private void testHelpFinish() throws Exception {
        mockMvc.perform(post("/help/finish")
                .session(session2)
                .param("helpId", helpId.toString()))
                .andExpect(status().is(450));

        mockMvc.perform(post("/help/finish")
                .param("helpId", helpId.toString()))
                .andExpect(status().is(401));

        mockMvc.perform(post("/help/finish")
                .session(session1)
                .param("helpId", "1234"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/help/finish")
                .session(session1)
                .param("helpId", helpId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.helpId").isNumber());
        Assert.assertTrue(helpService.getHelp(helpId).getFinished());
    }
}

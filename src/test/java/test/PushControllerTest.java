package test;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.QuestionService;
import com.sysu.yizhu.business.services.SOSService;
import com.sysu.yizhu.business.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
public class PushControllerTest  extends TestBase {
    private static final Logger Log = LoggerFactory.getLogger(PushControllerTest.class);

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
    private Integer sosId = null;

    private static final String USER_1_ID = "15622743170";
    private static final String USER_1_PW = "123456";
    private static final String USER_2_ID = "12345678911";
    private static final String USER_2_PW = "654321";

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SOSService sosService;

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
        testUpdateObjectId();
        testUpdateLocation();
//        testSOSPush();
//        testSOSAllValidId();
//        testSOSGetId();
//        testSOSResponse();
//        testSOSGetResponserId();
//        testSOSFinish();
    }


    private void testUpdateObjectId() throws Exception {

        mockMvc.perform(post("/user/updateObjectId")
                .param("objectId", "k2DyygK4RP8nquWmNl0wD7qXhJ44vJWK"))
                .andExpect(status().is(401));

        mockMvc.perform(post("/user/updateObjectId")
                .session(session1)
                .param("objectId", "123"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/updateObjectId")
                .session(session1)
                .param("objectId", "k2DyygK4RP8nquWmNl0wD7qXhJ44vJWK"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userId").value(USER_1_ID));
    }

    private void testUpdateLocation() throws Exception {
        mockMvc.perform(post("/user/updateLocation")
                .session(session2)
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().is(450));

        mockMvc.perform(post("/user/updateLocation")
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().is(401));

        mockMvc.perform(post("/user/updateLocation")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "120"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/updateLocation")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "-200"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/updateLocation")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(USER_1_ID));
    }

    private void testSOSPush() throws Exception {
        mockMvc.perform(post("/sos/push")
                .session(session2)
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().is(450));

        mockMvc.perform(post("/sos/push")
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().is(401));

        mockMvc.perform(post("/sos/push")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "120"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/sos/push")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "-200"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/sos/push")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sosId").isNumber());
        sosId = sosService.getAllValidSOSId().get(0);
    }

    private void testSOSAllValidId() throws Exception {
        mockMvc.perform(get("/sos/allValidId"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    private void testSOSGetId() throws Exception {
        mockMvc.perform(get("/sos/get/1234"))
                .andExpect(status().is(404));
        mockMvc.perform(get("/sos/get/" + sosId.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.sosId").isNumber())
                .andExpect(jsonPath("$.latitude").isNumber())
                .andExpect(jsonPath("$.longitude").isNumber())
                .andExpect(jsonPath("$.finished").isBoolean())
                .andExpect(jsonPath("$.pushUserId").isString());
    }

    private void testSOSResponse() throws Exception {
        mockMvc.perform(post("/sos/response")
                .session(session2)
                .param("sosId", sosId.toString()))
                .andExpect(status().is(450));

        mockMvc.perform(post("/sos/response")
                .param("sosId", sosId.toString()))
                .andExpect(status().is(401));

        mockMvc.perform(post("/sos/response")
                .session(session1)
                .param("sosId", "1234"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/sos/response")
                .session(session1)
                .param("sosId", sosId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isString());
    }

    private void testSOSGetResponserId() throws Exception {
        mockMvc.perform(get("/sos/response/1234"))
                .andExpect(status().is(404));
        mockMvc.perform(get("/sos/response/" + sosId.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    private void testSOSFinish() throws Exception {
        mockMvc.perform(post("/sos/finish")
                .session(session2)
                .param("sosId", sosId.toString()))
                .andExpect(status().is(450));

        mockMvc.perform(post("/sos/finish")
                .param("sosId", sosId.toString()))
                .andExpect(status().is(401));

        mockMvc.perform(post("/sos/finish")
                .session(session1)
                .param("sosId", "1234"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/sos/finish")
                .session(session1)
                .param("sosId", sosId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sosId").isNumber());
        Assert.assertTrue(sosService.getSOS(sosId).getFinished());
    }


}

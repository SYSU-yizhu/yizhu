package test;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.QuestionService;
import com.sysu.yizhu.business.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTest extends TestBase {
    private static final Logger Log = LoggerFactory.getLogger(ControllerTest.class);
    private User user = null;
    private Question question = null;
    private Answer answer = null;
    private AnswerAgree answerAgree = null;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setBirthDate(Date.valueOf("1995-01-05"));
        user.setUserId("15622743170");
        user.setPassword("123456");
        user.setName("wyf");
        user.setGender("male");
        user.setLocation("至九");
        user = userService.createUserWithRawPassword(user);

        question = questionService.createQuestion(user, "提问", "内容");

        answer = questionService.createAnswer(question, user, "回答");

        answerAgree = questionService.setAgreement(Boolean.TRUE, user, answer);
    }

    @Test
    public void testUserLogin() throws Exception {
        mockMvc.perform(post("/user/login")
                .param("userId", "1562274317")
                .param("password", "123456"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743170")
                .param("password", "12345"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743175")
                .param("password", "123456"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743170")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"));
    }

    @Test
    public void testUserInfo() throws Exception {
        mockMvc.perform(post("/user/info")
                .param("userId", "1562274317")
                .param("password", "123456"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743170")
                .param("password", "12345"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743175")
                .param("password", "12345"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743170")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"))
                .andExpect(jsonPath("$.name").value("wyf"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.birthDate").value("1995-01-05"))
                .andExpect(jsonPath("$.location").value("至九"));
    }

    @Test
    public void testUserModifyInfo() throws Exception {
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "1562274317")
                .param("password", "123456")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "15622743170")
                .param("password", "12345")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "15622743175")
                .param("password", "123456")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"));

        User user = userService.findOne("15622743170");
        Assert.assertEquals("李四", user.getName());
        Assert.assertEquals("female", user.getGender());
        Assert.assertEquals("1996-01-05", user.getBirthDate().toString());
        Assert.assertEquals("至十", user.getLocation());

    }

    @Test
    public void testQuestionAsk() throws Exception {
        mockMvc.perform(post("/question/ask")
                .param("userId", "1562274317")
                .param("password", "123456")
                .param("title", "提了一个问题")
                .param("content", "内容"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/question/ask")
                .param("userId", "15622743171")
                .param("password", "123456")
                .param("title", "提了一个问题")
                .param("content", "内容"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/question/ask")
                .param("userId", "15622743170")
                .param("password", "12345")
                .param("title", "提了一个问题")
                .param("content", "内容"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/question/ask")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("title", "提了一个问题")
                .param("content", "内容"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.questionId").isNumber());
    }

    @Test
    public void testQuestionAnswer() throws Exception {
        Integer questionId = questionService.getAllQuestionId().get(0);
        mockMvc.perform(post("/question/answer")
                .param("userId", "1562274317")
                .param("password", "123456")
                .param("questionId", "100")
                .param("content", "回答内容"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/question/answer")
                .param("userId", "15622743171")
                .param("password", "123456")
                .param("questionId", "100")
                .param("content", "回答内容"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/question/answer")
                .param("userId", "15622743170")
                .param("password", "12345")
                .param("questionId", "100")
                .param("content", "回答内容"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/question/answer")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("questionId", "100")
                .param("content", "回答内容"))
                .andExpect(status().is(450));
        mockMvc.perform(post("/question/answer")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("questionId", questionId.toString())
                .param("content", "内容"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.answerId").isNumber());

    }

    @Test
    public void testAgreeAnswer() throws Exception {
        Integer answerId = questionService.getAllAnswerIdByQuestionId(1).get(0);
        mockMvc.perform(post("/question/agreeAnswer")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("answerId", "4")
                .param("agreeOrNot", "true"))
                .andExpect(status().is(450));
        mockMvc.perform(post("/question/agreeAnswer")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("answerId", answerId.toString())
                .param("agreeOrNot", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.answerAgreeId").isNumber());
    }

    @Test
    public void testGetAllId() throws Exception {
        mockMvc.perform(get("/question/getAllId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testQuestionGetDigest() throws Exception {
        mockMvc.perform(get("/question/digest/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.questionId").isNumber())
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userName").isString())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.createDate").isString());
    }

    @Test
    public void testQuestionGetDetail() throws Exception {
        mockMvc.perform(get("/question/detail/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.questionId").isNumber())
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userName").isString())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.content").isString())
                .andExpect(jsonPath("$.createDate").isString());
    }

    @Test
    public void testQuestionGetAnswersId() throws Exception {
        mockMvc.perform(get("/question/getAnswerIds/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testQuestionGetAnswer() throws Exception {
        mockMvc.perform(get("/question/getAnswer/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.answerId").isNumber())
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userName").isString())
                .andExpect(jsonPath("$.content").isString())
                .andExpect(jsonPath("$.good").isNumber())
                .andExpect(jsonPath("$.bad").isNumber())
                .andExpect(jsonPath("$.createDate").isString());
    }
}

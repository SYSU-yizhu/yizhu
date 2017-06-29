package test.controller;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.services.QuestionService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class b_QuestionControllerTest extends ControllerTestBase {

    Integer questionId;
    Integer answerId;

    @Autowired
    private QuestionService questionService;

    @Test
    public void tests() throws Exception {
        testQuestionAsk();
        testQuestionAnswer();
        testAgreeAnswer();
        testGetAllId();
        testQuestionGetDigest();
        testQuestionGetDetail();
        testQuestionGetAnswersId();
        testQuestionGetAnswer();
    }

    private void testQuestionAsk() throws Exception {
        mockMvc.perform(post("/question/ask")
                .param("title", "提了一个问题")
                .param("content", "内容"))
                .andExpect(status().is(401));
        mockMvc.perform(post("/question/ask")
                .session(session1)
                .param("title", "提了一个问题")
                .param("content", "内容"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.questionId").isNumber());
    }

    private void testQuestionAnswer() throws Exception {
        questionId = questionService.getAllQuestionId().get(0);
        mockMvc.perform(post("/question/answer")
                .param("questionId", "100")
                .param("content", "回答内容"))
                .andExpect(status().is(401));
        mockMvc.perform(post("/question/answer")
                .session(session1)
                .param("questionId", "9999")
                .param("content", "回答内容"))
                .andExpect(status().is(450));
        mockMvc.perform(post("/question/answer")
                .session(session1)
                .param("questionId", questionId.toString())
                .param("content", "内容"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.answerId").isNumber());

    }

    private void testAgreeAnswer() throws Exception {
        answerId = questionService.getAllAnswerIdByQuestionId(questionId).get(0);
        mockMvc.perform(post("/question/agreeAnswer")
                .param("answerId", "4")
                .param("agreeOrNot", "true"))
                .andExpect(status().is(401));
        mockMvc.perform(post("/question/agreeAnswer")
                .session(session1)
                .param("answerId", "99999")
                .param("agreeOrNot", "true"))
                .andExpect(status().is(450));
        mockMvc.perform(post("/question/agreeAnswer")
                .session(session1)
                .param("answerId", answerId.toString())
                .param("agreeOrNot", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.answerAgreeId").isNumber());
    }

    private void testGetAllId() throws Exception {
        mockMvc.perform(get("/question/getAllId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    private void testQuestionGetDigest() throws Exception {
        mockMvc.perform(get("/question/digest/"+questionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.questionId").isNumber())
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userName").isString())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.createDate").isString());
    }

    private void testQuestionGetDetail() throws Exception {
        mockMvc.perform(get("/question/detail/"+questionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.questionId").isNumber())
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userName").isString())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.content").isString())
                .andExpect(jsonPath("$.createDate").isString());
    }

    private void testQuestionGetAnswersId() throws Exception {
        mockMvc.perform(get("/question/getAnswerIds/"+questionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.data").isArray());
    }

    private void testQuestionGetAnswer() throws Exception {
        mockMvc.perform(get("/question/getAnswer/"+answerId))
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

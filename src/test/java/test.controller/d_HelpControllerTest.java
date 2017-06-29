package test.controller;


import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.HelpService;
import com.sysu.yizhu.business.services.QuestionService;
import com.sysu.yizhu.business.services.UserService;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.TestBase;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class d_HelpControllerTest extends ControllerTestBase {

    private Integer helpId = null;

    @Autowired
    private HelpService helpService;

    @Before
    public void setupHelp() throws Exception {
        mockMvc.perform(post("/user/updateObjectId")
                .session(session1)
                .param("objectId", OBJECT_ID_1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userId").value(USER_1_ID));
        mockMvc.perform(post("/user/updateLocation")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(USER_1_ID));
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
                .param("title", "标题")
                .param("detail", "细节")
                .param("needs", "5"))
                .andExpect(status().is(450));

        mockMvc.perform(post("/help/push")
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "标题")
                .param("detail", "细节")
                .param("needs", "5"))
                .andExpect(status().is(401));

        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "标题")
                .param("detail", "细节")
                .param("needs", "11"))
                .andExpect(status().is(402));

        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "120")
                .param("title", "标题")
                .param("detail", "细节")
                .param("needs", "5"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "100")
                .param("longitude", "-200")
                .param("title", "标题")
                .param("detail", "细节")
                .param("needs", "5"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/help/push")
                .session(session1)
                .param("latitude", "21")
                .param("longitude", "120")
                .param("title", "标题")
                .param("detail", "细节")
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
        mockMvc.perform(get("/help/get/" + "9999"))
                .andExpect(status().is(404));
        mockMvc.perform(get("/help/get/" + helpId.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.helpId").isNumber())
                .andExpect(jsonPath("$.latitude").isNumber())
                .andExpect(jsonPath("$.longitude").isNumber())
                .andExpect(jsonPath("$.finished").isBoolean())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.detail").isString())
                .andExpect(jsonPath("$.needs").isNumber())
                .andExpect(jsonPath("$.responseNum").isNumber())
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
                .param("helpId", "9999"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/help/response")
                .session(session1)
                .param("helpId", helpId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isString());

        mockMvc.perform(post("/help/response")
                .session(session1)
                .param("helpId", helpId.toString()))
                .andExpect(status().is(403));

        //TODO 402
    }

    private void testHelpGetResponserId() throws Exception {
        mockMvc.perform(get("/help/response/"+"9999"))
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

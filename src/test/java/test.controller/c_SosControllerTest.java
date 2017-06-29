package test.controller;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.QuestionService;
import com.sysu.yizhu.business.services.SOSService;
import com.sysu.yizhu.business.services.UserService;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class c_SosControllerTest extends ControllerTestBase {
    private static final Logger Log = LoggerFactory.getLogger(c_SosControllerTest.class);

    private Integer sosId = null;

    @Autowired
    private SOSService sosService;

    @Before
    public void setupSos() throws Exception {
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
        testSOSPush();
        testSOSAllValidId();
        testSOSGetId();
        testSOSResponse();
        testSOSGetResponserId();
        testSOSFinish();
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
        mockMvc.perform(get("/sos/get/" + "9999"))
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
                .param("sosId", "9999"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/sos/response")
                .session(session1)
                .param("sosId", sosId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isString());

        mockMvc.perform(post("/sos/response")
                .session(session1)
                .param("sosId", sosId.toString()))
                .andExpect(status().is(403));
    }

    private void testSOSGetResponserId() throws Exception {
        mockMvc.perform(get("/sos/response/" + "9999"))
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

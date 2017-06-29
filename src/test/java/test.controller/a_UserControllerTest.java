package test.controller;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class a_UserControllerTest extends ControllerTestBase {
    private static final Logger Log = LoggerFactory.getLogger(a_UserControllerTest.class);

    @Test
    public void tests() throws Exception {
//        testSendSms();
//        testInfo();
        testLogin();
        testInfo();
        testModifyInfo();
        testUpdateObjectId();
        testUpdateLocation();
    }

    private void testSendSms() throws Exception {

    }

    private void testRegister() throws Exception {

    }

    private void testLogin() throws Exception {
        mockMvc.perform(post("/user/login")
                .param("userId", "1562274317")
                .param("password", USER_1_PW))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/login")
                .param("userId", USER_1_ID)
                .param("password", "12345"))
                .andExpect(status().is(400));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743175")
                .param("password", USER_1_PW))
                .andExpect(status().is(400));
        mockMvc.perform(post("/user/login")
                .param("userId", USER_1_ID)
                .param("password", USER_1_PW))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value(USER_1_ID));
    }

    private void testInfo() throws Exception {
        mockMvc.perform(post("/user/info"))
                .andExpect(status().is(401));
        mockMvc.perform(post("/user/info")
                .session(session1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value(USER_1_ID))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.gender").isString())
                .andExpect(jsonPath("$.birthDate").isString())
                .andExpect(jsonPath("$.location").isString());
    }

    private void testModifyInfo() throws Exception {
        String changeName = "李四";
        String changeGender = "female";
        String changeBirth = "1996-01-05";
        String changeLocation = "至十";
        mockMvc.perform(post("/user/modifyInfo")
                .param("name", changeName)
                .param("gender", changeGender)
                .param("birthDate", changeBirth)
                .param("location", changeLocation))
                .andExpect(status().is(401));
        mockMvc.perform(post("/user/modifyInfo")
                .session(session1)
                .param("name", changeName)
                .param("gender", "fff")
                .param("birthDate", changeBirth)
                .param("location", changeLocation))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/modifyInfo")
                .session(session1)
                .param("name", changeName)
                .param("gender", changeGender)
                .param("birthDate", "1996-01-055")
                .param("location", changeLocation))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/modifyInfo")
                .session(session1)
                .param("name", changeName)
                .param("gender", changeGender)
                .param("birthDate", changeBirth)
                .param("location", changeLocation))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value(USER_1_ID));

        // check
        mockMvc.perform(post("/user/info")
                .session(session1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value(USER_1_ID))
                .andExpect(jsonPath("$.name").value(changeName))
                .andExpect(jsonPath("$.gender").value(changeGender))
                .andExpect(jsonPath("$.birthDate").value(changeBirth))
                .andExpect(jsonPath("$.location").value(changeLocation));
    }

    private void testUpdateObjectId() throws Exception {

        mockMvc.perform(post("/user/updateObjectId")
                .param("objectId", OBJECT_ID_1))
                .andExpect(status().is(401));

        mockMvc.perform(post("/user/updateObjectId")
                .session(session1)
                .param("objectId", "123"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/updateObjectId")
                .session(session1)
                .param("objectId", OBJECT_ID_1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userId").value(USER_1_ID));
    }

    private void testUpdateLocation() throws Exception {
        mockMvc.perform(post("/user/updateLocation")
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().is(401));

        mockMvc.perform(post("/user/updateLocation")
                .session(session2)
                .param("latitude", "21")
                .param("longitude", "120"))
                .andExpect(status().is(450));

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
}
